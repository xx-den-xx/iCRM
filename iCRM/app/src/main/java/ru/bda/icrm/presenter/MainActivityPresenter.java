package ru.bda.icrm.presenter;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import ru.bda.icrm.database.DBController;
import ru.bda.icrm.model.Token;
import ru.bda.icrm.model.dto.AnswerServerDTO;
import ru.bda.icrm.model.dto.CallDTO;
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
    private int numberRequest = 0;
    private int period = 0;
    private boolean requestComplete = true;

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

    private void sendCallList(final CallDataDTO dataDTO) {
        if (dataDTO.getCallList() != null && dataDTO.getCallList().size() > 0) {
            for (int i = 0; i < dataDTO.getCallList().size(); i++) {
                CallDataDTO data = new CallDataDTO();
                data.setToken(dataDTO.getToken());
                List<CallDTO> callList = new ArrayList<>();
                callList.add(dataDTO.getCallList().get(i));

                data.setCallList(callList);
                period = i;
                Log.d("log_call", "period = " + period + dataDTO.getCallList().get(i).toString());
                if (requestComplete) {
                    requestComplete = false;
                    callListSubscription = model.sendPhoneLog(data)
                            .subscribe(new Observer<AnswerServerDTO>() {
                                @Override
                                public void onCompleted() {
                                }

                                @Override
                                public void onError(Throwable e) {
                                    /**numberRequest = numberRequest + 1;
                                     if (numberRequest < 5) sendCallList(dataDTO);
                                     else numberRequest = 0;*/
                                    view.showError(e.toString());
                                    requestComplete = false;
                                }

                                @Override
                                public void onNext(AnswerServerDTO dto) {
                                    if (period == dataDTO.getCallList().size() - 1) {
                                        view.sendCallLog(dto);
                                        Log.d("log_call", "complete");
                                    } else {
                                        requestComplete = true;
                                    }
                                }
                            });
                }
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        setSubscription(callListSubscription);
    }

    public void updateCallList (Token token) {
        callSubscription = model.updateCallListDB(token.getToken(), token.getManager(), dbController)
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
                        view.sendComplete(dataDTO);
                    }
                });
        setSubscription(callSubscription);
    }
}
