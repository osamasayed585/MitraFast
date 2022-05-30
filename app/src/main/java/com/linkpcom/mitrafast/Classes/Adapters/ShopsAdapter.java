package com.linkpcom.mitrafast.Classes.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.linkpcom.mitrafast.Classes.REST.Models.Beans.ObjectFavoritesShops;
import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Shop;
import com.linkpcom.mitrafast.Classes.Utils.PreferencesManager;
import com.linkpcom.mitrafast.Classes.Utils.Utils;
import com.linkpcom.mitrafast.R;
import com.linkpcom.mitrafast.databinding.ListItemShopBinding;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.subjects.PublishSubject;
import timber.log.Timber;

public class ShopsAdapter extends RecyclerView.Adapter<ShopsAdapter.ShopsAdapterViewHolder> {

    private List<Shop> mData;
    private List<ObjectFavoritesShops> favList;
    private ShopsAdapterOnClickHandler mClickHandler;
    public PublishSubject<Shop> isFavoriteClicked = PublishSubject.create();
    private Context context;
    public PreferencesManager preferencesManager;

    public void setPreferencesManager(PreferencesManager preferencesManager){
        this.preferencesManager = preferencesManager;
    }

    public void setItemClickListener(ShopsAdapterOnClickHandler clickHandler) {
        this.mClickHandler = clickHandler;
    }

    public void setData(List<Shop> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public void addFavoritesShops(List<ObjectFavoritesShops> favList) {
        this.favList = favList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ShopsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_shop, parent, false);
        return new ShopsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopsAdapterViewHolder holder, int position) {
        if (mData != null) {
            Timber.d("osAndroid setData Called");
            holder.bind(mData.get(position));
        } else {
            Timber.d("osAndroid addFavoritesShops Called");
            holder.bind(favList.get(position).getShop());
        }
    }

    @Override
    public int getItemCount() {
        if (mData != null)
            return mData.size();
        if (favList != null)
            return favList.size();
        return 0;
    }

    public class ShopsAdapterViewHolder extends RecyclerView.ViewHolder {
        //get an Instance for dataBinding
        ListItemShopBinding mBinding;

        public ShopsAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            mBinding = ListItemShopBinding.bind(itemView);

            itemView.setOnClickListener(this::onItemClick);

            mBinding.ivFavorite.setOnClickListener(view -> {

                if (mData != null) {

                    isFavoriteClicked.onNext(mData.get(getBindingAdapterPosition()));

                    if (mData.get(getBindingAdapterPosition()).is_favourite()) {
                        mData.get(getBindingAdapterPosition()).set_favourite(false);
                    } else {
                        if (preferencesManager.getAuthenticationToken() != null)
                            mData.get(getBindingAdapterPosition()).set_favourite(true);
                    }
                } else {
                    isFavoriteClicked.onNext(favList.get(getBindingAdapterPosition()).getShop());

                    if (favList.get(getBindingAdapterPosition()).getShop().is_favourite()) {
                        favList.get(getBindingAdapterPosition()).getShop().set_favourite(false);
                    } else {
                        favList.get(getBindingAdapterPosition()).getShop().set_favourite(true);
                    }
                }
                notifyDataSetChanged();
            });
        }

        public void onItemClick(View view) {

            if (mData != null) {
                Shop shop = mData.get(getBindingAdapterPosition());
                if (mClickHandler != null)
                    mClickHandler.onShopItemClick(shop);
            } else {
                Shop favoritesShops = favList.get(getBindingAdapterPosition()).getShop();
                if (mClickHandler != null)
                    mClickHandler.onShopItemClick(favoritesShops);
            }
        }

        private void bind(Shop shop) {

            Picasso.get().load(shop.getImage()).placeholder(R.mipmap.main_logo).error(R.mipmap.main_logo).into(mBinding.shopImage);
            mBinding.shopName.setText(shop.getName());
            mBinding.distance.setText(Utils.convertKmToArabic(context, shop.getDistance_text()));
            mBinding.isOpen.setText(shop.is_working() ? context.getString(R.string.open_now) : context.getString(R.string.closed_now));
            mBinding.isOpen.setTextColor(shop.is_working() ? context.getResources().getColor(R.color.colorAccent) : context.getResources().getColor(R.color.purple_700));

            mBinding.tvRate.setText(shop.getRate());
            Picasso.get().load(shop.getLogo()).error(R.mipmap.main_logo).placeholder(R.mipmap.main_logo).into(mBinding.ivShopImage);

            mBinding.tvTime.setText(String.format("%s ", shop.getDuration_text()));

            mBinding.tvIsTraceable.setText(shop.isTraceable() ? context.getString(R.string.traceable) : context.getString(R.string.not_traceable));

            mBinding.tvMinValue.setText(String.format("%s%s", shop.getMin_order_value(), shop.getCurrency()));
            mBinding.tvMaxValue.setText(String.format("%s%s", shop.getMax_order_value(), shop.getCurrency()));
            mBinding.tvDeliveryCost.setText(String.format("%s", shop.getDelivery_cost_with_tax()));
            mBinding.tvCurrency.setText(shop.getCurrency());

            if (shop.is_favourite()) {
                mBinding.ivFavorite.setImageResource(R.drawable.ic_full_favorite);
            } else mBinding.ivFavorite.setImageResource(R.drawable.ic_favorite);

            if (shop.is_working()) {
                mBinding.shopImage.setBorderColor(context.getResources().getColor(R.color.colorAccent));
                mBinding.layoutShop.setBackground(context.getResources().getDrawable(R.drawable.stroke_back));
            } else {
                mBinding.shopImage.setBorderColor(context.getResources().getColor(R.color.red));
                mBinding.layoutShop.setBackground(context.getResources().getDrawable(R.drawable.stroke_back_for_close));
            }
        }
    }

    public interface ShopsAdapterOnClickHandler {
        void onShopItemClick(Shop shop);
    }
}