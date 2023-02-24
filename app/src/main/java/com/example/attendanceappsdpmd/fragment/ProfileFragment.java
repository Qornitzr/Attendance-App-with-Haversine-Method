package com.example.attendanceappsdpmd.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.attendanceappsdpmd.R;
import com.example.attendanceappsdpmd.activities.EditProfileActivity;
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

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    private CircleImageView circleImageView;
    private TextView namaUser, nipUser, jabatanUser;
    private Button btnEdit;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private UsersData usersData;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        namaUser = view.findViewById(R.id.txtNamaUser);
        nipUser = view.findViewById(R.id.txtNipUser);
        jabatanUser = view.findViewById(R.id.txtJabatan);
        circleImageView = view.findViewById(R.id.profileImage);
        btnEdit = view.findViewById(R.id.btnEditProfile);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference("profile_images");
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UsersData usersData = dataSnapshot.getValue(UsersData.class);
                assert usersData != null;
                namaUser.setText(usersData.getNama());
                nipUser.setText("NIP. "+usersData.getNip());
                jabatanUser.setText(usersData.getJabatan());
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
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), EditProfileActivity.class));
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}