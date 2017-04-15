package ru.bda.icrm.presenter;

import android.content.Context;
import android.util.Log;

import java.util.List;

import ru.bda.icrm.database.DBController;
import ru.bda.icrm.model.Event;
import ru.bda.icrm.model.Token;
import ru.bda.icrm.model.dto.AnswerServerDTO;
import ru.bda.icrm.model.dto.EventDTO;
import ru.bda.icrm.presenter.mappers.EventMapper;
import ru.bda.icrm.view.EventFragmentView;
import rx.Observer;
import rx.Subscription;
import rx.subscriptions.Subscriptions;

public class EventFragmentPresenter extends BasePresenter{

    private EventFragmentView view;

    private Subscription eventSubscription = Subscriptions.empty();
    private Subscription eventListSubscription = Subscriptions.empty();

    private EventMapper eventMapper;
    private DBController dbController;

    public EventFragmentPresenter(EventFragmentView view, Context context) {
        this.view = view;
        dbController = new DBController(context);
        eventMapper = new EventMapper(dbController);
    }

    public void makeEvent(EventDTO eventDTO) {
        view.startProgress();
        Log.d("log_event", eventDTO.toString());

        eventSubscription = model.updateEvent(eventDTO)
                .map(eventMapper)
                .subscribe(new Observer<List<Event>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("log_event", e.toString());
                        view.showError(e.toString());
                    }

                    @Override
                    public void onNext(List<Event> events) {
                        view.makeEvent(events);
                    }
                });
        setSubscription(eventSubscription);

        /**eventSubscription = model.updateEventAnswer(eventDTO)
                .subscribe(new Observer<AnswerServerDTO>() {
                    @Override
                    public void onCompleted() {
                        Log.d("log_event", "update complete!!!!");
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showError(e.toString());
                        Log.e("log_event", e.toString());
                    }

                    @Override
                    public void onNext(AnswerServerDTO dto) {
                        Log.d("log_event", "answer server " + dto.getState());
                        view.showError(dto.getState());
                    }
                });
        setSubscription(eventSubscription);*/
    }

    private void saveEvent(Event event) {

    }

    public void getEventList(Token token) {
        //eventListSubscription = model.getEventList();
    }
}
