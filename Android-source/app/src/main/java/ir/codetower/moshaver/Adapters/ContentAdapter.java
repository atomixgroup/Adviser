package ir.codetower.moshaver.Adapters;

import android.content.Intent;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;

import ir.codetower.moshaver.Activities.ContentActivity;
import ir.codetower.moshaver.App;
import ir.codetower.moshaver.CustomTextView;
import ir.codetower.moshaver.Models.Content;
import ir.codetower.moshaver.R;

/**
 * Created by Mr-R00t on 2/10/2018.
 */

public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ContentViewHolder> {
    private ArrayList<Content> contents;
    public ContentAdapter(ArrayList<Content> contents){
        this.contents=contents;
    }
    @Override
    public ContentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(App.context).inflate(R.layout.content_adapter,parent,false);
        return new ContentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ContentViewHolder holder, int position) {
        holder.bind(contents.get(position));
    }

    @Override
    public int getItemCount() {
        return contents.size();
    }

    public class ContentViewHolder extends RecyclerView.ViewHolder{
        private CustomTextView item_title;
        private CustomTextView item_price;
        private CardView item_root;

        public ContentViewHolder(View itemView) {
            super(itemView);
            item_title=itemView.findViewById(R.id.item_title);
            item_price=itemView.findViewById(R.id.item_price);
            item_root=itemView.findViewById(R.id.item_root);
            item_root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(App.context,ContentActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("id",contents.get(getLayoutPosition()).getId());
                    App.context.startActivity(intent);
                }
            });
        }

        public void bind(Content content) {
            item_title.setText(content.getTitle());
            if (content.getPrice().equals("0")){
                item_price.setText("رایگان");
            }
            else{
                item_price.setText(content.getPrice()+" تومان");
            }

        }
    }
}
