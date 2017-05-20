package com.movie.jk.collectionview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.animation.Animator;

import android.view.View;

import android.widget.ImageView;
import android.widget.TextView;

import view.CollectionView;


public class MainActivity extends AppCompatActivity {

    private CollectionView collectionView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView t1 = (TextView) findViewById(R.id.startview);
        final ImageView endView = (ImageView) findViewById(R.id.endview);
        collectionView = new CollectionView(this);
        final TextView countView = (TextView) findViewById(R.id.count);


        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View layout = collectionView.getAnimateLayout(t1,endView,R.drawable.jshop_level_star_active);
                collectionView.startAnimation(layout);
            }
        });
        collectionView.setOnAnimateUpdateListener(new CollectionView.AnimateUpdateListener() {
            int count=0;


            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                count++;
                countView.setText(""+count);

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }
}
