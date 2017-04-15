package ru.bda.icrm.presenter.mappers;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import ru.bda.icrm.database.DBController;
import ru.bda.icrm.model.Event;
import ru.bda.icrm.model.dto.EventDTO;
import rx.Observable;
import rx.functions.Func1;

public class EventMapper implements Func1<EventDTO, List<Event>> {

    DBController dbController;

    public EventMapper(DBController dbController) {
        this.dbController = dbController;
    }

    @Override
    public List<Event> call(EventDTO eventDTO) {
        List<Event> events = Observable.just(eventDTO)
                .map(eventDTO1 -> {
                    List<Event> list;
                    Event event = new Event(eventDTO1);
                    event.setTimeBegin(event.getTimeBegin() * 1000);
                    event.setTimeEnd(event.getTimeEnd() * 1000);
                    synchronized (DBController.class) {
                        dbController.addEventToDB(event);
                        list = dbController.getEvent();
                        dbController.closeDb();
                    }
                    return list;
                })
                .toBlocking()
                .first();
        return events;
    }
}
