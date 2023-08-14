package com.srsoft.legendzone.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.srsoft.legendzone.R;
import com.srsoft.legendzone.databinding.GamesAdapterLayoutBinding;
import com.srsoft.legendzone.models.Game;
import com.srsoft.legendzone.ui.activity.WingoGame;

import java.util.List;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.MyViewHolder> {
    private Context context;
    private List<Game> items;
    private boolean isRedirectToActivity;

    public GameAdapter(Context context, List<Game> items, boolean isRedirectToActivity) {
        this.context = context;
        this.items = items;
        this.isRedirectToActivity = isRedirectToActivity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        GamesAdapterLayoutBinding mBinding = GamesAdapterLayoutBinding.inflate(LayoutInflater.from(context), parent, false);
        return new MyViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Game item = items.get(position);

        holder.mBinding.categoryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent intent = new Intent(context, WingoGame.class);
                    context.startActivity(intent);


            }
        });
        holder.mBinding.progressBar.setVisibility(View.VISIBLE);
        Glide.with(context)
                .load(item.getGameImgUrl())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.mBinding.progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.mBinding.progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .error(R.mipmap.ic_launcher)
                .into(holder.mBinding.categoryImage);
//        holder.mBinding.categoryTitle.setVisibility(View.VISIBLE);
//        holder.mBinding.categoryTitle.setText(item.getGameName());

        holder.mBinding.getRoot().setOnClickListener(v -> {
            switch(item.getGameId()){
                case "":
                    break;

            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        GamesAdapterLayoutBinding mBinding;

        MyViewHolder(GamesAdapterLayoutBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
        }
    }
}
