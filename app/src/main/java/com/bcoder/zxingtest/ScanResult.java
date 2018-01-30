package com.bcoder.zxingtest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.json.JSONObject;

public class ScanResult extends AppCompatActivity {
    ImageView iv = null;
    Bitmap b =null;
    String url1;
    global_variable g;
    Button button1,button2;
    View.OnClickListener listener1,listener2;
    private Handler handler=null;
    String temp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        iv = (ImageView) findViewById(R.id.imageView);
        g = (global_variable)getApplication();
        button1 = (Button) findViewById(R.id.button3);
        handler=new Handler();
        listener1 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScanResult.this, selfManager.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        };
        button1.setOnClickListener(listener1);

        button2 = (Button) findViewById(R.id.button4);
        listener2 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScanResult.this, Setbook.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        };
        button2.setOnClickListener(listener2);
        new Thread() {
            public void run() {
                try {
                    temp = g.readParse("http://"+g.hostip+":3000/download/json/" +g.results);
                    JSONObject jsondata = new JSONObject(temp);
                    g = (global_variable)getApplication();
                    g.test_book = new Book(temp);
                    g.test_book.saveid(g.hostip);
                    b = g.getBitmap(g.test_book.getimageurl(g.hostip));
                    g.test_book.setCover(b);
                }catch (Exception e){}
                handler.post(runnableUi);

            }
            ;
        }.start();

    }
    Runnable   runnableUi=new  Runnable(){
        @Override
        public void run() {
            //更新界面
            iv.setImageBitmap(g.test_book.getCover());
        }

    };




}
