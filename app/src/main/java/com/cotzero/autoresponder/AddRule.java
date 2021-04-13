package com.cotzero.autoresponder;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;
import java.util.StringTokenizer;

public class AddRule extends AppCompatActivity {

    CardView saveBtn;

    EditText receiveMessage,reply,contacts,ignoreContacts;

    CheckBox twp,tmes,ttel,tinsta,indi,group;
    TextView textView;

    Button all;


    JSONObject jsonObject;
    Function function;

    int type,id;
    ImageButton deleteBtn, info_reply,info_sp,info_ig;

    AlertDialog.Builder builder;

    String msg ="msg";
    String title="title";
    AlertDialog alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_rule);

        type = getIntent().getIntExtra("type",0);

        saveBtn = findViewById(R.id.float_save);
        receiveMessage = findViewById(R.id.et_receive_message);
        reply = findViewById(R.id.et_reply);
        contacts = findViewById(R.id.et_contacts);
        ignoreContacts = findViewById(R.id.et_ig_contacts);
        twp = findViewById(R.id.cb_whatsapp);
        tmes = findViewById(R.id.cb_messenger);
        ttel = findViewById(R.id.cb_telegram);
        tinsta = findViewById(R.id.cb_instagram);
        indi = findViewById(R.id.cb_indi);
        group = findViewById(R.id.cb_group);
        textView = findViewById(R.id.title);
        deleteBtn = findViewById(R.id.delete);
        all = findViewById(R.id.allBtn);
        info_reply = findViewById(R.id.info_reply);
        info_sp = findViewById(R.id.info_sp);
        info_ig = findViewById(R.id.info_ig);

        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                receiveMessage.setText("*");
            }
        });

        builder = new AlertDialog.Builder(this);




      //  alert.setMessage(msg);



        info_reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = "Your reply";
                msg = "For multiple reply write your replies with a separated comma(,)." +
                        "\nDuring sending of your reply app will choose one reply randomly and that will be sent.";
                builder.setMessage(msg)
                        .setTitle(title)
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                alert= builder.create();
                alert.show();
            }
        });

        info_sp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = "Contacts";
                msg = "For whatsapp and telegram type the name of a contact, a group name or a phone number like the are displayed in notification bar." +
                        "\nFor instagram type the username of a contact." +
                        "\nFor messenger use the name of a person which is on messenger." +
                        "\n" +
                        "For multiple contacts write with a separated comma(,)";
                builder.setMessage(msg)
                        .setTitle(title)
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                alert= builder.create();
                alert.show();
            }
        });

        info_ig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = "Contacts";
                msg = "For whatsapp and telegram type the name of a contact, a group name or a phone number like the are displayed in notification bar." +
                        "\nFor instagram type the username of a contact." +
                        "\nFor messenger use the name of a person which is on messenger." +
                        "\n" +
                        "For multiple contacts write with a separated comma(,)";
                builder.setMessage(msg)
                        .setTitle(title)
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                alert= builder.create();
                alert.show();
            }
        });


        jsonObject = new JSONObject();
        function = new Function(this);


        if(type==0){

            Random rand = new Random();
            id = rand.nextInt(999999999);
            deleteBtn.setVisibility(View.GONE);

        }else {

            textView.setText("Edit Rule");

            try {

                JSONObject ejson = new JSONObject(getIntent().getStringExtra("data"));

                receiveMessage.setText(extractString(ejson.getJSONArray("receiveMsg")));
                reply.setText(extractString(ejson.getJSONArray("reply")));
                contacts.setText(extractString(ejson.getJSONArray("contacts")));
                ignoreContacts.setText(extractString(ejson.getJSONArray("ignoreContacts")));

                int receiver = ejson.getInt("receiver");

                if(receiver==0){
                    indi.setChecked(true);
                }else if(receiver==1){
                    group.setChecked(true);
                }

                JSONArray targets = ejson.getJSONArray("target");

                for(int i=0; i<targets.length(); i++){
                    if(targets.getInt(i)==0){
                        twp.setChecked(true);
                    }
                    if(targets.getInt(i)==1){
                        tmes.setChecked(true);
                    }
                    if(targets.getInt(i)==2){
                        ttel.setChecked(true);
                    }
                    if(targets.getInt(i)==3){
                        tinsta.setChecked(true);
                    }
                }

                id= ejson.getInt("id");




            } catch (JSONException e) {
                e.printStackTrace();
            }

            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onClick(View view) {
                    function.delete(id);
                    finish();
                }
            });




        }




        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    if(!receiveMessage.getText().toString().equals("")) {

                        if(!reply.getText().toString().equals("")) {

                            jsonObject.put("receiveMsg", getStrings(receiveMessage.getText().toString()));
                            jsonObject.put("reply", getStrings(reply.getText().toString()));
                            jsonObject.put("contacts", getStrings(contacts.getText().toString()));
                            jsonObject.put("ignoreContacts", getStrings(ignoreContacts.getText().toString()));

                            int a = 2;
                            if (indi.isChecked()) {
                                a = 0;
                            }
                            if (group.isChecked()) {
                                a = 1;
                            }
                            if (indi.isChecked() && group.isChecked()) {
                                a = 2;
                            }
                            jsonObject.put("receiver", a);

                            JSONArray targets = new JSONArray();

                            if (twp.isChecked()) {
                                targets.put(0);
                            }
                            if (tmes.isChecked()) {
                                targets.put(1);
                            }
                            if (ttel.isChecked()) {
                                targets.put(2);
                            }
                            if (tinsta.isChecked()) {
                                targets.put(3);
                            }
                            if (targets.length() != 0) {

                                jsonObject.put("target", targets);



                                jsonObject.put("id",id);
                                jsonObject.put("enable",true);





                                if(type==0) {

                                    if(function.insert(jsonObject)){
                                        Toast.makeText(AddRule.this, "Saved", Toast.LENGTH_SHORT).show();
                                    }else {
                                        Toast.makeText(AddRule.this, "Error", Toast.LENGTH_SHORT).show();
                                    }

                                }else {



                                    if(function.editObject(id,jsonObject)){
                                        Toast.makeText(AddRule.this, "Saved", Toast.LENGTH_SHORT).show();
                                    }else {
                                        Toast.makeText(AddRule.this, "Error", Toast.LENGTH_SHORT).show();
                                    }



                                }

                                Log.i("jsonObject",function.getList().toString());


                                finish();


                            } else {
                                Toast.makeText(AddRule.this, "Choose a target messenger", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(AddRule.this, "Enter a reply", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(AddRule.this, "Enter receive message", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

              //
            }
        });




    }

    JSONArray getStrings(String s){
        JSONArray jsonArray = new JSONArray();
        if(s==null || s.equals(""))return jsonArray;

        if(s.contains(",")) {
            StringTokenizer stringTokenizer = new StringTokenizer(s, ",");
            while (stringTokenizer.hasMoreTokens()) {
                jsonArray.put(stringTokenizer.nextToken().trim());
            }
        }else {
            jsonArray.put(s);
        }
        return jsonArray;

    }

    private String extractString(JSONArray jsonArray){

        Log.i("neloy",jsonArray.toString());
        String s="";
        for(int i=0; i<jsonArray.length();i++){
            try {
                s = s + jsonArray.getString(i);
                if(i!=jsonArray.length()-1){
                    s=s+", ";
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return s;
    }

}
