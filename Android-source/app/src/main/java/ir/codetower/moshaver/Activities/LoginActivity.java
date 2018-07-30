package ir.codetower.moshaver.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import ir.codetower.moshaver.App;
import ir.codetower.moshaver.Config;
import ir.codetower.moshaver.CustomTextView;
import ir.codetower.moshaver.Helpers.WebService;
import ir.codetower.moshaver.Param;
import ir.codetower.moshaver.R;

public class LoginActivity extends AppCompatActivity {
    String userId="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final Button login=findViewById(R.id.login);
        final Button active=findViewById(R.id.active);
        final EditText mobile=findViewById(R.id.mobile);
        final EditText code=findViewById(R.id.code);
        final LinearLayout login_root,active_tooy;
        login_root=findViewById(R.id.login_root);
        active_tooy=findViewById(R.id.active_root);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login_root.setVisibility(View.GONE);
                active_tooy.setVisibility(View.VISIBLE);
                String number = mobile.getText() + "";
                if (!number.equals("") && number.length() >= 10 && number.length() <= 11) {
                    Param.clear();
                    Param.put("mobile", mobile.getText() + "");
                    App.webService.postRequest(Param.get(), Config.apiUrl + "setLogin", new WebService.OnPostReceived() {
                        @Override
                        public void onReceived(String message) {
                            if (!message.contains("ERROR")) {
                                userId=message;
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
                                App.getServerData();
                                Intent intent=new Intent(LoginActivity.this,MainActivity2.class);
                                startActivity(intent);
                                finish();

                            } else {
                                App.showToast("عملیات به مشکل برخورد لطفا دوباره با دقت امتحان کنید");
                                login_root.setVisibility(View.VISIBLE);
                                active_tooy.setVisibility(View.GONE);
                            }
                        }
                        @Override
                        public void onReceivedError(String message) {
                            App.showToast("مشکل در شبکه بوجود آمد آیا اینترنت خود را روشن کرده اید ؟");
                            login_root.setVisibility(View.VISIBLE);
                            active_tooy.setVisibility(View.GONE);
                        }
                    });
                } else {
                    App.showToast("کد وارد شده اشتباه است");
                }
            }
        });
    }
}
