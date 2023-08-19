package com.srsoft.legendzone.ui.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.srsoft.legendzone.R;
import com.srsoft.legendzone.databinding.FragmentHomeBinding;
import com.srsoft.legendzone.databinding.FragmentProfileBinding;
import com.srsoft.legendzone.ui.activity.SplashActivity;
import com.srsoft.legendzone.ui.activity.UpdateProfileActivity;
import com.srsoft.legendzone.ui.common.BaseFragment;


public class ProfileFragment extends BaseFragment {

    private FragmentProfileBinding binding;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        binding = FragmentProfileBinding.inflate(getLayoutInflater(), container, false);
        initialization();
        return binding.getRoot();
    }

    private void initialization(){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        binding.name.setText(user.getDisplayName());

        Glide.with(ProfileFragment.this)
                .load(user.getPhotoUrl())
                .into(binding.ivProfile);

        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Unable to Logout!");
                builder.setIcon(android.R.drawable.ic_menu_info_details);
                builder.setCancelable(false);
                AlertDialog dialog = builder.setNegativeButton("OK", (dialogInterface, i) -> dialogInterface.dismiss()).create();
                dialog.show();
            }
        });

        binding.settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("No settings available!");
                builder.setIcon(android.R.drawable.ic_menu_info_details);
                builder.setCancelable(false);
                AlertDialog dialog = builder.setNegativeButton("OK", (dialogInterface, i) -> dialogInterface.dismiss()).create();
                dialog.show();
            }
        });

        binding.feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://sites.google.com/view/legendzonegame/feedback"));
                startActivity(browserIntent);
            }
        });

        binding.beginnersguide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://sites.google.com/view/legendzonegame/beginners-guide"));
                startActivity(browserIntent);
            }
        });

        binding.aboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://sites.google.com/view/legendzonegame/about-us"));
                startActivity(browserIntent);
            }
        });

        binding.customersupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://sites.google.com/view/legendzonegame/customer-support"));
                startActivity(browserIntent);
            }
        });
    }

}