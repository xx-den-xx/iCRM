package ru.bda.icrm.model;

import ru.bda.icrm.model.dto.AnswerServerDTO;
import ru.bda.icrm.model.dto.ContragentDataDTO;
import ru.bda.icrm.model.dto.SearchContragentDTO;
import ru.bda.icrm.model.dto.TakeContragentDTO;
import rx.Observable;

public interface Model {
    Observable<Token> auth(String action, String login, String pass);
    Observable<ContragentDataDTO> getContragentList(TakeContragentDTO dto);
    Observable<ContragentDataDTO> searchContragentList(SearchContragentDTO dto);
    Observable<AnswerServerDTO> setGeoPoint(LocationObject location);
}
