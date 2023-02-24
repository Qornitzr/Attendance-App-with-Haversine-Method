package com.example.attendanceappsdpmd.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.attendanceappsdpmd.R;
import com.example.attendanceappsdpmd.adapter.AbsenMasukAdapter;
import com.example.attendanceappsdpmd.adapter.AbsenTidakMasukAdapter;
import com.example.attendanceappsdpmd.data.AbsenMasuk;
import com.example.attendanceappsdpmd.data.AbsenTidakMasuk;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DataAbsenTidakMasukActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AbsenTidakMasukAdapter absenTidakMasukAdapter;
    private ArrayList<AbsenTidakMasuk> absenTidakMasuk;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_absen_tidak_masuk);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Data Absen Tidak Masuk");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DataAbsenTidakMasukActivity.this, DashboardActivity.class));
                finish();
            }
        });
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseApp.initializeApp(this);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("absen_tidak_masuk");
        databaseReference.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                absenTidakMasuk = new ArrayList<>();
                for (DataSnapshot mDataSnapshot : dataSnapshot.getChildren()) {
                    AbsenTidakMasuk absen = mDataSnapshot.getValue(AbsenTidakMasuk.class);
                    assert absen != null;
                    absen.setTidakMasuk_Nip(mDataSnapshot.getKey());
                    absenTidakMasuk.add(absen);

                    absenTidakMasukAdapter =new AbsenTidakMasukAdapter(DataAbsenTidakMasukActivity.this, absenTidakMasuk);
                    recyclerView.setAdapter(absenTidakMasukAdapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DataAbsenTidakMasukActivity.this, databaseError.getDetails() + " " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(DataAbsenTidakMasukActivity.this,DashboardActivity.class));
        finish();
    }
}