package ru.bda.icrm.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import ru.bda.icrm.R;
import ru.bda.icrm.model.Contragent;

/**
 * Created by User on 29.06.2016.
 */
public class ContragentRecyclerAdapter extends RecyclerView.Adapter<ContragentRecyclerAdapter.ViewHolder> {

    private List<Contragent> agentList;

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
    }


    @Override
    public int getItemCount() {
        return agentList.size();
    }

    public void setAgentList(List<Contragent> list) {
        this.agentList = list;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTv;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTv = (TextView) itemView.findViewById(R.id.agent_name_text);
        }
    }
}
