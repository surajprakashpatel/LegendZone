package com.srsoft.legendzone.ui.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.icu.math.BigDecimal;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.inappmessaging.FirebaseInAppMessaging;
import com.srsoft.legendzone.R;
import com.srsoft.legendzone.databinding.ActivityWingoGameBinding;
import com.srsoft.legendzone.models.Game;
import com.srsoft.legendzone.models.GameRecord;
import com.srsoft.legendzone.ui.adapter.GameRecordAdapter;
import com.srsoft.legendzone.ui.common.BaseActivity;

import org.w3c.dom.Text;

import java.math.RoundingMode;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class WingoGame extends BaseActivity {

    private ActivityWingoGameBinding binding;
    private double balance=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityWingoGameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initialization();


    }

    @Override
    protected void onResume() {
        initialization();
        super.onResume();
    }

    private void initialization(){

        FirebaseInAppMessaging.getInstance().triggerEvent("wingoOpen");
        binding.fab0.setImageBitmap(textAsBitmap("0", 40, Color.RED));
        binding.fab1.setImageBitmap(textAsBitmap("1", 40, Color.WHITE));
        binding.fab2.setImageBitmap(textAsBitmap("2", 40, Color.WHITE));
        binding.fab3.setImageBitmap(textAsBitmap("3", 40, Color.WHITE));
        binding.fab4.setImageBitmap(textAsBitmap("4", 40, Color.WHITE));
        binding.fab5.setImageBitmap(textAsBitmap("5", 40, Color.WHITE));
        binding.fab6.setImageBitmap(textAsBitmap("6", 40, Color.WHITE));
        binding.fab7.setImageBitmap(textAsBitmap("7", 40, Color.WHITE));
        binding.fab8.setImageBitmap(textAsBitmap("8", 40, Color.WHITE));
        binding.fab9.setImageBitmap(textAsBitmap("9", 40, Color.WHITE));

        binding.btnAddFunds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WingoGame.this, DepositFundActivity.class);
                startActivity(intent);
            }
        });
        binding.btnWithdrawFunds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WingoGame.this, FundWithdrawalActivity.class);
                startActivity(intent);
            }
        });

        refresh();

        binding.fab0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBSheetForConfirmation("0");
            }
        });

        binding.fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBSheetForConfirmation("1");
            }
        });

        binding.fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBSheetForConfirmation("2");
            }
        });

        binding.fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBSheetForConfirmation("3");
            }
        });
        binding.fab4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBSheetForConfirmation("4");
            }
        });
        binding.fab5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBSheetForConfirmation("5");
            }
        });
        binding.fab6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBSheetForConfirmation("6");
            }
        });
        binding.fab7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBSheetForConfirmation("7");
            }
        });
        binding.fab8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBSheetForConfirmation("8");
            }
        });
        binding.fab9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBSheetForConfirmation("9");
            }
        });
        binding.fabGreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBSheetForConfirmation("green");
            }
        });

        binding.fabRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBSheetForConfirmation("red");
            }
        });
        binding.fabBlue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBSheetForConfirmation("blue");
            }
        });

        binding.fabBig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBSheetForConfirmation("big");
            }
        });
        binding.fabSmall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBSheetForConfirmation("small");
            }
        });
    }

    private void refresh() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        db.collection("wingolive").document("timer").addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                db.collection("wingogamerecords").limit(8).orderBy("timeofgame",Query.Direction.DESCENDING).get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        ArrayList<String> periods = new ArrayList<>();
                        ArrayList<GameRecord> gameRecords = new ArrayList<>();
                        for (DocumentSnapshot document : task.getResult()) {
                            GameRecord gameRecord = document.toObject(GameRecord.class);
                            gameRecords.add(gameRecord);
                            periods.add(document.getId().toString());
                        }
                        RecyclerView recyclerView =(RecyclerView) findViewById(R.id.games_recycler);
                        recyclerView.setLayoutManager(new LinearLayoutManager(WingoGame.this));

                        GameRecordAdapter myAdapter = new GameRecordAdapter(WingoGame.this,gameRecords,periods);
                        recyclerView.setAdapter(myAdapter);


                    }
                });

                db.collection("users").document(user.getUid()).get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                DocumentSnapshot document = task.getResult();
                                if(document.exists()) {
                                    balance = Double.parseDouble(document.get("totalBalance").toString());
                                    binding.balancetextview.setText("₹ "+document.get("totalBalance").toString());
                                }
                            }
                        });

                String inputTime = String.valueOf(value.get("time")) ;
                int hours = Integer.parseInt(inputTime.substring(0, 2));
                int minutes = Integer.parseInt(inputTime.substring(2, 4))+2;
                int seconds = Integer.parseInt(inputTime.substring(4, 6));

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hours);
                calendar.set(Calendar.MINUTE, minutes);
                calendar.set(Calendar.SECOND, seconds);

                Date inputDate = calendar.getTime();

                // Get the current time
                Date currentDate = new Date();
                // Calculate the time difference in milliseconds
                long timeDifferenceInMillis = inputDate.getTime()- currentDate.getTime() ;
                hideLoader();
                new CountDownTimer(timeDifferenceInMillis,1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                        binding.timer.setOTP(String.format("%04d",millisUntilFinished/1000));
                        if(millisUntilFinished/1000<6){
                            showLoader();
                        }
                    }

                    @Override
                    public void onFinish() {

                    }
                }.start();
            }
        });


    }

    public static Bitmap textAsBitmap(String text, float textSize, int textColor) {
        Paint paint = new Paint();
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        paint.setTextAlign(Paint.Align.LEFT);
        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(text) + 0.0f); // round
        int height = (int) (baseline + paint.descent() + 0.0f);
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 0, baseline, paint);
        return image;
    }

    private void openBSheetForConfirmation(String bet){
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_filter, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(WingoGame.this, R.style.bottomSheetDialog); // Style her
        bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        bottomSheetDialog.setContentView(view);

        BottomSheetBehavior mBehavior = BottomSheetBehavior.from((View) view.getParent());
        bottomSheetDialog.setOnShowListener(dialogInterface -> {
            mBehavior.setPeekHeight(view.getHeight());//get the height dynamically
        });

        TextView textView = bottomSheetDialog.findViewById(R.id.betTV);
        textView.setText(""+bet);
        bottomSheetDialog.show();
        EditText etAmount = bottomSheetDialog.findViewById(R.id.etAmount);
        etAmount.setText("10");
        Button betbtn = bottomSheetDialog.findViewById(R.id.btnapply);
        betbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                double amount = Double.parseDouble(etAmount.getText().toString().trim());
                if(amount>balance){
                    showAlertDialog("Not sufficient Balance",WingoGame.this);
                    bottomSheetDialog.hide();
                }else {
                    balance = balance-amount;
                    BigDecimal bal = new BigDecimal(balance).setScale(2,5);
                    balance = bal.doubleValue();
                    binding.balancetextview.setText("₹ "+balance);
                    float deduction = (float) amount;
                    amount = (double) (amount-amount*0.02);
                    getUpdateData(bet, amount,deduction);
                    bottomSheetDialog.hide();
                }
            }

        });

    }

    public void getUpdateData(String bet, double amount,float deduction) {
        showLoader();
        Map<String, Object> data = new HashMap<>();
        switch (bet){
            case "red":
                amount =amount*3;
                data.put("1", FieldValue.increment(amount));
                data.put("3", FieldValue.increment(amount));
                data.put("5", FieldValue.increment(amount));
                data.put("7", FieldValue.increment(amount));
                break;
            case "blue":
                amount =amount*3;
                data.put("0", FieldValue.increment(amount));
                data.put("9", FieldValue.increment(amount));
                break;
            case "green":
                amount =amount*3;
                data.put("2", FieldValue.increment(amount));
                data.put("4", FieldValue.increment(amount));
                data.put("6", FieldValue.increment(amount));
                data.put("8", FieldValue.increment(amount));
                break;
            case "small":
                amount =amount*2;
                data.put("0",FieldValue.increment(amount));
                data.put("1",FieldValue.increment(amount));
                data.put("2",FieldValue.increment(amount));
                data.put("3",FieldValue.increment(amount));
                data.put("4",FieldValue.increment(amount));
                break;

            case "big":
                amount =amount*2;
                data.put("5",FieldValue.increment(amount));
                data.put("6",FieldValue.increment(amount));
                data.put("7",FieldValue.increment(amount));
                data.put("8",FieldValue.increment(amount));
                data.put("9",FieldValue.increment(amount));
                break;

            default:
                amount =amount*10;
                data.put(bet,FieldValue.increment(amount));
                break;
        }
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        BigDecimal bd = new BigDecimal(amount).setScale(2,5);
        double winPrize = bd.doubleValue();

        db.collection("wingolive").document("twominutes").update(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Calendar calendar = Calendar.getInstance();
                Date currentDate = calendar.getTime();

                // Define the desired date format
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM HH:mm");

                // Format the date and time
                String formattedDate = dateFormat.format(currentDate);
                Map<String, Object> betdata = new HashMap<>();
                betdata.put("uId",user.getUid());
                betdata.put("betAmount", winPrize);
                betdata.put("betOn",bet);
                db.collection("wingoBets").document().set(betdata);
                betdata.replace("betAmount",deduction);
                betdata.remove("uId");
                betdata.put("dateTime",formattedDate);
                betdata.put("game","Wingo");
                db.collection("users").document(user.getUid()).collection("betHistory").document().set(betdata);
                db.collection("users").document(user.getUid()).update("totalBalance",FieldValue.increment(-deduction));
                hideLoader();
                Toast.makeText(WingoGame.this, "Bet Success", Toast.LENGTH_SHORT).show();
            }
        });

    }
}