package ir.codetower.moshaver.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import ir.codetower.moshaver.Adapters.ContentAdapter;
import ir.codetower.moshaver.Adapters.SearchAdapter;
import ir.codetower.moshaver.App;
import ir.codetower.moshaver.Models.Content;
import ir.codetower.moshaver.R;

public class FavoritesActivity extends AppCompatActivity {
    private SearchAdapter mAdapter;
    private RecyclerView content_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        content_list=findViewById(R.id.content_list);
        ArrayList<Content> contents=Content.getFavoritesContent();
        if(contents!=null){
            ContentAdapter adapter=new ContentAdapter(contents);
            content_list.setLayoutManager(new LinearLayoutManager(App.context));
            content_list.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

    }
}
