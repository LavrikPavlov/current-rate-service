package ru.kazan.currencyrateservice.service;

import ru.kazan.api.generated.model.CurrencyAllInfoResponse;
import ru.kazan.api.generated.model.CurrencyInfoResponse;

public interface CurrencyInfoService {
    /**
     * Получение всей информации о валютах и курсах
     *
     * @return response
     */
    CurrencyAllInfoResponse getAllInfoCurrency(String main);

    /**
     * Получить информацию об одной валюте и курсе
     *
     * @return response
     */
    CurrencyInfoResponse getInfoCurrency(String main, String currency);
}
