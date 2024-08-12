package stepanovvv.ru;

import lombok.extern.slf4j.Slf4j;
import stepanovvv.ru.models.StockMoex;
import stepanovvv.ru.repository.api_mosExchange.ServerRepository;

import java.util.List;

@Slf4j
public class TestService {
    public static void main(String[] args) {
        ServerRepository repository = new ServerRepository();
//        StockMoex[] stocks = repository.getStocksForHi2();
        List<StockMoex> stocks = repository.getStocksForHi2();
//        log.info("stocks size = {}", stocks.length);
        log.info("stocks size = {}", stocks.size());
    }
}
