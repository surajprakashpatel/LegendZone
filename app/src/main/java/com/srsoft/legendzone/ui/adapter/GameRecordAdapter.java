package com.srsoft.legendzone.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.srsoft.legendzone.databinding.GameRecordsItemBinding;

import com.srsoft.legendzone.models.GameRecord;

import java.util.List;

public class GameRecordAdapter extends  RecyclerView.Adapter<GameRecordAdapter.MyViewHolder>{

    private Context context;
    private List<GameRecord> items;
    private List<String> periods;

    public GameRecordAdapter(Context context, List<GameRecord> items,List<String> periods) {
        this.context = context;
        this.items = items;
        this.periods =periods;
    }

    @NonNull
    @Override
    public GameRecordAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        GameRecordsItemBinding mBinding = GameRecordsItemBinding.inflate(LayoutInflater.from(context), parent, false);
        return new GameRecordAdapter.MyViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull GameRecordAdapter.MyViewHolder holder, int position) {

        GameRecord item = items.get(position);
        holder.mBinding.period.setText(periods.get(position));
        holder.mBinding.number.setText(item.getResult());
        String color = null;
        String size = null;
        switch(item.getResult()){
            case "0": color="Blue"; size = "Small";
                break;
            case "1": color="Red"; size = "Small";
                break;
            case "2": color="Green"; size = "Small";
                break;
            case "3": color="Red"; size = "Small";
                break;
            case "4": color="Green"; size = "Small";
                break;
            case "5": color="Red"; size = "Big";
                break;
            case "6": color="Green";size = "Big";
                break;
            case "7": color="Green"; size = "Big";
                break;
            case "8": color="blue"; size = "Big";
                break;
            case "9": color="Blue";size = "Big";
                break;
            default:
                break;
        }
        holder.mBinding.color.setText(color);
        holder.mBinding.size.setText(size);
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
