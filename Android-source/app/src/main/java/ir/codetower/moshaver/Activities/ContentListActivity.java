package ir.codetower.moshaver.Activities;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import java.util.ArrayList;

import ir.codetower.moshaver.Adapters.ContentAdapter;
import ir.codetower.moshaver.App;
import ir.codetower.moshaver.Config;
import ir.codetower.moshaver.CustomTextView;
import ir.codetower.moshaver.Helpers.WebService;
import ir.codetower.moshaver.Models.Category;
import ir.codetower.moshaver.Models.Content;
import ir.codetower.moshaver.Param;
import ir.codetower.moshaver.R;

public class ContentListActivity extends AppCompatActivity {
    RecyclerView content_list;
    private int wholePrice = 0;
    private int g_id;
    private LinearLayout scrollRoot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_list);
        content_list = findViewById(R.id.list);
        Intent intent=getIntent();
        scrollRoot=findViewById(R.id.scroll_root);


        if(intent.hasExtra("id")){
            g_id=intent.getExtras().getInt("id");
        }
        else{
            finish();
        }

        final ArrayList<Content> contents = Content.getContents(g_id);
        Category category=Category.getCAtegory(g_id);
        CustomTextView title=findViewById(R.id.title);
        title.setText(category.getTitle());

        if (contents != null) {
            for (Content content :
                    contents) {
                if (!content.isPremium()) {
                    wholePrice += Integer.parseInt(content.getPrice());
                }
            }
//            for (Content content:contents) {
//                View item_view=LayoutInflater.from(ContentListActivity.this).inflate(R.layout.content_adapter,null,false);
//                CustomTextView item=item_view.findViewById(R.id.item_title);
//                item.setText(content.getTitle());
//                item=item_view.findViewById(R.id.item_price);
//                item.setText(content.getPrice()+" تومان");
//                scrollRoot.addView(item_view);
//            }
            ContentAdapter adapter = new ContentAdapter(contents);
            content_list.setLayoutManager(new LinearLayoutManager(App.context));
            content_list.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            final AppCompatButton buy_curse = findViewById(R.id.buy_curse);
            buy_curse.setText("مبلغ پرداختی کل دوره : "+wholePrice);
            for (Content content : contents) {
                if (!content.isPremium()) {
                    buy_curse.setVisibility(View.VISIBLE);
                    break;
                }
            }
            buy_curse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                        final View paidDialogView = LayoutInflater.from(App.context).inflate(R.layout.select_paid_dialog, null);
                        final AlertDialog paidDialog = new AlertDialog.Builder(ContentListActivity.this).create();
                        paidDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        paidDialog.setView(paidDialogView);
                        paidDialog.setCancelable(true);
                        paidDialog.show();

                        Button realtime = paidDialogView.findViewById(R.id.realtime);

                        AppCompatTextView amount;
                        amount = paidDialogView.findViewById(R.id.amount);
                        float price = ((float) wholePrice - ((float) wholePrice * 15 / 100));
                        price /= 100;
                        price = (float) Math.floor(price);
                        price *= 100;
                        price += 100;
                        amount.setText("مبلغ قابل پرداخت برای دوره: " + price + " تومان با 15 درصد تخفیف");
                        realtime.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String url = Config.apiUrl + "premiumRequest?id=" + g_id + "&userId=" + App.prefManager.getPreference("userId") + "&type=realtime&set=category";
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse(url));
                                startActivity(i);
                            }
                        });

                }

            });
        }


    }


}
