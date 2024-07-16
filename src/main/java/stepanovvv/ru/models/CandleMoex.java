package stepanovvv.ru.models;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CandleMoex {
    private Double open;
    private Double close;
    private Double high;
    private Double low;
    private Double value;
    private Double volume;
    private LocalDateTime begin;
    private LocalDateTime end;

    private double buyMetric;
    private double sellMetric;

}
