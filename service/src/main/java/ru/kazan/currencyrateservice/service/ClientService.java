package ru.kazan.currencyrateservice.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface ClientService {

    /**
     * Обрабрабатывает и сохраняет запрос клиента
     *
     * @param request тело запроса
     */
    void saveClient(HttpServletRequest request);
}
