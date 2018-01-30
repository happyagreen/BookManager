package com.bcoder.zxingtest;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONObject;

import java.io.IOException;

public class Register extends AppCompatActivity {
    Button b1,b2;
    EditText et1,et2,et3;
    global_variable g;
    private Handler handler=null;
    String account,pw1,pw2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        b1 = (Button)findViewById(R.id.button18);
        b2 = (Button)findViewById(R.id.button19);
        et1 = (EditText)findViewById(R.id.editText4);
        et2 = (EditText)findViewById(R.id.editText5);
        et3 = (EditText)findViewById(R.id.editText6);
        handler=new Handler();
        g= (global_variable)getApplication();
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                account = et1.getText().toString();
                pw1 = et2.getText().toString();
                pw2 = et3.getText().toString();
                if(account.equals("")){
                    new android.support.v7.app.AlertDialog.Builder(Register.this)
                            .setTitle("注册失败")
                            .setMessage("用户名不能为空")
                            .setPositiveButton("确认",null)
                            .show();
                }
                else if(pw1.equals("")||pw2.equals("")){
                    new android.support.v7.app.AlertDialog.Builder(Register.this)
                            .setTitle("注册失败")
                            .setMessage("密码不能为空")
                            .setPositiveButton("确认",null)
                            .show();
                }else if(!pw1.equals(pw2)){
                    new android.support.v7.app.AlertDialog.Builder(Register.this)
                            .setTitle("注册失败")
                            .setMessage("两次输入得密码不一致，请重新输入")
                            .setPositiveButton("确认",null)
                            .show();
                }else {
                    new Thread() {
                        public void run() {
                            try {
                                String temp = g.readParse("http://" + g.hostip + ":3000/users/register/" + account + "/" + pw1);
                                if (temp.equals("existence")) {
                                    handler.post(runnable1);
                                }
                                if (temp.equals("true")) {
                                    handler.post(runnable2);
                                }
                            } catch (Exception e) {
                            }
                        }

                        ;
                    }.start();
                }


            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, SignIn.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
    Runnable   runnable1=new  Runnable(){
        @Override
        public void run() {
            new android.support.v7.app.AlertDialog.Builder(Register.this)
                    .setTitle("注册失败")
                    .setMessage("用户名已存在")
                    .setPositiveButton("确认",null)
                    .show();
        }

    };
    Runnable   runnable2=new  Runnable(){
        @Override
        public void run() {
            new android.support.v7.app.AlertDialog.Builder(Register.this)
                    .setTitle("注册成功")
                    .setMessage("接下来您可以用您的账号登录了")
                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Register.this, SignIn.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    })
                    .show();
        }

    };
}
