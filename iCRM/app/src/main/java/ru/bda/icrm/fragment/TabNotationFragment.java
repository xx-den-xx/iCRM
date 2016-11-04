package ru.bda.icrm.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import ru.bda.icrm.R;
import ru.bda.icrm.model.Score;

/**
 * Created by User on 26.10.2016.
 */

public class TabNotationFragment extends Fragment {

    private Score mScore;
    private EditText mEtConditions;
    private EditText mEtOrder;
    private EditText mEtNotation;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_notation, null);
        initContent(view);
        return view;
    }

    private void initContent(View view) {
        mEtConditions = (EditText) view.findViewById(R.id.et_conditions);
        mEtOrder = (EditText) view.findViewById(R.id.et_order);
        mEtNotation = (EditText) view.findViewById(R.id.et_notation);
        setDataContent(mScore);
    }

    private void setDataContent(Score score) {
        mEtConditions.setText(score.getInitialConditions());
        mEtOrder.setText(score.getOrderWorks());
        mEtNotation.setText(score.getAnnotation());
    }

    public void setScore(Score score) {
        this.mScore = score;
    }

    public Score getScore() {
        mScore.setInitialConditions(mEtConditions.getText().toString());
        mScore.setOrderWorks(mEtOrder.getText().toString());
        mScore.setAnnotation(mEtNotation.getText().toString());
        return mScore;
    }
}
