package com.bcoder.zxingtest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CheckBookShelf extends AppCompatActivity {
    private LinearLayout mLinearLayout;
    private int[] image={R.drawable.a11,R.drawable.a22,R.drawable.a33,R.drawable.a44,R.drawable.a55};
    global_variable  g;
    Button b1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkbookshelf);
        g = (global_variable)getApplication();
        inintent();
        b1 = (Button)findViewById(R.id.button7);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CheckBookShelf.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void inintent() {
        mLinearLayout= (LinearLayout) findViewById(R.id.linear2);

        //开始添加数据
        for(int x=0; x<g.bookshelfs.size(); x++){
            //寻找行布局，第一个参数为行布局ID，第二个参数为这个行布局需要放到那个容器上
            View view= LayoutInflater.from(this).inflate(R.layout.item_bookshelf,mLinearLayout,false);
            //通过View寻找ID实例化控件
            ImageView img= (ImageView) view.findViewById(R.id.imageView);
            //实例化TextView控件
            TextView tv= (TextView) view.findViewById(R.id.textView);
            //将int数组中的数据放到ImageView中
            img.setImageResource(image[x%5]);
            //给TextView添加文字
            tv.setText(g.bookshelfs.get(x).getName());
            view.setId(x);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    g = (global_variable)getApplication();
                    g.needcheck = v.getId();
                    Intent intent = new Intent(CheckBookShelf.this, BookInBookShelf.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            });
            //把行布局放到linear里
            mLinearLayout.addView(view);
        }

    }
}
