package com.rubus.event_hub;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;




import com.github.piasy.biv.BigImageViewer;
import com.github.piasy.biv.indicator.progresspie.ProgressPieIndicator;

import com.github.piasy.biv.loader.glide.GlideImageLoader;
import com.github.piasy.biv.view.BigImageView;


public class LongImageActivity extends AppCompatActivity {

    protected String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        BigImageViewer.initialize(GlideImageLoader.with(getApplicationContext()));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_long_image);

        url= getIntent().getStringExtra("url");


        final BigImageView bigImageView = (BigImageView) findViewById(R.id.mBigImage);


        bigImageView.setProgressIndicator(new ProgressPieIndicator());


                bigImageView.showImage(Uri.parse(
                        url));

    }
}