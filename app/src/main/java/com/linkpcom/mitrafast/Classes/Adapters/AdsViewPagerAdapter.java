package com.linkpcom.mitrafast.Classes.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.viewpager.widget.PagerAdapter;

import com.squareup.picasso.Picasso;
import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Ad;
import com.linkpcom.mitrafast.R;
import com.linkpcom.mitrafast.databinding.ActivityAdSlideBinding;

import java.util.List;

public class AdsViewPagerAdapter extends PagerAdapter {

    Context mContext;
    List<Ad> sliders;

    ActivityAdSlideBinding mBinding;
    ItemClickListener itemClickListener;
    // ClickHandler Build
    private LayoutInflater layoutInflater;

    public AdsViewPagerAdapter(Context mContext) {
        this.mContext = mContext;
        layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        mBinding = ActivityAdSlideBinding.inflate(layoutInflater);

        Picasso.get().load(sliders.get(position).getImage()).placeholder(R.drawable.logo_holder).error(R.drawable.logo_holder).into(mBinding.adImage);

        container.addView(mBinding.getRoot());

        mBinding.adImage.setOnClickListener(v -> {
            if (itemClickListener != null)
                itemClickListener.onItemClick(sliders.get(position));
        });

        return mBinding.getRoot();

    }

    @Override
    public int getCount() {
        if (sliders == null) return 0;
        return sliders.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }

    public void setData(List<Ad> sliders) {
        this.sliders = sliders;
        notifyDataSetChanged();
    }

    public interface ItemClickListener {
        void onItemClick(Ad ad);
    }
}

