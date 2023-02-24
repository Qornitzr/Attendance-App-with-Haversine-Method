package com.example.attendanceappsdpmd.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest.permission;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.text.SimpleDateFormat;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.attendanceappsdpmd.R;
import com.example.attendanceappsdpmd.data.UsersData;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.skyfishjy.library.RippleBackground;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static java.lang.Math.abs;
import static java.lang.Math.asin;
import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.Math.toRadians;

public class AbsenActivity extends AppCompatActivity {

    private FloatingActionButton checkIn;
    private TextView tvScan, tvCheckinSuccess;
    private static final int ID_LOCATION_PERMISSION = 0;
    DatePickerDialog.OnDateSetListener setListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absen);
        tvScan = findViewById(R.id.tvScanning);
        tvCheckinSuccess = findViewById(R.id.tvCheckInSuccess);
        checkIn = findViewById(R.id.fabCheckIn);
        checkPermissionLocation();
        timePicker();
        onClick();

    }

    private void onClick() {
        checkIn = findViewById(R.id.fabCheckIn);
        checkIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadScanLocation();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getLastLocation();
                    }
                },3000);
            }
        });
    }

    private void loadScanLocation() {
        final RippleBackground rippleBackground = findViewById(R.id.rippleBackground);
        rippleBackground.startRippleAnimation();
        tvScan.setVisibility(View.VISIBLE);
        tvCheckinSuccess.setVisibility(View.GONE);
    }

    private void stopScanLocation() {
        final RippleBackground rippleBackground = findViewById(R.id.rippleBackground);
        tvScan = findViewById(R.id.tvScanning);
        rippleBackground.stopRippleAnimation();
        tvScan.setVisibility(View.GONE);
    }
    public void timePicker(){
        Calendar myCalendar = Calendar.getInstance();
        int hour = myCalendar.get(Calendar.HOUR_OF_DAY);
        int minute = myCalendar.get(Calendar.MINUTE);
        Calendar now = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(AbsenActivity.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if(hour >6 && now.get(Calendar.HOUR_OF_DAY)<8){
                                checkIn.setVisibility(View.GONE);
                        }else{
                            checkIn.setVisibility(View.GONE);
                        }
                    }
                }, hour, minute, false);


    }

    private void getLastLocation() {
        if(checkPermission()){
            if (isLocationEnabled()){
                LocationServices.getFusedLocationProviderClient(this).getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        final Double currentLat = location.getLatitude();
                        final Double currentLong = location.getLongitude();

                        double distance = 0;
                        try {
                            distance = calculateDistance(currentLat, currentLong, getAddress().get(0).getLatitude(), getAddress().get(0).getLongitude() )*1000;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (distance < 50) {
                            Toast.makeText(AbsenActivity.this, "Anda dapat absen. Silahkan pilih absen", Toast.LENGTH_SHORT).show();
                            showDialogFormAbsen();
                        }else{
                            Toast.makeText(AbsenActivity.this, "Anda melebihi 50 meter dari lokasi", Toast.LENGTH_SHORT).show();
                            showDialogFormTidakMasuk();
                        }
                        stopScanLocation();
                    }
                });
            }else{
                Toast.makeText(this, "Please turn on your location", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }else{
            requestPermission();
        }
    }

    private void showDialogFormTidakMasuk() {
        final View dialogForm = LayoutInflater.from(this).inflate(R.layout.layout_dialog_form_tidak_masuk, null);
        new AlertDialog.Builder(this).setView(dialogForm).setCancelable(true).setNegativeButton("Cancel",null).show();
        final TextView tvNama = dialogForm.findViewById(R.id.txtNama);
        final TextView tvNip = dialogForm.findViewById(R.id.txtNip);
        final ProgressBar progressBar = dialogForm.findViewById(R.id.progressBar);
        final EditText etAlasan = dialogForm.findViewById(R.id.etAlasan);
        Button bIzin = dialogForm.findViewById(R.id.btnIzin);
        Button bSakit = dialogForm.findViewById(R.id.btnSakit);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UsersData users = dataSnapshot.getValue(UsersData.class);
                assert users != null;
                tvNama.setText(users.getNama());
                tvNip.setText("(NIP. "+users.getNip()+")");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AbsenActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        bIzin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String keterangan = bIzin.getText().toString();
                String alasan = etAlasan.getText().toString();
                if (TextUtils.isEmpty(alasan)){
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(AbsenActivity.this, "Alasan harus diisi!", Toast.LENGTH_SHORT).show();
                }else{
                    progressBar.setVisibility(View.VISIBLE);
                    databaseReference.addValueEventListener(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UsersData usersData = snapshot.getValue(UsersData.class);
                        assert usersData != null;
                        String uNama = usersData.getNama();
                        String uNip = usersData.getNip();

                        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        Map<String,Object> hashMap = new HashMap<>();
                        hashMap.put("tidakMasuk_Nip",uNip);
                        hashMap.put("tidakMasuk_Nama",uNama);
                        hashMap.put("tidakMasuk_Keterangan",keterangan);
                        hashMap.put("tidakMasuk_Alasan",alasan);
                        final DatabaseReference absenTidakMasuk = database.getReference("absen_tidak_masuk").child(firebaseUser.getUid());
                        absenTidakMasuk.push().setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(AbsenActivity.this, "Absen Berhasil Terimakasih!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(AbsenActivity.this, DashboardActivity.class));
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AbsenActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                }
            }
        });

        bSakit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String keterangan = bSakit.getText().toString();
                String alasan = etAlasan.getText().toString();
                if (TextUtils.isEmpty(alasan)){
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(AbsenActivity.this, "Alasan harus diisi!", Toast.LENGTH_SHORT).show();
                }else{
                    progressBar.setVisibility(View.VISIBLE);
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            UsersData usersData = snapshot.getValue(UsersData.class);
                            assert usersData != null;
                            String uNama = usersData.getNama();
                            String uNip = usersData.getNip();

                            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            Map<String,Object> hashMap = new HashMap<>();
                            hashMap.put("tidakMasuk_Nip",uNip);
                            hashMap.put("tidakMasuk_Nama",uNama);
                            hashMap.put("tidakMasuk_Keterangan",keterangan);
                            hashMap.put("tidakMasuk_Alasan",alasan);
                            final DatabaseReference absenTidakMasuk = database.getReference("absen_tidak_masuk").child(firebaseUser.getUid());
                            absenTidakMasuk.push().setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(AbsenActivity.this, "Absen Berhasil Terimakasih!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(AbsenActivity.this, DashboardActivity.class));
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(AbsenActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
    }


    private void showDialogFormAbsen(){
        final View dialogForm = LayoutInflater.from(this).inflate(R.layout.layout_dialog_form_absen, null);
        new AlertDialog.Builder(this).setView(dialogForm).setCancelable(true).setNegativeButton("Cancel",null).show();
        final TextView tvNama = dialogForm.findViewById(R.id.txtNama);
        final TextView tvNip = dialogForm.findViewById(R.id.txtNip);
        final ProgressBar progressBar = dialogForm.findViewById(R.id.progressBar);
        Button bMasuk = dialogForm.findViewById(R.id.btnMasuk);
        Button bKeluar = dialogForm.findViewById(R.id.btnKeluar);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UsersData users = dataSnapshot.getValue(UsersData.class);
                assert users != null;
                tvNama.setText(users.getNama());
                tvNip.setText("(NIP. "+users.getNip()+")");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AbsenActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        bMasuk.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UsersData usersData = snapshot.getValue(UsersData.class);
                        assert usersData != null;
                        String uNama = usersData.getNama();
                        String uNip = usersData.getNip();
                        Date currentTime = Calendar.getInstance().getTime();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE dd-MM-yyyy HH:mm:ss", Locale.getDefault());
                        String time = dateFormat.format(currentTime);
                        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        Map<String,Object> hashMap = new HashMap<>();
                        hashMap.put("masuk_Nip",uNip);
                        hashMap.put("masuk_Nama",uNama);
                        hashMap.put("masuk_Waktu",time);
                        final DatabaseReference absenMasuk = database.getReference("absen_masuk").child(firebaseUser.getUid());
                        absenMasuk.push().setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(AbsenActivity.this, "Absen Masuk Berhasil Terimakasih!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(AbsenActivity.this, DashboardActivity.class));
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AbsenActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        bKeluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UsersData usersData = snapshot.getValue(UsersData.class);
                        assert usersData != null;
                        String uNama = usersData.getNama();
                        String uNip = usersData.getNip();
                        Date currentTime = Calendar.getInstance().getTime();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE dd-MM-yyyy HH:mm:ss", Locale.getDefault());
                        String time = dateFormat.format(currentTime);
                        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        Map<String,Object> hashMap = new HashMap<>();
                        hashMap.put("keluar_Nip",uNip);
                        hashMap.put("keluar_Nama",uNama);
                        hashMap.put("keluar_Waktu",time);
                        final DatabaseReference absenKeluar = database.getReference("absen_keluar").child(firebaseUser.getUid());
                        absenKeluar.push().setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(AbsenActivity.this, "Absen Keluar Berhasil Terimakasih!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(AbsenActivity.this, DashboardActivity.class));
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AbsenActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }



    private List<Address> getAddress() throws IOException {
        Geocoder geocoder;
        geocoder = new Geocoder(this, Locale.getDefault());
        return geocoder.getFromLocation(-6.4760, 106.8279,  100); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
    }

    private Double calculateDistance(Double lat1, Double lon1, Double lat2, Double lon2 ){
        final Double R = 6372.8; // In kilometers

        double dLat = toRadians(lat2 - lat1);
        double dLon = toRadians(lon2 - lon1);
        lat1 = toRadians(lat1);
        lat2 = toRadians(lat2);
        double a = pow(sin(dLat / 2), 2) + pow(sin(dLon / 2), 2) * cos(lat1) * cos(lat2);
        double c = 2 * asin(sqrt(a));
        return R * c;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == ID_LOCATION_PERMISSION){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED || grantResults[1] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Berhasil Di Izinkan", Toast.LENGTH_SHORT).show();

                if (!isLocationEnabled()){
                    Toast.makeText(this, "Please turn on your location", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }
            }else{
                Toast.makeText(this, "Tidak Di Izinkan", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void checkPermissionLocation() {
        if (checkPermission()){
            if (!isLocationEnabled()){
                Toast.makeText(this, "Please TurnOn Your Location", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }else{
            requestPermission();
        }
    }


    private boolean checkPermission(){
        if (ActivityCompat.checkSelfPermission(this, permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            return true;
        }
        return false;
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            return true;
        }
        return false;
    }


    private void requestPermission() {
        ActivityCompat.requestPermissions(AbsenActivity.this,
                new String[]{permission.ACCESS_FINE_LOCATION, permission.ACCESS_COARSE_LOCATION},
                ID_LOCATION_PERMISSION);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(AbsenActivity.this,DashboardActivity.class));
        finish();
    }
}