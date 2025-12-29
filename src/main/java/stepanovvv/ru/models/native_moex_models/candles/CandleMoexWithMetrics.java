package stepanovvv.ru.models.native_moex_models.candles;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CandleMoexWithMetrics {
    private CandleMoex candleMoex;
    private double buyMetric;
    private double sellMetric;

}
