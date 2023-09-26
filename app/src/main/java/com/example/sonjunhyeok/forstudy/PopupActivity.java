package com.example.sonjunhyeok.forstudy;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class PopupActivity extends Activity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popup);

        textView = (TextView)findViewById(R.id.txtText);

        Intent intent = getIntent();
        String data = intent.getStringExtra("data");
        textView.setText(data);

    }
    public void mOnClose(View view){
        Intent intent = new Intent();
        intent.putExtra("result","Close Popup");
        setResult(RESULT_OK,intent);

        finish();
    }
    @Override
    public void onBackPressed() {
        return;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        else {
            return true;
        }
    }

}