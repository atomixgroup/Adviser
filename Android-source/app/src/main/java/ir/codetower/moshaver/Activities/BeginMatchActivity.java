package ir.codetower.moshaver.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

import ir.codetower.moshaver.App;
import ir.codetower.moshaver.Config;
import ir.codetower.moshaver.Helpers.WebService;
import ir.codetower.moshaver.Models.Question;
import ir.codetower.moshaver.R;


public class BeginMatchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_begin_match);

        final AppCompatTextView alertMessage=findViewById(R.id.alert);
        final AVLoadingIndicatorView progress=findViewById(R.id.progress);
        final LayoutInflater factory = LayoutInflater.from(this);
        final View dialogView = factory.inflate(R.layout.cart_dialog, null);
        final AlertDialog dialog = new AlertDialog.Builder(BeginMatchActivity.this).create();
        dialog.setView(dialogView);
        final EditText name = (EditText) dialogView.findViewById(R.id.edtName);
        final EditText address = (EditText) dialogView.findViewById(R.id.edtAddress);
        final EditText postal_code = (EditText) dialogView.findViewById(R.id.postal_code);
        final EditText number = (EditText) dialogView.findViewById(R.id.edtNum);
        AppCompatTextView pageTitle=dialogView.findViewById(R.id.pageTitle);
        pageTitle.setText("لطفا مشخصات خود را به صورت کامل پر کنید تا در صورت برنده شدن یکی از کارشناسان ما با شما در تماس خواهند بود");

        Button dismiss = (Button) dialogView.findViewById(R.id.btnDisMiss);
        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });
        Button send = (Button) dialogView.findViewById(R.id.btnSend);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String,String> params=new HashMap<>();

                params.put("name",name.getText()+"");
                params.put("address",address.getText()+"");
                params.put("postal_code",postal_code.getText()+"");
                params.put("number",number.getText()+"");
                App.prefManager.savePreferences(params);
                params.put("IMEI",App.IMEI);
                dialog.dismiss();
                App.webService.postRequest(params, Config.apiUrl + "getQuestions", new WebService.OnPostReceived() {
                    @Override
                    public void onReceived(String message) {
                        progress.setVisibility(View.GONE);
                        if(message.equals("E1") || message.equals("ERROR:1") || message.equals("ERROR:0")){
                            alertMessage.setText("مشکل در شبکه به وجود آمده است لطفا بعدا امتحان فرمایید");
                            alertMessage.setVisibility(View.VISIBLE);
                        }
                        else if(message.equals("out")){
                            alertMessage.setText("شما قبلا در مسابقه شرکت کردید");
                            alertMessage.setVisibility(View.VISIBLE);
                        }
                        else{
                            try {
                                ArrayList<Question> questions = Question.jsonToArray(message);
                                Question.clearData();
                                Question.batch_insert_question(questions);
                                AppCompatButton start=findViewById(R.id.start);
                                start.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent=new Intent(BeginMatchActivity.this,MatchStart.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                                findViewById(R.id.linearLayout10).setVisibility(View.VISIBLE);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onReceivedError(String message) {
                        alertMessage.setText("مشکل در شبکه به وجود آمده است لطفا بعدا امتحان فرمایید");
                        alertMessage.setVisibility(View.GONE);
                    }
                });
            }
        });
        name.setText(App.prefManager.getPreference("name"));
        address.setText(App.prefManager.getPreference("address"));
        postal_code.setText(App.prefManager.getPreference("postal_code"));
        number.setText(App.prefManager.getPreference("number"));
        dialog.show();
    }
}
