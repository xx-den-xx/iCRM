package ru.bda.icrm.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import ru.bda.icrm.R;
import ru.bda.icrm.listener.OnClickScoreListener;
import ru.bda.icrm.model.Score;

/**
 * Created by User on 19.10.2016.
 */

public class RecyclerScoreAdapter extends RecyclerView.Adapter<RecyclerScoreAdapter.ViewHolder>{

    private List<Score> mListScore;
    private OnClickScoreListener listener;

    public RecyclerScoreAdapter(List<Score> scores) {
        this.mListScore = scores;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.score_adapter_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Score score = mListScore.get(position);
        String numberAccount = score.getNumberAccount();
        String nameClient = score.getClient();
        String summ = score.getSumScore() + " RUB";
        String date = getDate(score.getDateAccount(), "dd.MM.yyyy");
        holder.tvCompanyTitle.setText(nameClient + ", " + numberAccount);
        holder.tvDate.setText(date);
        holder.tvCoast.setText(summ);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickItemScore(score);
            }
        });
    }

    private String getDate(long milliSeconds, String dateFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        //formatter.setTimeZone(TimeZone.getTimeZone("GMS"));
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    @Override
    public int getItemCount() {
        return mListScore.size();
    }

    public void setListScore (List<Score> scores) {
        this.mListScore = scores;
    }

    public void setOnClickScoreListener(OnClickScoreListener listener) {
        this.listener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvCompanyTitle;
        public TextView tvDate;
        public TextView tvCoast;

        public ViewHolder(View itemView) {
            super(itemView);
            tvCompanyTitle = (TextView) itemView.findViewById(R.id.tv_company_title);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
            tvCoast = (TextView) itemView.findViewById(R.id.tv_coast);
        }
    }
}
