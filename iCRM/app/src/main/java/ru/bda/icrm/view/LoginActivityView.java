package ru.bda.icrm.view;

import ru.bda.icrm.model.Token;

public interface LoginActivityView extends View{
    void saveToken(Token token);
}
