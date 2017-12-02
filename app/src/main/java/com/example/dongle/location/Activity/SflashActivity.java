package com.example.dongle.location.Activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.dongle.location.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SflashActivity extends AppCompatActivity implements Animation.AnimationListener{
    @BindView(R.id.layotFlash)
    RelativeLayout layout;
    @BindView(R.id.imgFlash)
    ImageView img_icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sflash);
        ButterKnife.bind(this);

        Animation transtion_icon_Ani= AnimationUtils.loadAnimation(this,R.anim.transtion_icon);
        img_icon.setAnimation(transtion_icon_Ani);

        Animation anphaAni= AnimationUtils.loadAnimation(this,R.anim.anpha);
        layout.setAnimation(anphaAni);

        anphaAni.setAnimationListener(this);
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SflashActivity.this,CategoriesActivity.class));
                finish();
            }
        },1000);

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
