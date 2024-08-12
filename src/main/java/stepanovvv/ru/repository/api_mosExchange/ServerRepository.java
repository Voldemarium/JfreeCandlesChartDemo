package stepanovvv.ru.repository.api_mosExchange;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;
import stepanovvv.ru.models.StockMoex;

import java.util.List;

@Slf4j
@org.springframework.stereotype.Repository
public class ServerRepository {
    private final RestClient restClient;
    // Адрес для запроса к контроллеру основного (серверного) приложения
    private final String myServerUrl = "http://localhost:8080/";

    public ServerRepository() {
        this.restClient = RestClient.create();
    }

    // Загрузка списка всех акций, по которым Мосбиржа ведет расчет метрик HI2
    public List<StockMoex> getStocksForHi2() {
     return getStocks("allStocksForHi2");
    }

    // Общий метод загрузки нужной информации по акции
    public List<StockMoex> getStocks(String endUrl) {
        ResponseEntity<List<StockMoex>> response = restClient
                .get()                                             // Метод GET
                .uri(myServerUrl + "stocks/" + endUrl)
                .retrieve()                                        // получение ответа
                .toEntity(new ParameterizedTypeReference<>() {});  // десериализация ответа в параметризованный тип List
        return response.getBody();
    }

}



