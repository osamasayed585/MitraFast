package com.linkpcom.mitrafast.Classes.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.linkpcom.mitrafast.Classes.REST.Models.Beans.IntroSlider;
import com.linkpcom.mitrafast.R;
import com.linkpcom.mitrafast.databinding.ListItemIntroSliderBinding;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


public class IntroSlidersAdapter extends RecyclerView.Adapter<IntroSlidersAdapter.IntroSlidersAdapterViewHolder> {
    //The list of Objects that will populate the RecyclerView
    private List<IntroSlider> mData;
    private IntroSlidersAdapterOnClickHandler mClickHandler;
    Context context;

    @Inject
    public IntroSlidersAdapter() {
    }

    @NonNull
    @Override
    public IntroSlidersAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new IntroSlidersAdapterViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_intro_slider, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull IntroSlidersAdapterViewHolder holder, int position) {
        holder.mBinding.tvTitle.setText(mData.get(position).getTitle());
        holder.mBinding.tvBody.setText(mData.get(position).getBody());

        Glide.with(context).load(mData.get(position).getImage()).placeholder(R.drawable.logo_holder).error(R.drawable.logo_holder).into(holder.mBinding.image);

        holder.mBinding.image.setVisibility(View.GONE);
        holder.mBinding.playerView.setVisibility(View.GONE);

        if (mData.get(position).getIntro_type() == 3) {
            holder.mBinding.playerView.setVisibility(View.VISIBLE);
            MediaItem mediaItem = MediaItem.fromUri(mData.get(position).getImage());
            SimpleExoPlayer player = new SimpleExoPlayer.Builder(context).build();
            player.setMediaItem(mediaItem);
            player.prepare();
            holder.mBinding.playerView.setPlayer(player);
            player.play();
        } else {
            holder.mBinding.image.setVisibility(View.VISIBLE);
            Glide
                    .with(context) // replace with 'this' if it's in activity
                    .load(mData.get(position).getImage())
                    .into(holder.mBinding.image);
        }

    }

    @Override
    public int getItemCount() {
        if (null == mData) return 0;
        return mData.size();
    }

    public void setData(List<IntroSlider> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public class IntroSlidersAdapterViewHolder extends RecyclerView.ViewHolder {
        ListItemIntroSliderBinding mBinding;

        public IntroSlidersAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            mBinding = ListItemIntroSliderBinding.bind(itemView);
            itemView.setOnClickListener(this::onItemClick);
        }

        public void onItemClick(View view) {
            if (mClickHandler != null)
                mClickHandler.onIntroSliderItemClick(mData.get(getAdapterPosition()));
        }
    }

    //The interface that will be implemented later in parent activity
    public interface IntroSlidersAdapterOnClickHandler {
        void onIntroSliderItemClick(IntroSlider introSlider);
    }

    public void setItemClickListener(IntroSlidersAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }
}