package ir.codetower.moshaver.Adapters;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


import java.util.HashMap;

import ir.codetower.moshaver.Activities.VideoActivity;
import ir.codetower.moshaver.App;
import ir.codetower.moshaver.Config;
import ir.codetower.moshaver.R;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {
    private String[] urls;
    public VideoAdapter(String[] urls){
        this.urls=urls;
    }
    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VideoViewHolder(LayoutInflater.from(App.context).inflate(R.layout.video_adapter,parent,false));
    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return urls.length;
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder{
        AppCompatImageView image;
        LinearLayout item_root;
        AppCompatTextView item_text;
        public VideoViewHolder(View itemView) {
            super(itemView);
            image=itemView.findViewById(R.id.item_image);
            item_root=itemView.findViewById(R.id.item_root);
            item_text=itemView.findViewById(R.id.item_text);
            item_root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(App.context, VideoActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("url",urls[getLayoutPosition()]);
                    App.context.startActivity(intent);
                }
            });
        }
        public void bind(int pos){
            item_text.setText("ویدیوی شماره "+(pos+1));
        }

    }
    public static Bitmap retriveVideoFrameFromVideo(String videoPath)throws Throwable
    {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try
        {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            if (Build.VERSION.SDK_INT >= 14)
                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            else
                mediaMetadataRetriever.setDataSource(videoPath);
            //   mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever.getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new Throwable("Exception in retriveVideoFrameFromVideo(String videoPath)"+ e.getMessage());
        }
        finally
        {
            if (mediaMetadataRetriever != null)
            {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }
}
