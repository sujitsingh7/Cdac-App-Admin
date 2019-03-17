package com.example.sujit.utkal_hacks;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.example.sujit.utkal_hacks.R;
import com.google.firebase.auth.FirebaseAuth;

public class HomeScreenActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navigationView;
    DrawerLayout drawerLayout;
    ViewPager mViewPager;
    TabLayout mTabLayout;
    Toolbar toolbar;
    FirebaseAuth mAuth;
    private SectionsPagerAdapter mSectionsPagerAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        navigationView =findViewById(R.id.navigation_drawer);
        drawerLayout = findViewById(R.id.drawer_layout);

        mViewPager = findViewById(R.id.view_pager);
        mTabLayout = findViewById(R.id.tab_layout);

        navigationView.setNavigationItemSelectedListener(this);


        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Utkal-Hacks Admin");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mTabLayout.setupWithViewPager(mViewPager);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);

            }
        });


        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {


        drawerClose();

        switch(item.getItemId()) {


            case R.id.schedule_lab_assignment:


                Intent intent = new Intent(HomeScreenActivity.this,ScheduleAssignmentActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("assignment_type","lab_assignment");
                startActivity(intent);
                return true;


            case R.id.publish_attendance:

                Intent attendanceIntent  = new Intent(HomeScreenActivity.this,AttendanceResultActivity.class);
                attendanceIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                attendanceIntent.putExtra("type","attendance");
                startActivity(attendanceIntent);




                return true;


            case R.id.publish_result:

                Intent publishResult  = new Intent(HomeScreenActivity.this,AttendanceResultActivity.class);
               publishResult.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
               publishResult.putExtra("type","result");
                startActivity(publishResult);


                return true;


            case R.id.send_notifications:

                Intent sendNotifications  = new Intent(HomeScreenActivity.this,CourseExamSchedule.class);
                sendNotifications .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                sendNotifications.putExtra("type","notifications");
                startActivity(sendNotifications);

                return true;




            case R.id.log_out:


                mAuth.signOut();
                startActivity(new Intent(HomeScreenActivity.this,SignInActivity.class));

                finish();
                return true;

            case R.id.push_notifications:
                Intent pushNotifications = new Intent(HomeScreenActivity.this,PushNotifications.class);
                pushNotifications.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(pushNotifications);
                return  true;



        }
        return false;


    }

    public void drawerClose()

    {

        drawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerClose();
        else
            super.onBackPressed();


    }
}
