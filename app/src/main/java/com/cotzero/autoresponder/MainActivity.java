package com.cotzero.autoresponder;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    CardView addRule,wpBtn,mesBtn,telBtn,instaBtn;
    Function function;

    Switch wpSw,mesSw,telSw,instaSw;

    TextView w_rule_count,m_rule_count,t_rule_count,i_rule_count;

    Intent intent;

    Intent serviceIntent;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        serviceIntent = new Intent(this,NotificationReceiver.class);

        addRule = findViewById(R.id.float_add);
        wpBtn = findViewById(R.id.wpBtn);
        mesBtn = findViewById(R.id.messengerBtn);
        telBtn = findViewById(R.id.telBtn);
        instaBtn = findViewById(R.id.instaBtn);

        function = new Function(this);
        wpSw = findViewById(R.id.wsw);
        mesSw = findViewById(R.id.msw);
        telSw = findViewById(R.id.tsw);
        instaSw = findViewById(R.id.isw);

        w_rule_count = findViewById(R.id.w_rule_count);
        m_rule_count = findViewById(R.id.m_rule_count);
        t_rule_count = findViewById(R.id.t_rule_count);
        i_rule_count = findViewById(R.id.i_rule_count);



        JSONObject jsonObject = function.target();

        try {

            if(jsonObject.getBoolean("wp")){
                wpSw.setChecked(true);
            }else {
                wpSw.setChecked(false);
            }

            if(jsonObject.getBoolean("mes")){
                mesSw.setChecked(true);
            }else {
                mesSw.setChecked(false);
            }

            if(jsonObject.getBoolean("tel")){
                telSw.setChecked(true);
            }else {
                telSw.setChecked(false);
            }

            if(jsonObject.getBoolean("insta")){
                instaSw.setChecked(true);
            }else {
                instaSw.setChecked(false);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        addRule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,AddRule.class).putExtra("type",0));
            }
        });

        intent = new Intent(this,RuleList.class);

        wpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("type",0);
                startActivity(intent);
            }
        });

        mesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("type",1);
                startActivity(intent);
            }
        });

        telBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("type",2);
                startActivity(intent);
            }
        });

        instaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("type",3);
                startActivity(intent);
            }
        });

        wpSw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(wpSw.isChecked()){

                    function.enableTarget(0,true);
                    startService(serviceIntent);

                }else {

                    function.enableTarget(0,false);
                    stopService(serviceIntent);

                }
            }
        });

        mesSw.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(mesSw.isChecked()){

                    function.enableTarget(1,true);

                }else {

                    function.enableTarget(1,false);
                }
            }
        });
        telSw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(telSw.isChecked()){

                    function.enableTarget(2,true);

                }else {

                    function.enableTarget(2,false);
                }
            }
        });
        instaSw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(instaSw.isChecked()){

                    function.enableTarget(3,true);

                }else {

                    function.enableTarget(3,false);
                }
            }
        });

      //  NotificationReceiver notificationReceiver = new NotificationReceiver();

    }

    @Override
    protected void onResume() {
        super.onResume();
        JSONObject count = function.getRulesCount();

        try {
            w_rule_count.setText(getRuleString(count.getInt("wpCount")));
            m_rule_count.setText(getRuleString(count.getInt("mesCount")));
            t_rule_count.setText(getRuleString(count.getInt("telCount")));
            i_rule_count.setText(getRuleString(count.getInt("instaCount")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String getRuleString(int no){
        if(no>1){
            return no+" Rules";
        }else {
            return no+" Rule";
        }
    }

}