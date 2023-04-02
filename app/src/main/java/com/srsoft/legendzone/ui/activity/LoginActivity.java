package com.srsoft.legendzone.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.srsoft.legendzone.databinding.ActivityLoginBinding;
import com.srsoft.legendzone.ui.common.BaseActivity;
import com.srsoft.legendzone.utils.Constant;

public class LoginActivity extends BaseActivity {

    private ActivityLoginBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initialization();
    }

    private void initialization() {

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    Intent intent = new Intent(LoginActivity.this, OTPVerificationActivity.class);
                    intent.putExtra(Constant.USER_PHONE_NO,"+91"+binding.etPhone.getText().toString());
                    startActivity(intent);
                }
            }
        });


    }

    private boolean validate() {
        boolean valid = true;

        if (binding.etPhone.getText().toString().isEmpty()) {
            binding.etPhone.setError("Please enter your mobile no");
            binding.etPhone.requestFocus();
            valid = false;
        } else if (binding.etPhone.getText().toString().length() != 10) {
            binding.etPhone.setError("Please enter valid mobile no");
            binding.etPhone.requestFocus();
            valid = false;
        } else {
            binding.etPhone.setError(null);
        }

        return valid;
    }

}