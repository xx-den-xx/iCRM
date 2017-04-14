package ru.bda.icrm.model;

import ru.bda.icrm.model.api.ApiInterface;
import ru.bda.icrm.model.api.ApiModule;
import ru.bda.icrm.model.dto.AnswerServerDTO;
import ru.bda.icrm.model.dto.ContragentDataDTO;
import ru.bda.icrm.model.dto.SearchContragentDTO;
import ru.bda.icrm.model.dto.TakeContragentDTO;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ModelImpl implements Model {

    public ApiInterface apiInterface = ApiModule.getApiInterface();

    @Override
    public Observable<Token> auth(String action, String login, String pass) {
        return apiInterface.auth(action, login, pass)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<ContragentDataDTO> getContragentList(TakeContragentDTO dto) {
        return apiInterface.getContragentList(dto)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<ContragentDataDTO> searchContragentList(SearchContragentDTO dto) {
        return apiInterface.searchContragentList(dto)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<AnswerServerDTO> setGeoPoint(LocationObject location) {
        return apiInterface.setGeoPoint(location)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
