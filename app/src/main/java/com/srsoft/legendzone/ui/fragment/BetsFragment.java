package com.srsoft.legendzone.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.srsoft.legendzone.R;
import com.srsoft.legendzone.databinding.FragmentBetsBinding;
import com.srsoft.legendzone.models.BetRecord;
import com.srsoft.legendzone.models.GameRecord;
import com.srsoft.legendzone.ui.activity.WingoGame;
import com.srsoft.legendzone.ui.adapter.BetRecordAdapter;
import com.srsoft.legendzone.ui.adapter.GameRecordAdapter;

import java.util.ArrayList;


public class BetsFragment extends Fragment {

    private FragmentBetsBinding binding;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    public BetsFragment() {
        // Required empty public constructor
    }

    public static BetsFragment newInstance(String param1, String param2) {
        BetsFragment fragment = new BetsFragment();
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
        binding = FragmentBetsBinding.inflate(getLayoutInflater(), container, false);
        initialization();
        return binding.getRoot();
    }

    private void initialization() {


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users").document(user.getUid()).collection("betHistory")
                .limit(10).orderBy("dateTime").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        ArrayList<BetRecord> betRecords = new ArrayList<>();
                        for (DocumentSnapshot document : task.getResult()) {
                            BetRecord betRecord = document.toObject(BetRecord.class);
                            betRecords.add(betRecord);
                        }
                        if(betRecords.isEmpty()){
                            binding.betsRecycler.setVisibility(View.GONE);
                            binding.tvNoHistory.setVisibility(View.VISIBLE);
                        }
                        RecyclerView recyclerView = binding.betsRecycler;
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                        BetRecordAdapter myAdapter = new BetRecordAdapter(getContext(),betRecords);
                        recyclerView.setAdapter(myAdapter);
                    }
                });
    }
}