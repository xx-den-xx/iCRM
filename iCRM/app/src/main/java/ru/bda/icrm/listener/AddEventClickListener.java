package ru.bda.icrm.listener;

import ru.bda.icrm.model.Event;

/**
 * Created by User on 06.10.2016.
 */

public interface AddEventClickListener {

    void onLeftBtnClick();
    void onRightBtnClick(Event event);
}
