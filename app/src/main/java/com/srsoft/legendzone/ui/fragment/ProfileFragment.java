package com.srsoft.legendzone.ui.fragment;

import android.content.Intent;
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


public class ProfileFragment extends Fragment {

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



    }
}