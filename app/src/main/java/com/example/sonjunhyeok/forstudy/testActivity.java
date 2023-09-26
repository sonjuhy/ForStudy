package com.example.sonjunhyeok.forstudy;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.TypedValue;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import bg.devlabs.transitioner.Transitioner;

public class testActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        android.support.v7.widget.Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        final List<String> testStrings = new ArrayList<>();
        for(int i = 0; i < 100; i++) {
            testStrings.add(i + " 번째 item");
        }
        RecyclerView recyclerView = findViewById(R.id.main_scrollview_recyclerview);
        recyclerView.setAdapter(new RecyclerView.Adapter<TestViewHolder>() {
            @Override
            public TestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = View.inflate(getApplicationContext(), android.R.layout.simple_list_item_1, null);
                return new TestViewHolder(view);
            }

            @Override
            public void onBindViewHolder(TestViewHolder holder, int position) {
                holder.textView.setText(testStrings.get(position));
            }
            public int getItemCount() {
                return testStrings.size();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public static class TestViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public TestViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}
