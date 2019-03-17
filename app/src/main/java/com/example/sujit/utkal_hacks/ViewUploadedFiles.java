package com.example.sujit.utkal_hacks;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import java.io.File;

public class ViewUploadedFiles extends AppCompatActivity {

    RecyclerView recyclerView;

    private FirebaseRecyclerAdapter<Files, ViewHolder> firebaseRecyclerAdapter;
    private FirebaseRecyclerOptions<Files> options;
    private Query query;

    DatabaseReference databaseReference;
    StorageReference storageReference;


    ProgressDialog progressDialog;
    String term,batch,subject,subject_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_uploaded_files);

        term = getIntent().getStringExtra("term");
       // batch = getIntent().getStringExtra("batch");
        subject = getIntent().getStringExtra("subject");


        //Log.i("term",term);
       // Log.i("batch",batch);
       // Log.i("subject",subject);
      //  Log.i("subject_name",subject_name);

        permission_check();



    }

    private void permission_check() {
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},100);

            }
        }

        initialize();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 100 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            initialize();
        }else {
            permission_check();
        }
    }

    public void initialize(){


        //listView.setAdapter(arrayAdapter);
        // handler = new Handler();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Downloading...");
        progressDialog.setMax(100);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        recyclerView = findViewById(R.id.recycler_view);


        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        query = FirebaseDatabase.getInstance()
                .getReference().child("files").child(subject);

        query.keepSynced(true);

        options = new FirebaseRecyclerOptions.Builder<Files>()
                .setQuery(query, Files.class).setLifecycleOwner(this)
                .build();


        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists())

                {



                    firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Files, ViewUploadedFiles.ViewHolder>(options) {



                        @Override
                        protected void onBindViewHolder(@NonNull final ViewHolder holder, int position, @NonNull final  Files model) {

                            final String docFormat = model.getType();
                            Log.i("fileFormat",docFormat);

                            holder.textView.setText(model.getName());

                            holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String key =model.getPushId();
                                    storageReference=FirebaseStorage.getInstance().getReference().child("upload").child(term).child(subject).child(key);
                                    databaseReference=FirebaseDatabase.getInstance().getReference().child("files").child(subject).child(key);

                                    databaseReference.removeValue(new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                                            if(databaseError==null)
                                            {
                                                storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    // File deleted successfully
                                                    Toast.makeText(ViewUploadedFiles.this, "File deleted!", Toast.LENGTH_SHORT).show();
                                                    firebaseRecyclerAdapter.notifyDataSetChanged();



                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception exception) {
                                                    // Uh-oh, an error occurred!
                                                    Toast.makeText(ViewUploadedFiles.this, "File from storage could not be deleted!", Toast.LENGTH_SHORT).show();

                                                }
                                            });

                                            }
                                            else{
                                                Toast.makeText(ViewUploadedFiles.this, "File could not be deleted!", Toast.LENGTH_SHORT).show();


                                            }

                                        }
                                    });



                                }
                            });

                            holder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    StorageReference httpsReference = FirebaseStorage.getInstance().getReferenceFromUrl(model.getUrl());



                                    try {
                                        //final File localFile = File.createTempFile(model.getPushId(),"."+model.getType(),getCacheDir());

                                        final File localFile = new File(getCacheDir(),model.getName());
                          /* final File localFile;
                            localFile = new File(model.getName());
                            localFile.setWritable(true);*/
                                        if(!localFile.exists()) {
                                            httpsReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                                    // Local temp file has been created

                                                    //String uri= localFile.getAbsolutePath();
                                                    // Uri uri =Uri.fromFile(localFile);

                                                    Uri uri = FileProvider.getUriForFile(ViewUploadedFiles.this,
                                                            getString(R.string.file_provider_authority),
                                                            localFile);


                                                       Log.i("URI", uri.toString());
                                                        Intent intent = new Intent(Intent.ACTION_VIEW);
                                                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                                                        intent.setDataAndType(uri, "application/*");

                                                   /* Intent i;
                                                    pdf viewer code added ..!!
                                                    if(docFormat.equals("pdf")) {
                                                        i = new Intent(ViewUploadedFiles.this, FileReaderActivity.class);
                                                        i.putExtra("uri", uri.toString());
                                                        startActivity(i);

                                                    }*/



                                                    progressDialog.dismiss();


                                                     startActivity(intent);


                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception exception) {
                                                    // Handle any errors
                                                    progressDialog.dismiss();

                                                    Log.i("Exception",exception.getMessage());
                                                }
                                            }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                                                @Override
                                                public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {

                                                    progressDialog.show();
                                                    int progresspercentage = (int) ((int)(100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount());
                                                    progressDialog.incrementProgressBy(progresspercentage);

                                                }
                                            });

                                        }
                                        else
                                        {
                                            Uri uri = FileProvider.getUriForFile(ViewUploadedFiles.this,
                                                    getString(R.string.file_provider_authority),
                                                    localFile);


                                            Log.i("URI", uri.toString());
                                            Intent intent = new Intent(Intent.ACTION_VIEW);
                                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                                            intent.setDataAndType(uri, "application/*");
                                             startActivity(intent);

                                            //code for pdfViewer starts -->>

                                           /* if(docFormat.equals("pdf")) {
                                                Intent i = new Intent(ViewUploadedFiles.this, FileReaderActivity.class);
                                                i.putExtra("uri", uri.toString());
                                                startActivity(i);
                                            }*/


                                        }
                                    }
                                    catch(Exception e)
                                    {
                                        Log.i("Exception",e.getMessage());
                                        Toast.makeText(ViewUploadedFiles.this, "File could not be downloaded !!", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });




                        }

                        @NonNull
                        @Override
                        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


                            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.individual_item_layout, parent, false);
                            ViewUploadedFiles.ViewHolder holder = new ViewUploadedFiles.ViewHolder(view);
                            return holder;

                        }
                    };


                    recyclerView.setAdapter(firebaseRecyclerAdapter);






                }
                else
                {
                    Toast.makeText(ViewUploadedFiles.this, "No files for this unit.", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }


    public static  class ViewHolder  extends RecyclerView.ViewHolder{

        View itemView;
        TextView textView;
        ImageView deleteButton;
        public ViewHolder(View mview) {
            super(mview);
            itemView = mview;
            textView = itemView.findViewById(R.id.textView);
            deleteButton=itemView.findViewById(R.id.deleteButton);
        }
    }

    }
