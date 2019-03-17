package com.example.sujit.utkal_hacks;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sujit.utkal_hacks.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class PushNotifications extends AppCompatActivity {


    MaterialSpinner materialSpinner;
    EditText notificationEditText,titleEditText;
    Button sendNotificationButton;

    DatabaseReference mRef;
    String mTerm,mTopic;
    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_notifications);

        mRef= FirebaseDatabase.getInstance().getReference();
        mRef.keepSynced(true);
        mTopic="";title="";

        titleEditText=findViewById(R.id.notification_title_editText);
        materialSpinner=findViewById(R.id.notification_spinner);
        notificationEditText=findViewById(R.id.notification_editText);
        sendNotificationButton=findViewById(R.id.notification_button);
        materialSpinner.setItems("--","term1","term2","term3","term4");
        materialSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                mTerm=String.valueOf(item);
                Log.i("term",mTerm);
                mRef.child("batch").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for(DataSnapshot snapshot:dataSnapshot.getChildren())
                        {
                            Log.i("snapshot",snapshot.toString());
                            if(snapshot.getValue().equals(mTerm))
                            {
                                mTopic=String.valueOf(snapshot.getKey());
                                Log.i("mTopic",mTopic);

                            }
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });
        sendNotificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String notification=String.valueOf(notificationEditText.getText()).trim();
               title=String.valueOf(titleEditText.getText()).trim();
                if(mTopic!=null&&mTerm!="--"&& !TextUtils.isEmpty(notification))
                {

                    String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

                    HashMap<String,String> hashMap = new HashMap<>();
                    hashMap.put("body",notification);
                    hashMap.put("title",title);
                    String key=mRef.child("notifications").child(mTopic).push().getKey();
                    hashMap.put("key",key);
                    hashMap.put("topic",mTopic);
                    hashMap.put("date",currentDate);

                    mRef.child("notifications").child(mTopic).child(key).setValue(hashMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                            if(databaseError==null)
                            {

                                Toast.makeText(PushNotifications.this, "notification sent.", Toast.LENGTH_SHORT).show();

                            }
                            else{
                                Toast.makeText(PushNotifications.this, "Notification could not be sent.", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                }

            }
        });



    }
}
