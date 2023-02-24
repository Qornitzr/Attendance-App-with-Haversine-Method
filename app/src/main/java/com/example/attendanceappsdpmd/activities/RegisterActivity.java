package com.example.attendanceappsdpmd.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.attendanceappsdpmd.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    private MaterialEditText nip, name, emailAddress, password, handPhone, alamat;
    private TextView tvTglLahir;
    private RadioGroup radioGroup;
    private Spinner jabatanSpinner, golonganSpinner;
    private Button registerBtn;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    DatePickerDialog.OnDateSetListener setListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
        nip = findViewById(R.id.nip);
        name = findViewById(R.id.nama);
        emailAddress = findViewById(R.id.email);
        password = findViewById(R.id.password);
        handPhone = findViewById(R.id.handphone);
        alamat = findViewById(R.id.alamat);
        tvTglLahir = findViewById(R.id.tglLahir);
        radioGroup = findViewById(R.id.radioButton);
        jabatanSpinner = findViewById(R.id.spinnerJabatan);
        golonganSpinner = findViewById(R.id.spinnerGolongan);
        registerBtn = findViewById(R.id.register);
        progressBar = findViewById(R.id.progressBar);

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        tvTglLahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(RegisterActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,setListener,year,month,day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month+1;
                String date = day+"-"+month+"-"+year;
                tvTglLahir.setText(date);
            }
        };

        ArrayAdapter<CharSequence> adapterJabatan = ArrayAdapter.createFromResource(this, R.array.jabatan, android.R.layout.simple_spinner_item);
        adapterJabatan.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jabatanSpinner.setPrompt("Jabatan");
        ArrayAdapter<CharSequence> adapterGolongan = ArrayAdapter.createFromResource(this, R.array.golongan, android.R.layout.simple_spinner_item);
        adapterGolongan.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        golonganSpinner.setPrompt("Golongan");

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth = FirebaseAuth.getInstance();

                final String niP = nip.getText().toString();
                final String namA = name.getText().toString();
                final String email = emailAddress.getText().toString();
                final String txt_password = password.getText().toString();
                final String handphone = handPhone.getText().toString();
                final String alamaT = alamat.getText().toString();
                final String tglLahir = tvTglLahir.getText().toString();
                final String spinnerJabatan = jabatanSpinner.getSelectedItem().toString();
                final String spinnerGolongan = golonganSpinner.getSelectedItem().toString();
                int checkedId = radioGroup.getCheckedRadioButtonId();
                RadioButton selected_gender = radioGroup.findViewById(checkedId);
                if (selected_gender == null){
                    Toast.makeText(RegisterActivity.this, "Pilih Jenis Kelamin", Toast.LENGTH_SHORT).show();
                } else {
                    final String gender = selected_gender.getText().toString();
                    if (TextUtils.isEmpty(niP) || TextUtils.isEmpty(namA) || TextUtils.isEmpty(email) || TextUtils.isEmpty(txt_password) || TextUtils.isEmpty(handphone) || TextUtils.isEmpty(alamaT) || TextUtils.isEmpty(tglLahir) || TextUtils.isEmpty(spinnerJabatan) || TextUtils.isEmpty(spinnerGolongan)){
                        Toast.makeText(RegisterActivity.this, "Data field harus diisi!", Toast.LENGTH_SHORT).show();
                    }else {
                        register(niP, namA, email, txt_password, handphone, spinnerJabatan, spinnerGolongan, alamaT, gender, tglLahir);
                    }
                }
            }
        });
    }

    private void register(final String niP, final String namA, final String email, String txt_password, final String handphone, final String spinnerJabatan, final String spinnerGolongan, final String alamaT, final String gender, final String tglLahir) {
        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.createUserWithEmailAndPassword(email, txt_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser rUser= firebaseAuth.getCurrentUser();
                    assert rUser != null;
                    String userId = rUser.getUid();
                    databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
                    HashMap<String,String> hashMap = new HashMap<>();
                    hashMap.put("userId",userId);
                    hashMap.put("nip",niP);
                    hashMap.put("nama",namA);
                    hashMap.put("email",email);
                    hashMap.put("gender",gender);
                    hashMap.put("handphone",handphone);
                    hashMap.put("alamat",alamaT);
                    hashMap.put("lahir",tglLahir);
                    hashMap.put("jabatan",spinnerJabatan);
                    hashMap.put("golongan",spinnerGolongan);
                    hashMap.put("imageUrl","default");
                    databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressBar.setVisibility(View.GONE);
                            if (task.isSuccessful()){
                                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }else{
                                Toast.makeText(RegisterActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(RegisterActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}