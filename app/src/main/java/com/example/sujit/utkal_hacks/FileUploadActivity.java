package com.example.sujit.utkal_hacks;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.sujit.utkal_hacks.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class FileUploadActivity extends AppCompatActivity {


    Uri uri;
   String serverUri;
    ImageView chooseFileImageView,uploadChosenFileImageView;

    TextView chosenFileTextView;
    TextView uploadChosenFileTextView;




    DatabaseReference databaseReference,databaseReference1;
    StorageReference storageReference;

    ProgressDialog progressDialog;

    String term,subject,subject_number,batch;

    Button launchStudyMaterialButton,submissionListTheoryButton,viewStudyMaterial;
    int progresspercentage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_upload);


		serverUri="http://172.29.5.9:8090/PHPExcel-1.8/submissionlist.php";

       
        term =getIntent().getStringExtra("term");
        subject=getIntent().getStringExtra("subject");
        subject_number=getIntent().getStringExtra("subject_number");
        Log.i("term",term);
        Log.i("subject",subject);
        Log.i("subject_number",subject_number);
        submissionListTheoryButton=findViewById(R.id.downloadSheetButtonTheory);


        progressDialog = new ProgressDialog(this);
        progressDialog.setMax(100);
        progressDialog.setMessage("Please wait....");
        progressDialog.setTitle("Uploading Document");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);


        chooseFileImageView = findViewById(R.id.choose_file_imageview);
        viewStudyMaterial=findViewById(R.id.view_files);
        uploadChosenFileImageView = findViewById(R.id.upload_chosen_file_imageview);

        uploadChosenFileTextView = findViewById(R.id.upload_chosen_file_textview);

        chosenFileTextView = findViewById(R.id.chosen_file_textview);

        launchStudyMaterialButton = findViewById(R.id.launch_study_material);

        launchStudyMaterialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference1= FirebaseDatabase.getInstance().getReference().child("terms").child(term).child(subject_number).child("status");
                databaseReference1.setValue("active", new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                        if(databaseError==null)
                            Toast.makeText(FileUploadActivity.this, "Study Materials launched ! ", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(FileUploadActivity.this, "Sorry !Could not launch.", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        submissionListTheoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                submissionlist("theory_assignment");




            }
        });


        viewStudyMaterial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(FileUploadActivity.this,ViewUploadedFiles.class);
                intent.putExtra("term",term);
                intent.putExtra("subject",subject);
                intent.putExtra("subject_number",subject_number);
                startActivity(intent);


            }
        });


        chooseFileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //For marshmellow the permission is already granted..
                if (Build.VERSION.SDK_INT < 23) {
                    chooseFile();

                } else {
                    //if sdk int>23 check for self permission

                    if (ContextCompat.checkSelfPermission(FileUploadActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        //if permission is not granted ask for permission

                        ActivityCompat.requestPermissions(FileUploadActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

                    } else {
                        chooseFile();
                    }


                }

            }
        });


        // uploading process begins here ------------->>>>>>>>>


        uploadChosenFileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (uri != null) {

                    File file = new File(uri.getPath());

                    final String fileName = file.getName();
                    String format[] = fileName.split("\\.");

                    // Log.i("format", format.toString());
                    final String fileFormat = format[format.length-1];



                    progresspercentage=0;


                    databaseReference = FirebaseDatabase.getInstance().getReference().child("files").child(subject).push();
                    databaseReference.keepSynced(true);
                    final String pushId = databaseReference.getKey();

                    storageReference = FirebaseStorage.getInstance().getReference().child("upload").child(term).child(subject).child(pushId);

                    storageReference.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                            if (task.isSuccessful()) {


                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri1) {

                                        HashMap<String, String> value = new HashMap<>();
                                        value.put("pushId", pushId);
                                        value.put("url", uri1.toString());
                                        value.put("type", fileFormat);
                                        value.put("name", fileName);
                                        value.put("subject",subject);

                                        databaseReference.setValue(value).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if (task.isSuccessful()) {
                                                    progressDialog.dismiss();

                                                    Toast.makeText(FileUploadActivity.this, "File Uploaded !", Toast.LENGTH_SHORT).show();

                                                } else {

                                                    progressDialog.dismiss();
                                                    Toast.makeText(FileUploadActivity.this, "File could not be uploaded !", Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                        });


                                    }
                                });

                            } else {

                                Toast.makeText(FileUploadActivity.this, "File could not be uploaded !", Toast.LENGTH_SHORT).show();


                            }
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {


                            progressDialog.show();
                             progresspercentage = (int) ((int)(100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount());
                            progressDialog.incrementProgressBy(progresspercentage);



                        }
                    });


                }
            }
        });




    }

    private void submissionlist(final String assignment_type) {


        Log.i("inside","true");
        Log.i("term",term);
        DatabaseReference batchReference = FirebaseDatabase.getInstance().getReference().child("batch");
        batchReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    // TODO: handle the post

                    Log.i("postSnapShot",postSnapshot.getValue().toString());
                    if (dataSnapshot != null) {
                        if (String.valueOf(postSnapshot.getValue()).equals(term)) {
                            batch = postSnapshot.getKey();
                            Log.i("batch snap", batch);

                        }
                    }
                }

                if (batch != null) {

                    Log.i("batch", batch);

                    StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, serverUri,
                            new com.android.volley.Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.i("response", response);
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        String success = String.valueOf(jsonObject.get("message"));

                                        Log.i("response", success);

                                        Toast.makeText(FileUploadActivity.this, "Excel Sheet Generation :" + success, Toast.LENGTH_SHORT).show();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Log.i("tagconvertstr", "[" + response + "]");
                                        Log.i("error", e.getMessage());
                                        Toast.makeText(FileUploadActivity.this, "Error in generating sheet!", Toast.LENGTH_SHORT).show();
                                    }


                                }
                            }, new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Log.i("error", error.getMessage());
                            Toast.makeText(FileUploadActivity.this, "Error in generating sheet!", Toast.LENGTH_SHORT).show();
                        }
                    })

                    {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();
                            params.put("type", assignment_type);
                            params.put("subject", subject.toLowerCase());

                            params.put("batch", batch);
                            params.put("term", term);

                            return params;
                        }
                    };

                    MySingleton.getInstance(FileUploadActivity.this).addToRequestQueue(stringRequest);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });






    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode==1&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {
            chooseFile();


        }

    }

    private void chooseFile() {
        progresspercentage=0;

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        startActivityForResult(intent,0);


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
