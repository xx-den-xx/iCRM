package ru.bda.icrm.model;

import retrofit2.http.Query;
import ru.bda.icrm.database.DBController;
import ru.bda.icrm.model.dto.AnswerServerDTO;
import ru.bda.icrm.model.dto.CallDataDTO;
import ru.bda.icrm.model.dto.ContragentDataDTO;
import ru.bda.icrm.model.dto.EventDTO;
import ru.bda.icrm.model.dto.EventDataDTO;
import ru.bda.icrm.model.dto.NomenclatureDataDTO;
import ru.bda.icrm.model.dto.SearchContragentDTO;
import ru.bda.icrm.model.dto.TakeContragentListDTO;
import ru.bda.icrm.model.dto.TakeNomenclatureDTO;
import rx.Observable;

public interface Model {
    Observable<Token> auth(String action, String login, String pass);
    Observable<ContragentDataDTO> getContragentList(TakeContragentListDTO dto);
    Observable<ContragentDataDTO> getContragentListMap(Token token);
    Observable<ContragentDataDTO> searchContragentList(SearchContragentDTO dto);
    Observable<AnswerServerDTO> setGeoPoint(LocationObject location);
    Observable<EventDTO> updateEvent(EventDTO eventDTO);
    Observable<EventDataDTO> getEventList(Token token);
    Observable<EventDTO> deleteEvent(EventDTO eventDTO);
    Observable<NomenclatureDataDTO> getNomenclatureTree(TakeNomenclatureDTO dto);
    Observable<AnswerServerDTO> getNomenclatureTreeAnswer(TakeNomenclatureDTO dto);
    Observable<AnswerServerDTO> sendPhoneLog(CallDataDTO calls);
    Observable<CallDataDTO> setCallListToDB(String token, String id, DBController dbController);
    Observable<CallDataDTO> updateCallListDB(String token, String id, DBController dbController);
    Observable<AnswerServerDTO> getContragent(String token, String id);
}
