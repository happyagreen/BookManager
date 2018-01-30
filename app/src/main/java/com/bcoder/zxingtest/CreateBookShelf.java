package com.bcoder.zxingtest;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class CreateBookShelf extends AppCompatActivity {
    private global_variable g;
    Button b1,b2;
    EditText e1;
    Spinner s1,s2;
    private int line=1,column=1;
    Bookshelf bs;
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_book_shelf);
        g = (global_variable)getApplication();
        e1 = (EditText)findViewById(R.id.editText);
        b1 = (Button)findViewById(R.id.button13);
        b2 = (Button)findViewById(R.id.button14);
        s1 = (Spinner)findViewById(R.id.spinner);
        s2 = (Spinner)findViewById(R.id.spinner2);
        handler = new Handler();
        s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                line = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        s2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                column = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
       b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateBookShelf.this, selfManager.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
       b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = e1.getText().toString();
                for(int i=0;i<g.bookshelfs.size();i++){
                    if(name.equals(g.bookshelfs.get(i).getName())){
                        new android.support.v7.app.AlertDialog.Builder(CreateBookShelf.this)
                                .setTitle("提示")
                                .setMessage("已存在同名书架")
                                .setPositiveButton("确认", null)
                                .show();
                        return;
                    }
                }
                bs = new Bookshelf(0,(column+1)*10,(line+1));
                bs.setName(name);
                g.bookshelfs.add(bs);
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        try {
                            if(bs.update(g.hostip,g.accout)){
                                bs.getid(g.hostip,g.accout);
                                handler.post(runnableUi1);
                            }else
                                handler.post(runnableUi);
                        }catch (Exception e){}
                    }
                }.start();
           }
        });

    }
    Runnable   runnableUi=new  Runnable(){
        @Override
        public void run() {
            new android.support.v7.app.AlertDialog.Builder(CreateBookShelf.this)
                    .setTitle("提示")
                    .setMessage("添加失败请稍后重试")
                    .setPositiveButton("确认", null)
                    .show();
        }

    };
    Runnable   runnableUi1=new  Runnable(){
        @Override
        public void run() {
            Intent intent = new Intent(CreateBookShelf.this, selfManager.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

    };
}
