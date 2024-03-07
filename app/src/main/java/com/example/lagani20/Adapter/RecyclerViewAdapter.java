package com.example.lagani20.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lagani20.R;
import com.example.lagani20.classes.Donations;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<Donations> list;
    private FirebaseDatabase firebaseDatabase;


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
        holder.resturantname.setText(donations.getResturant());
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
        public Button viewphoto;
        public Button accpetbtn;
        private FirebaseAuth auth;
        private FirebaseStorage firebaseStorage;
        private StorageReference storageReference;
        private FirebaseUser user;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            accpetbtn = itemView.findViewById(R.id.accpet_btn);
            accpetbtn.setOnClickListener(this);
            firebaseDatabase = FirebaseDatabase.getInstance();
            donationtype = itemView.findViewById(R.id.type);
            donationweight = itemView.findViewById(R.id.weight_card);
            pincode = itemView.findViewById(R.id.pincode_card);
            address = itemView.findViewById(R.id.address_card);
            mobile = itemView.findViewById(R.id.contact_card);
            resturantname = itemView.findViewById(R.id.res_name);
            viewphoto = itemView.findViewById(R.id.viewphoto);
            vehicletype = itemView.findViewById(R.id.vehicle_card);
            auth = FirebaseAuth.getInstance();
            user = auth.getCurrentUser();

            viewphoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Dialog dialog = new Dialog(context);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.viewphoto);
                    storageReference = FirebaseStorage.getInstance().getReference();
                    int position = getAdapterPosition();
                    Donations donations = list.get(position);
                    String key = donations.getDonationid();
                    StorageReference reference = storageReference.child("donation/" + key + ".jpg");
                    ImageView image = dialog.findViewById(R.id.view_img);
                    reference.getFile(new File(context.getCacheDir(),"Donation.jpg")).addOnCompleteListener(task -> {
                        Bitmap bitmap = BitmapFactory.decodeFile(new File(context.getCacheDir(), "Donation.jpg").getAbsolutePath());
                        if (image != null && bitmap != null) {
                            image.setImageBitmap(bitmap);
                        } else {
                            Log.e("ProfileDialog", "ImageView or Bitmap is null");
                        }
                    });
                    Bitmap bitmap = BitmapFactory.decodeFile(new File(context.getCacheDir(), "Donation.jpg").getAbsolutePath());

                    if (image != null && bitmap != null) {
                        image.setImageBitmap(bitmap);
                    } else {
                        Log.e("ProfileDialog", "ImageView or Bitmap is null");
                    }
                    Window window = dialog.getWindow();
                    if (window != null) {
                        window.setBackgroundDrawableResource(android.R.color.transparent);
                        dialog.show();
                    }
                }
            });
        }

        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Donations donations = list.get(position);
                donations.setStatus("1");
                donations.setUserid2(user.getUid());
                firebaseDatabase.getReference()
                        .child("Donations")
                        .child(donations.getDonationid())
                        .setValue(donations)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                list.remove(position);
                                notifyItemRemoved(position);
                                Toast.makeText(context, "Order Accepted", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }
    }
}
