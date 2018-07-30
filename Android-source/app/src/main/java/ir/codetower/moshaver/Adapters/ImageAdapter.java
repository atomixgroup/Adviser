package ir.codetower.moshaver.Adapters;

import android.content.Intent;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import ir.codetower.moshaver.Activities.PhotoActivity;
import ir.codetower.moshaver.Activities.VideoActivity;
import ir.codetower.moshaver.App;
import ir.codetower.moshaver.R;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private String[] urls;
    public ImageAdapter(String[] urls){
        this.urls=urls;
    }
    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageAdapter.ImageViewHolder(LayoutInflater.from(App.context).inflate(R.layout.image_adapter,parent,false));
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        holder.bind(urls[position]);
    }

    @Override
    public int getItemCount() {
        return urls.length;
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{
        AppCompatImageView image;
        LinearLayout item_root;
        public ImageViewHolder(View itemView) {
            super(itemView);
            image=itemView.findViewById(R.id.item_image);
            item_root=itemView.findViewById(R.id.item_root);
            item_root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(App.context, PhotoActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("url",urls[getLayoutPosition()]);
                    App.context.startActivity(intent);
                }
            });
        }
        public void bind(String url){
            Picasso.with(App.context).load(url).into(image);
        }
    }
}