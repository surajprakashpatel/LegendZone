package com.srsoft.legendzone.ui.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.inappmessaging.FirebaseInAppMessaging;
import com.srsoft.legendzone.R;
import com.srsoft.legendzone.databinding.FragmentHomeBinding;
import com.srsoft.legendzone.models.Game;
import com.srsoft.legendzone.ui.activity.WelcomeActivity;
import com.srsoft.legendzone.ui.adapter.GameAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private List<Game> games = new ArrayList<>();

    private GameAdapter gameAdapter;
    FirebaseFirestore database;
    private FragmentHomeBinding binding;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(getLayoutInflater(), container, false);
        initialization();
        return binding.getRoot();
    }

    private void initialization() {

        database = FirebaseFirestore.getInstance();
        FirebaseInAppMessaging.getInstance().triggerEvent("version1.1");
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        binding.gamesRecycler.setLayoutManager(layoutManager);
        gameAdapter = new GameAdapter(getActivity(), games, false);
        binding.gamesRecycler.setAdapter(gameAdapter);


        getBanner();
        getGames();
        getUpdateInfo();
        getNotice();
        getrecentWithdrawals();



    }

    private void getUpdateInfo() {
        database.collection("appConfig").document("updateManager").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                if(document.getString("updateAvailable").matches("yes")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage(document.getString("updateMsg"));
                    builder.setIcon(android.R.drawable.ic_menu_info_details);
                    builder.setCancelable(false);
                    AlertDialog dialog = builder.setNegativeButton("OK", (dialogInterface, i) -> dialogInterface.dismiss()).create();
                    dialog.show();
                }

            }
        });
    }

    private void getNotice() {
        database.collection("appConfig").document("notice").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                String notice = document.getString("notice");
                binding.notice.setText(notice);
            }
        });
    }

    private void getBanner() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        binding.textView.setText(user.getDisplayName());
        database.collection("sliderImages").document("sliderImage").get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String imageUrl = document.getString("imageUrl");
                                Glide.with(getContext())
                                        .load(imageUrl)
                                        .into(binding.slidePager);
                                binding.sliderShimmer.hideShimmer();

                            } else {

                            }
                        } else {

                        }
                    }
                });

    }

    private void getrecentWithdrawals() {
        binding.recentwithdrawal.hideShimmer();
        binding.recentwithdrawallayout.setVisibility(View.GONE);
    }

    private void getGames() {
        database.collection("games").whereEqualTo("gameStatus","active").get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    games.clear();
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        games.add(document.toObject(Game.class));
                                    }
                                    binding.gamesShimmer.hideShimmer();
                                    binding.gamesShimmer.setVisibility(View.GONE);
                                    gameAdapter.notifyDataSetChanged();
                                } else {

                                }
                            }
                        });



    }


    @Override
    public void onResume() {
        super.onResume();
//        mHandler = new Handler();

    }


    @Override
    public void onPause() {
        super.onPause();
//        mHandler.removeCallbacksAndMessages(null);
    }
    
}