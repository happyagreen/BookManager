package com.bcoder.zxingtest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.bcoder.zxingtest.MainActivity.readParse;

public class SignIn extends AppCompatActivity {
    Button button,button2;
    String temp;
    global_variable g;
    Intent intent;
    EditText et1,et2;
    Handler handler;
    SharedPreferences sp;
    CheckBox cb1;
    String name;
    String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        button = (Button)findViewById(R.id.button16);
        et1 = (EditText)findViewById(R.id.editText2);
        et2 = (EditText)findViewById(R.id.editText3);
        button2 = (Button)findViewById(R.id.button17);
        cb1 = (CheckBox)findViewById(R.id.checkBox);
        intent = new Intent(SignIn.this, Preload.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        handler=new Handler();
        g = (global_variable)getApplication();
        g.bookshelfs.clear();
        sp = getSharedPreferences("loginToken", 0);
        name = sp.getString("userName", null);
        password = sp.getString("userPassword", null);
        new Thread() {
            public void run() {
                try {
                    if (name != null) {
                        temp = g.readParse("http://" + g.hostip + ":3000/users/login/" + name + "/" + password);
                        if (temp.equals("true")) {
                            g.accout = name;
                            startActivity(intent);
                        }
                    }
                } catch (Exception e) {
                }
            }
        }.start();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    public void run() {
                        try {
                            String accout = et1.getText().toString();
                            String password = et2.getText().toString();
                            temp = g.readParse("http://"+g.hostip+":3000/users/login/"+ accout +"/" + password);
                            if(temp.equals("true")) {
                                g.accout = accout;
                                if(cb1.isChecked()) {
                                    sp = getSharedPreferences("loginToken", 0);
                                    SharedPreferences.Editor editor = sp.edit();
                                    editor.putString("userName", accout);
                                    editor.putString("userPassword", password);
                                    editor.commit();
                                }else if(!cb1.isChecked()){
                                    sp = getSharedPreferences("loginToken", 0);
                                    SharedPreferences.Editor editor = sp.edit();
                                    editor.putString("userName", null);
                                    editor.putString("userPassword", null);
                                    editor.commit();
                                }
                                startActivity(intent);
                            }
                            else if(temp.equals("false")){
                                handler.post(runnableUi);

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    ;
                }.start();

            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignIn.this, Register.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    Runnable   runnableUi=new  Runnable(){
        @Override
        public void run() {
            //更新界面
            new android.support.v7.app.AlertDialog.Builder(SignIn.this)
                    .setTitle("登录失败")
                    .setMessage("用户名密码错误")
                    .setPositiveButton("确认",null)
                    .show();
        }

    };





}
