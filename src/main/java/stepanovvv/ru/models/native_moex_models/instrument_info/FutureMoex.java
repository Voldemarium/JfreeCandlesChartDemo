package stepanovvv.ru.models.native_moex_models.instrument_info;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@JsonPropertyOrder({ // порядок при сериализации
        "SECID",
        "SHORTNAME",
        "DECIMALS",
        "MINSTEP",
        "LASTTRADEDATE",
        "LASTDELDATE",
        "ASSETCODE",
        "PREVOPENPOSITION",
        "LOTVOLUME",
        "INITIALMARGIN",
        "HIGHLIMIT",
        "LOWLIMIT",
        "STEPPRICE",
        "IMTIME",

        "BUYSELLFEE",
        "SCALPERFEE",
        "EXERCISEFEE",

        "VALUE",
        "VOLUME",
        "DATE"
})

// Список всех торгуемых на данный момент фьючерсов
// https://iss.moex.com//iss/statistics/engines/futures/markets/forts/series.html
// Названия всех колонок по фьючерсам
// https://iss.moex.com/iss/engines/futures/markets/forts/securities/columns.html
// Данные по Hi2  для фьючерсов за конкретную дату
// https://iss.moex.com/iss/datashop/algopack/fo/hi2.json?date=2024-08-13&iss.meta=off&iss.only=data&iss.json=extended
// Пример данных для фьючерса РТС - RIU4
//https://iss.moex.com/iss/engines/futures/markets/forts/securities/RIU4.html
@Data
public class FutureMoex {

    // -------------- Информация из секции "securities"-------------------------------------------
    @JsonProperty("SECID")
    String secId;
    @JsonProperty("SHORTNAME")
    String shortName;                 // Краткое наименование ценной бумаги
    @JsonProperty("DECIMALS")
    Integer decimals;                 // Точность, знаков после запятой
    @JsonProperty("MINSTEP")
    Double minStep;                   // Мин. шаг цены
    @JsonProperty("LASTTRADEDATE")
    private LocalDate lastTradeDate; // последний день торгов (часто совпадает с днем экпирации фьючерса)
    @JsonProperty("LASTDELDATE")
    private LocalDate lastDelDate;   // день экпирации (исполнения) фьючерса
    @JsonProperty("ASSETCODE")
    String assetCode;                 // Код базового актива

    @JsonProperty("PREVOPENPOSITION")
    Integer prevOpenPosition;         // Открытые позиции предыдущего дня, контрактов
    @JsonProperty("LOTVOLUME")
    Integer lotVolume;                // К-во единиц базового актива в инструменте
    @JsonProperty("INITIALMARGIN")
    Double initialMargin;             // Гарантийное обеспечение на первом уровне лимита концентрации** (ГО, руб.)
    /*
    С учетом объединенных площадок (если Единый счет у брокера) в залог для открытия позиции могут идти:
    - Акции;- Иностранные валюты; - Облигации федеральных займов (ОФЗ)
     */
    @JsonProperty("HIGHLIMIT")
    Double highLimit;             // Верхний лимит цены. При достижении ценой контракта «планки» и нахождении на ней
    // в течение определенного времени размер ГО может сильно увеличиться
    @JsonProperty("LOWLIMIT")
    Double lowLimit;             // Нижний лимит цены. При достижении ценой контракта «планки» и нахождении на ней
    // в течение определенного времени размер ГО может сильно увеличиться
    @JsonProperty("STEPPRICE")
    Double stepPrice;             // Стоимость шага цены
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonProperty("IMTIME")
    private LocalDateTime imTime;   // Время данных по ГО

    @JsonProperty("BUYSELLFEE")
    Double buySellFee;             // Сбор за регистрацию сделки*, руб
    @JsonProperty("SCALPERFEE")
    Double scalperFee;             // Сбор за скальперскую сделку*, руб.
    /*
    Скальперская скидка применяется к сделкам, приводящим коткрытию противоположных позиции в течение одного торгового
     дня.Размер скидки – 50% от величины биржевых сборов за скальперские сделки
     Сделка или часть сделки, которая уменьшает позицию,тарифицируются по нулевой ставке.
    Пример: В течение торговой сессии фьючерс продали и купили. Сделки по покупке ипродаже являются скальперскими
   Распределение комиссии по сделкам:
   • за первую сделку = 1,25руб.
   • за вторую сделку = 0 руб
     */
    @JsonProperty("EXERCISEFEE")
    Double exerciseFee;             // Клиринговая комиссия за исполнение контракта*, руб.

    //---------------------дополнительные поля (берутся из последних свеч D1)-----------------------------------
    @JsonProperty("VALUE")
    private Double prevValue;           // объем торгов в рублях за последний день
    @JsonProperty("VOLUME")
    private Double prevVolume;           // объем торгов в лотах за последний день
    @JsonProperty("DATE")
    private LocalDate prevDateTrade; // последний день торгов

}
