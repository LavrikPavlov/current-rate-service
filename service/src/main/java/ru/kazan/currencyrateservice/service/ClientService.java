package ru.kazan.currencyrateservice.service;

import jakarta.servlet.http.HttpServletRequest;
import ru.kazan.api.generated.model.UserResultDto;

public interface ClientService {

    /**
     * Обрабрабатывает и сохраняет запрос клиента
     *
     * @param request тело запроса
     */
    void saveClient(HttpServletRequest request);

    void sendUser();

    void processUser(UserResultDto dto);
}
