package ir.codetower.moshaver.Activities;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import ir.codetower.moshaver.App;
import ir.codetower.moshaver.Config;
import ir.codetower.moshaver.Fragments.CategoryFragment;
import ir.codetower.moshaver.Fragments.ContentFragment;
import ir.codetower.moshaver.Fragments.FavoritesFragment;
import ir.codetower.moshaver.Fragments.InfoFragment;
import ir.codetower.moshaver.Fragments.MainFragment;
import ir.codetower.moshaver.Fragments.SearchFragment;
import ir.codetower.moshaver.Helpers.WebService;
import ir.codetower.moshaver.OnItemClickListener;
import ir.codetower.moshaver.Param;
import ir.codetower.moshaver.R;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private MainFragment mainFragment;
    private String currentFragment = "main";
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatImageView share=findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareApplication();
            }
        });
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        Toolbar toolbar = findViewById(R.id.toolbar);
//        mainFragment = new MainFragment(new OnItemClickListener() {
//            @Override
//            public void onItemClick(int id) {
//                Log.i("id", id + "");
//                switch (id) {
//                    case R.id.content:
//                        transaction = manager.beginTransaction();
//                        transaction.replace(R.id.fragment_section, new CategoryFragment(new OnItemClickListener() {
//                                    @Override
//                                    public void onItemClick(int id) {
//                                        transaction = manager.beginTransaction();
//                                        transaction.replace(R.id.fragment_section, new ContentFragment(MainActivity.this, id));
//                                        transaction.commit();
//                                        currentFragment = "content";
//                                    }
//                                })
//                        );
//                        transaction.commit();
//                        currentFragment = "category";
//                        break;
//                    case R.id.search:
//                        transaction = manager.beginTransaction();
//                        transaction.replace(R.id.fragment_section, new SearchFragment());
//                        transaction.commit();
//                        currentFragment = "search";
//                        break;
//                    case R.id.favorites:
//                        transaction = manager.beginTransaction();
//                        transaction.replace(R.id.fragment_section, new FavoritesFragment());
//                        transaction.commit();
//                        currentFragment = "favorites";
//                        break;
//                    case R.id.shareapp:
//                        shareApplication();
//                        break;
//                    case R.id.rate:
//                        try {
//                            Intent intent = new Intent(Intent.ACTION_EDIT);
//                            intent.setData(Uri.parse("bazaar://details?id=" + "ir.codetower.moshaver"));
//                            intent.setPackage("com.farsitel.bazaar");
//                            startActivity(intent);
//                        } catch (Exception e) {
//                            Toast.makeText(App.context, "قبل از دادن امتیاز برنامه کافه بازار را نصب کنید", Toast.LENGTH_LONG).show();
//                        }
//                        break;
//                }
//            }
//        });
        Param.clear();
        Param.put("userId",App.prefManager.getPreference("userId"));
        App.webService.postRequest(Param.get(), Config.apiUrl + "getWallet", new WebService.OnPostReceived() {
            @Override
            public void onReceived(String message) {
                try {
                    int wallet = Integer.parseInt(message);
                    App.prefManager.savePreference("wallet", wallet + "");
                } catch (Exception e) {

                }
            }

            @Override
            public void onReceivedError(String message) {

            }
        });
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
//        navigationView.getBackground().setAlpha(125);
//        navigationView.addHeaderView(LayoutInflater.from(this).inflate(R.layout.nav_header_main,null,false));

        findViewById(R.id.menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);
            }
        });


        manager = getSupportFragmentManager();

//        transaction = manager.beginTransaction();
//        transaction.replace(R.id.fragment_section, mainFragment);
//        transaction.commit();

        Menu m = navigationView.getMenu();




    }

    public void Go(View view) {

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://gognuus.ir"));
        startActivity(browserIntent);


    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
//            if (currentFragment.equals("main")) {
//                super.onBackPressed();
//            } else if (currentFragment.equals("content")) {
//                currentFragment = "category";
//                transaction = manager.beginTransaction();
//                transaction.replace(R.id.fragment_section, new CategoryFragment(new OnItemClickListener() {
//                    @Override
//                    public void onItemClick(int id) {
//                        transaction = manager.beginTransaction();
//                        transaction.replace(R.id.fragment_section, new ContentFragment(MainActivity.this, id));
//                        transaction.commit();
//                        currentFragment = "content";
//                    }
//                }));
//                transaction.commit();
//            } else {
//                currentFragment = "main";
//                transaction = manager.beginTransaction();
//                transaction.replace(R.id.fragment_section, mainFragment);
//                transaction.commit();
//            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {

            case R.id.menu_exit:
                finish();
                break;
            case R.id.menu_info:
//                transaction = manager.beginTransaction();
//                transaction.replace(R.id.fragment_section, new InfoFragment());
//                transaction.commit();
//                currentFragment = "info";
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void shareApplication() {
        ApplicationInfo app = getApplicationContext().getApplicationInfo();
        String filePath = app.sourceDir;

        Intent intent = new Intent(Intent.ACTION_SEND);

        // MIME of .apk is "application/vnd.android.package-archive".
        // but Bluetooth does not accept this. Let's use "*/*" instead.
        intent.setType("*/*");

        // Append file and send Intent
        File originalApk = new File(filePath);

        try {
            //Make new directory in new location
            File tempFile = new File(getExternalCacheDir() + "/ExtractedApk");
            //If directory doesn't exists create new
            if (!tempFile.isDirectory())
                if (!tempFile.mkdirs())
                    return;
            //Get application's name and convert to lowercase
            tempFile = new File(tempFile.getPath() + "/" + getString(app.labelRes).replace(" ", "").toLowerCase() + ".apk");
            //If file doesn't exists create new
            if (!tempFile.exists()) {
                if (!tempFile.createNewFile()) {
                    return;
                }
            }
            //Copy file to new location
            InputStream in = new FileInputStream(originalApk);
            OutputStream out = new FileOutputStream(tempFile);

            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
            System.out.println("File copied.");
            //Open share dialog
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(tempFile));
            startActivity(Intent.createChooser(intent, "اشتراک گذاری با "));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void loginUser() {
        final View loginDialogView = LayoutInflater.from(App.context).inflate(R.layout.login_dialog, null);
        final AlertDialog loginDialog = new AlertDialog.Builder(MainActivity.this).create();
        loginDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loginDialog.setView(loginDialogView);
        loginDialog.setCancelable(true);
        loginDialog.show();

        final EditText mobile = loginDialogView.findViewById(R.id.mobile);
        Button login = loginDialogView.findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = mobile.getText() + "";
                if (!number.equals("") && number.length() >= 10 && number.length() <= 11) {
                    Param.clear();
                    Param.put("mobile", mobile.getText() + "");
                    Param.put("token", App.prefManager.getPreference("token"));
                    Param.put("imei", App.IMEI);
                    App.webService.postRequest(Param.get(), Config.apiUrl + "setLogin", new WebService.OnPostReceived() {
                        @Override
                        public void onReceived(String message) {
                            if (!message.contains("ERROR")) {
                                loginDialog.dismiss();
                                activateUser(message);
                            } else {
                                App.showToast("عملیات به مشکل برخورد لطفا دوباره با دقت امتحان کنید");
                            }
                        }

                        @Override
                        public void onReceivedError(String message) {
                            App.showToast("مشکل در شبکه بوجود آمد آیا اینترنت خود را روشن کرده اید ؟");
                        }
                    });
                } else {
                    App.showToast("شماره موبایل وارد شده اشتباه است");
                }


            }
        });
    }

    private void activateUser(final String userId) {

        final View activeDialogView = LayoutInflater.from(App.context).inflate(R.layout.active_dialog, null);
        final AlertDialog activeDialog = new AlertDialog.Builder(MainActivity.this).create();
        activeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        activeDialog.setView(activeDialogView);
        activeDialog.setCancelable(true);
        activeDialog.show();
        final EditText code = activeDialogView.findViewById(R.id.code);
        Button active = activeDialogView.findViewById(R.id.active);
        active.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String codeText = code.getText() + "";
                if (!codeText.equals("") && codeText.length() == 4) {
                    Param.clear();
                    Param.put("code", codeText);
                    Param.put("userId", userId);
                    App.webService.postRequest(Param.get(), Config.apiUrl + "setActive", new WebService.OnPostReceived() {
                        @Override
                        public void onReceived(String message) {
                            if (!message.contains("ERROR")) {
                                App.showToast("عملیات با موفقیت انجام شد");
                                App.prefManager.savePreference("userId", message);
                                App.prefManager.savePreference("registered", "registered");
                                activeDialog.dismiss();
                                App.getServerData();


                            } else {
                                App.showToast("عملیات به مشکل برخورد لطفا دوباره با دقت امتحان کنید");
                            }
                        }

                        @Override
                        public void onReceivedError(String message) {
                            App.showToast("مشکل در شبکه بوجود آمد آیا اینترنت خود را روشن کرده اید ؟");
                        }
                    });
                } else {
                    App.showToast("کد وارد شده اشتباه است");
                }
            }
        });


    }
}
