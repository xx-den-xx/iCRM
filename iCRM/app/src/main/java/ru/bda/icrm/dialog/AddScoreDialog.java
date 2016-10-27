package ru.bda.icrm.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ru.bda.icrm.R;
import ru.bda.icrm.adapter.AddScoreAdapter;
import ru.bda.icrm.listener.AddScoreClickListener;
import ru.bda.icrm.listener.OnChangeCoastListener;
import ru.bda.icrm.model.Price;
import ru.bda.icrm.model.Score;

/**
 * Created by User on 26.10.2016.
 */

public class AddScoreDialog extends DialogFragment {

    private AddScoreClickListener listener;
    private Button mBtnClient;
    private Button mBtnProduct;
    private Button mLeftBtn;
    private Button mRightBtn;
    private TextView mTvCoast;
    private Score mScore = new Score();
    private List<Price> mPriceList = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutMangaer;
    private AddScoreAdapter mAdapter;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.MyDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        RelativeLayout root = new RelativeLayout(getActivity());
        root.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT ,
                RelativeLayout.LayoutParams.WRAP_CONTENT));
        dialog.setContentView(root);
        dialog.getWindow().setLayout( ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_add_score, container, false);
        initContent(rootView);
        return rootView;
    }

    private void initContent(View view) {
        mLeftBtn = (Button) view.findViewById(R.id.btn_left);
        mLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onLeftBtnClick();
                }
                dismiss();
            }
        });
        mRightBtn = (Button) view.findViewById(R.id.btn_right);
        mRightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    mScore.setClient("");
                    mScore.setStatus(1);
                    mScore.setPriority(1);
                    mScore.setDateAccount(Calendar.getInstance().getTimeInMillis());
                    mScore.setProductList(new ArrayList<Price>());
                    listener.onRightBtnClick(null);
                }
                dismiss();
            }
        });

        mBtnClient = (Button) view.findViewById(R.id.btn_client);
        mBtnProduct = (Button) view.findViewById(R.id.btn_products);

        mTvCoast = (TextView) view.findViewById(R.id.tv_coast);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_products);
        mLayoutMangaer = new LinearLayoutManager(getActivity());
        Price price = new Price();
        price.setTitle("\"AURA\" Ватные диски 50 шт. КК /48");
        price.setCode("УТМК0010762");
        price.setPrice("40");
        mPriceList.add(price);
        mAdapter = new AddScoreAdapter(mPriceList);
        mAdapter.setOnChangeCoastListener(new OnChangeCoastListener() {
            @Override
            public void onChangeCoast(int coast) {
                mTvCoast.setText(coast + " RUB");
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(mLayoutMangaer);
        mRecyclerView.setHasFixedSize(true);
    }

    public void show(FragmentActivity activity) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        show(fragmentManager, null);
    }

    public AddScoreDialog init (AddScoreClickListener listener) {
        this.listener = listener;
        return this;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        listener = null;
    }

    /**private class GetPriceTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }
    }*/

}
