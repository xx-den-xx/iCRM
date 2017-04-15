package ru.bda.icrm.view;

import java.util.List;

import ru.bda.icrm.model.Contragent;

public interface MapFragmentView extends View {
    void loadContragents(List<Contragent> contragents);
}
