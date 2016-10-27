package ru.bda.icrm.listener;


import ru.bda.icrm.model.Score;

/**
 * Created by User on 26.10.2016.
 */

public interface AddScoreClickListener {
    void onLeftBtnClick();
    void onRightBtnClick(Score score);
}
