package ir.codetower.moshaver.Helpers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ir.codetower.moshaver.BuildConfig;
import ir.codetower.moshaver.Config;

public class DbHelper
        extends SQLiteOpenHelper {
    public String content_table = "content";

    private final String SQL_CREATE_CONTENT = "CREATE TABLE IF NOT EXISTS " + content_table + "( " +
            "id INTEGER UNIQUE ," +
            "title VARCHAR(256)," +
            "body VARCHAR(256)," +
            "favorite VARCHAR(5)," +
            "g_id INTEGER,"+
            "audio_count INTEGER,"+
            "premium VARCHAR(5),"+
            "price VARCHAR(256),"+
            "video TEXT,"+
            "image TEXT)";


    public String category_table = "category";

    private final String SQL_CREATE_CATEGORY = "CREATE TABLE IF NOT EXISTS " + category_table + "( " +
            "id INTEGER UNIQUE ," +
            "title VARCHAR(256))";
    public String question_table = "question";

    private final String SQL_CREATE_QUESTION = "CREATE TABLE IF NOT EXISTS " + question_table + "( " +
            "id INTEGER UNIQUE ," +
            "g_id INTEGER ," +
            "question TEXT ," +
            "answer1 TEXT ," +
            "answer2 TEXT ," +
            "answer3 TEXT ," +
            "answer4 TEXT )";


    public SQLiteDatabase db = getWritableDatabase();

    public void onCreate(SQLiteDatabase paramSQLiteDatabase) {

        paramSQLiteDatabase.execSQL(SQL_CREATE_CONTENT);
        paramSQLiteDatabase.execSQL(SQL_CREATE_QUESTION);
        paramSQLiteDatabase.execSQL(SQL_CREATE_CATEGORY);

    }

    public void onUpgrade(SQLiteDatabase paramSQLiteDatabase, int paramInt1, int paramInt2) {
        paramSQLiteDatabase.execSQL("DROP TABLE IF EXISTS " + content_table);
        paramSQLiteDatabase.execSQL("DROP TABLE IF EXISTS " + question_table);
        paramSQLiteDatabase.execSQL("DROP TABLE IF EXISTS " + category_table);

        onCreate(paramSQLiteDatabase);
    }

    public DbHelper(Context paramContext) {
        super(paramContext, Config.dbName, null, BuildConfig.VERSION_CODE);
    }
    public Cursor customQuery(String table, String select, String where) {
        String sql = "SELECT " + select + " FROM " + table;
        if (where != null) {
            sql += " WHERE " + where;
        }
        return this.db.rawQuery(sql, null);
    }
    public int delete(String table, String field, String value){
        HashMap<String,String> params=new HashMap<>();
        params.put(field,value);
        return delete(table,params);
    }
    public int delete(String table, HashMap<String, String> where) {
        String keys = "";
        boolean flag = false;
        ArrayList<String> values = new ArrayList<>();
        String[] vals;
        if (where == null) {
            vals = null;
            keys = null;
        } else {

            for (Map.Entry<String, String> entry : where.entrySet()) {
                if (flag) {
                    keys += " and ";
                }
                keys += entry.getKey() + "=?";
                values.add(entry.getValue());
                flag = true;
            }
            vals = new String[values.size()];
            for (int i = 0; i < values.size(); i++) {
                vals[i] = values.get(i);
            }
        }
        return db.delete(table, keys, vals);
    }

    protected void finalize()
            throws Throwable {
        this.db.close();
        super.finalize();
    }

    public Cursor getQuery(String table, HashMap<String, String> where) { //get data from database in cursor
        String query = "SELECT * FROM " + table;
        String keys = "";
        boolean flag = false;
        ArrayList<String> values = new ArrayList<>();
        String[] vals;
        if (where == null) {
            vals = null;
        } else {
            query += " WHERE ";
            for (Map.Entry<String, String> entry : where.entrySet()) {
                if (flag) {
                    keys += " and ";
                }
                keys += entry.getKey() + "=?";
                values.add(entry.getValue());
                flag = true;
            }
            query += keys;
            vals = new String[values.size()];
            for (int i = 0; i < values.size(); i++) {
                vals[i] = values.get(i);
            }
        }


        return db.rawQuery(query, vals);
    }
    public Cursor getQuery2(String table, HashMap<String, String> where) { //get data from database in cursor
        String query = "SELECT * FROM " + table;
        String keys = "";
        boolean flag = false;
        ArrayList<String> values = new ArrayList<>();
        String[] vals;
        if (where == null) {
            vals = null;
        } else {
            query += " WHERE ";
            for (Map.Entry<String, String> entry : where.entrySet()) {
                if (flag) {
                    keys += " and ";
                }
                keys += entry.getKey() + "=?";
                values.add(entry.getValue());
                flag = true;
            }
            query += keys;
            vals = new String[values.size()];
            for (int i = 0; i < values.size(); i++) {
                vals[i] = values.get(i);
            }
        }
        query+=" ORDER BY section asc";

        return db.rawQuery(query, vals);
    }





}
