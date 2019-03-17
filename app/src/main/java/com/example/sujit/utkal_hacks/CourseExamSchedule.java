package com.example.sujit.utkal_hacks;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sujit.utkal_hacks.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.io.File;
import java.util.HashMap;

public class CourseExamSchedule extends AppCompatActivity {

    Uri uri;
    String term,subject,type,term_in_numbers,batch,schedule_type;
    MaterialSpinner mSpinner,mSubspinner;
    HashMap<String,String> hashMap;

    ProgressDialog progressDialog;


    ImageView chooseFileImageView,uploadChosenFileImageView;

    TextView chosenFileTextView;
    TextView uploadChosenFileTextView;

    DatabaseReference databaseReference,mRef;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_exam_schedule);

        mSpinner =  findViewById(R.id.spinner);
        //mSubspinner = findViewById(R.id.subspinner);

        chooseFileImageView = findViewById(R.id.choose_file_imageview);

        uploadChosenFileImageView = findViewById(R.id.upload_chosen_file_imageview);

        uploadChosenFileTextView = findViewById(R.id.upload_chosen_file_textview);

        chosenFileTextView = findViewById(R.id.chosen_file_textview);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMax(100);
        progressDialog.setMessage("Please wait....");
        progressDialog.setTitle("Uploading Document");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);


        uploadChosenFileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (uri != null) {

                    File file = new File(uri.getPath());

                    final String fileName = file.getName();
                    String format[] = fileName.split("\\.");

                    final String fileFormat = format[format.length-1];

                            String pushId = databaseReference.getKey();

                                storageReference = FirebaseStorage.getInstance().getReference().child("schedule").child(term).child(pushId);

                                storageReference.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                                        if (task.isSuccessful()) {


                                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri1) {

                                                    HashMap<String, String> value = new HashMap<>();
                                                    value.put("url", uri1.toString());
                                                    value.put("type", fileFormat);
                                                    value.put("name", fileName);
                                                    value.put("subject",subject);
                                                    value.put("pushId",databaseReference.push().getKey());


                                                    databaseReference.setValue(value).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {

                                                            if (task.isSuccessful()) {
                                                                progressDialog.dismiss();

                                                                Toast.makeText(CourseExamSchedule.this, "File Uploaded !", Toast.LENGTH_SHORT).show();

                                                            } else {

                                                                progressDialog.dismiss();
                                                                Toast.makeText(CourseExamSchedule.this, "File could not be uploaded !", Toast.LENGTH_SHORT).show();
                                                            }

                                                        }
                                                    });


                                                }
                                            });

                                        } else {

                                            Toast.makeText(CourseExamSchedule.this, "File could not be uploaded !", Toast.LENGTH_SHORT).show();


                                        }
                                    }
                                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                                        progressDialog.setProgress(0);

                                        progressDialog.show();
                                        int progresspercentage = (int) ((int)(100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount());
                                        progressDialog.incrementProgressBy(progresspercentage);



                                    }
                                });





                            }

            }
        });


        chooseFileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //choose your file


                //For marshmellow the permission is already granted..
                if (Build.VERSION.SDK_INT < 23) {
                    chooseFile();

                } else {
                    //if sdk int>23 check for self permission

                    if (ContextCompat.checkSelfPermission(CourseExamSchedule.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        //if permission is not granted ask for permission

                        ActivityCompat.requestPermissions(CourseExamSchedule.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

                    } else {
                        chooseFile();
                    }


                }

            }




        });

        mSpinner.setItems("--","term1", "term2", "term3","term4");
        mSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {

                term = item;

                term_in_numbers = term.substring(4);
                //mSubspinner.setItems("--","Course", "Exam");







                chooseFileImageView.setVisibility(View.VISIBLE);
                chooseFileImageView.setEnabled(true);



            }
        });


    }




    private void chooseFile() {

        databaseReference = FirebaseDatabase.getInstance().getReference().child("schedule").child(term).push();

        databaseReference.keepSynced(true);

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        startActivityForResult(intent,0);


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode==1&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {
            chooseFile();


        }

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {

        if (requestCode == 0 && data != null) {

            uri = data.getData();

            Log.i("URI",String.valueOf(uri));
            File file= new File(uri.getPath());
            chosenFileTextView.setVisibility(View.VISIBLE);
            chosenFileTextView.setText(file.getName());

            uploadChosenFileTextView.setVisibility(View.VISIBLE);
            uploadChosenFileImageView.setVisibility(View.VISIBLE);

        }
    }
}

