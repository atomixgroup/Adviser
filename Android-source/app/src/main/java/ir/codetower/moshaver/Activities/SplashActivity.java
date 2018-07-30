package ir.codetower.moshaver.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ir.codetower.moshaver.App;
import ir.codetower.moshaver.BuildConfig;
import ir.codetower.moshaver.Config;
import ir.codetower.moshaver.Helpers.DownloadTask;
import ir.codetower.moshaver.Helpers.WebService;
import ir.codetower.moshaver.Models.Category;
import ir.codetower.moshaver.Models.Content;
import ir.codetower.moshaver.Param;
import ir.codetower.moshaver.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        if(checkPermissions()){
            App.webService.postRequest("update", "update", Config.apiUrl + "checkUpdate", new WebService.OnPostReceived() {
                @Override
                public void onReceived(String message) {

                    int verNum=0;
                    try
                    {
                        verNum= Integer.parseInt(message);

                    } catch (NumberFormatException ex)
                    {

                    }
                    final int num = verNum;
                    if (num > BuildConfig.VERSION_CODE) {
                        Uri updateApk = null;
                        File fileApkAddress= new File(Config.sdTempFolderAddress + "update.apk");

                        if (Build.VERSION.SDK_INT >= 24) {
                            updateApk= FileProvider.getUriForFile(SplashActivity.this, BuildConfig.APPLICATION_ID + ".provider",fileApkAddress);
                        }
                        else{
                            updateApk=Uri.fromFile(fileApkAddress);
                        }
                        if (fileApkAddress.exists()) {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setDataAndType(updateApk, "application/vnd.android.package-archive");
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivity(intent);
                        } else {
                            final Uri finalUpdateApk = updateApk;
                            DownloadTask downloadTask = new DownloadTask(new DownloadTask.ProgressPercentListener() {
                                @Override
                                public void onListen(int percent) {

                                }

                                @Override
                                public void onEnd(String result) {
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setDataAndType(finalUpdateApk, "application/vnd.android.package-archive");
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                    startActivity(intent);
                                }
                            });
                            downloadTask.execute(Config.apiUrl + "getUpdate", Config.sdTempFolderAddress + "update.apk");
                        }
                    } else {
                        new File(Config.sdTempFolderAddress + "update.apk").delete();

                    }
                    getServerData();

                }

                @Override
                public void onReceivedError(String message) {
                    if (checkPermissions()) {
                        getServerData();

                    }
                }
            });

        }

        findViewById(R.id.brain_logo).startAnimation(AnimationUtils.loadAnimation(this,R.anim.fader));

    }
    public static final int MULTIPLE_PERMISSIONS = 10;
    String[] permissions = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MULTIPLE_PERMISSIONS);
            return false;
        }
        App.makeDirectories();
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull final String[] permissions, @NonNull int[] grantResults) {
        boolean flag = false;
        for (int grant : grantResults) {
            if (grant != 0) {
                flag = true;
            }
        }
        if (flag) {
            checkPermissions();
        } else {

            App.makeDirectories();
            getServerData();
        }

    }
    private void getServerData() {

        Param.clear();
        if(!App.prefManager.getPreference("registered").equals("")){
            Param.put("userId",App.prefManager.getPreference("userId"));
        }


        App.webService.postRequest(Param.get(),Config.apiUrl+"getContents", new WebService.OnPostReceived() {
            @Override
            public void onReceived(String message) {

                Content.clearData();
                Category.clearData();

                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(message);
                    ArrayList<Content> contents = Content.jsonToContents(jsonArray.getString(1));
                    ArrayList<Category> categories = Category.jsonToObject(jsonArray.getString(0));
                    Content.batch_insert_content(contents);
                    Category.batch_insert_content(categories);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(App.prefManager.getPreference("registered").equals("registered")){
                    Intent intent=new Intent(SplashActivity.this,MainActivity2.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Intent intent=new Intent(SplashActivity.this,MainActivity2.class);
                    startActivity(intent);
                    finish();
                }

            }

            @Override
            public void onReceivedError(String message) {
                App.showToast("لطفا از اتصال اینترنت خود اطمینان داشته باشید برنامه را برای بروزرسانی دوباره باز کنید");

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                            if(App.prefManager.getPreference("registered").equals("registered")){
                                Intent intent=new Intent(SplashActivity.this,MainActivity2.class);
                                startActivity(intent);
                                finish();
                            }
                            else{
                                Intent intent=new Intent(SplashActivity.this,MainActivity2.class);
                                startActivity(intent);
                                finish();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }
}
