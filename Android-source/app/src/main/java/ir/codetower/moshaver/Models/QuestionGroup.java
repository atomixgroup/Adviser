package ir.codetower.moshaver.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Mr-R00t on 1/11/2018.
 */
public class QuestionGroup {
    private ArrayList<Question> questions;
    private String title;
    private int id;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }

    public static ArrayList<QuestionGroup> jsonToArray(String json) {
        ArrayList<QuestionGroup> questionGroups = new ArrayList<>();
        JSONArray array = null;
        try {
            array = new JSONArray(json);
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonItem = array.getJSONObject(i);
                QuestionGroup questionGroup = new QuestionGroup();
                questionGroup.setId(jsonItem.getInt("id"));
                questionGroup.setTitle(jsonItem.getString("title"));
                questionGroups.add(questionGroup);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return questionGroups;
    }

}