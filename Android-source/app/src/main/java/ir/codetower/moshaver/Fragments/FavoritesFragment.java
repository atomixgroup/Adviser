package ir.codetower.moshaver.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ir.codetower.moshaver.Adapters.ContentAdapter;
import ir.codetower.moshaver.Adapters.SearchAdapter;
import ir.codetower.moshaver.App;
import ir.codetower.moshaver.Models.Content;
import ir.codetower.moshaver.R;


public class FavoritesFragment extends Fragment {
    private SearchAdapter mAdapter;
    private RecyclerView content_list;
    public FavoritesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_content, container, false);

        content_list=view.findViewById(R.id.content_list);
        ArrayList<Content> contents=Content.getFavoritesContent();
        if(contents!=null){
            ContentAdapter adapter=new ContentAdapter(contents);
            content_list.setLayoutManager(new LinearLayoutManager(App.context));
            content_list.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
        return view;
    }




}
