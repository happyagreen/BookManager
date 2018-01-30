package com.bcoder.zxingtest;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONObject;

import static com.bcoder.zxingtest.MainActivity.readParse;

public class Preload extends AppCompatActivity {
    Intent intent;
    Button b1,b2,b3,b4,b5;
    int num =0;
    private Handler handler=null;
    global_variable g;
    int flag=0;
    String temp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preload);
        intent = new Intent(Preload.this, Menu.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        b1 = (Button)findViewById(R.id.button25);
        b2 = (Button)findViewById(R.id.button26);
        b3 = (Button)findViewById(R.id.button27);
        b4 = (Button)findViewById(R.id.button28);
        b5 = (Button)findViewById(R.id.button29);
        g = (global_variable)getApplication();
        handler=new Handler();
        new Thread() {
            @Override
            public void run() {
                super.run();
                while(flag==0){
                    try {
                        switch (num){
                            case 0:
                                handler.post(runnable1);
                                Thread.sleep(300);
                                num++;
                                break;
                            case 1:
                                handler.post(runnable2);
                                Thread.sleep(300);
                                num++;
                                break;
                            case 2:
                                handler.post(runnable3);
                                Thread.sleep(300);
                                num++;
                                break;
                            case 3:
                                handler.post(runnable4);
                                Thread.sleep(300);
                                num++;
                                break;
                            case 4:
                                handler.post(runnable5);
                                Thread.sleep(300);
                                num=0;
                                break;
                            default:
                                num =0;
                                break;
                        }

                    }catch (InterruptedException e){

                    }
                }
            }
        }.start();
        new Thread() {
            @Override
            public void run() {
                super.run();
                    try {
                        temp = readParse("http://"+g.hostip+":3000/get/bookshelf/all/"+g.accout+"/");
                        JSONArray array1 = new JSONArray(temp);
                        for(int i=0;i<array1.length();i++) {
                            JSONObject jsondata = array1.getJSONObject(i);
                            Bookshelf bs = new Bookshelf(Integer.parseInt(jsondata.getString("id")), Integer.parseInt(jsondata.getString("column")), Integer.parseInt(jsondata.getString("line")));
                            bs.setName(jsondata.getString("name"));
                            g.bookshelfs.add(bs);
                        }
                        temp = readParse("http://"+g.hostip+":3000/get/relation/"+g.accout+"/");
                        JSONArray array2 = new JSONArray(temp);
                        for(int i=0;i<array2.length();i++) {
                            JSONObject jsondata = array2.getJSONObject(i);
                            String temp1 = readParse("http://"+g.hostip+":3000/get/json/"+jsondata.getString("bookid")+"/");
                            Book newbook = new Book(temp1);
                            newbook.setId(Integer.parseInt(jsondata.getString("bookid")));
                            newbook.setCover(g.getBitmap(newbook.getimageurl(g.hostip)));
                            for(int j=0;j<g.bookshelfs.size();j++){
                                if(g.bookshelfs.get(j).getBookshelfid() == Integer.parseInt(jsondata.getString("bookshelfid"))){
                                    int line =Integer.parseInt(jsondata.getString("line"));
                                    int column = Integer.parseInt(jsondata.getString("column"));
                                    g.bookshelfs.get(j).addbook(newbook,line,column);
                                }
                            }
                        }
                        Thread.sleep(1000);
                       startActivity(intent);
                    }catch (Exception e){

                    }
                }
        }.start();

    }
    Runnable   runnable1=new  Runnable(){
        @Override
        public void run() {
            //更新界面
            b1.setBackgroundColor(getResources().getColor(R.color.lightgreen));
            b5.setBackgroundColor(Color.WHITE);

        }

    };
    Runnable   runnable2=new  Runnable(){
        @Override
        public void run() {
            //更新界面
            b2.setBackgroundColor(getResources().getColor(R.color.lightgreen));
            b1.setBackgroundColor(Color.WHITE);
        }

    };
    Runnable   runnable3=new  Runnable(){
        @Override
        public void run() {
            //更新界面
            b3.setBackgroundColor(getResources().getColor(R.color.lightgreen));
            b2.setBackgroundColor(Color.WHITE);
        }

    };
    Runnable   runnable4=new  Runnable(){
        @Override
        public void run() {
            //更新界面
            b4.setBackgroundColor(getResources().getColor(R.color.lightgreen));
            b3.setBackgroundColor(Color.WHITE);
        }

    };
    Runnable   runnable5=new  Runnable(){
        @Override
        public void run() {
            //更新界面
            b5.setBackgroundColor(getResources().getColor(R.color.lightgreen));
            b4.setBackgroundColor(Color.WHITE);
        }

    };
}
