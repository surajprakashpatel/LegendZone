package com.srsoft.legendzone.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.srsoft.legendzone.R;
import com.srsoft.legendzone.databinding.ActivityDepositFundBinding;
import com.srsoft.legendzone.ui.common.BaseActivity;

import org.json.JSONObject;

import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DepositFundActivity extends BaseActivity implements PaymentResultListener {

    Checkout checkout;
    EditText editText;
    String uId;
    String amount = "0";
    private ActivityDepositFundBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDepositFundBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initialization();
    }

    private void initialization() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uId= user.getUid();

        final String[] key = {"rzp_live_kK1dIM8sm3Ssxt"};
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("appConfig").document("paymentId").get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                if(document.exists())
                 key[0] = document.get("key").toString();
            }
        });

        checkout=new Checkout();
        checkout.preload(getApplicationContext());
        checkout.setKeyID(key[0]);
        binding.btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPayment();
            }
        });
    }

    public void startPayment() {
        amount = binding.amountField.getText().toString().trim();
        if (amount.matches("")) {
            Toast.makeText(this, "Please Enter an amount", Toast.LENGTH_SHORT).show();
        } else if (Integer.parseInt(amount)<199) {
            showAlertDialog("Enter amount above 200",DepositFundActivity.this);
        } else {
            amount = amount + "00";
            checkout.setImage(R.drawable.app_logo_transparent);

            final Activity activity = this;


            try {
                JSONObject options = new JSONObject();

                options.put("name", "LegendZone");
                options.put("description", "LegendZone");
                options.put("theme.color", "#151F49");
                options.put("currency", "INR");
                options.put("amount", amount);//pass amount in currency subunits
                options.put("prefill.email", "support@peenakle.com");
                options.put("prefill.contact", "9990099900 ");
                JSONObject retryObj = new JSONObject();
                retryObj.put("enabled", true);
                retryObj.put("max_count", 4);
                options.put("retry", retryObj);

                checkout.open(activity, options);

            } catch (Exception e) {
                Toast.makeText(this, "Error in starting Razorpay Checkout", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onPaymentSuccess(String s) {


        updateRechargeHistory();

    }

    private void updateRechargeHistory() {
        showLoader();
        SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        int fund = Integer.valueOf(amount.substring(0,amount.length()-2));
        Map<String, Object> data = new HashMap<>();
        data.put("date",dt.toString());
        data.put("amount",fund);
        db.collection("users").document(uId).collection("rechargeHistory").document().set(data);
        db.collection("withdrawalRequests").document(user.getUid()).set(data);
        updateBalance();
        hideLoader();
    }

    private void updateBalance() {
        int fund = Integer.valueOf(amount.substring(0,amount.length()-2));
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(uId).update("totalBalance", FieldValue.increment(fund));
        Toast.makeText(this, "Recharge Successful!", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(DepositFundActivity.this, DashboardActivity.class);
        startActivity(intent);
        finishAffinity();
    }

    @Override
    public void onPaymentError(int i, String s) {
        showAlertDialog("Payment failed!",this);
    }
}