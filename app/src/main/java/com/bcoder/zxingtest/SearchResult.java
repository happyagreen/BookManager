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

public class SearchResult extends AppCompatActivity {
    global_variable g;
    private LinearLayout mLinearLayout;
    public ArrayList<Book> toShow = new ArrayList<Book>();
    String result ="";
    private Handler handler=null;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        g = (global_variable)getApplication();
        handler = new Handler();
        new Thread(){
            public void run(){
                try {
                    switch (g.searchtype) {
                        case 0:
                            result = g.readParse("http://" + g.hostip + ":3000/search/name/" + g.searchkey + "/");
                            JSONArray array = new JSONArray(result);
                            for(int i = 0;i<array.length();i++){
                                JSONObject json = array.getJSONObject(i);
                                for(int j =0 ;j<g.bookshelfs.size();j++){
                                    Book temp = g.bookshelfs.get(j).find(Integer.parseInt(json.getString("id")));
                                    if(temp != null){
                                        toShow.add(temp);
                                    }
                                }
                            }
                            handler.post(runnableUi);
                            break;
                        case 1:
                            result = g.readParse("http://" + g.hostip + ":3000/search/isbn/" + g.searchkey + "/");
                            JSONArray array1 = new JSONArray(result);
                            for(int i = 0;i<array1.length();i++){
                                JSONObject json = array1.getJSONObject(i);
                                for(int j =0 ;j<g.bookshelfs.size();j++){
                                    Book temp = g.bookshelfs.get(j).find(Integer.parseInt(json.getString("id")));
                                    if(temp != null){
                                        toShow.add(temp);
                                    }
                                }
                            }
                            handler.post(runnableUi);
                            break;
                        case 2:
                            result = g.readParse("http://" + g.hostip + ":3000/search/name/all/" + g.searchkey + "/");
                            JSONArray array2 = new JSONArray(result);
                            for(int i = 0;i<array2.length();i++){
                                JSONObject json = array2.getJSONObject(i);
                                result = g.readParse("http://" + g.hostip + ":3000/download/jsonbyid/" + json.getString("id") + "/");
                                Book temp = new Book(result);
                                temp.setCover(g.getBitmap(temp.getimageurl(g.hostip)));
                                temp.saveid(g.hostip);
                                temp.setBookshelfcolumn(0);
                                temp.setBookshelfline(0);
                                toShow.add(temp);
                            }
                            handler.post(runnableUi);
                            break;
                        case 3:
                            result = g.readParse("http://" + g.hostip + ":3000/search/isbn/all/" + g.searchkey + "/");
                            JSONArray array3 = new JSONArray(result);
                            for(int i = 0;i<array3.length();i++){
                                JSONObject json = array3.getJSONObject(i);
                                Book temp = new Book(json);
                                temp.saveid(g.hostip);
                                temp.setCover(g.getBitmap(temp.getimageurl(g.hostip)));
                                temp.setBookshelfcolumn(0);
                                temp.setBookshelfline(0);
                                toShow.add(temp);
                            }
                            handler.post(runnableUi);
                            break;
                    }


                }catch (Exception e){}

            }
        }.start();
        button = (Button)findViewById(R.id.button36);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(g.searchtype<2){
                    Intent intent = new Intent(SearchResult.this, selfManager.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(SearchResult.this, InternetBook.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }

            }
        });
    }

    Runnable   runnableUi=new  Runnable(){
        @Override
        public void run() {
            //更新界面
            inintent();
        }

    };
    private void inintent() {
        mLinearLayout= (LinearLayout) findViewById(R.id.linearsearch);


        //开始添加数据
        for(int x=0; x<toShow.size(); x++){
            //寻找行布局，第一个参数为行布局ID，第二个参数为这个行布局需要放到那个容器上
            View view= LayoutInflater.from(this).inflate(R.layout.item_text,mLinearLayout,false);
            //通过View寻找ID实例化控件
            ImageView img= (ImageView) view.findViewById(R.id.imageView);
            //实例化TextView控件
            TextView tv= (TextView) view.findViewById(R.id.textView);
            //给TextView添加文字
            tv.setText(toShow.get(x).gettitle());
            //将int数组中的数据放到ImageView中
            img.setImageBitmap(toShow.get(x).getCover());
            view.setId(x);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(g.searchtype < 2) {
                        int id = v.getId();
                        g.Becheck = toShow.get(id);
                        for(int i=0;i<g.bookshelfs.size();i++){
                            if(g.bookshelfs.get(i).getBookshelfid() == g.Becheck.getBookshelfid()) {
                                g.Becheck_bookshelfname = g.bookshelfs.get(i).getName();
                                g.Becheck.setBookshelfname(g.Becheck_bookshelfname);
                                break;
                            }
                        }
                        g.backinterface = 0;
                        Intent intent = new Intent(SearchResult.this, BookDetails.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                    else{
                        int id = v.getId();
                        g.Becheck = toShow.get(id);
                        g.backinterface = 2;
                        Intent intent = new Intent(SearchResult.this, InternetSearchReasult.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }
            });
            //把行布局放到linear里
            mLinearLayout.addView(view);
        }

    }
}
