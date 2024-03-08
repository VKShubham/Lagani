package com.example.lagani20.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lagani20.R;
import com.example.lagani20.classes.Donations;

import org.w3c.dom.Text;

import java.util.List;

public class RecyclerViewForHistory extends RecyclerView.Adapter<RecyclerViewForHistory.ViewHolder> {

    private Context context;
    private List<Donations> list;

    public RecyclerViewForHistory(Context context, List<Donations> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerViewForHistory.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.historycard,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewForHistory.ViewHolder holder, int position) {
        Donations donations = list.get(position);
        holder.orderno.setText(donations.getDonationnumber());
        holder.placename.setText(donations.getResturant());
        holder.date.setText(donations.getDate());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView orderno;
        public TextView placename;
        public TextView date;
        public ImageView btn1;
        public  ImageView btn2;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderno = itemView.findViewById(R.id.history_order);
            placename = itemView.findViewById(R.id.history_placename);
            date = itemView.findViewById(R.id.history_date);
            btn1 = itemView.findViewById(R.id.call_btn);
            btn2 = itemView.findViewById(R.id.direction_btn);

            btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    Donations donations = list.get(position);
                    String phoneno = donations.getMobileno();
                    Intent call = new Intent(Intent.ACTION_CALL);
                    call.setData(Uri.parse("tel: " + phoneno));
                    context.startActivity(call);
                }
            });

            btn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    Donations donations = list.get(position);
                    Double longitude = donations.getLongitude2();
                    Double latitude = donations.getLatitude2();
                    Uri uri = Uri.parse("geo:" + latitude + "," + longitude + "?q=" + latitude + "," + longitude);
                    Intent mapintent = new Intent(Intent.ACTION_VIEW, uri);
                    mapintent.setPackage("com.google.android.apps.maps");
                    if(mapintent.resolveActivity(context.getPackageManager()) != null){
                        context.startActivity(mapintent);
                    }
                    else{
                        Toast.makeText(context, "Google Map Is Not Installed", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

        @Override
        public void onClick(View view) {

        }

    }
}
