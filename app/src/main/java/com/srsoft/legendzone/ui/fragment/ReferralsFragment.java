package com.srsoft.legendzone.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.srsoft.legendzone.databinding.FragmentReferralsBinding;

public class ReferralsFragment extends Fragment {

    private FragmentReferralsBinding binding;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public ReferralsFragment() {

    }

    public static ReferralsFragment newInstance(String param1, String param2) {
        ReferralsFragment fragment = new ReferralsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentReferralsBinding.inflate(getLayoutInflater(), container, false);
        initialization();
        return binding.getRoot();
    }
    private void initialization(){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uId= user.getUid();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("appConfig").document("referralInfo").get().addOnCompleteListener(
                new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if(documentSnapshot.exists()){
                            binding.referralline1.setText(documentSnapshot.get("get").toString());
                            binding.referralline2.setText(documentSnapshot.get("for").toString());
                            binding.referralline3.setText(documentSnapshot.get("when").toString());
                        }
                    }
                }
        );

        db.collection("users").document(uId).collection("referrals").document(uId).get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                DocumentSnapshot document = task.getResult();
                                if(document.exists()) {
                                    binding.tvReferralCount.setText(document.get("referralCount").toString());
                                    binding.tvReferralEarning.setText(document.get("referralEarnings").toString());
                                }
                            }
                        });

        db.collection("users").document(uId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot = task.getResult();
                if(documentSnapshot.exists()) {
                    binding.etCode.setText(documentSnapshot.get("referralId").toString());
                }
            }
        });
        binding.referraltutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://sites.google.com/view/legendzonegame/refer-and-earn"));
                startActivity(browserIntent);
            }
        });


    }
}