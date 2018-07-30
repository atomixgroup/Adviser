package ir.codetower.moshaver.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import ir.codetower.moshaver.Activities.ContentActivity;
import ir.codetower.moshaver.Adapters.ContentAdapter;
import ir.codetower.moshaver.App;
import ir.codetower.moshaver.Config;
import ir.codetower.moshaver.Helpers.WebService;
import ir.codetower.moshaver.Models.Content;
import ir.codetower.moshaver.Param;
import ir.codetower.moshaver.R;


@SuppressLint("ValidFragment")
public class ContentFragment extends Fragment {
    private Activity main;
    RecyclerView content_list;
    private int wholePrice = 0;
    private int g_id;

    public ContentFragment(Activity main, int g_id) {
        // Required empty public constructor
        this.g_id = g_id;
        this.main = main;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_content, container, false);
        content_list = view.findViewById(R.id.content_list);
        final ArrayList<Content> contents = Content.getContents(g_id);

        if (contents != null) {
            for (Content content :
                    contents) {
                if (!content.isPremium()) {
                    wholePrice += Integer.parseInt(content.getPrice());
                }
            }
            ContentAdapter adapter = new ContentAdapter(contents);
            content_list.setLayoutManager(new LinearLayoutManager(App.context));
            content_list.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            final AppCompatButton buy_curse = view.findViewById(R.id.buy_curse);
            for (Content content : contents) {
                if (!content.isPremium()) {
                    buy_curse.setVisibility(View.VISIBLE);
                    break;
                }
            }
            buy_curse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (App.prefManager.getPreference("registered").equals("registered")) {
                        final View paidDialogView = LayoutInflater.from(App.context).inflate(R.layout.select_paid_dialog, null);
                        final AlertDialog paidDialog = new AlertDialog.Builder(main).create();
                        paidDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        paidDialog.setView(paidDialogView);
                        paidDialog.setCancelable(true);
                        paidDialog.show();
//                        Button wallet = paidDialogView.findViewById(R.id.wallet);
                        Button realtime = paidDialogView.findViewById(R.id.realtime);

                        AppCompatTextView walletPrice, amount;
                        walletPrice = paidDialogView.findViewById(R.id.wallet_price);
                        amount = paidDialogView.findViewById(R.id.amount);
                        float price = ((float) wholePrice - ((float) wholePrice * 15 / 100));
                        price /= 100;
                        price = (float) Math.floor(price);
                        price *= 100;
                        price += 100;
                        amount.setText("مبلغ قابل پرداخت برای دوره: " + price + " تومان با 15 درصد تخفیف");
                        walletPrice.setText("موجودی کیف پول : " + App.prefManager.getPreference("wallet"));

//                        wallet.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                Param.clear();
//                                Param.put("userId", App.prefManager.getPreference("UserId"));
//                                Param.put("g_id", g_id + "");
//                                App.webService.postRequest(Param.get(), Config.apiUrl + "premiumWalletRequest", new WebService.OnPostReceived() {
//                                    @Override
//                                    public void onReceived(String message) {
//                                        if (message.equals("OK:1")) {
//
//                                            Toast.makeText(App.context, "عملیات با موفقیت انجام شد.", Toast.LENGTH_LONG).show();
//                                            for (Content content : contents) {
//                                                content.setPremium(true);
//                                                content.update();
//                                            }
//                                            buy_curse.setVisibility(View.GONE);
////
//                                        } else if (message.equals("ERROR:-1")) {
//                                            Toast.makeText(App.context, "موجودی کیف پول شما کافی نمیباشد.", Toast.LENGTH_LONG).show();
//                                        }
//                                        else if(message.equals("ERROR:0")){
//                                            App.showToast("نام کاربری شما شناسایی نشده است دوباره وارد شوید");
//                                            loginUser();
//                                        }
//                                        else {
//                                            Toast.makeText(App.context, "مشکل در عملیات بوجود آمده است.", Toast.LENGTH_LONG).show();
//
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onReceivedError(String message) {
//                                        Toast.makeText(App.context, "مشکل در شبکه بوجود آمده است.", Toast.LENGTH_LONG).show();
//                                    }
//                                });
//                                paidDialog.dismiss();
//                            }
//                        });
                        realtime.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String url = Config.apiUrl + "premiumRequest?id=" + g_id + "&userId=" + App.prefManager.getPreference("userId") + "&type=realtime&set=category";
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse(url));
                                startActivity(i);
                            }
                        });
                    } else {

                    }
                }

            });
        }
        return view;
    }



}
