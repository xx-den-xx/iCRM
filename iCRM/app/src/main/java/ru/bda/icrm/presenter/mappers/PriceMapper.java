package ru.bda.icrm.presenter.mappers;

import java.util.List;

import ru.bda.icrm.model.PriceSum;
import ru.bda.icrm.model.dto.NomenclatureDTO;
import ru.bda.icrm.model.dto.NomenclatureDataDTO;
import rx.Observable;
import rx.functions.Func1;

public class PriceMapper implements Func1<NomenclatureDataDTO, List<PriceSum>> {
    @Override
    public List<PriceSum> call(NomenclatureDataDTO nomenclatureDataDTO) {
        List<NomenclatureDTO> items = nomenclatureDataDTO.getNomenclature();

        List<PriceSum> prices = Observable.from(items)
                .map(itemDTO -> new PriceSum(Integer.parseInt(itemDTO.getId()), itemDTO.getCode(), itemDTO.getParent(),
                        itemDTO.getNomenclature(), itemDTO.getUnit(), itemDTO.getIsgroup().equals("1") ? true : false,
                        itemDTO.getPrice().equals("") ? 0 : Double.parseDouble(itemDTO.getPrice()),
                        itemDTO.getOstatok().equals("") ? 0 : Integer.parseInt(itemDTO.getOstatok()),
                        itemDTO.getPrice().equals("") ? 0 : Double.parseDouble(itemDTO.getPrice())))
                .toList()
                .toBlocking()
                .first();
        return prices;
    }
}
