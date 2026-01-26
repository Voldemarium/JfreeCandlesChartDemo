package stepanovvv.ru.service;

import lombok.extern.slf4j.Slf4j;
import stepanovvv.ru.models.instrument_info.StockInfoDto;
import stepanovvv.ru.models.native_moex_models.candles.CandleMoex;
import stepanovvv.ru.repository.MoexRepository;
import stepanovvv.ru.strategyPanel.Timeframe;

import java.util.List;

@Slf4j
public class MoexService {
   private final MoexRepository<StockInfoDto> stockInfoDtoRepository1 = new MoexRepository<>();
   private final MoexRepository<String> stockInfoDtoRepository2 = new MoexRepository<>();
    private final MoexRepository<CandleMoex> stockInfoDtoRepository3 = new MoexRepository<>();

   ///  Получение информации об акциях с расчетом Hi2
    public List<StockInfoDto> getStocksInfoDtoForHi2() {
        return stockInfoDtoRepository1.getListByParsingJson("stocks/allStockInfoDtoForHi2", StockInfoDto.class);
    }

    //futuresSpread3/currency_future_spread_3/expirationDates
    /// Получение дат экспираций фьючерсов, участвующих в расчете 3-хногого спреда
    public List<String> getExpirationDateForCurrenciesFuturesSpread_3() {
        return stockInfoDtoRepository2.getListByParsingJson("futuresSpread3/currency_future_spread_3/expirationDates",
                String.class);
    }

    /// Получение свеч CandleMoex по составленному url
    public List<CandleMoex> getCandleMoexByUrlAndTicker(String url) {
        return stockInfoDtoRepository3.getListByParsingJson(url, CandleMoex.class);
    }

}
