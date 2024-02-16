package com.example.lagani20.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lagani20.R;
import com.example.lagani20.classes.Donations;

import org.w3c.dom.Text;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<Donations> list;

    public RecyclerViewAdapter(Context context, List<Donations> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardformat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Donations donations = list.get(position);
        holder.address.setText(donations.getAddress());
        holder.mobile.setText(donations.getMobileno());
        holder.donationtype.setText(donations.getDonationtype());
        holder.donationweight.setText(donations.getDonationweight());
        holder.pincode.setText(donations.getPincode());
        holder.vehicletype.setText(donations.getSuggestedvehicle());
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
        public TextView resturantname;
        public TextView vehicletype;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            donationtype = itemView.findViewById(R.id.type);
            donationweight = itemView.findViewById(R.id.weight_card);
            pincode = itemView.findViewById(R.id.pincode_card);
            address = itemView.findViewById(R.id.address_card);
            mobile = itemView.findViewById(R.id.contact_card);
           // resturantname = itemView.findViewById(R.id.res_name);
            vehicletype = itemView.findViewById(R.id.vehicle_card);

        }

        @Override
        public void onClick(View view) {

        }
    }
}

