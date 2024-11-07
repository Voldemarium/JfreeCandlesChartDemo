package stepanovvv.ru.models.native_moex_models.instrument_info;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.time.LocalDate;

// Названия всех колонок по акциям:
// https://iss.moex.com/iss/engines/stock/markets/shares/boards/TQBR/securities/columns.html
// Пример для акций ВТБ-------------------------------------
// https://iss.moex.com/iss/engines/stock/markets/shares/boards/TQBR/securities/VTBR.html
@JsonPropertyOrder({ // порядок при сериализации
        "SECID",
        "SHORTNAME",
        "SECNAME",
        "LISTLEVEL",
        "LOTSIZE",
        "MINSTEP",
        "DECIMALS",
        "VALUE",
        "VOLUME",
        "DATE"
})
@Data
public class StockMoex {
    @JsonProperty("SECID")
    String secId;
    @JsonProperty("SHORTNAME")
    String shortName;          // Краткое наименование ценной бумаги
    @JsonProperty("SECNAME")
    String secName;            // Полное наименование финансового инструмента
    @JsonProperty("LISTLEVEL")
    Integer listLevel;         // Уровень листинга
    @JsonProperty("LOTSIZE")
    Integer lotSize;           // Размер лота (Количество ценных бумаг в одном стандартном лоте)
    @JsonProperty("MINSTEP")
    Double minStep;            // Мин. шаг цены
    @JsonProperty("DECIMALS")
    Integer decimals;          // Точность, знаков после запятой

    // дополнительные поля (берутся из последних свеч D1)
    @JsonProperty("VALUE")
    private Double prevValue;           // объем торгов  в рублях за последний день
    @JsonProperty("VOLUME")
    private Double prevVolume;           // объем торгов в лотах за последний день
    @JsonProperty("DATE")
    private LocalDate prevDateTrade; // последний день торгов
}
