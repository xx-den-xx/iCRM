package ru.bda.icrm.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import java.util.List;

import ru.bda.icrm.R;
import ru.bda.icrm.listener.OnPhotoClickListener;
import ru.bda.icrm.model.Photo;

/**
 * Created by User on 27.12.2016.
 */

public class PhotoRecyclerAdapter extends RecyclerView.Adapter<PhotoRecyclerAdapter.ViewHolder> {

    private Context mContext;
    private List<Photo> mPhotoList;
    private OnPhotoClickListener listener;

    public PhotoRecyclerAdapter(Context mContext, List<Photo> mPhotoList) {
        this.mContext = mContext;
        this.mPhotoList = mPhotoList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.photo_adapter_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Photo photo = mPhotoList.get(position);
        int widhtScreen =  ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
        int changedWidth = widhtScreen / 3;
        holder.ivPhoto.setMaxWidth(changedWidth);
        holder.ivPhoto.setMaxHeight(changedWidth);
        //holder.llPhoto.setLayoutParams(new LinearLayout.LayoutParams(changedWidth, changedWidth));
        Picasso.with(mContext).load(photo.getLink())
                .centerCrop()
                .resize(changedWidth, changedWidth)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(holder.ivPhoto);
        holder.ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onPhotoClick(photo);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPhotoList.size();
    }

    public void setOnPhotoClickListener(OnPhotoClickListener listener) {
        this.listener = listener;
    }

    public void setPhotoList(List<Photo> photos) {
        this.mPhotoList = photos;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivPhoto;
        public LinearLayout llPhoto;
        public ViewHolder(View itemView) {
            super(itemView);
            ivPhoto = (ImageView) itemView.findViewById(R.id.iv_photo);
            llPhoto = (LinearLayout) itemView.findViewById(R.id.ll_feed_adapter);
        }
    }
}
