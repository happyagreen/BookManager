package com.bcoder.zxingtest;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Collectdetails extends AppCompatActivity {
    TextView t1,t2,t3,t4,t5;
    global_variable g;
    Button b1,b2,b3,b4;
    ImageView iv;
    int index;
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collectdetails);
        t1 =(TextView)findViewById(R.id.textView3);
        t2 =(TextView)findViewById(R.id.textView5);
        t3 =(TextView)findViewById(R.id.textView7);
        t5 =(TextView)findViewById(R.id.textView22);
        g = (global_variable)getApplication();
        t1.setText(g.Becheck.gettitle());
        t2.setText(g.Becheck.get("author"));
        t3.setText(g.Becheck.get("pubdate"));
        t5.setText(g.Becheck.get("price"));
        iv = (ImageView)findViewById(R.id.imageView2);
        iv.setImageBitmap(g.Becheck.getCover());
        handler = new Handler();
        b1 = (Button)findViewById(R.id.button10);
        b2 = (Button)findViewById(R.id.button11);
        b3 = (Button)findViewById(R.id.button12);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Collectdetails.this, Collection.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Collectdetails.this, InternetBook.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(){
                    public void run() {
                        try {
                            if (g.readParse("http://" + g.hostip + ":3000/delete/collection/" + g.Becheck.getId() + "/" + g.accout + "/").equals("true")) {
                                handler.post(runnable1);
                            } else handler.post(runnable2);
                        }catch(Exception e){}
                    }}.start();
            }
        });
    }
    Runnable   runnable1=new  Runnable(){
        @Override
        public void run() {
            //更新界面
            new android.support.v7.app.AlertDialog.Builder(Collectdetails.this)
                    .setTitle("删除成功")
                    .setMessage("您已将该书从收藏中删除")
                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(Collectdetails.this, Collection.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    })
                    .show();
        }

    };
    Runnable   runnable2=new  Runnable(){
        @Override
        public void run() {
            //更新界面
            new android.support.v7.app.AlertDialog.Builder(Collectdetails.this)
                    .setTitle("删除失败")
                    .setMessage("请查看后重试")
                    .setPositiveButton("确认",null )
                    .show();
        }

    };
}
