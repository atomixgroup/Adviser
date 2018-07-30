package ir.codetower.moshaver.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ir.codetower.moshaver.Adapters.SearchAdapter;
import ir.codetower.moshaver.App;
import ir.codetower.moshaver.Models.Content;
import ir.codetower.moshaver.R;


public class SearchFragment extends Fragment {
    private SearchAdapter mAdapter;
    private RecyclerView content_list;
    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_search, container, false);
        final AppCompatEditText search=view.findViewById(R.id.search_edit);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (mAdapter != null) mAdapter.getFilter().filter(search.getText());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        content_list=view.findViewById(R.id.content_list);
        ArrayList<Content> contents=Content.getContents();
        if(contents!=null){
            mAdapter=new SearchAdapter(contents);
            content_list.setLayoutManager(new LinearLayoutManager(App.context));
            content_list.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }
        return view;
    }




}
