package com.srsoft.legendzone.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.srsoft.legendzone.databinding.ActivityFundWithdrawalBinding;
import com.srsoft.legendzone.ui.common.BaseActivity;

import java.util.HashMap;
import java.util.Map;

public class FundWithdrawalActivity extends BaseActivity {

    private ActivityFundWithdrawalBinding binding;

    int withdrawable=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFundWithdrawalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initialization();
    }

    private void initialization() {
        showLoader();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
               DocumentSnapshot documentSnapshot = task.getResult();
               String balance = documentSnapshot.get("totalBalance").toString();
               binding.balancetv.setText("Available Balance: "+balance);
               withdrawable = Integer.parseInt(balance);
               hideLoader();
            }
        });

        binding.btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount = binding.amountField.getText().toString().trim();
                String upiId = binding.upiField.getText().toString().trim();
                float fund = Float.parseFloat(amount);

                if(withdrawable<=Integer.parseInt(amount)){
                    showAlertDialog("Enter amount less than balance",FundWithdrawalActivity.this);
                } else if (amount.matches("")) {
                    showAlertDialog("Enter amount",FundWithdrawalActivity.this);
                } else if (upiId.matches("")) {
                    showAlertDialog("Enter UPI Id",FundWithdrawalActivity.this);
                }else {
                    SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    Map<String, Object> data = new HashMap<>();
                    data.put("date",dt.toString());
                    data.put("amount",amount);
                    data.put("UPI",upiId);
                    db.collection("users").document(user.getUid()).collection("withdrawHistory").document().set(data);
                    db.collection("withdrawalRequests").document(user.getUid()).set(data);
                    db.collection("users").document(user.getUid())
                            .update("totalBalance", FieldValue.increment(-fund))
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(FundWithdrawalActivity.this, "Withdrawal Successful!", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(FundWithdrawalActivity.this, DashboardActivity.class);
                                    startActivity(intent);
                                    finishAffinity();
                                }
                            });

                }

            }
        });

    }


}