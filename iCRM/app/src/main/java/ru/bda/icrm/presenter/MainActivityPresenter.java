package ru.bda.icrm.presenter;

import ru.bda.icrm.database.DBController;
import ru.bda.icrm.model.Token;
import ru.bda.icrm.model.dto.AnswerServerDTO;
import ru.bda.icrm.model.dto.CallDataDTO;
import ru.bda.icrm.view.MainActivityView;
import rx.Observer;
import rx.Subscription;
import rx.subscriptions.Subscriptions;

public class MainActivityPresenter extends BasePresenter {

    private MainActivityView view;
    private Subscription callSubscription = Subscriptions.empty();
    private Subscription callListSubscription = Subscriptions.empty();
    private DBController dbController;

    public MainActivityPresenter(MainActivityView view, DBController dbController) {
        this.view = view;
        this.dbController = dbController;
    }

    public void getCallLog(Token token) {
        callSubscription = model.setCallListToDB(token.getToken(), token.getManager(), dbController)
                .subscribe(new Observer<CallDataDTO>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showError(e.toString());
                    }

                    @Override
                    public void onNext(CallDataDTO dataDTO) {
                        sendCallList(dataDTO);
                    }
                });
        setSubscription(callSubscription);
    }

    private void sendCallList(CallDataDTO dataDTO) {
        callListSubscription = model.sendPhoneLog(dataDTO)
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
                        view.sendCallLog(dto);
                    }
                });
        setSubscription(callListSubscription);
    }

}
