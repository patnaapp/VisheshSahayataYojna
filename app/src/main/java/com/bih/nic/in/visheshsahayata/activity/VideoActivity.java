package com.bih.nic.in.visheshsahayata.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.bih.nic.in.visheshsahayata.R;


public class VideoActivity extends Activity {
    VideoView vid,vid_one;
    ImageView iv_play_pause,iv_play_pause_one;
    Button btn_video;

    int videoNum=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        vid = (VideoView)findViewById(R.id.img_thumnail);
        vid_one = (VideoView)findViewById(R.id.img_thumnail_one);
        iv_play_pause = (ImageView)findViewById(R.id.iv_play_pause);
        iv_play_pause_one = (ImageView)findViewById(R.id.iv_play_pause_one);
        btn_video = (Button)findViewById(R.id.btn_video);

        MediaController m = new MediaController(this);
        vid.setMediaController(m);

        String path = "android.resource://com.bih.nic.in.biharmukhyamantrisahayata/" + R.raw.testing1;

        Uri u = Uri.parse(path);

        vid.setVideoURI(u);

        vid.start();
        iv_play_pause.setVisibility(View.GONE);
        iv_play_pause_one.setVisibility(View.VISIBLE);

        btn_video.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

//                AlertDialog.Builder ab = new AlertDialog.Builder(VideoActivity.this);
//                ab.setMessage(Html.fromHtml(
//                        "<font color=#000000>बिहार मुख्यमंत्री विशेष सहायता योजना के लिए आवेदन की अंतिम तिथि खत्म हो गई है | </font>"));
//                ab.setPositiveButton("ओके", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                        dialog.dismiss();
//                    }
//                });
//                ab.create().getWindow().getAttributes().windowAnimations = R.style.alert_animation;
//                ab.show();
                Intent i=new Intent(VideoActivity.this,PhotoViewer.class);
                startActivity(i);
            }
        });



    }
    public void playVideo(View v) {
//        if(videoNum==2)
//        {
//            vid_one.stopPlayback();
//
//        }

           // videoNum = 1;
            MediaController m = new MediaController(this);
            vid.setMediaController(m);

            String path = "android.resource://com.bih.nic.in.biharmukhyamantrisahayata/" + R.raw.testing1;

            Uri u = Uri.parse(path);

            vid.setVideoURI(u);

            vid.start();
            iv_play_pause.setVisibility(View.GONE);
          iv_play_pause_one.setVisibility(View.VISIBLE);

    }
//    public void playVideo_one(View v) {
//        if(videoNum==1)
//        {
//            vid.stopPlayback();
//        }
//
//            videoNum = 2;
//            MediaController m1 = new MediaController(this);
//            vid_one.setMediaController(m1);
//
//            String path = "android.resource://com.bih.nic.in.mukhyamantrisahayata/" + R.raw.testing_new;
//
//            Uri u1 = Uri.parse(path);
//
//            vid_one.setVideoURI(u1);
//
//            vid_one.start();
//            iv_play_pause_one.setVisibility(View.GONE);
//           iv_play_pause.setVisibility(View.VISIBLE);
//
//    }


    @Override
    protected void onResume() {
        super.onResume();

        MediaController m = new MediaController(this);
        vid.setMediaController(m);

        String path = "android.resource://com.bih.nic.in.biharmukhyamantrisahayata/" + R.raw.testing1;

        Uri u = Uri.parse(path);

        vid.setVideoURI(u);

        vid.start();
        iv_play_pause.setVisibility(View.GONE);
        iv_play_pause_one.setVisibility(View.VISIBLE);
    }
}
