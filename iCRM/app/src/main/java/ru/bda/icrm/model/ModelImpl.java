package ru.bda.icrm.model;

import ru.bda.icrm.database.DBController;
import ru.bda.icrm.model.api.ApiInterface;
import ru.bda.icrm.model.api.ApiModule;
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
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ModelImpl implements Model {

    private ApiInterface apiInterface = ApiModule.getApiInterface();

    @Override
    public Observable<Token> auth(String action, String login, String pass) {
        return apiInterface.auth(action, login, pass)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<ContragentDataDTO> getContragentList(TakeContragentListDTO dto) {
        return apiInterface.getContragentList(dto)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<ContragentDataDTO> getContragentListMap(Token token) {
        return apiInterface.getContragentListMap(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<ContragentDataDTO> searchContragentList(SearchContragentDTO dto) {
        return apiInterface.searchContragentList(dto)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<AnswerServerDTO> setGeoPoint(LocationObject location) {
        return apiInterface.setGeoPoint(location)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<EventDTO> updateEvent(EventDTO eventDTO) {
        return apiInterface.updateEvent(eventDTO)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    @Override
    public Observable<EventDataDTO> getEventList(Token token) {
        return apiInterface.getEventList(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<EventDTO> deleteEvent(EventDTO eventDTO) {
        return apiInterface.updateEvent(eventDTO)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<NomenclatureDataDTO> getNomenclatureTree(TakeNomenclatureDTO dto) {
        return apiInterface.getNomenclatureTree(dto)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<AnswerServerDTO> getNomenclatureTreeAnswer(TakeNomenclatureDTO dto) {
        return apiInterface.getNomenclatureTreeAnswer(dto)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<AnswerServerDTO> sendPhoneLog(CallDataDTO calls) {
        return apiInterface.sendPhoneLog(calls)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<CallDataDTO> setCallListToDB(final String token, final String id, final DBController dbController) {
        Observable<CallDataDTO> observable;
        observable = Observable.create(
                (Observable.OnSubscribe<CallDataDTO>) subscriber -> {
                    subscriber.onNext(new CallDataDTO(token, id, dbController.getCallForServer()));
                    subscriber.onCompleted();
                })
                .observeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io());
        return observable;
    }

    @Override
    public Observable<AnswerServerDTO> getContragent(String token, String id) {
        return apiInterface.getContragent(token, id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }
}
