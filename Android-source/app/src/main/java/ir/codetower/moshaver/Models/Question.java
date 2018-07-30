package ir.codetower.moshaver.Models;

import android.content.ContentValues;
import android.database.Cursor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ir.codetower.moshaver.App;

/**
 * Created by Mr-R00t on 1/11/2018.
 */

public class Question {
    private int g_id;
    private int id;
    private String question;
    private String Answer1;
    private String Answer2;
    private String Answer3;
    private String Answer4;
    private int trueAnswer;

    public int getG_id() {
        return g_id;
    }

    public void setG_id(int g_id) {
        this.g_id = g_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer1() {
        return Answer1;
    }

    public void setAnswer1(String answer1) {
        Answer1 = answer1;
    }

    public String getAnswer2() {
        return Answer2;
    }

    public void setAnswer2(String answer2) {
        Answer2 = answer2;
    }

    public String getAnswer3() {
        return Answer3;
    }

    public void setAnswer3(String answer3) {
        Answer3 = answer3;
    }

    public String getAnswer4() {
        return Answer4;
    }

    public void setAnswer4(String answer4) {
        Answer4 = answer4;
    }

    public int getTrueAnswer() {
        return trueAnswer;
    }

    public void setTrueAnswer(int trueAnswer) {
        this.trueAnswer = trueAnswer;
    }

    public static ArrayList<Question> jsonToArray(String json) throws JSONException {
        ArrayList<Question> questions = new ArrayList<>();
        JSONArray array = null;

        array = new JSONArray(json);
        for (int i = 0; i < array.length(); i++) {
            JSONObject jsonItem = array.getJSONObject(i);
            Question question = new Question();

            question.setId(jsonItem.getInt("id"));
            question.setQuestion(jsonItem.getString("question"));
            question.setG_id(jsonItem.getInt("g_id"));
            question.setAnswer1(jsonItem.getString("answer1"));
            question.setAnswer2(jsonItem.getString("answer2"));
            question.setAnswer3(jsonItem.getString("answer3"));
            question.setAnswer4(jsonItem.getString("answer4"));
            question.setG_id(jsonItem.getInt("g_id"));
            questions.add(question);
        }
        return questions;
    }

    public static boolean batch_insert_question(ArrayList<Question> questions) {

        App.dbHelper.db.beginTransaction();
        try {
            for (Question item : questions) {
                item.insert();
            }
            App.dbHelper.db.setTransactionSuccessful();
            return true;
        } finally {
            App.dbHelper.db.endTransaction();
        }
    }

    private ContentValues parseToContent() {
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("question", question);
        contentValues.put("answer1", Answer1);
        contentValues.put("answer2", Answer2);
        contentValues.put("answer3", Answer3);
        contentValues.put("answer4", Answer4);
        contentValues.put("g_id", g_id);
        return contentValues;
    }
    private static ArrayList<Question> parseContents(Cursor paramCursor, boolean single) {
        ArrayList<Question> questions = new ArrayList<>();
        if (paramCursor != null) {
            while (paramCursor.moveToNext()) {
                Question question = new Question();
                question.setId(paramCursor.getInt(0));
                question.setG_id(paramCursor.getInt(1));
                question.setQuestion(paramCursor.getString(2));
                question.setAnswer1(paramCursor.getString(3));
                question.setAnswer2(paramCursor.getString(4));
                question.setAnswer3(paramCursor.getString(5));
                question.setAnswer4(paramCursor.getString(6));

                questions.add(question);
            }
            paramCursor.close();
        }
        return questions;
    }
    public Long insert() {

        return App.dbHelper.db.insert("question", null, parseToContent());

    }

    public static ArrayList<Question> getqQuestions() {
        Cursor cursor = App.dbHelper.getQuery("question",null);
        ArrayList<Question> questions = parseContents(cursor, false);
        cursor.close();
        if (questions.isEmpty()) {
            return null;
        } else {
            return questions;
        }

    }
    public static void clearData(){
        App.dbHelper.delete("question",null);
    }


}
