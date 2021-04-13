package com.cotzero.autoresponder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

public class RuleList extends AppCompatActivity {

    RecyclerView recyclerView;
    Function function;
    int target;

    TextView title;
    LinearLayout emptyView;

    CardView add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rule_list);

        recyclerView = findViewById(R.id.rules);
        title = findViewById(R.id.title);
        emptyView = findViewById(R.id.empty_view);
        add = findViewById(R.id.float_add);



        function = new Function(this);
        target = getIntent().getIntExtra("type",0);

        switch (target){
            case 0:
                title.setText("Whatsapp Rules");
                break;

            case 1:
                title.setText("Messenger Rules");
                break;

            case 2:
                title.setText("Telegram Rules");
                break;
            case 3:
                title.setText("Instagram Rules");
                break;
        }

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RuleList.this,AddRule.class).putExtra("type",0));
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        try {

            JSONArray jsonArray = function.getList(target);

            if(jsonArray.length()==0){
                recyclerView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
            }else {
                recyclerView.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
                Adapter adapter = new Adapter(this,jsonArray);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                recyclerView.setAdapter(adapter);
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}