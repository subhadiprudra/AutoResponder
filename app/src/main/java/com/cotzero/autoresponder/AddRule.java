package com.cotzero.autoresponder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
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


    JSONObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_rule);

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

        jsonObject = new JSONObject();




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
                            if (indi.isChecked() && indi.isChecked()) {
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

                                Random rand = new Random();
                                int rand_int1 = rand.nextInt(999999999);

                                jsonObject.put("id",rand_int1);
                                jsonObject.put("enable",true);

                                Log.i("jsonObject",jsonObject.toString());

                                Toast.makeText(AddRule.this, "Saved", Toast.LENGTH_SHORT).show();


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

}
