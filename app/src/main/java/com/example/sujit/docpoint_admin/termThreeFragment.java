package com.example.sujit.docpoint_admin;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class termThreeFragment extends Fragment {


    CardView oneCardView,twoCardView,threeCardView,fourCardView,fiveCardView,sixCardView,sevenCardView;
    TextView oneTextView,twoTextView,threeTextView,fourTextView,fiveTextView,sixTextView,sevenTextView;



    public termThreeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_term_three, container, false);


        oneCardView=v.findViewById(R.id.cardview_one);
        twoCardView=v.findViewById(R.id.cardview_two);
        threeCardView=v.findViewById(R.id.cardview_three);
        fourCardView=v.findViewById(R.id.cardview_four);
        fiveCardView=v.findViewById(R.id.cardview_five);
        sixCardView=v.findViewById(R.id.cardview_six);
        sevenCardView=v.findViewById(R.id.cardview_seven);

        oneTextView=v.findViewById(R.id.textview_one);
        twoTextView=v.findViewById(R.id.textview_two);
        threeTextView=v.findViewById(R.id.textview_three);
        fourTextView=v.findViewById(R.id.textview_four);
        fiveTextView=v.findViewById(R.id.textview_five);
        sixTextView=v.findViewById(R.id.textview_six);
        sevenTextView=v.findViewById(R.id.textview_seven);



        oneCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpIntent(v,"subject1");

            }
        });

        twoCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpIntent(v,"subject2");

            }
        });
        threeCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpIntent(v,"subject3");

            }
        });
        fourCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpIntent(v,"subject4");

            }
        });
        fiveCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpIntent(v,"subject5");

            }
        });
        sixCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpIntent(v,"subject7");

            }
        });
        sevenCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpIntent(v,"subject6");

            }
        });

        return v;
    }
    private void setUpIntent(View v,String subject_number) {

        Intent intent = new Intent(getActivity(),FileUploadActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("term","term3");
        intent.putExtra("subject",String.valueOf(v.getTag()));
        intent.putExtra("subject_number",subject_number);
        startActivity(intent);


    }

}
