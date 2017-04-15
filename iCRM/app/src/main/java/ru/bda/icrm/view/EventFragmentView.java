package ru.bda.icrm.view;

import java.util.List;

import ru.bda.icrm.model.Event;

public interface EventFragmentView extends FragmentView {
    void makeEvent(List<Event> listEvent);
    void updateEvent(Event event);
    void deleteEvent(Event event);
}
