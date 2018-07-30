package ir.codetower.moshaver.Models;

import android.content.ContentValues;
import android.database.Cursor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import ir.codetower.moshaver.App;
import ir.codetower.moshaver.Param;


public class Content {
    private int id;
    private String title;
    private String category_title;
    private String body;
    private boolean favorite;
    private int g_id;
    private boolean premium;

    private String price;
    private String video="";
    private String image="";

    public String getCategory_title() {
        return category_title;
    }

    public void setCategory_title(String category_title) {
        this.category_title = category_title;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    public boolean hasVideo(){
        return !video.equals("null") && !video.equals("") && !video.equals("[]");
    }
    public boolean hasImage(){
        return !(image.equals("null") ||image.equals("") || image.equals("[]"));
    }
    public int getG_id() {
        return g_id;
    }

    public void setG_id(int g_id) {
        this.g_id = g_id;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

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

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean isPremium() {
        return premium;
    }

    public void setPremium(boolean premium) {
        this.premium = premium;
    }



    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    private static Content parseContent(Cursor paramCursor) {
        ArrayList<Content> contents = parseContents(paramCursor, true);
        if (contents.isEmpty()) {
            return null;
        }
        return contents.get(0);
    }

    private static ArrayList<Content> parseContents(Cursor paramCursor, boolean single) {
        ArrayList<Content> contents = new ArrayList<>();
        if (paramCursor != null) {
            while (paramCursor.moveToNext()) {
                Content content = new Content();
                content.setId(paramCursor.getInt(0));
                content.setTitle(paramCursor.getString(1));
                content.setBody(paramCursor.getString(2));
                content.setFavorite(paramCursor.getString(3).equals("yes"));
                content.setG_id(paramCursor.getInt(4));
                content.setPremium(paramCursor.getString(6).equals("yes"));
                content.setPrice(paramCursor.getString(7));
                content.setVideo(paramCursor.getString(8));
                content.setImage(paramCursor.getString(9));
                contents.add(content);
            }
            paramCursor.close();
        }
        return contents;
    }

    private ContentValues parseToContent() {
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("title", title);
        contentValues.put("body", body);
        contentValues.put("favorite", favorite ? "yes" : "no");
        contentValues.put("premium", premium ? "yes" : "no");
        contentValues.put("g_id", g_id);

        contentValues.put("price", price);
        contentValues.put("video", video);
        contentValues.put("image", image);

        return contentValues;
    }

    public static ArrayList<Content> jsonToContents(String json) {
        ArrayList<Content> contents = new ArrayList<>();
        JSONArray array = null;
        try {
            array = new JSONArray(json);
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonItem = array.getJSONObject(i);
                Content content = new Content();

                content.setId(jsonItem.getInt("id"));
                content.setTitle(jsonItem.getString("title"));
                content.setBody(jsonItem.getString("content"));
                content.setG_id(jsonItem.getInt("g_id"));
                content.setPremium(jsonItem.getString("premium").equals("yes"));
                content.setPrice(jsonItem.getString("price"));

                content.setVideo(jsonItem.getString("video"));
                content.setImage(jsonItem.getString("image"));
                contents.add(content);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return contents;
    }

    public static Content jsonToContent(String json) throws JSONException {
        JSONObject jsonItem = new JSONObject(json);
        Content content = new Content();
        content.setId(jsonItem.getInt("id"));
        content.setTitle(jsonItem.getString("title"));
        content.setBody(jsonItem.getString("body"));
        content.setG_id(jsonItem.getInt("g_id"));

        content.setPremium(jsonItem.getString("premium").equals("yes"));
        content.setPrice(jsonItem.getString("price"));
        content.setVideo(jsonItem.getString("video"));
        content.setImage(jsonItem.getString("image"));
        return content;
    }


    public Long insert() {

        return App.dbHelper.db.insert("content", null, parseToContent());

    }

    public static boolean batch_insert_content(ArrayList<Content> contents) {

        String pref = App.prefManager.getPreference("favorites");
        if (!pref.equals("")) {
            for (String item : pref.split("-")) {
                for (int i = 0; i < contents.size(); i++) {
                    if (item.equals(contents.get(i).getId() + "")) {
                        Content temp = contents.get(i);
                        temp.setFavorite(true);
                        contents.set(i, temp);
                    }
                }
            }
        }
        App.dbHelper.db.beginTransaction();
        try {
            for (Content item : contents) {
                item.insert();
            }
            App.dbHelper.db.setTransactionSuccessful();
            return true;
        } finally {
            App.dbHelper.db.endTransaction();
        }
    }


    public static ArrayList<Content> getContents() {
        Cursor cursor = App.dbHelper.getQuery("content", null);
        ArrayList<Content> contents = parseContents(cursor, false);
        cursor.close();
        if (contents.isEmpty()) {
            return null;
        } else {
            return contents;
        }

    }
    public static ArrayList<Content> getPrimiumContents() {
        Param.clear();
        Param.put("premium","yes");
        Cursor cursor = App.dbHelper.getQuery("content", Param.get());
        ArrayList<Content> contents = parseContents(cursor, false);
        cursor.close();
        if (contents.isEmpty()) {
            return null;
        } else {
            return contents;
        }

    }
    public static ArrayList<Content> getContents(int g_id) {
        HashMap<String, String> params = new HashMap<>();
        params.put("g_id", g_id + "");
        Cursor cursor = App.dbHelper.getQuery("content", params);
        ArrayList<Content> contents = parseContents(cursor, false);
        cursor.close();
        if (contents.isEmpty()) {
            return null;
        } else {
            return contents;
        }

    }

    public static ArrayList<Content> getFavoritesContent() {
        HashMap<String, String> params = new HashMap<>();
        params.put("favorite", "yes");
        Cursor cursor = App.dbHelper.getQuery("content", params);
        ArrayList<Content> contents = parseContents(cursor, false);
        cursor.close();
        if (contents.isEmpty()) {
            return null;
        } else {
            return contents;
        }

    }

    public static Content getContent(int id) {
        Cursor cursor = App.dbHelper.customQuery("content", "*", "id=" + id);
        return parseContent(cursor);
    }


    public int update() {
        ContentValues contentValues = parseToContent();
        String keys = "id = ?";
        String[] vals = {id + ""};
        return App.dbHelper.db.update("content", contentValues, keys, vals);
    }


    public static boolean batch_update_content(ArrayList<Content> contents) {
        App.dbHelper.db.beginTransaction();
        try {
            for (Content item : contents) {
                item.update();
            }
            App.dbHelper.db.setTransactionSuccessful();
            return true;
        } finally {
            App.dbHelper.db.endTransaction();
        }
    }

    public static void clearData() {
        App.dbHelper.delete("content", null);
    }


}
