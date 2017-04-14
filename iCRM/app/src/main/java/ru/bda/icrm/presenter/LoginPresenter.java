package ru.bda.icrm.presenter;

import ru.bda.icrm.model.Token;
import ru.bda.icrm.view.LoginActivityView;
import rx.Observer;
import rx.Subscription;
import rx.subscriptions.Subscriptions;

public class LoginPresenter extends BasePresenter{

    private LoginActivityView view;

    private String login;
    private String pass;
    private Subscription subscriptionToken = Subscriptions.empty();

    public LoginPresenter(LoginActivityView view, String login, String pass) {
        this.view = view;
        this.login = login;
        this.pass = pass;
    }

    public void loadData() {
        subscriptionToken = model.auth("auth", login, pass)
                .subscribe(new Observer<Token>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showError(e.toString());
                    }

                    @Override
                    public void onNext(Token token) {
                        view.saveToken(token);
                    }
                });
        setSubscription(subscriptionToken);
    }
}
