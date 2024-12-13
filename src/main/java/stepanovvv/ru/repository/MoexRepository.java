package stepanovvv.ru.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
public class MoexRepository<T extends Record> {
    private final ObjectMapper mapper = new ObjectMapper();
    private final RestClient restClient;
    // Адрес для запроса к контроллеру основного (серверного) приложения (переместить в параметры запуска
    // приложения)
    private final String myServerUrl = "http://localhost:8080/";

    public MoexRepository() {
        this.restClient = RestClient.create();
    }

    public List<T> getDto(String endUrl, Class<T> clazz) {
        Object[] body = getResponseBody(endUrl);
        return Collections.unmodifiableList(getParsingJsonToList(body, clazz));
    }

    // Парсинг объектов из JSON
    public List<T> getParsingJsonToList(Object[] body, Class<T> clazz) {
        // десериализация ответа
        return Arrays.stream(body).map(o -> mapper.convertValue(o, clazz)).toList();
    }

    // расшифровка ответа сервиса ApiMoex на запрос
    public Object[] getResponseBody(String endUrl) {
        String fullUrl = myServerUrl + endUrl;
        ResponseEntity<Object[]> response = restClient
                .get()                                             // Метод GET
                .uri(fullUrl)
                .retrieve()                                        // получение ответа
                .toEntity(new ParameterizedTypeReference<>() {
                });  // десериализация ответа в параметризованный тип List
        Object[] objects = response.getBody();
        assert objects != null;
        if (objects.length == 0) {
            log.error("NO CONTENT Objects on My Server, url: {}", fullUrl);
        }
        return objects;
    }

}



