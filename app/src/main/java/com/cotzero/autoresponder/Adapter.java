package com.cotzero.autoresponder;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.w3c.dom.Text;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>  {

    Context context;
    JSONArray jsonArray;
    Function function;

    public Adapter(Context context, JSONArray jsonArray) {
        this.context = context;
        this.jsonArray = jsonArray;
        function = new Function(context);
    }

    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);
        return new  ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Adapter.ViewHolder holder, final int position) {

        try {

            holder.receive.setText(extractString(jsonArray.getJSONObject(position).getJSONArray("receiveMsg")));
            holder.send.setText(extractString(jsonArray.getJSONObject(position).getJSONArray("reply")));

            if(jsonArray.getJSONObject(position).getBoolean("enable")){
                holder.aSwitch.setChecked(true);
            }else {
                holder.aSwitch.setChecked(false);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {

                    Intent intent = new Intent(context,AddRule.class);
                    intent.putExtra("type",1);
                    intent.putExtra("data",jsonArray.getJSONObject(position).toString());
                    context.startActivity(intent);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        holder.aSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.aSwitch.isChecked()){
                    try {
                        function.enable(jsonArray.getJSONObject(position).getInt("id"),true);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    try {
                        function.enable(jsonArray.getJSONObject(position).getInt("id"),false);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });



    }

    @Override
    public int getItemCount() {
        return jsonArray.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView receive,send;
        CardView cardView;
        Switch aSwitch;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            receive = itemView.findViewById(R.id.receive);
            send = itemView.findViewById(R.id.send);
            cardView = itemView.findViewById(R.id.card);
            aSwitch = itemView.findViewById(R.id.item_sw);


        }
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
