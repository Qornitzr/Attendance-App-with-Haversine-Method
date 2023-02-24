package com.example.attendanceappsdpmd.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.attendanceappsdpmd.R;
import com.example.attendanceappsdpmd.adapter.ImagesRecyclerAdapter;
import com.example.attendanceappsdpmd.data.ImagesList;
import com.example.attendanceappsdpmd.data.UsersData;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {

    private MaterialEditText namaUsers, hpUsers, alamatUsers, nipUsers, emailUsers, jkUsers, jabatanUsers, golonganUsers, imageUsers;
    private TextView gantiPw, tvTglLahir;
    private ImageView nama, hp, alamat;
    private Button mBtnSave;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    DatePickerDialog.OnDateSetListener setListener;

    private CircleImageView circleImageView;
    private ImagesRecyclerAdapter imagesRecyclerAdapter;
    private List<ImagesList> imagesList;
    private static final int IMAGE_REQUEST = 1;
    private StorageTask storageTask;
    private Uri imageUri;
    private StorageReference storageReference;
    private UsersData usersData;

    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        super.onAttachFragment(fragment);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Edit Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditProfileActivity.this, DashboardActivity.class));
            }
        });
        namaUsers = findViewById(R.id.txtNamaUser);
        hpUsers = findViewById(R.id.txtHandphoneUser);
        alamatUsers = findViewById(R.id.txtAlamatUser);
        tvTglLahir = findViewById(R.id.tglLahir);
        nipUsers = findViewById(R.id.txtNip);
        emailUsers = findViewById(R.id.txtEmail);
        jkUsers = findViewById(R.id.txtJk);
        jabatanUsers = findViewById(R.id.txtJabatan);
        golonganUsers = findViewById(R.id.txtGolongan);
        imageUsers = findViewById(R.id.txtImage);

        nama = findViewById(R.id.penEditNama);
        hp = findViewById(R.id.penEditHp);
        alamat = findViewById(R.id.penEditAlamat);
        gantiPw = findViewById(R.id.gantiPassword);
        imagesList = new ArrayList<>();
        circleImageView = findViewById(R.id.profileImage);
        mBtnSave = findViewById(R.id.btnSave);

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        tvTglLahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(EditProfileActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,setListener,year,month,day);
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

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        storageReference = FirebaseStorage.getInstance().getReference("profile_images");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final UsersData usersData = dataSnapshot.getValue(UsersData.class);
                assert usersData != null;
                namaUsers.setText(usersData.getNama());
                hpUsers.setText(usersData.getHandphone());
                alamatUsers.setText(usersData.getAlamat());
                nipUsers.setText(usersData.getNip());
                emailUsers.setText(usersData.getEmail());
                jkUsers.setText(usersData.getGender());
                jabatanUsers.setText(usersData.getJabatan());
                golonganUsers.setText(usersData.getGolongan());
                tvTglLahir.setText(usersData.getLahir());
                imageUsers.setText(usersData.getImageUrl());
                if(usersData.getImageUrl().equals("default")){
                    circleImageView.setImageResource(R.drawable.potodefault);
                }else{
                    Glide.with(getApplicationContext()).load(usersData.getImageUrl()).into(circleImageView);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(EditProfileActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        gantiPw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditProfileActivity.this, ChangePasswordActivity.class));
            }
        });
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
                builder.setCancelable(true);
                View mView = LayoutInflater.from(EditProfileActivity.this).inflate(R.layout.select_image_layout,null);
                RecyclerView recyclerView = mView.findViewById(R.id.recyclerView);
                collectOldImages();
                recyclerView.setLayoutManager(new GridLayoutManager(EditProfileActivity.this,3));
                recyclerView.setHasFixedSize(true);
                imagesRecyclerAdapter = new ImagesRecyclerAdapter(imagesList,EditProfileActivity.this);
                recyclerView.setAdapter(imagesRecyclerAdapter);
                imagesRecyclerAdapter.notifyDataSetChanged();
                final Button openImages = mView.findViewById(R.id.openImages);
                openImages.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openImages();
                    }
                });
                builder.setView(mView);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {FirebaseUser rUser= firebaseAuth.getCurrentUser();
                assert rUser != null;
                String userId = rUser.getUid();
                final String nama = namaUsers.getText().toString();
                final String hp = hpUsers.getText().toString();
                final String alamat = alamatUsers.getText().toString();
                final String nip = nipUsers.getText().toString();
                final String email = emailUsers.getText().toString();
                final String jk = jkUsers.getText().toString();
                final String jabatan = jabatanUsers.getText().toString();
                final String golongan = golonganUsers.getText().toString();
                final String lahir = tvTglLahir.getText().toString();
                final String image = imageUsers.getText().toString();

                databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("userId", userId);
                hashMap.put("nama", nama);
                hashMap.put("handphone", hp);
                hashMap.put("alamat", alamat);
                hashMap.put("nip", nip);
                hashMap.put("email", email);
                hashMap.put("gender", jk);
                hashMap.put("jabatan", jabatan);
                hashMap.put("golongan", golongan);
                hashMap.put("lahir", lahir);
                hashMap.put("imageUrl", image);
                databaseReference.setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void mVoid) {
                        startActivity(new Intent(EditProfileActivity.this, DashboardActivity.class));
                        Toast.makeText(EditProfileActivity.this, "Data berhasil di update !", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private void openImages() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageUri = data.getData();
        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            if (storageTask != null && storageTask.isInProgress()){
                Toast.makeText(this, "Uploading in Progress", Toast.LENGTH_SHORT).show();
            }else{
                uploadImage();
            }
        }
    }

    private void uploadImage() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading Image");
        progressDialog.show();
        if (imageUri != null){
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri);
            }catch (IOException e){
                e.printStackTrace();
            }
            ByteArrayOutputStream byteArrayInputStream = new ByteArrayOutputStream();
            assert bitmap != null;
            bitmap.compress(Bitmap.CompressFormat.JPEG,25,byteArrayInputStream);
            byte[] imageFileToByte = byteArrayInputStream.toByteArray();
            final StorageReference imageReference = storageReference.child(System.currentTimeMillis()+".jpg");
            storageTask = imageReference.putBytes(imageFileToByte);
            storageTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return imageReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull final Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        String sDownloadUri = downloadUri.toString();
                        Map<String,Object> hashMap = new HashMap<>();
                        hashMap.put("imageUrl",sDownloadUri);
                        databaseReference.updateChildren(hashMap);
                        final DatabaseReference profileImagesReference = FirebaseDatabase.getInstance().getReference("profile_images").child(firebaseUser.getUid());
                        profileImagesReference.push().setValue(hashMap).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                if (task.isSuccessful()){
                                    progressDialog.dismiss();
                                }else{
                                    progressDialog.dismiss();
                                    Toast.makeText(EditProfileActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EditProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
        }
    }

    private void collectOldImages() {
        DatabaseReference imageListReference = FirebaseDatabase.getInstance().getReference("profile_images").child(firebaseUser.getUid());
        imageListReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                imagesList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    imagesList.add(snapshot.getValue(ImagesList.class));
                }
                imagesRecyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(EditProfileActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(EditProfileActivity.this,DashboardActivity.class));
        finish();
    }
}