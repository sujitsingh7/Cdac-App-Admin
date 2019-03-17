package com.example.sujit.utkal_hacks;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sujit.utkal_hacks.R;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.HashMap;

public class ScheduleAssignmentActivity extends AppCompatActivity {


    String assignment_type;
    MaterialSpinner spinner;
    MaterialSpinner subspinner;
    String term,subject;
    CalendarView calendarView;

    LinearLayout startLinearLayout,endLinearLayout;
    TextView startTextView,endTextView;
    Button startButton,endButton;
    HashMap<String,String> hashMap;

    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_assignment);
        spinner =  findViewById(R.id.spinner);
        subspinner = findViewById(R.id.subspinner);
       hashMap =new HashMap();

        calendarView = findViewById(R.id.simpleCalendarView);

        startLinearLayout=findViewById(R.id.startDateLinearLayout);
        endLinearLayout=findViewById(R.id.endDateLinearLayout);

        startTextView=findViewById(R.id.startDateTextView);
        endTextView=findViewById(R.id.endDateTextView);
        startTextView.setText("");
        endTextView.setText("");

        startButton =findViewById(R.id.setStartButton);
        endButton=findViewById(R.id.setEndButton);

        assignment_type = getIntent().getStringExtra("assignment_type");


        spinner.setItems("--","term1", "term2", "term3","term4");
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {

                term = item;
                if(item.equals("term1"))
                {
                    subspinner.setItems("--","Engineering Mechanics", "Basic Thermodynamics", "Computer Programming","Engineering Mathematics","Basic Electronics Engineering");

                }
                if(item.equals("term2"))
                {

                    subspinner.setItems("--","Computer Networking","Data Structure","Probabilty Statistics","Database Engineering","Semiconductor Devices");
                }
                if(item.equals("term3"))
                {

                    subspinner.setItems("--","Computer Organisation","Theory of Computation","Machine Learning","Hadoop Ecosystem","Internet Security");
                }

                if(item.equals("term4"))
                {

                    subspinner.setItems("--","Microprocessor Engineering","Image Processing","Internet of Things","Cloud Computing","Parallel Computing");
                }


                subspinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(MaterialSpinner view1, int position1, long id1, Object item1) {

                        subject=String.valueOf(item1);
                        databaseReference= FirebaseDatabase.getInstance().getReference().child("active_assignments").child(term);

                        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                            @Override
                            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                             String month_string,dayOfMonth_string;
                                if(month<10)
                                {
                                  month_string ="0"+String.valueOf(month+1);
                                }
                                else
                                    month_string=String.valueOf(month+1);
                                if(dayOfMonth<10)
                                {
                                    dayOfMonth_string ="0"+String.valueOf(dayOfMonth);


                                }
                                else
                                    dayOfMonth_string=String.valueOf(dayOfMonth);

                                startLinearLayout.setVisibility(View.VISIBLE);

                                if(TextUtils.isEmpty(startTextView.getText()))
                                {
                                    String start_date=dayOfMonth_string+"-"+month_string+"-"+String.valueOf(year);
                                    startTextView.setText(start_date);

                                }
                                else
                                {
                                    String end_date=dayOfMonth_string+"-"+month_string+"-"+String.valueOf(year);
                                    endTextView.setText(end_date);

                                }


                                Log.i("year",String.valueOf(year));
                                Log.i("month",String.valueOf(month));
                                Log.i("dayOfMonth",String.valueOf(dayOfMonth));



                                startButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        hashMap.put("subject",subject);
                                        hashMap.put("start_date",String.valueOf(startTextView.getText()));
                                        hashMap.put("type",assignment_type);

                                        endLinearLayout.setVisibility(View.VISIBLE);
                                    }
                                });
                                endButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        hashMap.put("end_date",String.valueOf(endTextView.getText()));

                                        databaseReference.setValue(hashMap, new DatabaseReference.CompletionListener() {
                                            @Override
                                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                                                if(databaseError==null)
                                                {
                                                    Toast.makeText(ScheduleAssignmentActivity.this, "Assignment Scheduled", Toast.LENGTH_SHORT).show();
                                                }
                                                else
                                                {
                                                    Toast.makeText(ScheduleAssignmentActivity.this, "Sorry,Assignment could not be scheduled!", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });



                                    }
                                });

                            }
                        });

                        Log.i("term",term);
                        Log.i("subject",subject);
                    }
                });

            }
        });




    }
}
