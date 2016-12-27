package ru.bda.icrm.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import ru.bda.icrm.R;
import ru.bda.icrm.listener.OnObjectClickListener;
import ru.bda.icrm.model.ClientObject;

/**
 * Created by User on 22.12.2016.
 */

public class ObjectContragentAdapter extends RecyclerView.Adapter<ObjectContragentAdapter.ViewHolder> {

    private List<ClientObject> mList;
    private OnObjectClickListener clickListener;

    public ObjectContragentAdapter(List<ClientObject> mList) {
        this.mList = mList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.object_item_adapter, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ClientObject object = mList.get(position);
        holder.tvObjectName.setText(object.getName());
        holder.llObject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.objectClickListener(object);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setListObject(List<ClientObject> mList) {
        this.mList = mList;
    }

    public void setOnObjectClickListener(OnObjectClickListener clickListener) {
        this.clickListener = clickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvObjectName;
        LinearLayout llObject;

        public ViewHolder(View itemView) {
            super(itemView);
            tvObjectName = (TextView) itemView.findViewById(R.id.tv_object_name);
            llObject = (LinearLayout) itemView.findViewById(R.id.ll_object);
        }
    }
}
