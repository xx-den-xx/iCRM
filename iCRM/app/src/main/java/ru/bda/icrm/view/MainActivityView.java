package ru.bda.icrm.view;

import ru.bda.icrm.model.dto.AnswerServerDTO;

public interface MainActivityView extends View{
    void sendCallLog(AnswerServerDTO answerServer);
}
