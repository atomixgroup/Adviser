package ir.codetower.moshaver.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import ir.codetower.moshaver.App;
import ir.codetower.moshaver.Config;
import ir.codetower.moshaver.CustomAppIntro;
import ir.codetower.moshaver.Fragments.QuestionSlideFragment;
import ir.codetower.moshaver.Helpers.WebService;
import ir.codetower.moshaver.Models.Question;
import ir.codetower.moshaver.R;


public class MatchStart extends CustomAppIntro {
    private ArrayList<QuestionSlideFragment> questionSlideFragment = new ArrayList<>();
    private Long time;
    private boolean matchFinished=false;
    private Thread timer;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTimer();
        ArrayList<Question> questions = Question.getqQuestions();

        for (Question question : questions) {
            QuestionSlideFragment fragment = QuestionSlideFragment.newInstance();
            fragment.setQuestion(question);
            questionSlideFragment.add(fragment);
            addSlide(fragment);
        }
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        finish();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        setMatchFinished();
        finish();
    }

    private void setMatchFinished() {
        JSONArray array = new JSONArray();
        try {
            for (QuestionSlideFragment questionFragment : questionSlideFragment) {
                JSONObject object = new JSONObject();

                object.put("id", questionFragment.getQuestion().getId());
                object.put("option", questionFragment.getQuestion().getTrueAnswer());
                array.put(object);
            }
            HashMap<String, String> params = new HashMap<>();
            params.put("IMEI", App.IMEI);
            params.put("data", array.toString());
            App.webService.postRequest(params, Config.apiUrl + "setMatch", new WebService.OnPostReceived() {
                @Override
                public void onReceived(String message) {
                    Toast.makeText(App.context,"در صورت درست بودن جواب های شما برای شما جایزه ای در نظر گرفته می شود",Toast.LENGTH_LONG).show();
                }

                @Override
                public void onReceivedError(String message) {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
        matchFinished=true;
        if(timer!=null){
            timer.interrupt();
        }
    }

    private void setTimer() {
        final AppCompatTextView timeTxt = findViewById(R.id.time);
        HashMap<String, String> params = new HashMap<>();
        params.put("IMEI", App.IMEI);
        App.webService.postRequest(params, Config.apiUrl + "getTimer", new WebService.OnPostReceived() {
            @Override
            public void onReceived(String message) {
                try {
                    time = Long.parseLong(message);
                    timer=new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (true) {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                time--;


                                timeTxt.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        long minutes = (time % 3600) / 60;
                                        long seconds = time % 60;
                                        timeTxt.setText(String.format("%02d:%02d", minutes, seconds));
                                    }
                                });
                                if (time <= 0 || matchFinished) {
                                    break;
                                }

                            }
                            if(!matchFinished){
                                setMatchFinished();
                                finish();
                            }

                        }
                    });
                    timer.start();
                } catch (Exception e) {
                    if(message.equals("out")){
                        Toast.makeText(App.context,"وقت مسابقه برای شما تمام شده است",Toast.LENGTH_LONG).show();

                    }
                    else{
                        Toast.makeText(App.context,"مشکل در شبکه بوجود آمده",Toast.LENGTH_LONG).show();

                    }
                    Question.clearData();
                    finish();
                }
            }

            @Override
            public void onReceivedError(String message) {
                Toast.makeText(App.context,"مشکل در شبکه بوجود آمده",Toast.LENGTH_LONG).show();

                Question.clearData();
                finish();
            }
        });
    }
}
