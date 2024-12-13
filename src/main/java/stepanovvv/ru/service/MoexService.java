package stepanovvv.ru.service;

import lombok.extern.slf4j.Slf4j;
import stepanovvv.ru.models.instrument_info.StockInfoDto;
import stepanovvv.ru.repository.MoexRepository;

import java.util.List;

@Slf4j
public class MoexService {
   final MoexRepository<StockInfoDto> stockInfoDtoRepository = new MoexRepository<>();

    public List<StockInfoDto> getStocksInfoDtoForHi2() {
        return stockInfoDtoRepository.getDto("stocks/allStockInfoDtoForHi2", StockInfoDto.class);

    }
}
