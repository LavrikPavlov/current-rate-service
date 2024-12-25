package ru.kazan.currencyrateservice.service;

import ru.kazan.currencyrateservice.domain.CurrencyEntity;

/**
 * Сервис для калькуляции курсов валют относительного нового
 */
public interface CalculationCurrencyService {

    /**
     * Калькуляция нового курса валют относительно главногой валюты
     *
     * @param currency расчитываемая валюта
     * @param mainCurrency главная валюта
     * @return обработанная валюта
     */
    CurrencyEntity relativeMainCurrency(CurrencyEntity currency, CurrencyEntity mainCurrency);
}
