package stepanovvv.ru.strategyPanel;

import lombok.extern.slf4j.Slf4j;
import org.jfree.chart.util.Args;
import org.jfree.data.xy.OHLCDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MyMovingAverage {
    /// suffix - это название скользящей
    /// period - это период расчета средней скользящей в миллисек. (например для периода 20 на графике H1
    /// period = 1000 * 60 * 60)
//    public static XYDataset createMovingAverageDataSet(XYDataset source, String suffix, int period) {
//        return createMovingAverage(source, suffix, period);
//    }

    public static XYDataset createMovingAverage(XYDataset source, String suffix, int period) {
        Args.nullNotPermitted(source, "source"); // проверка на null  source
        XYSeriesCollection result = new XYSeriesCollection();
        int seriesCount = source.getSeriesCount();  // = 1 (содержится 1 серия)
        for (int i = 0; i < seriesCount; i++) {
            XYSeries s = calculateEMA(source, i, source.getSeriesKey(i) + suffix, period);
            result.addSeries(s);
        }
        return result;
    }

    public static XYDataset[] createMovingAverage_1_2(XYDataset ema_0, XYSeries stdDevValues,
                                                      String name1, String name2) {
        XYSeriesCollection result_1 = new XYSeriesCollection();
        XYSeries xySeries_1 = new XYSeries(name1);
        XYSeriesCollection result_2 = new XYSeriesCollection();
        XYSeries xySeries_2 = new XYSeries(name2);
        if (ema_0.getItemCount(0) == stdDevValues.getItemCount()) {
            for (int i = 0; i < stdDevValues.getItemCount(); i++) {
                 double value_1 = ema_0.getYValue(0, i) + stdDevValues.getY(i).doubleValue();
                 double value_2 = ema_0.getYValue(0, i) - stdDevValues.getY(i).doubleValue();
                 xySeries_1.add(ema_0.getXValue(0, i), value_1);
                 xySeries_2.add(ema_0.getXValue(0, i), value_2);
            }
            result_1.addSeries(xySeries_1);
            result_2.addSeries(xySeries_2);
        } else {
            log.error("The number of elements does not match!!!");
        }
        return new XYDataset[] {result_1, result_2};
    }


    /**
    // реализация вычисления экспоненциальной скользящей средней (EMA)
     */
    public static XYSeries calculateEMA(XYDataset source, int series, String name, int period) {
        Args.nullNotPermitted(source, "source");
        if (period < Double.MIN_VALUE) {
            throw new IllegalArgumentException("period must be positive.");
        }
        XYSeries emaValues = new XYSeries(name);

        double multiplier = 2.0 / (period + 1);

        double first_time = source.getXValue(series, 0);
        // Первое значение EMA - простое среднее за первые period значений
        double firstSMA = source.getYValue(0, 0);
        emaValues.add(first_time, firstSMA);

        // Расчет последующих значений EMA
        for (int i = period; i < source.getItemCount(series); i++) {
            double current_time = source.getXValue(series, i);
            double currentValue = source.getYValue(series, i);
            double prevEMA = emaValues.getY(emaValues.getItemCount() - 1).doubleValue();
            double currentEMA = (currentValue - prevEMA) * multiplier + prevEMA;
            emaValues.add(current_time, currentEMA);
        }
        return emaValues;
    }



    /**
     * Вычисляем экспоненциальное скользящее стандартное отклонение
     */
    public static XYSeries calculateExponentialStdDev(OHLCDataset source, XYDataset ema, String name, int period) {
        if (source.getItemCount(0) < period) {
            return new XYSeries(name);
        }
        XYSeries stdDevValues = new XYSeries(name);
//        XYDataset ema = createMovingAverage(source, suffix, period);
        double alpha = 2.0 / (period + 1);

        // Первое значение времени для стандартного отклонения
        double first_time = source.getXValue(0, period);
        // Первое значение - обычное стандартное отклонение
        List<Double> firstCurrentValues = new ArrayList<>();
        for (int i = 0; i < period; i++) {
            firstCurrentValues.add(source.getHighValue(0, i));
            firstCurrentValues.add(source.getLowValue(0, i));
        }
        double firstStdDev = calculateStdDev(firstCurrentValues);
        stdDevValues.add(first_time, firstStdDev);

        // Расчет экспоненциального стандартного отклонения
        for (int i = period; i < source.getItemCount(0); i++) {
            double currentTime = source.getXValue(0, i);
            double highDeviation = Math.abs(source.getHighValue(0, i) - ema.getYValue(0, i - period));
            double lowDeviation = Math.abs(source.getLowValue(0, i) - ema.getYValue(0, i - period));
            double prevStdDev = stdDevValues.getY(stdDevValues.getItemCount() - 1).doubleValue();
            double currentStdDev = prevStdDev + alpha * (Math.max(highDeviation, lowDeviation) - prevStdDev);
            stdDevValues.add(currentTime, currentStdDev);
        }
        return stdDevValues;
    }


    /*
    Пример: если средняя цена акции в течение года составила 70 рублей, а рассчитанное по дневным ценам закрытия
     стандартное отклонение — 2 рубля, это означает:
    68% дневных цен закрытия лежат в диапазоне стандартного отклонения — от 68 до 72 рублей;
    95% этих цен не выходят за пределы двойного среднеквадратичного отклонения — в диапазоне 66–74 рубля;
    99,5% цен дневного закрытия располагаются в пределах тройного стандартного отклонения — в границах коридора 64–76 рублей

    Стандартное отклонение обычно рассчитывается на основе заданного количества периодов (часто 20 или 30 свечей).
    В настройках индикатора можно указать, по какой цене строится индикатор (Close, Open, High и др.)
     */

    // обычное стандартное отклонение
    private static double calculateStdDev(List<Double> values) {
        double meanAverage = values.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);

        double variance = values.stream()
                .mapToDouble(value -> Math.pow(value - meanAverage, 2))
                .average()
                .orElse(0.0);
        return Math.sqrt(variance);
    }
}
