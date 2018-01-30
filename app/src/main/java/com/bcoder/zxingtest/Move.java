package com.bcoder.zxingtest;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Move extends AppCompatActivity {
    Button b1;
    ImageView imageView;
    global_variable g;
    LinearLayout mLinearLayout;
    private int screenWidth, screenHeight;
    private int lastX, lastY;
    int maxindex = 9;
    int index;
    int lowerdistence = 999999;
    Bookshelf b;
    Handler handler = null;
    int Line;
    private int[] image = {R.drawable.a11, R.drawable.a22, R.drawable.a33, R.drawable.a44, R.drawable.a55};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move);
        g = (global_variable) getApplication();
        b1 = (Button) findViewById(R.id.button39);
        imageView = (ImageView) findViewById(R.id.imageView6);
        imageView.setImageBitmap(g.Bemove.getCover());
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Move.this, selfManager.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        handler = new Handler();
        inintent();
        initListener();
    }

    private void initListener() {
        screenWidth = getScreenWidth(this);//获取屏幕宽度
        screenHeight = getScreenHeight(this) - getStatusHeight(Move.this);//屏幕高度-状态栏
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        int dx = (int) event.getRawX() - lastX;
                        int dy = (int) event.getRawY() - lastY;

                        int top = v.getTop() + dy;

                        int left = v.getLeft() + dx;

                        if (top <= 0) {
                            top = 0;
                        }
                        if (top >= screenHeight - imageView.getHeight()) {
                            top = screenHeight - imageView.getHeight();
                        }
                        if (left >= screenWidth - imageView.getWidth()) {
                            left = screenWidth - imageView.getWidth();
                        }

                        if (left <= 0) {
                            left = 0;
                        }
                        v.getWidth();
                        v.getHeight();
                        RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(v.getWidth(), v.getHeight());
                        param.leftMargin = left;
                        param.topMargin = top;
                        v.setLayoutParams(param);
//                        v.layout(left, top, left+v.getWidth(), top+v.getHeight());

                        v.postInvalidate();

                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();

                        break;
                    case MotionEvent.ACTION_UP:
                        HorizontalScrollView H = (HorizontalScrollView) findViewById(R.id.horizontalScrollView);
                        maxindex = 9;
                        lowerdistence = 999999;
                        for (int x = 0; x < g.bookshelfs.size(); x++) {
                            View v1 = mLinearLayout.getChildAt(x);
                            if (Math.abs(v1.getTop() - imageView.getTop()) < 150) {
                                int t = Math.abs(v1.getLeft() - imageView.getLeft());
                                if (lowerdistence > Math.abs(v1.getLeft() - H.getScrollX() - imageView.getLeft())) {
                                    lowerdistence = Math.abs(v1.getLeft() - H.getScrollX() - imageView.getLeft());
                                    maxindex = x;
                                }
                            }
                        }
                        if (maxindex != 9) {
                            new android.support.v7.app.AlertDialog.Builder(Move.this)
                                    .setTitle("确认")
                                    .setMessage("移动到书架" + (maxindex + 1))
                                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            b = g.bookshelfs.get(maxindex);
                                            if (!b.ifexistence(g.Bemove)) {
                                                final EditText editText = new EditText(Move.this);
                                                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                                                AlertDialog.Builder inputDialog =
                                                        new AlertDialog.Builder(Move.this);
                                                inputDialog.setTitle("插入到第几行(1-"+b.getLine()+")").setView(editText);
                                                inputDialog.setNegativeButton("取消", null);
                                                inputDialog.setPositiveButton("确定",
                                                        new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                Line = Integer.parseInt(editText.getText().toString())-1;
                                                                if(0<=Line && Line <b.getLine()) {
                                                                    if (b.addbook(g.Bemove, Line, g.hostip, g.accout)) {
                                                                        index = g.Bemove.getBookshelfid();
                                                                        for (int i = 0; i < g.bookshelfs.size(); i++) {
                                                                            if (index == g.bookshelfs.get(i).getBookshelfid()) {
                                                                                index = i;
                                                                                break;
                                                                            }
                                                                        }
                                                                        new Thread() {
                                                                            public void run() {
                                                                                if (g.bookshelfs.get(index).deletebook(g.hostip, g.Bemove.getBookshelfline(), g.Bemove.getBookshelfcolumn())) {
                                                                                    handler.post(runnable1);
                                                                                } else {
                                                                                    handler.post(runnable2);
                                                                                }
                                                                            }
                                                                        }.start();
                                                                    } else {
                                                                        new android.support.v7.app.AlertDialog.Builder(Move.this)
                                                                                .setTitle("移动失败")
                                                                                .setMessage("当前书架已满请添加至别的书架")
                                                                                .setPositiveButton("确认", null)
                                                                                .show();
                                                                    }
                                                                }else {
                                                                    new android.support.v7.app.AlertDialog.Builder(Move.this)
                                                                            .setTitle("移动失败")
                                                                            .setMessage("超出范围")
                                                                            .setPositiveButton("确认", null)
                                                                            .show();
                                                                }
                                                            }
                                                        }).show();
                                            } else {
                                                new android.support.v7.app.AlertDialog.Builder(Move.this)
                                                        .setTitle("移动失败")
                                                        .setMessage("该书架已经存在此书")
                                                        .setPositiveButton("确认", null)
                                                        .show();
                                            }
                                        }
                                    })
                                    .setNegativeButton("取消", null)
                                    .show();

                        }
                        break;

                }

                return true;
            }
        });
    }

    private void inintent() {
        mLinearLayout = (LinearLayout) findViewById(R.id.linear);


        //开始添加数据
        for (int x = 0; x < g.bookshelfs.size(); x++) {
            //寻找行布局，第一个参数为行布局ID，第二个参数为这个行布局需要放到那个容器上
            View view = LayoutInflater.from(this).inflate(R.layout.item_bookshelf_small, mLinearLayout, false);
            //通过View寻找ID实例化控件
            ImageView img = (ImageView) view.findViewById(R.id.imageView);
            //实例化TextView控件
            TextView tv = (TextView) view.findViewById(R.id.textView);
            //将int数组中的数据放到ImageView中
            img.setImageResource(image[x % 5]);
            //给TextView添加文字
            tv.setText(g.bookshelfs.get(x).getName());
            view.setId(x + 100);
            //把行布局放到linear里
            mLinearLayout.addView(view);
        }

    }

    /**
     * 获取屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * 获取屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    /**
     * 获得状态栏的高度
     *
     * @param context
     * @return
     */
    public static int getStatusHeight(Context context) {

        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    Runnable runnable1 = new Runnable() {
        @Override
        public void run() {
            new android.support.v7.app.AlertDialog.Builder(Move.this)
                    .setTitle("移动成功")
                    .setMessage("移动到该书架")
                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Intent intent = new Intent(Move.this, BookInBookShelf.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    })
                    .show();

        }

    };

    Runnable runnable2 = new Runnable() {
        @Override
        public void run() {
            new android.support.v7.app.AlertDialog.Builder(Move.this)
                    .setTitle("移动失败")
                    .setMessage("无法从原书架删除")
                    .setPositiveButton("确认", null)
                    .show();
        }

    };
}
