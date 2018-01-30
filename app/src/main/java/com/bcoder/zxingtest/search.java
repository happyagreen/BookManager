package com.bcoder.zxingtest;

import android.content.Intent;
import android.graphics.Picture;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class search extends AppCompatActivity {
    EditText et1,et2;
    Button check1,check2;
    global_variable g;
    RadioGroup rg;
    RadioButton radioButton1,radioButton2;
    int selectid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        et1 = (EditText)findViewById(R.id.editText7);
        check1 = (Button)findViewById(R.id.button22);

        selectid = radioButton1.getId();
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                selectid = checkedId;
            }
        });
        g= (global_variable)getApplication();
        check1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                g.searchkey = et1.getText().toString();
                g.searchtype = 0 ;
                if(selectid == radioButton2.getId()) {
                    g.searchtype+= 2;
                }
                Intent intent = new Intent(search.this,SearchResult.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        et2 = (EditText)findViewById(R.id.editText9);
        check2 = (Button)findViewById(R.id.button23);
        check2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                g.searchkey = et2.getText().toString();
                g.searchtype = 1 ;
                if(selectid == radioButton2.getId()) {
                    g.searchtype+= 2;
                }
                Intent intent = new Intent(search.this,SearchResult.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

}
