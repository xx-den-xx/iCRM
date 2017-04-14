package ru.bda.icrm.model.api;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import ru.bda.icrm.model.LocationObject;
import ru.bda.icrm.model.dto.AnswerServerDTO;
import ru.bda.icrm.model.dto.ContragentDataDTO;
import ru.bda.icrm.model.Token;
import ru.bda.icrm.model.dto.SearchContragentDTO;
import ru.bda.icrm.model.dto.TakeContragentDTO;
import rx.Observable;

public interface ApiInterface {

    @GET("api.php?")
    Observable<Token> auth(@Query("action") String action, @Query("login") String login, @Query("pass") String pass);

    @POST("api.php?action=getContragentList")
    Observable<ContragentDataDTO> getContragentList(@Body TakeContragentDTO dto);

    @POST("api.php?action=getContragentList")
    Observable<ContragentDataDTO> searchContragentList(@Body SearchContragentDTO dto);

    @POST("api.php?action=setGeoPoint")
    Observable<AnswerServerDTO> setGeoPoint (@Body LocationObject location);


}
