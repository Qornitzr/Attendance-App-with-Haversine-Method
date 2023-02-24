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
import com.example.attendanceappsdpmd.adapter.AbsenKeluarAdapter;
import com.example.attendanceappsdpmd.adapter.AbsenMasukAdapter;
import com.example.attendanceappsdpmd.data.AbsenKeluar;
import com.example.attendanceappsdpmd.data.AbsenMasuk;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DataAbsenKeluarActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AbsenKeluarAdapter absenKeluarAdapter;
    private ArrayList<AbsenKeluar> absenKeluar;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_absen_keluar);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Data Absen Keluar");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DataAbsenKeluarActivity.this, DashboardActivity.class));
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
        databaseReference = firebaseDatabase.getReference("absen_keluar");
        databaseReference.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                absenKeluar = new ArrayList<>();
                for (DataSnapshot mDataSnapshot : dataSnapshot.getChildren()) {
                    AbsenKeluar absen = mDataSnapshot.getValue(AbsenKeluar.class);
                    assert absen != null;
                    absen.setKeluar_Nip(mDataSnapshot.getKey());
                    absenKeluar.add(absen);

                    absenKeluarAdapter =new AbsenKeluarAdapter(DataAbsenKeluarActivity.this, absenKeluar);
                    recyclerView.setAdapter(absenKeluarAdapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DataAbsenKeluarActivity.this, databaseError.getDetails() + " " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(DataAbsenKeluarActivity.this,DashboardActivity.class));
        finish();
    }
}
