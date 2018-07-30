package ir.codetower.moshaver.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ir.codetower.moshaver.OnItemClickListener;
import ir.codetower.moshaver.R;


@SuppressLint("ValidFragment")
public class MainFragment extends Fragment implements View.OnClickListener {
    private OnItemClickListener onItemClickLinstener;
    public MainFragment(OnItemClickListener onItemClickLinstener) {
        this.onItemClickLinstener=onItemClickLinstener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_main, container, false);
        view.findViewById(R.id.shareapp).setOnClickListener(this);
        view.findViewById(R.id.favorites).setOnClickListener(this);
        view.findViewById(R.id.search).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        onItemClickLinstener.onItemClick(view.getId());
    }





}
