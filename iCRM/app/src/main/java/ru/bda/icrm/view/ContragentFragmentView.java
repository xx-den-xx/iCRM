package ru.bda.icrm.view;

import java.util.List;

import ru.bda.icrm.model.Contragent;
import ru.bda.icrm.model.dto.ContragentItemDTO;

public interface ContragentFragmentView extends FragmentView {
    void loadList(List<Contragent> list);
    void searchList(List<Contragent> list);
}
