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

public class BookDetails extends AppCompatActivity {
    TextView t1,t2,t3,t4,t5;
    global_variable g;
    Button b1,b2,b3,b4;
    ImageView iv;
    int index;
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);
        t1 =(TextView)findViewById(R.id.textView3);
        t2 =(TextView)findViewById(R.id.textView5);
        t3 =(TextView)findViewById(R.id.textView7);
        t4 =(TextView)findViewById(R.id.textView9);
        t5 =(TextView)findViewById(R.id.textView22);
        g = (global_variable)getApplication();
        t1.setText(g.Becheck.gettitle());
        t2.setText(g.Becheck.get("author"));
        t3.setText(g.Becheck.get("pubdate"));
        t5.setText(g.Becheck.get("price"));
        iv = (ImageView)findViewById(R.id.imageView2);
        iv.setImageBitmap(g.Becheck.getCover());
        handler = new Handler();
        t4.setText("书架:" + (g.Becheck.getBookshelfname())+" " +"第" +(g.Becheck.getBookshelfline()+1) +"层" +"第" +(g.Becheck.getBookshelfcolumn()+1) +"本");
        b1 = (Button)findViewById(R.id.button10);
        b2 = (Button)findViewById(R.id.button11);
        b3 = (Button)findViewById(R.id.button12);
        b4 = (Button)findViewById(R.id.button33);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                if(g.backinterface == 0 )
                    intent = new Intent(BookDetails.this, SearchResult.class);
                else if(g.backinterface == 1)
                    intent = new Intent(BookDetails.this, BookInBookShelf.class);
                else if(g.backinterface == 2 )
                    intent = new Intent(BookDetails.this, SearchResult.class);

                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                g.shareISBN = g.Becheck.get("isbn13");
                Intent intent = new Intent(BookDetails.this, Share.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookDetails.this, selfManager.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new android.support.v7.app.AlertDialog.Builder(BookDetails.this)
                        .setTitle("删除")
                        .setMessage("确认要将此书从您的书架上删除么")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                index = g.Becheck.getBookshelfid();
                                for(int i=0; i<g.bookshelfs.size();i++){
                                    if(index == g.bookshelfs.get(i).getBookshelfid()){
                                        index = i;
                                        break;
                                    }
                                }
                                new Thread(){
                                    public void run(){
                                        if(g.bookshelfs.get(index).deletebook(g.hostip,g.Becheck.getBookshelfline(),g.Becheck.getBookshelfcolumn())){
                                            handler.post(runnable1);
                                        }else handler.post(runnable2);
                                    }
                                }.start();

                            }
                        })
                        .setNegativeButton("取消",null)
                        .show();

            }
        });
    }
    Runnable   runnable1=new  Runnable(){
        @Override
        public void run() {
            //更新界面
            new android.support.v7.app.AlertDialog.Builder(BookDetails.this)
                    .setTitle("删除成功")
                    .setMessage("已从您的书架上删除此书")
                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(BookDetails.this, BookInBookShelf.class);
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
            new android.support.v7.app.AlertDialog.Builder(BookDetails.this)
                    .setTitle("删除失败")
                    .setMessage("请查看后重试")
                    .setPositiveButton("确认",null )
                    .show();
        }

    };
}
