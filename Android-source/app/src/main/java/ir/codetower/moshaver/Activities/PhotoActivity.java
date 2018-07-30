package ir.codetower.moshaver.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;


import com.squareup.picasso.Picasso;

import ir.codetower.moshaver.R;

public class PhotoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatImageView image=findViewById(R.id.image);
        setContentView(R.layout.activity_photo);
        if(!getIntent().hasExtra("url")){
            finish();
        }
        else {
            Picasso.with(PhotoActivity.this).load(getIntent().getExtras().getString("url")).into(image);
        }
    }
}
