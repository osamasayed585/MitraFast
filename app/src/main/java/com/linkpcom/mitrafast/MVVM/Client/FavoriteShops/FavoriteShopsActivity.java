package com.linkpcom.mitrafast.MVVM.Client.FavoriteShops;

import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.INTENTS.OBJECT;

import android.content.Intent;
import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;

import com.linkpcom.mitrafast.Classes.Adapters.ShopsAdapter;
import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Shop;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.ShopsRequest;
import com.linkpcom.mitrafast.MVVM.$Base.BaseActivity;
import com.linkpcom.mitrafast.MVVM.Client.ShopDetails.ShopDetailsActivity;
import com.linkpcom.mitrafast.databinding.ActivityFavoriteOrdersBinding;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;


@AndroidEntryPoint
public class FavoriteShopsActivity extends BaseActivity {

    private ActivityFavoriteOrdersBinding mBinding;
    private FavoriteViewModel viewModel;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityFavoriteOrdersBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        viewModel = new ViewModelProvider(this).get(FavoriteViewModel.class);

        ShopsAdapter favoriteAdapter = new ShopsAdapter();
        mBinding.Favorites.setAdapter(favoriteAdapter);
        compositeDisposable.add(favoriteAdapter.isFavoriteClicked.subscribe(this::isFavoriteUnClicked));
        favoriteAdapter.setItemClickListener(this::onItemClicked);

        viewModel.requestFavorites(ShopsRequest.builder()
                .lat(preferencesManager.getLat())
                .lng(preferencesManager.getLng())
                .build()).observe(this, response -> {
            favoriteAdapter.addFavoritesShops(response.getData());
        });
        Timber.d("osAndroid this is  a get count %s",favoriteAdapter.getItemCount());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    private void isFavoriteUnClicked(Shop shop) {
        if (shop.is_favourite()) {

        } else {

        }
    }

    private void onItemClicked(Shop favoritesShops) {
        startActivity(new Intent(this, ShopDetailsActivity.class).putExtra(OBJECT, favoritesShops));
    }

}