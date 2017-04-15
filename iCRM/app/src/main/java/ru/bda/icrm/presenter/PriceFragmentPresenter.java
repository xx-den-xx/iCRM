package ru.bda.icrm.presenter;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ru.bda.icrm.model.PriceSum;
import ru.bda.icrm.model.dto.AnswerServerDTO;
import ru.bda.icrm.model.dto.NomenclatureDTO;
import ru.bda.icrm.model.dto.NomenclatureDataDTO;
import ru.bda.icrm.model.dto.TakeNomenclatureDTO;
import ru.bda.icrm.presenter.mappers.PriceMapper;
import ru.bda.icrm.view.PriceFragmentView;
import rx.Observer;
import rx.Subscription;
import rx.subscriptions.Subscriptions;

public class PriceFragmentPresenter extends BasePresenter {

    private PriceFragmentView view;

    private Subscription priceSubscription = Subscriptions.empty();
    private PriceMapper mapper = new PriceMapper();

    public PriceFragmentPresenter(PriceFragmentView view) {
        this.view = view;
    }

    public void loadPrice(TakeNomenclatureDTO dto) {
        view.startProgress();
        priceSubscription = model.getNomenclatureTree(dto)
                .map(mapper)
                .subscribe(new Observer<List<PriceSum>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showError(e.toString());
                        Log.d("log_price", e.getMessage());
                    }

                    @Override
                    public void onNext(List<PriceSum> list) {
                        view.loadPriceList(list);
                    }
                });
        setSubscription(priceSubscription);

        /**priceSubscription = model.getNomenclatureTreeAnswer(dto)
                //.map(mapper)
                .subscribe(new Observer<AnswerServerDTO>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showError(e.toString());
                        Log.d("log_price", e.getMessage());
                    }

                    @Override
                    public void onNext(AnswerServerDTO list) {
                        view.loadPriceList(new ArrayList<>());
                    }
                });
        setSubscription(priceSubscription);*/
    }
}
