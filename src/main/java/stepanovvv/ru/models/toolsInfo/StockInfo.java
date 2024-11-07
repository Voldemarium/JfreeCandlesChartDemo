package stepanovvv.ru.models.toolsInfo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import stepanovvv.ru.models.native_moex_models.instrument_info.StockMoex;

import java.time.LocalDate;

@Data
public class StockInfo{
    StockMoex stockMoex;

    // дополнительные поля (берутся из последних свеч D1)
    @JsonProperty("VALUE")
    private Double prevValue;           // объем торгов в рублях за последний день
    @JsonProperty("VOLUME")
    private Double prevVolume;           // объем торгов в лотах за последний день
    @JsonProperty("DATE")
    private LocalDate prevDateTrade; // последний день торгов
}
