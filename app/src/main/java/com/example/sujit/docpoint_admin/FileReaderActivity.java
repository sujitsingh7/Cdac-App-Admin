package com.example.sujit.docpoint_admin;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

//import com.github.barteksc.pdfviewer.PDFView;

public class FileReaderActivity extends AppCompatActivity {

    String fileUri;
    //PDFView pdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_reader);
       // pdfView=findViewById(R.id.pdfViewer);

        fileUri=getIntent().getStringExtra("uri");
        Log.i("File Uri",fileUri);

       // pdfView.fromUri(Uri.parse(fileUri)).load();
    }
}
