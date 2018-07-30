package ir.codetower.moshaver;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import ir.codetower.moshaver.Activities.MainActivity;
import ir.codetower.moshaver.Activities.SplashActivity;
import ir.codetower.moshaver.Helpers.DbHelper;
import ir.codetower.moshaver.Helpers.SharedPrefManager;
import ir.codetower.moshaver.Helpers.WebService;
import ir.codetower.moshaver.Models.Category;
import ir.codetower.moshaver.Models.Content;

/**
 * Created by Mr-R00t on 2/10/2018.
 */

public class App extends Application {
    public static Context context;
    public static DbHelper dbHelper;
    public static SharedPrefManager prefManager;
    public static WebService webService;
    public static String IMEI="0";
    public static HashMap<String,String> params;
    public static String Token="0";
    private static Typeface irsans;
    private static Typeface khandevane;
    private static Typeface yekan;
    public static AssetManager assetManager;
    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();
        dbHelper = new DbHelper(context);

        prefManager=new SharedPrefManager(Config.dbName);
        webService=new WebService();

        assetManager = getAssets();
        yekan = Typeface.createFromAsset(assetManager, "fonts/yekan.ttf");
        khandevane = Typeface.createFromAsset(assetManager, "fonts/khandevane.ttf");
        irsans = Typeface.createFromAsset(assetManager, "fonts/irsans.ttf");

    }
    public static void showToast(String text){
        Toast.makeText(context,text,Toast.LENGTH_LONG).show();
    }
    public static void makeDirectories() {
        new File(Config.sdTempFolderAddress).mkdirs();
    }
    public static void makeDirectories(String path) {
        new File(path).mkdirs();
    }
    public static void getServerData(){
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

            }

            @Override
            public void onReceivedError(String message) {

            }
        });
    }
    public static Typeface getFontWithName(String name) {
        switch (name) {
            case "khandevane":
                return khandevane;
            case "yekan":
                return yekan;
            case "irsans":
                return irsans;
            default:
                return yekan;
        }
    }

    public static void removeDir(String dir) {
        File file=new File(dir);
        if(file.isDirectory()){
            for (File item :file.listFiles()) {
                if(item.isDirectory()){
                    removeDir(item.getAbsolutePath());
                }
                else{
                    item.delete();
                }
            }
        }
        else{
            file.delete();
        }
    }
}
