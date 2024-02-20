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

import java.util.List;

public class RecyclerView2 extends RecyclerView.Adapter<com.example.lagani20.Adapter.RecyclerView2.ViewHolder2> {

        private Context context;
        private List<Donations> list;

        public RecyclerView2(Context context, List<Donations> list) {
            this.context = context;
            this.list = list;
        }

        @NonNull
        @Override
        public com.example.lagani20.Adapter.RecyclerView2.ViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardformat2, parent, false);
            return new com.example.lagani20.Adapter.RecyclerView2.ViewHolder2(view);
        }

        @Override
        public void onBindViewHolder(@NonNull com.example.lagani20.Adapter.RecyclerView2.ViewHolder2 holder, int position) {
            Donations donations = list.get(position);
            holder.address.setText(donations.getAddress());
            holder.mobile.setText(donations.getMobileno());
            holder.donationtype.setText(donations.getDonationtype());
            holder.donationweight.setText(donations.getDonationweight());
            holder.pincode.setText(donations.getPincode());
            holder.vehicletype.setText(donations.getSuggestedvehicle());
            holder.resturantname.setText(donations.getResturant());
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ViewHolder2 extends RecyclerView.ViewHolder implements View.OnClickListener {

            public TextView donationtype;
            public TextView donationweight;
            public TextView pincode;
            public TextView address;
            public TextView mobile;
            public TextView resturantname;
            public TextView vehicletype;

            public ViewHolder2(@NonNull View itemView) {
                super(itemView);
                //itemView.setOnClickListener(this);
                donationtype = itemView.findViewById(R.id.type_2);
                donationweight = itemView.findViewById(R.id.weight_card_2);
                pincode = itemView.findViewById(R.id.pincode_card_2);
                address = itemView.findViewById(R.id.address_card_2);
                mobile = itemView.findViewById(R.id.contact_card_2);
                resturantname = itemView.findViewById(R.id.res_name_2);
                vehicletype = itemView.findViewById(R.id.vehicle_card_2);
            }

            @Override
            public void onClick(View view) {
                // Handle item click if needed
            }
        }
}
