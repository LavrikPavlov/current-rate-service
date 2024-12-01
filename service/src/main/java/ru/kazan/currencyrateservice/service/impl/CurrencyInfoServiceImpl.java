package ru.kazan.currencyrateservice.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.kazan.api.generated.model.CurrencyAllInfoResponse;
import ru.kazan.api.generated.model.CurrencyInfoResponse;
import ru.kazan.currencyrateservice.domain.mapper.CurrentEntityMapper;
import ru.kazan.currencyrateservice.repository.CurrencyEntityRepository;
import ru.kazan.currencyrateservice.service.CurrencyInfoService;

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
        return currentEntityMapper.map(main, listCurrency);
    }

    @Override
    public CurrencyInfoResponse getInfoCurrency(String main, String currency) {
        var currencyEntity = currencyEntityRepository.findByCharCode(currency)
                .orElseThrow(() -> {
                    log.error("Ошибка в получение информации о валюте: {}", currency);
                    return new IllegalArgumentException("Ошибка в получение информации о валюте: " + currency);
                });

        return currentEntityMapper.map(main, currencyEntity);
    }
}
