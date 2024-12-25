package ru.kazan.currencyrateservice.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.kazan.api.generated.model.CurrencyAllInfoResponse;
import ru.kazan.api.generated.model.CurrencyInfoResponse;
import ru.kazan.currencyrateservice.domain.CurrencyEntity;
import ru.kazan.currencyrateservice.domain.mapper.CurrentEntityMapper;
import ru.kazan.currencyrateservice.repository.CurrencyEntityRepository;
import ru.kazan.currencyrateservice.service.CalculationCurrencyService;
import ru.kazan.currencyrateservice.service.CurrencyInfoService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CurrencyInfoServiceImpl implements CurrencyInfoService {

    private final CurrencyEntityRepository currencyEntityRepository;
    private final CurrentEntityMapper currentEntityMapper;
    private final CalculationCurrencyService calculationCurrencyService;


    @Override
    public CurrencyAllInfoResponse getAllInfoCurrency(String main) {
        var listCurrency = currencyEntityRepository.findAll();
        if (main.equals("RUB"))
            return currentEntityMapper.map(main, listCurrency);

        return getAllAnotherMainCurrency(listCurrency, main);
    }

    @Override
    public CurrencyInfoResponse getInfoCurrency(String main, String currency) {
        var currentCurrency = getCurrentCurrency(currency);
        if (main.equals("RUB"))
            return currentEntityMapper.map(main, currentCurrency);

        return getAnotherMainCurrency(currentCurrency, main);

    }

    private CurrencyInfoResponse getAnotherMainCurrency(CurrencyEntity currency, String main) {
        var mainCurrency = getCurrentCurrency(main);
        var result = calculationCurrencyService.relativeMainCurrency(currency, mainCurrency);
        return currentEntityMapper.map(main, result);
    }

    private CurrencyAllInfoResponse getAllAnotherMainCurrency(List<CurrencyEntity> list, String main) {
        var currentCurrency = getCurrentCurrency(main);
        var listCurrency = list.parallelStream()
                .map(currency -> calculationCurrencyService.relativeMainCurrency(currency, currentCurrency))
                .toList();

        return currentEntityMapper.map(main, listCurrency);
    }


    private CurrencyEntity getCurrentCurrency(String currency) {
        return currencyEntityRepository.findByCharCode(currency)
                .orElseThrow(() -> {
                    log.error("Ошибка в получение информации о валюте: {}", currency);
                    return new IllegalArgumentException("Ошибка в получение информации о валюте: " + currency);
                });
    }
}
