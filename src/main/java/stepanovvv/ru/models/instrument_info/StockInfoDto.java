package stepanovvv.ru.models.instrument_info;

public record StockInfoDto(String secId,
                           String stockMoexShortName,
                           Integer stockMoexListLevel,
                           Double prevValue,
                           Double prevVolume) {
}