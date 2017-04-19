package ru.bda.icrm.model.api;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import ru.bda.icrm.model.LocationObject;
import ru.bda.icrm.model.dto.AnswerServerDTO;
import ru.bda.icrm.model.dto.CallDTO;
import ru.bda.icrm.model.dto.CallDataDTO;
import ru.bda.icrm.model.dto.ContragentDataDTO;
import ru.bda.icrm.model.Token;
import ru.bda.icrm.model.dto.EventDTO;
import ru.bda.icrm.model.dto.EventDataDTO;
import ru.bda.icrm.model.dto.NomenclatureDataDTO;
import ru.bda.icrm.model.dto.SearchContragentDTO;
import ru.bda.icrm.model.dto.TakeContragentListDTO;
import ru.bda.icrm.model.dto.TakeNomenclatureDTO;
import rx.Observable;

public interface ApiInterface {

    @GET("api.php?")
    Observable<Token> auth(@Query("action") String action, @Query("login") String login, @Query("pass") String pass);

    @POST("api.php?action=getContragentList")
    Observable<ContragentDataDTO> getContragentList(@Body TakeContragentListDTO dto);

    @POST("api.php?action=getContragentList")
    Observable<ContragentDataDTO> getContragentListMap(@Body Token token);

    @POST("api.php?action=getContragentList")
    Observable<ContragentDataDTO> searchContragentList(@Body SearchContragentDTO dto);

    @POST("api.php?action=setGeoPoint")
    Observable<AnswerServerDTO> setGeoPoint(@Body LocationObject location);

    @POST("api.php?action=getEvent")
    Observable<EventDataDTO> getEventList(@Body Token token);

    @POST("api.php?action=updateEvent")
    Observable<EventDTO> updateEvent(@Body EventDTO eventDTO);

    @POST("api.php?action=deleteEvent")
    Observable<EventDTO> deleteEvent(@Body EventDTO eventDTO);

    @POST("api.php?action=getNomenclature")
    Observable<NomenclatureDataDTO> getNomenclatureTree(@Body TakeNomenclatureDTO dto);

    @POST("api.php?action=getNomenclature")
    Observable<AnswerServerDTO> getNomenclatureTreeAnswer(@Body TakeNomenclatureDTO dto);

    @POST("api.php?action=phoneLog")
    Observable<AnswerServerDTO> sendPhoneLog(@Body CallDataDTO calls);
}
