package com.example.attendanceappsdpmd.fragment;

import android.app.Activity;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.attendanceappsdpmd.activities.DataAbsenTidakMasukActivity;
import com.example.attendanceappsdpmd.R;
import com.example.attendanceappsdpmd.activities.DataAbsenKeluarActivity;
import com.example.attendanceappsdpmd.activities.DataAbsenMasukActivity;
import com.example.attendanceappsdpmd.activities.LoginActivity;
import com.example.attendanceappsdpmd.data.UsersData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment {
    private CircleImageView circleImageView;
    private TextView userName, tanggal;
    private ImageView logout;
    private CardView cardViewMasuk, cardViewKeluar, cardViewTidakMasuk;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private UsersData usersData;

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getApplicationContext();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ImageView logout = getActivity().findViewById(R.id.logout);
        userName = getActivity().findViewById(R.id.txtHelloUser);
        tanggal = getActivity().findViewById(R.id.txtTanggal);
        final CircleImageView circleImageView = getActivity().findViewById(R.id.profileImage);
        cardViewMasuk = getActivity().findViewById(R.id.cardPresensiMasuk);
        cardViewKeluar = getActivity().findViewById(R.id.cardPresensiKeluar);
        cardViewTidakMasuk = getActivity().findViewById(R.id.cardPresensiTidakMasuk);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        Calendar calendar =  Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMMM d");
        String date = dateFormat.format(calendar.getTime());
        tanggal.setText(date);
        storageReference = FirebaseStorage.getInstance().getReference("profile_images");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final UsersData usersData = dataSnapshot.getValue(UsersData.class);
                assert usersData != null;
                userName.setText("Hello, "+usersData.getNama());
                if(usersData.getImageUrl().equals("default")){
                    circleImageView.setImageResource(R.drawable.potodefault);
                }else{
                    Glide.with(Objects.requireNonNull(getActivity()).getApplicationContext()).load(usersData.getImageUrl()).into(circleImageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        cardViewMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), DataAbsenMasukActivity.class));
            }
        });
        cardViewKeluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), DataAbsenKeluarActivity.class));
            }
        });
        cardViewTidakMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), DataAbsenTidakMasukActivity.class));
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                Objects.requireNonNull(getActivity()).finish();
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


}