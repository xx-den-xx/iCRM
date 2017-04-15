package ru.bda.icrm.view;

import java.util.List;

import ru.bda.icrm.model.PriceSum;

public interface PriceFragmentView extends FragmentView{
    void loadPriceList(List<PriceSum> list);
}
