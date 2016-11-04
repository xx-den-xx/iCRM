package ru.bda.icrm.listener;

import ru.bda.icrm.model.Contact;

/**
 * Created by User on 15.09.2016.
 */
public interface AddClientClickListener {

    void onLeftBtnClick();
    void onRightBtnClick(Contact clients);
}
