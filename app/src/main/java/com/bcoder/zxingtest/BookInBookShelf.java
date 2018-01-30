package com.bcoder.zxingtest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BookInBookShelf extends AppCompatActivity {
    private LinearLayout mLinearLayout;
    global_variable g;
    Button b1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_in_book_shelf);
        g = (global_variable)getApplication();
        inintent();
        b1 = (Button)findViewById(R.id.button9);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookInBookShelf.this, selfManager.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void inintent() {
        mLinearLayout= (LinearLayout) findViewById(R.id.linear3);
        Bookshelf b = null;
        for(int i=0;i<g.bookshelfs.size();i++) {
            if(g.needcheck == g.bookshelfs.get(i).getBookshelfid()) {
                b = g.bookshelfs.get(i);
                g.needcheckindex = i;
                break;
            }
        }
        //开始添加数据
        for(int x=0; x< b.getLine(); x++){
            HorizontalScrollView view1 = new HorizontalScrollView(BookInBookShelf.this);
            LinearLayout linear = new LinearLayout(BookInBookShelf.this);
            for(int y=0;y< b.getColumn();y++) {
                Book book = b.getbook(x,y);
                if(book.checkbook()) {
                    //寻找行布局，第一个参数为行布局ID，第二个参数为这个行布局需要放到那个容器上
                    View view = LayoutInflater.from(this).inflate(R.layout.item_text2, linear, false);
                    //通过View寻找ID实例化控件
                    ImageView img = (ImageView) view.findViewById(R.id.imageView);
                    //实例化TextView控件
                    TextView tv = (TextView) view.findViewById(R.id.textView);
                    //将int数组中的数据放到ImageView中
                    img.setImageBitmap(book.getCover());
                    //给TextView添加文字
                    tv.setText(book.gettitle());
                    view.setId(g.needcheckindex*10000+x*100+y);
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int id = v.getId();
                            Bookshelf temp1 = g.bookshelfs.get(id/10000);
                            g.Becheck_bookshelfname = temp1.getName();
                            g.Becheck = temp1.getbook((id%10000)/100,id%100);
                            g.Becheck.setBookshelfname(g.Becheck_bookshelfname);
                            if(g.searchtype<2)
                            g.backinterface =1;
                            else g.backinterface =2;
                            Intent intent = new Intent(BookInBookShelf.this, BookDetails.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    });
                    view.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            int id = view.getId();
                            Bookshelf temp1 = g.bookshelfs.get(id/10000);
                            g.Bemove = temp1.getbook((id%10000)/100,id%100);
                            Intent intent = new Intent(BookInBookShelf.this, Move.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            return false;
                        }
                    });
                    //把行布局放到linear里
                    linear.addView(view);
                }
            }
            view1.addView(linear);
            mLinearLayout.addView(view1);
        }

    }
}
