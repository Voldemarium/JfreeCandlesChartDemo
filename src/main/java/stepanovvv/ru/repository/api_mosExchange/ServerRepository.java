package stepanovvv.ru.repository.api_mosExchange;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;
import stepanovvv.ru.models.FutureMoex;
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

    // Загрузка списка всех фьючерсоа, по которым Мосбиржа ведет расчет метрик HI2
    public List<FutureMoex> getFuturesForHi2() {
        return getFutures("allFuturesForHi2");
    }

    // Общий метод загрузки нужной информации по акциям
    public List<StockMoex> getStocks(String endUrl) {
        ResponseEntity<List<StockMoex>> response = restClient
                .get()                                             // Метод GET
                .uri(myServerUrl + "stocks/" + endUrl)
                .retrieve()                                        // получение ответа
                .toEntity(new ParameterizedTypeReference<>() {
                });  // десериализация ответа в параметризованный тип List
        return response.getBody();
    }

    // Общий метод загрузки нужной информации по фьючерсам
    public List<FutureMoex> getFutures(String endUrl) {
        ResponseEntity<List<FutureMoex>> response = restClient
                .get()                                             // Метод GET
                .uri(myServerUrl + "futures/" + endUrl)
                .retrieve()                                        // получение ответа
                .toEntity(new ParameterizedTypeReference<>() {
                });  // десериализация ответа в параметризованный тип List
        return response.getBody();
    }

}



