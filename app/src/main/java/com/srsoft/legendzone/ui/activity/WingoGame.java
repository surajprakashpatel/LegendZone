package com.srsoft.legendzone.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
;
import com.srsoft.legendzone.databinding.ActivityWingoGameBinding;


public class WingoGame extends AppCompatActivity {

    private ActivityWingoGameBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityWingoGameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initialization();


    }

    private void initialization(){
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

        binding.timer.setOTP("0159");
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
}