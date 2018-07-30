package ir.codetower.moshaver.Models;

import android.content.ContentValues;
import android.database.Cursor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ir.codetower.moshaver.App;

public class Category {
    private static String tableName="category";


    private int id;
    private String title;
    private int price;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    private static Category parseContent(Cursor paramCursor) {
        ArrayList<Category> contents = parseContents(paramCursor, true);
        if (contents.isEmpty()) {
            return null;
        }
        return contents.get(0);
    }

    private static ArrayList<Category> parseContents(Cursor paramCursor, boolean single) {
        ArrayList<Category> contents = new ArrayList<>();
        if (paramCursor != null) {
            while (paramCursor.moveToNext()) {
                Category content = new Category();
                content.setId(paramCursor.getInt(0));
                content.setTitle(paramCursor.getString(1));
                contents.add(content);
            }
            paramCursor.close();
        }
        return contents;
    }

    private  ContentValues parseToContent() {
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("title", title);
        return contentValues;
    }
    public  Long insert() {
        int res = update();
        if (res == 0) {
            return App.dbHelper.db.insert(tableName, null, parseToContent());
        } else {
            return (long) res;
        }
    }
    public static boolean batch_insert_content(ArrayList<Category> contents) {


        App.dbHelper.db.beginTransaction();
        try {
            for (Category item : contents) {
                item.insert();
            }
            App.dbHelper.db.setTransactionSuccessful();
            return true;
        } finally {
            App.dbHelper.db.endTransaction();
        }
    }


    public static ArrayList<Category> getCategories() {
        Cursor cursor = App.dbHelper.getQuery(tableName,null);
        ArrayList<Category> contents = parseContents(cursor, false);
        cursor.close();
        if (contents.isEmpty()) {
            return null;
        } else {
            return contents;
        }

    }

    public static Category getCAtegory(int id) {
        Cursor cursor = App.dbHelper.customQuery(tableName,"*","id="+id);
        return parseContent(cursor);
    }



    public  int update() {
        ContentValues contentValues = parseToContent();
        String keys = "id = ?";
        String[] vals = { id+ ""};
        return App.dbHelper.db.update(tableName, contentValues, keys, vals);
    }

    public static ArrayList<Category> jsonToObject(String json) {
        ArrayList<Category> contents = new ArrayList<>();
        JSONArray array = null;
        try {
            array = new JSONArray(json);
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonItem = array.getJSONObject(i);
                Category content = new Category();

                content.setId(jsonItem.getInt("id"));
                content.setTitle(jsonItem.getString("title"));
                contents.add(content);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return contents;
    }

    public static boolean batch_update_content(ArrayList<Category> contents) {
        App.dbHelper.db.beginTransaction();
        try {
            for (Category item : contents) {
                item.update();
            }
            App.dbHelper.db.setTransactionSuccessful();
            return true;
        } finally {
            App.dbHelper.db.endTransaction();
        }
    }
    public static void clearData(){
        App.dbHelper.delete(tableName,null);
    }



}
