package ir.codetower.moshaver.Adapters;

import android.content.Intent;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Random;

import ir.codetower.moshaver.Activities.ContentListActivity;
import ir.codetower.moshaver.App;
import ir.codetower.moshaver.CustomTextView;
import ir.codetower.moshaver.Models.Category;
import ir.codetower.moshaver.OnItemClickListener;
import ir.codetower.moshaver.R;

/**
 * Created by Mr-R00t on 2/10/2018.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private ArrayList<Category> categories;
    private Random random;
    public CategoryAdapter(ArrayList<Category> contents){
        this.categories =contents;

        random=new Random();
    }
    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(App.context).inflate(R.layout.category_adapter,parent,false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        holder.bind(categories.get(position));
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder{
        private CustomTextView item_title;
        private CardView item_root;
        AppCompatImageView item_image;
        public CategoryViewHolder(View itemView) {
            super(itemView);
            item_title=itemView.findViewById(R.id.item_title);
            item_root=itemView.findViewById(R.id.item_root);
            item_image=itemView.findViewById(R.id.item_image);

            item_root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(App.context, ContentListActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("id",categories.get(getLayoutPosition()).getId());
                    App.context.startActivity(intent);
                }
            });
        }

        public void bind( Category category)
        {
            int number=random.nextInt(7)+1;
            switch (number){
                case 1:item_image.setImageResource(R.drawable.back1);
                    break;
                case 2:item_image.setImageResource(R.drawable.back2);
                    break;
                case 3:item_image.setImageResource(R.drawable.back3);
                    break;
                case 4:item_image.setImageResource(R.drawable.back4);
                    break;
                case 5:item_image.setImageResource(R.drawable.back5);
                    break;
                case 6:item_image.setImageResource(R.drawable.back6);
                    break;
                case 7:item_image.setImageResource(R.drawable.back7);
                    break;
                case 8:item_image.setImageResource(R.drawable.back8);
                    break;
            }
            item_title.setText(category.getTitle());
        }
    }

}
