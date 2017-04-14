package ru.bda.icrm.presenter;

import ru.bda.icrm.model.Model;
import ru.bda.icrm.model.ModelImpl;
import rx.Subscription;
import rx.subscriptions.Subscriptions;

public abstract class BasePresenter implements Presenter {

    protected Model model = new ModelImpl();
    private Subscription compositeSubscription = Subscriptions.empty();

    protected void setSubscription(Subscription subscription) {
        this.compositeSubscription = subscription;
    }

    @Override
    public void onStop() {
        if (!compositeSubscription.isUnsubscribed()) compositeSubscription.unsubscribe();
    }
}
