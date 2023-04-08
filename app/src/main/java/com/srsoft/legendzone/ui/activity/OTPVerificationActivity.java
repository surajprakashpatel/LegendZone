package com.srsoft.legendzone.ui.activity;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.srsoft.legendzone.R;
import com.srsoft.legendzone.databinding.ActivityOtpverifiationBinding;
import com.srsoft.legendzone.ui.common.BaseActivity;
import com.srsoft.legendzone.utils.Constant;

import java.util.concurrent.TimeUnit;

public class OTPVerificationActivity extends BaseActivity {

    private ActivityOtpverifiationBinding binding;
    private FirebaseAuth mAuth;
    private CountDownTimer countDownTimer;
    private int countDown = 120000;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =ActivityOtpverifiationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FirebaseApp.initializeApp(getApplicationContext());
        FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
        firebaseAppCheck.installAppCheckProviderFactory(
                PlayIntegrityAppCheckProviderFactory.getInstance());

        initialization();

        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private void initialization() {

        mAuth= FirebaseAuth.getInstance();
        setCallbacks();

        showSnackBar("Please wait! Sending OTP");
        sendOTP();
        setUpTimerForResend();

        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        binding.btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, binding.otpView.getOTP());
                signInWithCredential(credential);
            }
        });

        binding.resendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOTP();
            }
        });

    }

    private void sendOTP() {
        Intent intent = getIntent();
        String phoneNumber = intent.getStringExtra(Constant.USER_PHONE_NO);

        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth) //mAuth is defined on top
                .setPhoneNumber(phoneNumber)       // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(this)                 // Activity (for callback binding)
                .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void setCallbacks() {

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d("verificationcompleted", "onVerificationCompleted:" + phoneAuthCredential);
                showSnackBar("Verifcation Completed");
                signInWithCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                // Show a message and update the UI

                Log.w("verificationfailed", "onVerificationFailed", e);
                showSnackBar("Verification failed! Try again");
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    showSnackBar("Invalid Request!");
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    showSnackBar("SMS quota has been exceeded");
                }

            }

            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                super.onCodeSent(verificationId, token);
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d("codesent", "onCodeSent:" + verificationId);
                // Save verification ID and resending token so we can use them later
                showSnackBar("Code has been sent");
                mVerificationId = verificationId;
                mResendToken = token;
            }

        };
    }

    private void signInWithCredential(PhoneAuthCredential credential){

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("signinsuccess", "signInWithCredential:success");
                            showSnackBar("SignIn sucess!");

                            FirebaseUser user = task.getResult().getUser();
                            Boolean newuser=task.getResult().getAdditionalUserInfo().isNewUser();

                            // Update UI
                            if (user != null) {
                                if(newuser){
                                    Intent intent = new Intent(getApplicationContext(), UpdateProfileActivity.class);
                                    startActivity(intent);
                                    finish();
                                }else {
                                    Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            } else {

                                // No user is signed in
                            }

                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w("signin failed", "signInWithCredential:failure", task.getException());
                            showSnackBar("SignIn failed");
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                showSnackBar("Code Invalid");
                            }
                        }

                    }
                });
    }

    private void setUpTimerForResend() {
        binding.resendOtp.setEnabled(false);

        //set timer min
        countDown = 120000; //(120000 sec)

        if (countDownTimer != null)
            countDownTimer.cancel();

        countDownTimer = new CountDownTimer(countDown, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                long Days = countDown / (24 * 60 * 60 * 1000);
                long Hours = countDown / (60 * 60 * 1000) % 24;
                long Minutes = countDown / (60 * 1000) % 60;
                long Seconds = countDown / 1000 % 60;
                countDown = countDown - 1000;

                binding.resendOtp.setText(String.format("%02d", Seconds) + " Second left");
            }

            @Override
            public void onFinish() {
                binding.resendOtp.setText(getString(R.string.resend_otp));
                binding.resendOtp.setEnabled(true);
            }
        }.start();
    }
}