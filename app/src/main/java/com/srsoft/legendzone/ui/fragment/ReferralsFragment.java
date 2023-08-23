package com.srsoft.legendzone.ui.fragment;

import static android.content.Context.CLIPBOARD_SERVICE;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.inappmessaging.FirebaseInAppMessaging;
import com.srsoft.legendzone.databinding.FragmentReferralsBinding;

public class ReferralsFragment extends Fragment {

    private FragmentReferralsBinding binding;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ClipboardManager clipboardManager;
    private ClipData clipData;
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

        FirebaseInAppMessaging.getInstance().triggerEvent("referral");
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

        binding.btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                db.collection("appConfig").document("updateManager").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                       DocumentSnapshot document = task.getResult();
                        try {
                            Intent shareIntent = new Intent(Intent.ACTION_SEND);
                            shareIntent.setType("text/plain");
                            String text;
                            text = binding.etCode.getText().toString();
                            String shareMessage =document.getString("updateUrl")+"\n" +"Download Legendzone - No. of Esports App. Use my referral code to avail benefits."+"\n\n Code: "+text;
                            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                            startActivity(shareIntent);

                        } catch (Exception e) {
                        }
                    }
                });

            }
        });

        binding.btnCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clipboardManager = (ClipboardManager) requireContext().getSystemService(CLIPBOARD_SERVICE);
                String text;
                text = binding.etCode.getText().toString();
                clipData = ClipData.newPlainText("text", text);
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(getContext(), "Copied!", Toast.LENGTH_SHORT).show();
            }
        });


    }
}