package com.bcoder.zxingtest;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class selfManager extends AppCompatActivity {
    private LinearLayout mLinearLayout;
    private int[] image={R.drawable.a11,R.drawable.a22,R.drawable.a33,R.drawable.a44,R.drawable.a55};
    global_variable  g;
    Button b1,b2,b3,b4;
    String results;
    String temp;
    Spinner s;
    EditText e1;
    int Bedeleteid;
    Handler handler =null;
    LinearLayout la;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_manager);
        g=(global_variable)getApplication();
        g.searchtype =0;
        b1 = (Button)findViewById(R.id.button24);
        b2 = (Button)findViewById(R.id.button30);
        b4 = (Button)findViewById(R.id.button32);
        s= (Spinner)findViewById(R.id.spinner3);
        handler = new Handler();
        e1 = (EditText)findViewById(R.id.editText8);
        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0) {
                    g.searchtype=0;
                    b1.setText("模糊搜索");
                }
                if(i==1){
                    g.searchtype=1;
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
                Intent intent = new Intent(selfManager.this,SearchResult.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(selfManager.this, CreateBookShelf.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(selfManager.this, Menu.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        inintent();
    }
    public void onScanBarcode(View v){
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setPrompt("扫描条形码");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.initiateScan();
    }

    public void onScanQrcode(View v){
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setPrompt("扫描二维码");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        final IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "扫码取消！", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "扫描成功，条码值: " + result.getContents(), Toast.LENGTH_LONG).show();
                g.results =  ""+result.getContents()/*"9787544143158"*/;
                new Thread() {
                    public void run() {
                        try {
                            Intent intent = new Intent(selfManager.this,ScanResult.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    ;
                }.start();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    private void inintent() {
        mLinearLayout = (LinearLayout) findViewById(R.id.linear4);

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
            view.setId(g.bookshelfs.get(x).getBookshelfid());
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    g = (global_variable)getApplication();
                    g.needcheck = v.getId();
                    Intent intent = new Intent(selfManager.this, BookInBookShelf.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            });

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Bedeleteid = view.getId();
                    new android.support.v7.app.AlertDialog.Builder(selfManager.this)
                            .setTitle("删除")
                            .setMessage("是否要删除该书架")
                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    new Thread(){
                                        public void run(){
                                            try {
                                                for(int i=0;i<g.bookshelfs.size();i++){
                                                    if(Bedeleteid == g.bookshelfs.get(i).getBookshelfid()){
                                                        Bedeleteid = i;
                                                    }
                                                }
                                                String result = g.readParse("http://" + g.hostip + ":3000/delete/bookshelf/" + g.bookshelfs.get(Bedeleteid).getBookshelfid());
                                                if(result.equals("true")) {
                                                    handler.post(runnable1);}
                                                else handler.post(runnable2);
                                            }catch (Exception e){}
                                        }
                                    }.start();
                                }
                            })
                            .setNegativeButton("取消",null)
                            .show();
                    return false;
                }
            });
            //把行布局放到linear里
            mLinearLayout.addView(view);
        }
    }
    Runnable   runnable1=new  Runnable(){
        @Override
        public void run() {
            //更新界面
            g.bookshelfs.remove(Bedeleteid);
            la = (LinearLayout)findViewById(R.id.linear4);
            la.removeViewAt(Bedeleteid);
            new android.support.v7.app.AlertDialog.Builder(selfManager.this)
                    .setTitle("删除成功")
                    .setMessage("该书架已从您的列表中去除")
                    .setPositiveButton("确认", null)
                    .show();
        }

    };
    Runnable   runnable2=new  Runnable(){
        @Override
        public void run() {
            //更新界面
            new android.support.v7.app.AlertDialog.Builder(selfManager.this)
                    .setTitle("删除失败")
                    .setMessage("请查看后重试")
                    .setPositiveButton("确认", null)
                    .show();
        }

    };
}
