package com.example.lagani20.Adapter;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lagani20.R;
import com.example.lagani20.classes.Donations;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class RecyclerView2 extends RecyclerView.Adapter<RecyclerView2.ViewHolder2> {

    private Context context;
    private List<Donations> list;
    private static final int REQUEST_LOCATION_PERMISSION = 1001;

    public RecyclerView2(Context context, List<Donations> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardformat2, parent, false);
        return new ViewHolder2(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder2 holder, int position) {
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
        public Button btn;
        public Button btn1;
        private FirebaseDatabase firebaseDatabase;
        private double currentLatitude;
        private double currentLongitude;

        public ViewHolder2(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            donationtype = itemView.findViewById(R.id.type_2);
            donationweight = itemView.findViewById(R.id.weight_card_2);
            pincode = itemView.findViewById(R.id.pincode_card_2);
            address = itemView.findViewById(R.id.address_card_2);
            mobile = itemView.findViewById(R.id.contact_card_2);
            resturantname = itemView.findViewById(R.id.res_name_2);
            vehicletype = itemView.findViewById(R.id.vehicle_card_2);
            btn = itemView.findViewById(R.id.deliverd_btn);
            btn1 = itemView.findViewById(R.id.direction_btn);
            firebaseDatabase = FirebaseDatabase.getInstance();

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Confirm Action");
                        builder.setMessage("Are you sure you Delivered?");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ProgressDialog dialog = new ProgressDialog(context);
                                dialog.setMessage("Getting Location...");
                                dialog.setCancelable(false);
                                dialog.setCanceledOnTouchOutside(false);
                                dialog.show();
                                FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
                                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
                                    return;
                                }
                                fusedLocationClient.getLastLocation()
                                        .addOnSuccessListener((Activity) context, location -> {
                                            if (location != null) {
                                                currentLatitude = location.getLatitude();
                                                currentLongitude = location.getLongitude();
                                                Donations donation = list.get(position);
                                                donation.setStatus("2");
                                                donation.setLatitude2(currentLatitude);
                                                donation.setLongitude2(currentLongitude);
                                                firebaseDatabase.getReference()
                                                        .child("Donations")
                                                        .child(donation.getDonationid())
                                                        .setValue(donation)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                list.remove(position);
                                                                notifyItemRemoved(position);
                                                                dialog.dismiss();
                                                                Toast.makeText(context, "Order Delivered", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                            } else {
                                                Toast.makeText(context, "Location can't be retrieved. Try again.", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener((Activity) context, e -> {
                                            Toast.makeText(context,"Location can't be retrieved.",Toast.LENGTH_LONG).show();
                                        });
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        builder.show();
                    }
                }
            });


            btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    Donations donations = list.get(position);
                    Double longitude = donations.getLongitude();
                    Double latitude = donations.getLatitude();
                    Uri uri = Uri.parse("geo:" + latitude + "," + longitude + "?q=" + latitude + "," + longitude);
                    Intent mapintent = new Intent(Intent.ACTION_VIEW, uri);
                    mapintent.setPackage("com.google.android.apps.maps");
                    if (mapintent.resolveActivity(context.getPackageManager()) != null) {
                        context.startActivity(mapintent);
                    } else {
                        Toast.makeText(context, "Google Map Is Not Installed", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

        @Override
        public void onClick(View view) {

        }

        private void requestLocationUpdates() {

        }
    }
}
