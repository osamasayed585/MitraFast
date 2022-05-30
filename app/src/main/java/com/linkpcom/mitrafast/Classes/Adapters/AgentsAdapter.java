package com.linkpcom.mitrafast.Classes.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Agent;
import com.linkpcom.mitrafast.R;
import com.linkpcom.mitrafast.databinding.ListItemAgentBinding;

import java.util.ArrayList;
import java.util.List;


public class AgentsAdapter extends RecyclerView.Adapter<AgentsAdapter.AgentsAdapterViewHolder> {

    //The list of Objects that will populate the RecyclerView
    private List<Agent> mData = new ArrayList<>();
    private List<String> mDurationData = new ArrayList<>();
    private List<String> mServiceCostData = new ArrayList<>();

    // ClickHandler Build
    private AcceptTechnicianOnClickHandler mClickHandler;


    public AgentsAdapter() {

    }




    public void setItemClickListener(AcceptTechnicianOnClickHandler clickHandler) {
        mClickHandler = clickHandler;

    }

    @NonNull
    @Override
    public AgentsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.list_item_agent;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new AgentsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AgentsAdapterViewHolder holder, int position) {

        Picasso.get().load(mData.get(position).getImage()).placeholder(R.drawable.logo_holder).error(R.drawable.logo_holder).into(holder.mBinding.agentImage);
        holder.mBinding.agentName.setText(mData.get(position).getName());
        holder.mBinding.agentRate.setStar(mData.get(position).getRate());

        if (position < mDurationData.size()) {
            if (mDurationData.get(position) != null)
                holder.mBinding.arrivalTime.setText(mDurationData.get(position));
        }
        if (position < mServiceCostData.size()) {
            if (mServiceCostData.get(position) != null)
                holder.mBinding.deliveryCost.setText(String.format("%s %s", mServiceCostData.get(position), mData.get(position).getCountry().getCurrency().getName()));

        }

    }

    @Override
    public int getItemCount() {
        if (null == mData) return 0;
        return mData.size();
    }

    public void setData(List<Agent> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public void appendData(Agent data) {
        mData.add(data);
        notifyDataSetChanged();
    }

    public void addOrderDuration(String duration) {
        mDurationData.add(duration);
        notifyDataSetChanged();
    }

    public void addAgentServiceCost(String serviceCost) {
        mServiceCostData.add(serviceCost);
        notifyDataSetChanged();
    }

    //The interface that will be implemented later in parent activity
    public interface AcceptTechnicianOnClickHandler {
        void onAcceptAgentClick(int acceptedAgentId,String agentOfferCost);

        void onRejectAgentClick(int rejectedTechnicianId);

    }

    public class AgentsAdapterViewHolder extends RecyclerView.ViewHolder {

        //get an Instance for dataBinding
        ListItemAgentBinding mBinding;


        public AgentsAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

        mBinding = ListItemAgentBinding.bind(itemView);
            mBinding.btnAccept.setOnClickListener(this::onAcceptAgentClick);
            mBinding.btnReject.setOnClickListener(this::onRejectAgentClick);

        }

        public void onAcceptAgentClick(View view) {
            int adapterPosition = getAdapterPosition();
            Agent acceptedAgent = mData.get(adapterPosition);
            //Data passed when clicked
            if (mClickHandler != null)
                mClickHandler.onAcceptAgentClick(acceptedAgent.getId(),mServiceCostData.get(adapterPosition));
        }

        public void onRejectAgentClick(View view) {
            int adapterPosition = getAdapterPosition();
            Agent rejectedAgent = mData.get(adapterPosition);
            //Data passed when clicked
            if (mClickHandler != null)
                mClickHandler.onRejectAgentClick(rejectedAgent.getId());
            mData.remove(adapterPosition);
            notifyItemRemoved(adapterPosition);
        }

    }

}
