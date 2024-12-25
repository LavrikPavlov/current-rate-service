package ru.kazan.currencyrateservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import ru.kazan.currencyrateservice.domain.CurrencyEntity;
import ru.kazan.currencyrateservice.service.CalculationCurrencyService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class CalculationCurrencyServiceImpl implements CalculationCurrencyService {


    @Override
    public CurrencyEntity relativeMainCurrency(CurrencyEntity currency, CurrencyEntity mainCurrency) {
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
        if (!checkInfoCurrency(currency.getPrevious(), mainCurrency.getPrevious())) {
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
                                                    Long mainNominal, Long otherNominal) {

        BigDecimal adjustedMain = main.divide(BigDecimal.valueOf(mainNominal), 10, RoundingMode.HALF_UP);
        BigDecimal adjustedOther = other.divide(BigDecimal.valueOf(otherNominal), 10, RoundingMode.HALF_UP);

        return adjustedOther.divide(adjustedMain, 10, RoundingMode.HALF_UP);
    }
}
