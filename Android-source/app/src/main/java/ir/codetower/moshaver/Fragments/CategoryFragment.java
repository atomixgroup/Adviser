package ir.codetower.moshaver.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ir.codetower.moshaver.Adapters.CategoryAdapter;
import ir.codetower.moshaver.App;
import ir.codetower.moshaver.Models.Category;
import ir.codetower.moshaver.OnItemClickListener;
import ir.codetower.moshaver.R;


@SuppressLint("ValidFragment")
public class CategoryFragment extends Fragment {

    RecyclerView content_list;
    public CategoryFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_category, container, false);
        content_list=view.findViewById(R.id.content_list);
        ArrayList<Category> contents= Category.getCategories();
        if(contents!=null){
            CategoryAdapter adapter=new CategoryAdapter(contents);
            content_list.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
            content_list.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

        return view;
    }




}
