package ru.bda.icrm.presenter;

import ru.bda.icrm.model.dto.AnswerServerDTO;
import ru.bda.icrm.view.ContragentActivityView;
import rx.Observer;
import rx.Subscription;
import rx.subscriptions.Subscriptions;

public class ContragentActivityPresenter extends BasePresenter{

    private ContragentActivityView view;
    private Subscription contragentSubscription = Subscriptions.empty();

    public ContragentActivityPresenter(ContragentActivityView view) {
        this.view = view;
    }

    public void loadContragent(String token, String id) {
        contragentSubscription = model.getContragent(token, id)
                .subscribe(new Observer<AnswerServerDTO>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showError(e.toString());
                    }

                    @Override
                    public void onNext(AnswerServerDTO dto) {
                        view.dataContragent();
                    }
                });
        setSubscription(contragentSubscription);
    }
}
