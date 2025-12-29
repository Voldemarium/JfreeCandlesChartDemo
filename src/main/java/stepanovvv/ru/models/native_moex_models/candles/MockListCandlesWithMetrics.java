package stepanovvv.ru.models.native_moex_models.candles;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class MockListCandlesWithMetrics {
    List<CandleMoexWithMetrics> candleMoexWithMetricsList;

    public MockListCandlesWithMetrics() {
        CandleMoex candleMoex1 = CandleMoex.builder()
                .begin(LocalDateTime.of(2024, 7, 4, 10, 0))
                .volume(104.32)
                .close(301.1)
                .open(298.5)
                .high(301.4)
                .low(298.0)
                .build();
        CandleMoex candleMoex2 = CandleMoex.builder()
                .begin(LocalDateTime.of(2024, 7, 5, 10, 0))
                .volume(154.1)
                .close(302.1)
                .open(299.5)
                .high(302.4)
                .low(297.0)
                .build();
        CandleMoex candleMoex3 = CandleMoex.builder()
                .begin(LocalDateTime.of(2024, 7, 8, 10, 0))
                .volume(115.1)
                .close(290.1)
                .open(302.5)
                .high(303.4)
                .low(287.0)
                .build();
        CandleMoex candleMoex4 = CandleMoex.builder()
                .begin(LocalDateTime.of(2024, 7, 9, 10, 0))
                .volume(135.1)
                .close(290.1)
                .open(302.5)
                .high(303.4)
                .low(287.0)
                .build();

        this.candleMoexWithMetricsList = List.of(
                CandleMoexWithMetrics.builder()
                        .candleMoex(candleMoex1)
                        .buyMetric(200.5)
                        .sellMetric(456.3)
                        .build(),
                CandleMoexWithMetrics.builder()
                        .candleMoex(candleMoex2)
                        .buyMetric(178.5)
                        .sellMetric(234.0)
                        .build(),
                CandleMoexWithMetrics.builder()
                        .candleMoex(candleMoex3)
                        .buyMetric(278.5)
                        .sellMetric(189.6)
                        .build(),
                CandleMoexWithMetrics.builder()
                        .candleMoex(candleMoex4)
                        .buyMetric(456.5)
                        .sellMetric(345.3)
                        .build()

        );
    }
}
