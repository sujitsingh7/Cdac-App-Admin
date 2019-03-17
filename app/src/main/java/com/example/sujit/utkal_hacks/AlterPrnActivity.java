package com.example.sujit.utkal_hacks;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.util.HashMap;

public class AlterPrnActivity extends AppCompatActivity {

    DatabaseReference databaseReference;


    EditText oldPrn,newPrn;
    String old_prn,new_prn;

    Button makeChanges;

 String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alter_prn);

        oldPrn=findViewById(R.id.old_prn_number);
        newPrn=findViewById(R.id.new_prn_number);
        makeChanges=findViewById(R.id.make_changes_button);
        databaseReference= FirebaseDatabase.getInstance().getReference();

        makeChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                old_prn=String.valueOf(oldPrn.getText());
                new_prn=String.valueOf(newPrn.getText());
                databaseReference.child("users").child(old_prn).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {



                        if(dataSnapshot.exists())
                        {
                            final HashMap<Object,String> hashMap =new HashMap<>();



                            for(DataSnapshot mDataSnapshot : dataSnapshot.getChildren())
                            {
                                hashMap.put(mDataSnapshot.getKey(),String.valueOf(mDataSnapshot.getValue()));
                                Log.i(mDataSnapshot.getKey(),mDataSnapshot.getValue().toString());
                                if(mDataSnapshot.getKey().equals("uid"))
                                {
                                    uid=String.valueOf(mDataSnapshot.getValue());
                                }

                            }
                            databaseReference.child("users").child(old_prn).removeValue(new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {


                                    if(databaseError==null)
                                    {

                                       databaseReference.child("uid").child(uid).setValue(new_prn, new DatabaseReference.CompletionListener() {
                                           @Override
                                           public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                                               if(databaseError==null)
                                               {
                                                   databaseReference.child("users").child(new_prn).setValue(hashMap, new DatabaseReference.CompletionListener() {
                                                       @Override
                                                       public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                                           if(databaseError==null)
                                                           {
                                                               Toast.makeText(AlterPrnActivity.this, "Prn changed successfully", Toast.LENGTH_SHORT).show();
                                                           }

                                                           else{

                                                               Toast.makeText(AlterPrnActivity.this, "Sorry changes could not be made!", Toast.LENGTH_SHORT).show();
                                                           }

                                                       }
                                                   });



                                               }
                                               else
                                               {
                                                   Toast.makeText(AlterPrnActivity.this, "Sorry changes could not be made !", Toast.LENGTH_SHORT).show();


                                               }

                                           }
                                       });



                                    }
                                    else
                                    {

                                        Toast.makeText(AlterPrnActivity.this, "Sorry changes could not be made", Toast.LENGTH_SHORT).show();
                                    }


                                }
                            });





                        }
                        else{

                            Toast.makeText(AlterPrnActivity.this, "No such prn exists", Toast.LENGTH_SHORT).show();
                        }



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });




    }
}
