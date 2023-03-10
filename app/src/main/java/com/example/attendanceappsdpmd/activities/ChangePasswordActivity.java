package com.example.attendanceappsdpmd.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.attendanceappsdpmd.R;
import com.example.attendanceappsdpmd.activities.EditProfileActivity;
import com.example.attendanceappsdpmd.activities.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rengwuxian.materialedittext.MaterialEditText;

public class ChangePasswordActivity extends AppCompatActivity {

    private MaterialEditText oldPsw,newPsw,confirmPsw;
    private Button changePsw;
    private FirebaseUser firebaseUser;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Ganti Password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChangePasswordActivity.this, EditProfileActivity.class));
            }
        });
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        oldPsw = findViewById(R.id.oldPassword);
        newPsw = findViewById(R.id.newPassword);
        confirmPsw = findViewById(R.id.confirmPassword);
        changePsw = findViewById(R.id.resetPassword);
        progressBar = findViewById(R.id.progressBar);
        changePsw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txtOldPsw = oldPsw.getText().toString();
                String txtNewPsw = newPsw.getText().toString();
                String txtConfirmPsw = confirmPsw.getText().toString();
                if (txtOldPsw.isEmpty() || txtNewPsw.isEmpty() || txtConfirmPsw.isEmpty()){
                    Toast.makeText(ChangePasswordActivity.this, "Semua field harus diisi", Toast.LENGTH_SHORT).show();
                }else if (txtNewPsw.length() <6){
                    Toast.makeText(ChangePasswordActivity.this, "Password kurang dari 6 karakter", Toast.LENGTH_SHORT).show();
                }else if (! txtConfirmPsw.equals(txtNewPsw)){
                    Toast.makeText(ChangePasswordActivity.this, "Password tidak sama!", Toast.LENGTH_SHORT).show();
                }else{
                    changePassword(txtOldPsw,txtNewPsw);
                }
            }
        });
    }

    private void changePassword(String txtOldPsw, final String txtNewPsw) {
        progressBar.setVisibility(View.VISIBLE);
        AuthCredential credential = EmailAuthProvider.getCredential(firebaseUser.getEmail(),txtOldPsw);
        firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    firebaseUser.updatePassword(txtNewPsw).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                firebaseAuth.signOut();
                                Intent intent = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }else {
                                Toast.makeText(ChangePasswordActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(ChangePasswordActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}