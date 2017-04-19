package ru.bda.icrm.presenter.mappers;

import java.util.List;

import ru.bda.icrm.database.DBController;
import ru.bda.icrm.model.Event;
import ru.bda.icrm.model.dto.EventDTO;
import ru.bda.icrm.model.dto.EventDataDTO;
import rx.Observable;
import rx.functions.Func1;

public class EventListMapper implements Func1<EventDataDTO, List<Event>>{

    DBController dbController;

    public EventListMapper(DBController dbController) {
        this.dbController = dbController;
    }

    @Override
    public List<Event> call(EventDataDTO eventDataDTO) {
        List<Event> events = Observable.just(eventDataDTO)
                .map(eventDataDTO1 -> {
                    List<Event> list;
                    List<EventDTO> listDTO = eventDataDTO1.getData();
                    synchronized (DBController.class) {
                        list = dbController.setEventList(listDTO);
                        dbController.closeDb();
                    }
                    return list;
                })
                .toBlocking()
                .first();
        return events;
    }
}
