package com.linkpcom.mitrafast.Classes.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.linkpcom.mitrafast.Classes.Dialogs.ConfirmDialog;
import com.linkpcom.mitrafast.Classes.REST.Models.Beans.FavoritePlace;
import com.linkpcom.mitrafast.R;
import com.linkpcom.mitrafast.databinding.ListItemFavoritePlaceBinding;

import java.util.ArrayList;
import java.util.List;


public class FavoritePlacesAdapter extends RecyclerView.Adapter<com.linkpcom.mitrafast.Classes.Adapters.FavoritePlacesAdapter.FavoritePlacesAdapterViewHolder> {

    private final ConfirmDialog deleteAddressDialog;
    private List<FavoritePlace> mData;
    private FavoritePlacesAdapterOnClickHandler mClickHandler;

    public FavoritePlacesAdapter(Context context) {
        deleteAddressDialog = new ConfirmDialog(context);
        deleteAddressDialog.setTitle(context.getString(R.string.delete_from_addresses));

    }

    public void setItemClickListener(FavoritePlacesAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public FavoritePlacesAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.list_item_favorite_place;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new FavoritePlacesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritePlacesAdapterViewHolder holder, int position) {
        Log.d("osAndroid", "onBindViewHolder: called " + position);
        holder.mBinding.nameTextView.setText(mData.get(position).getName());
    }

    @Override
    public int getItemCount() {
        if (null == mData) return 0;
        return mData.size();
    }

    public List<FavoritePlace> getData() {
        return mData;
    }

    public void setData(List<FavoritePlace> data) {
        if (mData == null)
            mData = data;
        else
            mData.addAll(data);
        Log.d("osAndroid", "setData: called  " + data.size());
        notifyDataSetChanged();
    }

    public void clear() {
        if (mData != null) {
            mData.clear();
            notifyDataSetChanged();
        }
    }

    public class FavoritePlacesAdapterViewHolder extends RecyclerView.ViewHolder {

        ListItemFavoritePlaceBinding mBinding;

        public FavoritePlacesAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            mBinding = ListItemFavoritePlaceBinding.bind(itemView);
            itemView.setOnClickListener(this::onItemClick);
            mBinding.delete.setOnClickListener(this::onDeleteButtonClick);

        }

        public void onDeleteButtonClick(View view) {
            int adapterPosition = getBindingAdapterPosition();
            FavoritePlace favoritePlace = mData.get(getBindingAdapterPosition());
            deleteAddressDialog.setConfirmListDialogHandler(() -> {
                mData.remove(adapterPosition);
                if (mClickHandler != null)
                    mClickHandler.onFavoritePlaceDeleteClick(favoritePlace.getId());
                notifyDataSetChanged();
                deleteAddressDialog.getDialog().dismiss();
            });
            deleteAddressDialog.getDialog().show();
        }

        public void onItemClick(View view) {
            FavoritePlace favoritePlace = mData.get(getBindingAdapterPosition());
            if (mClickHandler != null)
                mClickHandler.onFavoritePlaceItemClick(favoritePlace);
        }

    }

    public interface FavoritePlacesAdapterOnClickHandler {
        void onFavoritePlaceItemClick(FavoritePlace favoritePlace);

        void onFavoritePlaceDeleteClick(int favoritePlaceId);
    }
}
