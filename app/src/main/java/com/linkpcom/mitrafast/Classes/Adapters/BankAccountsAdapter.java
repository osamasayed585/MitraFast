package com.linkpcom.mitrafast.Classes.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Bank;
import com.linkpcom.mitrafast.Classes.REST.Models.Beans.BankAccount;
import com.linkpcom.mitrafast.R;
import com.linkpcom.mitrafast.databinding.RowBandAcountBinding;

import java.util.List;

public class BankAccountsAdapter extends RecyclerView.Adapter<BankAccountsAdapter.MyBankAccountsHolder> {

    private List<BankAccount> mData;
    private Context context;

    public void setData(List<BankAccount> mData) {
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyBankAccountsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        return new MyBankAccountsHolder(LayoutInflater.from(context).inflate(R.layout.row_band_acount, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyBankAccountsHolder holder, int position) {
        holder.bind(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyBankAccountsHolder extends RecyclerView.ViewHolder {
        RowBandAcountBinding binding;

        public MyBankAccountsHolder(@NonNull View itemView) {
            super(itemView);
            binding = RowBandAcountBinding.bind(itemView);
        }

        void bind(BankAccount bankAccount) {
            binding.tvBankName.setText(String.format("%s : %s", context.getResources().getString(R.string.nameBank), bankAccount.getBank().getName()));
            binding.tvAccountOwnerName.setText(String.format("%s : %s", context.getResources().getString(R.string.account_owner_name), bankAccount.getAccount_name()));
            binding.tvIbanNumber.setText(String.format("%s : %s", context.getResources().getString(R.string.iban_num), bankAccount.getIban_number()));
        }
    }
}
