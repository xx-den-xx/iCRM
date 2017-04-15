package ru.bda.icrm.presenter;

import java.util.List;

import ru.bda.icrm.model.Contragent;
import ru.bda.icrm.model.Token;
import ru.bda.icrm.presenter.mappers.ContragentMappers;
import ru.bda.icrm.view.MapFragmentView;
import rx.Observer;
import rx.Subscription;
import rx.subscriptions.Subscriptions;

public class MapFragmentPresenter extends BasePresenter {

    private MapFragmentView view;

    private Subscription mapSubscription = Subscriptions.empty();

    private ContragentMappers contragentMappers = new ContragentMappers();

    public MapFragmentPresenter(MapFragmentView view) {
        this.view = view;
    }

    public void loadData(Token token) {
        mapSubscription = model.getContragentListMap(token)
                .map(contragentMappers)
                .subscribe(new Observer<List<Contragent>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<Contragent> list) {
                        view.loadContragents(list);
                    }
                });
        setSubscription(mapSubscription);
    }
}
