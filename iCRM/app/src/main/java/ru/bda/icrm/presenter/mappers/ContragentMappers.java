package ru.bda.icrm.presenter.mappers;

import java.util.List;

import ru.bda.icrm.model.Contragent;
import ru.bda.icrm.model.dto.ContragentDataDTO;
import ru.bda.icrm.model.dto.ContragentItemDTO;
import rx.Observable;
import rx.functions.Func1;

public class ContragentMappers implements Func1<ContragentDataDTO, List<Contragent>> {

    @Override
    public List<Contragent> call(ContragentDataDTO contragentDataDTOs) {
        List<ContragentItemDTO> items = contragentDataDTOs.getData();
        List<Contragent> contragents = Observable.from(items)
                .map(itemDTO -> new Contragent(itemDTO))
                .toList()
                .toBlocking()
                .first();
        return contragents;
    }
}
