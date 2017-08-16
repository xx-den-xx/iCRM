package ru.bda.icrm.view;

import ru.bda.icrm.model.dto.AnswerServerDTO;
import ru.bda.icrm.model.dto.CallDataDTO;

public interface MainActivityView extends View{
    void sendCallLog(AnswerServerDTO answerServer);
    void sendComplete(CallDataDTO dataDTO);
}
