package ru.kazan.currencyrateservice.service;

import jakarta.servlet.http.HttpServletRequest;

public interface ClientService {

    /**
     * Обрабрабатывает и сохраняет запрос клиента
     *
     * @param request тело запроса
     */
    void saveClient(HttpServletRequest request);
}
