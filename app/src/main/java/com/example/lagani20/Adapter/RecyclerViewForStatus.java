package com.example.lagani20.Adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lagani20.R;
import com.example.lagani20.classes.Donations;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class RecyclerViewForStatus extends RecyclerView.Adapter<RecyclerViewForStatus.ViewHolder> {

    private Context context;
    private List<Donations> list;

    public RecyclerViewForStatus(Context context, List<Donations> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.status_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Donations donations = list.get(position);
        holder.orderno.setText(donations.getDonationnumber());
        holder.address.setText(donations.getAddress());
        holder.mobile.setText(donations.getMobileno());
        holder.donationtype.setText(donations.getDonationtype());
        holder.donationweight.setText(donations.getDonationweight());
        holder.pincode.setText(donations.getPincode());
        holder.vehicletype.setText(donations.getSuggestedvehicle());
        holder.status.setText(status(donations));
        if(donations.getStatus().equals("1")){
            holder.statuscolor.setImageResource(R.drawable.greenround);
        }else{
            holder.statuscolor.setImageResource(R.drawable.red_round);
        }
        holder.orderno.setText(donations.getDonationnumber());
    }

    public String status(Donations donations){
        String n = donations.getStatus();
        if(n.equals("1")){
            return "Active";
        }
        return "Completed";
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView donationtype;
        public TextView donationweight;
        public TextView pincode;
        public TextView address;
        public TextView mobile;
        public TextView vehicletype;
        public TextView orderno;
        public TextView status;
        public ImageView statuscolor;

        private  FirebaseDatabase firebaseDatabase;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            donationtype = itemView.findViewById(R.id.type_3);
            donationweight = itemView.findViewById(R.id.weight_card_3);
            pincode = itemView.findViewById(R.id.pincode_card_3);
            address = itemView.findViewById(R.id.address_card_3);
            mobile = itemView.findViewById(R.id.contact_card_3);
            vehicletype = itemView.findViewById(R.id.vehicle_card_3);
            orderno = itemView.findViewById(R.id.order_no);
            status = itemView.findViewById(R.id.status_text);
            statuscolor = itemView.findViewById(R.id.status_color);
            firebaseDatabase = FirebaseDatabase.getInstance();
            // Set click listener if needed
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
