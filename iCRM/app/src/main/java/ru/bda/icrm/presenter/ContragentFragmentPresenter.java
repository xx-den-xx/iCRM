package ru.bda.icrm.presenter;

import java.util.List;

import ru.bda.icrm.model.Contragent;
import ru.bda.icrm.model.dto.SearchContragentDTO;
import ru.bda.icrm.model.dto.TakeContragentDTO;
import ru.bda.icrm.presenter.mappers.ContragentMappers;
import ru.bda.icrm.view.ContragentFragmentView;
import rx.Observer;
import rx.Subscription;
import rx.subscriptions.Subscriptions;

public class ContragentFragmentPresenter extends BasePresenter {

    private ContragentFragmentView view;

    private String token;

    private Subscription contragentSubscription = Subscriptions.empty();

    private ContragentMappers contragentMappers = new ContragentMappers();

    public ContragentFragmentPresenter(ContragentFragmentView view, String token) {
        this.view = view;
        this.token = token;
    }

    public void loadData(TakeContragentDTO dto) {
        view.startProgress();
        contragentSubscription = model.getContragentList(dto)
                .map(contragentMappers)
                .subscribe(new Observer<List<Contragent>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showError(e.toString());
                    }

                    @Override
                    public void onNext(List<Contragent> list) {
                        view.loadList(list);
                    }
                });
        setSubscription(contragentSubscription);
    }

    public void searchData(SearchContragentDTO dto) {
        view.startProgress();
        contragentSubscription = model.searchContragentList(dto)
                .map(contragentMappers)
                .subscribe(new Observer<List<Contragent>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showError(e.toString());
                    }

                    @Override
                    public void onNext(List<Contragent> list) {
                        view.searchList(list);
                    }
                });
        setSubscription(contragentSubscription);
    }
}
