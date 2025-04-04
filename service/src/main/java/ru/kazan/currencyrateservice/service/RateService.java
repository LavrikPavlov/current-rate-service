package ru.kazan.currencyrateservice.service;

import ru.kazan.api.generated.model.CurrencyResultDto;

public interface RateService {

    /**
     * Обновления курса валют
     */
    void updateRates();

    void sendRates();

    void processRates(CurrencyResultDto dto);
}
