package com.bcoder.zxingtest;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Collection extends AppCompatActivity {
    public ArrayList<Book> toShow = new ArrayList<Book>();
    String result ="";
    private Handler handler=null;
    global_variable g;
    Button b1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        g = (global_variable)getApplication();
        handler = new Handler();
        new Thread(){
            public void run(){
                try {
                    result = g.readParse("http://" + g.hostip + ":3000/get/collection/" + g.accout + "/");
                    JSONArray array2 = new JSONArray(result);
                    for(int i = 0;i<array2.length();i++){
                        JSONObject json = array2.getJSONObject(i);
                        result = g.readParse("http://" + g.hostip + ":3000/download/jsonbyid/" + json.getString("bookid") + "/");
                        Book temp = new Book(result);
                        temp.setCover(g.getBitmap(temp.getimageurl(g.hostip)));
                        temp.saveid(g.hostip);
                        temp.setBookshelfcolumn(0);
                        temp.setBookshelfline(0);
                        toShow.add(temp);
                    }
                    handler.post(runnableUi);
                }catch (Exception e){}

            }
        }.start();
        b1 = (Button)findViewById(R.id.button41);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Collection.this, InternetBook.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
    Runnable   runnableUi=new  Runnable(){
        @Override
        public void run() {
           inintent();
        }

    };
    private void inintent() {
        LinearLayout mLinearLayout = (LinearLayout) findViewById(R.id.linearcollect);


        //开始添加数据
        for (int x = 0; x < toShow.size(); x++) {
            //寻找行布局，第一个参数为行布局ID，第二个参数为这个行布局需要放到那个容器上
            View view = LayoutInflater.from(this).inflate(R.layout.item_text, mLinearLayout, false);
            //通过View寻找ID实例化控件
            ImageView img = (ImageView) view.findViewById(R.id.imageView);
            //实例化TextView控件
            TextView tv = (TextView) view.findViewById(R.id.textView);
            //将int数组中的数据放到ImageView中
            img.setImageBitmap(toShow.get(x).getCover());
            //给TextView添加文字
            tv.setText(toShow.get(x).gettitle());
            view.setId(x);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = v.getId();
                    g.Becheck = toShow.get(id);
                    g.backinterface = 0;
                    Intent intent = new Intent(Collection.this, Collectdetails.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            });
            //把行布局放到linear里
            mLinearLayout.addView(view);
        }
    }
}
