package ru.kazan.currencyrateservice.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import ru.kazan.api.generated.model.CurrencyAllInfoResponse;
import ru.kazan.api.generated.model.CurrencyInfoResponse;
import ru.kazan.currencyrateservice.domain.CurrencyEntity;
import ru.kazan.currencyrateservice.domain.mapper.CurrentEntityMapper;
import ru.kazan.currencyrateservice.repository.CurrencyEntityRepository;
import ru.kazan.currencyrateservice.service.CurrencyInfoService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CurrencyInfoServiceImpl implements CurrencyInfoService {

    private final CurrencyEntityRepository currencyEntityRepository;
    private final CurrentEntityMapper currentEntityMapper;


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
        var result = relativeMainCurrency(currency, mainCurrency);
        return currentEntityMapper.map(main, result);
    }

    private CurrencyAllInfoResponse getAllAnotherMainCurrency(List<CurrencyEntity> list, String main) {
        var currentCurrency = getCurrentCurrency(main);
        var listCurrency = list.parallelStream()
                .filter(currency -> currency.getNominal() == 1L)
                .map(currency -> relativeMainCurrency(currency, currentCurrency))
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

    private CurrencyEntity relativeMainCurrency(CurrencyEntity currency, CurrencyEntity mainCurrency) {

        if (checkInfoCurrency(currency.getValue(), mainCurrency.getValue())) {
            log.error("Информация о валюте не имеет достаточно ифнормации \n {}",
                    currency.getValue() == null ? currency : mainCurrency
            );
            throw new IllegalArgumentException("Одно из значений валюты или основной валюты не найдено");
        }

        var newCurrency = new CurrencyEntity();
        BeanUtils.copyProperties(currency, newCurrency);

        var changeValue = getCurrentCurrencyAndNominal(
                mainCurrency.getValue(), currency.getValue(), mainCurrency.getNominal(), currency.getNominal()
        );

        BigDecimal lastUpdateValue = null;
        if(!checkInfoCurrency(currency.getPrevious(), mainCurrency.getPrevious())) {
            lastUpdateValue = getCurrentCurrencyAndNominal(
                    mainCurrency.getValue(), currency.getValue(), mainCurrency.getNominal(), currency.getNominal()
            );
        }

        newCurrency.setValue(changeValue);
        newCurrency.setPrevious(lastUpdateValue);

        return newCurrency;
    }

    private boolean checkInfoCurrency(BigDecimal currency, BigDecimal mainCurrency) {
        return Objects.isNull(currency) || Objects.isNull(mainCurrency);
    }


    private BigDecimal getCurrentCurrencyAndNominal(BigDecimal main, BigDecimal other,
                                                    Long mainNominal, Long otherNominal){

        if (mainNominal > 1 || otherNominal > 1)
            throw new IllegalArgumentException("Сервис пока не обрабатывает валюты с номиналами");


        /*
            BigDecimal first = mainNominal > 1 ? main.multiply(new BigDecimal(mainNominal)) : main;
            BigDecimal second = otherNominal > 1 ? other.multiply(new BigDecimal(otherNominal)) : other;
        */

        return main.divide(other, 10, RoundingMode.HALF_UP);
    }
}
