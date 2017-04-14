package ru.bda.icrm.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import ru.bda.icrm.R;
import ru.bda.icrm.listener.AddContragentClickListener;
import ru.bda.icrm.listener.OnContragentClickListener;
import ru.bda.icrm.model.Contragent;

public class ContragentRecyclerAdapter extends RecyclerView.Adapter<ContragentRecyclerAdapter.ViewHolder> {

    private List<Contragent> agentList;
    private OnContragentClickListener listener;
    private AddContragentClickListener addListener;

    public ContragentRecyclerAdapter(List<Contragent> list) {
        this.agentList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contragent_adapter_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Contragent contragent = agentList.get(position);
        holder.nameTv.setText(contragent.getNameContragent());
        holder.llItem.setOnClickListener(v -> {
            if (listener != null) {
                listener.onContragentClick(contragent.getId());

            }
            if (addListener != null) {
                addListener.addContragentListener(contragent);
            }
        });
        if (contragent.getLon() != 0 && contragent.getLat() != 0) {
            holder.ivMap.setVisibility(View.VISIBLE);
        } else {
            holder.ivMap.setVisibility(View.GONE);
        }
    }


    @Override
    public int getItemCount() {
        return agentList.size();
    }

    public void setAgentList(List<Contragent> list) {
        this.agentList = list;
    }

    public void setOnContragentClickListener (OnContragentClickListener listener) {
        this.listener = listener;
    }

    public void addContragentClickListener(AddContragentClickListener addListener) {
        this.addListener = addListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTv;
        public LinearLayout llItem;
        public ImageView ivMap;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTv = (TextView) itemView.findViewById(R.id.agent_name_text);
            llItem = (LinearLayout) itemView.findViewById(R.id.ll_item);
            ivMap = (ImageView) itemView.findViewById(R.id.iv_map);
        }
    }
}
