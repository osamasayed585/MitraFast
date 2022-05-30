package com.linkpcom.mitrafast.Classes.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Choice;
import com.linkpcom.mitrafast.R;
import com.linkpcom.mitrafast.databinding.ListItemChoiceBinding;

import java.util.ArrayList;
import java.util.List;

import rx.subjects.PublishSubject;

public class ChoicesAdapter extends RecyclerView.Adapter<ChoicesAdapter.ChoicesAdapterViewHolder> {
    //The list of Objects that will populate the RecyclerView
    private List<Choice> mData = new ArrayList<>();
    private ChoicesAdapterOnClickHandler mClickHandler;
    private PublishSubject<Double> price=PublishSubject.create();

    public PublishSubject<Double> getPrice() {
        return price;
    }

    Context context;
    private double totalCost = 0.0;

    public double getTotalCost() {
        if (mData != null && mData.size() > 0) {
            totalCost = mData.get(0).getPrice();
            return totalCost;
        } else return 0;

    }

    private String currency;

    public ChoicesAdapter() {
    }

    public void setItemClickListener(ChoicesAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public ChoicesAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_choice, parent, false);
        return new ChoicesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChoicesAdapterViewHolder holder, int position) {
        Choice choice = mData.get(position);
        holder.mBinding.choice.setText(choice.getName());
        holder.mBinding.cost.setText(String.format("%s%s", choice.getPrice(), currency));
        holder.mBinding.choice.setBackgroundResource(choice.isChecked() ? R.drawable.rounded_orange_solid : R.drawable.rounded_orange_stroke);
        holder.mBinding.choice.setTextColor(context.getResources().getColor(choice.isChecked() ? R.color.white : R.color.colorPrimary));
    }

    @Override
    public int getItemCount() {
        if (null == mData) return 0;
        return mData.size();
    }

    public void setData(List<Choice> data) {
        mData = data;
        if(mData.size()>0) {
            mData.get(0).setChecked(true);
            totalCost = mData.get(0).getPrice();
            price.onNext(totalCost);
        }
        notifyDataSetChanged();
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public List<Integer> getSelectedDataIdes() {
        List<Integer> choices = new ArrayList<>();
        for (int i = 0; i < mData.size(); i++) {
            if (mData.get(i).isChecked())
                choices.add(mData.get(i).getId());
        }
        return choices;
    }

    public List<String> getSelectedDataNames() {
        List<String> choices = new ArrayList<>();
        for (int i = 0; i < mData.size(); i++) {
            if (mData.get(i).isChecked())
                choices.add(mData.get(i).getName());
        }
        return choices;
    }

    //The interface that will be implemented later in parent activity
    public interface ChoicesAdapterOnClickHandler {
        void onChoiceItemClick(double totalCost);
    }


    public class ChoicesAdapterViewHolder extends RecyclerView.ViewHolder {
        //get an Instance for dataBinding
        ListItemChoiceBinding mBinding;

        public ChoicesAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            mBinding = ListItemChoiceBinding.bind(itemView);
            mBinding.choice.setOnClickListener(this::OnChoiceClick);
        }

        private void OnChoiceClick(View view) {
            mData.get(getAdapterPosition()).reverseCheck();
            totalCost = mData.get(getAdapterPosition()).isChecked() ? totalCost + mData.get(getAdapterPosition()).getPrice() : totalCost - mData.get(getAdapterPosition()).getPrice();
            if (mClickHandler != null)
                mClickHandler.onChoiceItemClick(totalCost);
            notifyDataSetChanged();

        }


    }
}