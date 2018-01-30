package com.bcoder.zxingtest;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class InternetBook extends AppCompatActivity {
    Spinner s;
    Button b1,b2,b3;
    EditText e1;
    global_variable g;
    private LinearLayout mLinearLayout;
    public ArrayList<Book> goodbook = new ArrayList<Book>();
    private Handler handler=null;
    String result;
    ScrollView sv;
    int nowx = 0,nowbookid;
    View.OnTouchListener OT= null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internet_book);
        s= (Spinner)findViewById(R.id.spinner4);
        e1 = (EditText)findViewById(R.id.editText10);
        g=(global_variable)getApplication();
        b1 = (Button)findViewById(R.id.button35);
        b2 = (Button)findViewById(R.id.button37);
        b3 = (Button)findViewById(R.id.button40);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InternetBook.this,Collection.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InternetBook.this,Menu.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        g.searchtype = 2;
        handler=new Handler();
        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0) {
                    g.searchtype=2;
                    b1.setText("模糊搜索");
                }
                if(i==1){
                    g.searchtype=3;
                    b1.setText("ISBN搜索");
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                g.searchkey = e1.getText().toString();
                Intent intent = new Intent(InternetBook.this,SearchResult.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        sv = (ScrollView)findViewById(R.id.ScrollView);
        new Thread(){
            public void run(){
                try {
                    result = g.readParse("http://" + g.hostip + ":3000/get/allid/");
                    JSONArray array = new JSONArray(result);
                    int i = array.length()-1;
                    for(;i>=0 && i>array.length()-22;i--){
                        JSONObject json = array.getJSONObject(i);
                        result = g.readParse("http://" + g.hostip + ":3000/download/jsonbyid/" + json.getString("id") + "/");
                        Book temp = new Book(result);
                        temp.setCover(g.getBitmap(temp.getimageurl(g.hostip)));
                        temp.saveid(g.hostip);
                        temp.setBookshelfcolumn(0);
                        temp.setBookshelfline(0);
                        goodbook.add(temp);
                        System.out.println("       "+goodbook.size()   +  "    " +i);
                    }
                    nowbookid = i;
                }catch (Exception e){

                }
                handler.post(runnableUi);
            }
        }.start();
        OT = new View.OnTouchListener(){
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int scrollY=view.getScrollY();
                        int height=view.getHeight();
                        int scrollViewMeasuredHeight=sv.getChildAt(0).getMeasuredHeight();
                        if(scrollY==0){
                            System.out.println("滑动到了顶端 view.getScrollY()="+scrollY);
                        }
                        if((scrollY+height)==scrollViewMeasuredHeight){
                            new Thread(){
                                public void run(){
                                    if(nowbookid >0) {
                                        try {
                                            sv.setOnTouchListener(null);
                                            result = g.readParse("http://" + g.hostip + ":3000/get/allid/");
                                            JSONArray array = new JSONArray(result);
                                            int i = nowbookid;
                                            for (; i >= 0 && i>nowbookid-21; i--) {
                                                JSONObject json = array.getJSONObject(i);
                                                result = g.readParse("http://" + g.hostip + ":3000/download/jsonbyid/" + json.getString("id") + "/");
                                                Book temp = new Book(result);
                                                temp.setCover(g.getBitmap(temp.getimageurl(g.hostip)));
                                                temp.saveid(g.hostip);
                                                temp.setBookshelfcolumn(0);
                                                temp.setBookshelfline(0);
                                                goodbook.add(temp);
                                                System.out.println("       " + goodbook.size() + "    " + i + "     "+ nowbookid);
                                            }
                                            nowbookid = i;
                                        } catch (Exception e) {

                                        }
                                        handler.post(runnableUi);
                                    }else{
                                        handler.post(runnableU2);
                                    }
                                }
                            }.start();
                        }
                        break;

                    default:
                        break;
                }
                return false;
            }
        };
        sv.setOnTouchListener(OT);
    }
    Runnable   runnableUi=new  Runnable(){
        @Override
        public void run() {
            //更新界面
            inintent();
        }

    };

    Runnable   runnableU2=new  Runnable(){
        @Override
        public void run() {
            //更新界面
            mLinearLayout= (LinearLayout) findViewById(R.id.linear5);
            TextView t= new TextView(InternetBook.this);
            t.setText("没有更多了");
            t.setTextSize(20);
            t.setGravity(Gravity.CENTER);
            mLinearLayout.addView(t);
            sv.setOnTouchListener(null);
        }

    };
    private void inintent() {
        mLinearLayout= (LinearLayout) findViewById(R.id.linear5);

        //开始添加数据
        for(int x=nowx,j=0;; ){
            //寻找行布局，第一个参数为行布局ID，第二个参数为这个行布局需要放到那个容器上
            View view = LayoutInflater.from(this).inflate(R.layout.item_book_search,mLinearLayout,false);
            //通过View寻找ID实例化控件
            ImageView img1= (ImageView) view.findViewById(R.id.imageView3);
            ImageView img2= (ImageView) view.findViewById(R.id.imageView4);
            ImageView img3= (ImageView) view.findViewById(R.id.imageView5);
            //实例化TextView控件
            TextView tv1= (TextView) view.findViewById(R.id.textView23);
            TextView tv2= (TextView) view.findViewById(R.id.textView24);
            TextView tv3= (TextView) view.findViewById(R.id.textView25);
            //将int数组中的数据放到ImageView中
            if(x<goodbook.size()) {
                img1.setImageBitmap(goodbook.get(x).getCover());
                img1.setLabelFor(x);
                img1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int id =view.getLabelFor();
                        g.Becheck = goodbook.get(id);
                        g.backinterface = 3;
                        Intent intent = new Intent(InternetBook.this, InternetSearchReasult.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });
                tv1.setText(goodbook.get(x).gettitle());
                x++;
            }else  {
                nowx = x;break;
            }
            if(x<goodbook.size()) {
                img2.setImageBitmap(goodbook.get(x).getCover());
                tv2.setText(goodbook.get(x).gettitle());
                img2.setLabelFor(x);
                img2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int id =view.getLabelFor();
                        g.Becheck = goodbook.get(id);
                        g.backinterface = 3;
                        Intent intent = new Intent(InternetBook.this, InternetSearchReasult.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });
                x++;
            }else  {
                view.setId(j);
                mLinearLayout.addView(view);
                nowx = x;break;
            }

            if(x<goodbook.size()) {
                img3.setImageBitmap(goodbook.get(x).getCover());
                tv3.setText(goodbook.get(x).gettitle());
                img3.setLabelFor(x);
                img3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int id =view.getLabelFor();
                        g.Becheck = goodbook.get(id);
                        g.backinterface = 3;
                        Intent intent = new Intent(InternetBook.this, InternetSearchReasult.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });
                x++;
                view.setId(j);
                j++;
                mLinearLayout.addView(view);
            }else   {
                view.setId(j);
                mLinearLayout.addView(view);
                nowx = x;break;
            }
        }
        sv.setOnTouchListener(OT);

    }

}
