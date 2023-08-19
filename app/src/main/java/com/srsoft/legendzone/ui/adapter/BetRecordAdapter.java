package com.srsoft.legendzone.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.srsoft.legendzone.databinding.GameRecordsItemBinding;
import com.srsoft.legendzone.models.BetRecord;

import java.util.List;

public class BetRecordAdapter extends  RecyclerView.Adapter<BetRecordAdapter.MyViewHolder>{

    private Context context;
    private List<BetRecord> items;

    public BetRecordAdapter(Context context, List<BetRecord> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public BetRecordAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        GameRecordsItemBinding mBinding = GameRecordsItemBinding.inflate(LayoutInflater.from(context), parent, false);
        return new BetRecordAdapter.MyViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull BetRecordAdapter.MyViewHolder holder, int position) {

        BetRecord item = items.get(position);
        holder.mBinding.period.setText(item.getGame());
        holder.mBinding.number.setText(item.getBetOn());
        holder.mBinding.color.setText(item.getBetAmount()+"");
        holder.mBinding.size.setText(item.getDateTime());


    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        GameRecordsItemBinding mBinding;
        public MyViewHolder(GameRecordsItemBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
        }
    }
}
