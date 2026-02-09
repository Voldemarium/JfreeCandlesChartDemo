package stepanovvv.ru.strategyPanel;

import lombok.extern.slf4j.Slf4j;
import org.jfree.chart.util.Args;
import org.jfree.data.xy.OHLCDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import stepanovvv.ru.models.WeekNumberAndYear;
import stepanovvv.ru.tools.Median;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.*;

@Slf4j
public class MyMovingAverage {
//    private static final int numberForMedian = 13; // кол-во предыдущих элементов (свечей)
    // для медианного сглаживания - для того, чтобы игнорировать резкие ценовые выбросы

    /// name - это название скользящей
    /// period - это период расчета средней скользящей - кол-во свечей

    /// Метод для вычисления EMA по заданному периоду (D1)
    public static XYDataset createMovingAverageTimeD1(XYDataset source, String name) {
        Args.nullNotPermitted(source, "source"); // проверка на null  source
        XYSeriesCollection result = new XYSeriesCollection();
        int seriesCount = source.getSeriesCount();  // = 1 (содержится 1 серия)
        int series = 0;
        if (seriesCount > 1) {
            log.error("seriesCount > 1");
        }
        // Вычисляем кол-во свеч в каждом дне
        LinkedHashMap<LocalDate, Integer> numberOfCandlesPerDayMap = countingNumberCandlesPerDay(source, series);
        LocalDate firstLocalDate = numberOfCandlesPerDayMap.entrySet().iterator().next().getKey();

        LocalDate prevDay = null;
        List<Double> valuesByPeriod = new ArrayList<>();
        XYSeries emaValues = new XYSeries(name);
        int period;
        double multiplier = 0;

        for (int i = 0; i < source.getItemCount(series); i++) {
            double current_time = source.getXValue(series, i);
            double currentValue = source.getYValue(series, i);
            Date currentDate = new Date(Math.round(current_time));
            LocalDate currentLocalDate = new Timestamp(currentDate.getTime()).toLocalDateTime().toLocalDate();
            if (i == 0) {
                prevDay = currentLocalDate;
                valuesByPeriod.add(currentValue);
            } else {
                if (currentLocalDate.isAfter(prevDay)) {
                    period = numberOfCandlesPerDayMap.get(prevDay);
                    multiplier = 2.0 / (period + 1);
                    if (prevDay.isEqual(firstLocalDate)) {
                        // Первое значение EMA - простое среднее за первые period значений
//                    double firstSMA1 = valuesByPeriod.stream().mapToDouble(Double::doubleValue).average().orElseThrow();
                        // Первое значение EMA - медиана за первые period значений
                        double firstSMA = Median.calculateMedian(valuesByPeriod);
                        emaValues.add(current_time, firstSMA);
                        valuesByPeriod.clear();
                    } else {
                        /// Если это первая свеча нового дня
                        double prevEMA = emaValues.getY(emaValues.getItemCount() - 1).doubleValue();
                        double currentEMA = (currentValue - prevEMA) * multiplier + prevEMA;
                        emaValues.add(current_time, currentEMA);
                    }
                    prevDay = currentLocalDate;

                } else if (currentLocalDate.equals(prevDay)) {
                    if (prevDay.isEqual(firstLocalDate)) {
                        valuesByPeriod.add(currentValue);
                    }
                    if (currentLocalDate.isAfter(firstLocalDate)) {
                        // Расчет последующих значений EMA
                        double prevEMA = emaValues.getY(emaValues.getItemCount() - 1).doubleValue();
                        /// Применим медианное сглаживание для того, чтобы игнорировать резкие ценовые выбросы
                        // выборка из текущего и 5 предыдущих значений
//                        List<Double> valuesForMedian = new ArrayList<>();
//                        valuesForMedian.add(currentValue);
//                        for (int j = 1; j <= numberForMedian; j++) {
//                            valuesForMedian.add(source.getYValue(series, i - j));
//                        }
//                        double currentMedianValue = Median.calculateMedian(valuesForMedian);
                        double currentEMA = (currentValue - prevEMA) * multiplier + prevEMA;
//                        double currentEMA = (currentMedianValue - prevEMA) * multiplier + prevEMA;
                        emaValues.add(current_time, currentEMA);
                    }
                } else {
                    log.error("incorrect LocalDate!!!");
                }
            }
        }
        result.addSeries(emaValues);
        return result;
    }

    /// Метод для вычисления EMA по заданному периоду (W)
    public static XYDataset createMovingAverageTimeW(XYDataset source, String name) {
        Args.nullNotPermitted(source, "source"); // проверка на null  source
        XYSeriesCollection result = new XYSeriesCollection();
        int seriesCount = source.getSeriesCount();  // = 1 (содержится 1 серия)
        int series = 0;
        if (seriesCount > 1) {
            log.error("seriesCount > 1");
        }
        // Вычисляем кол-во свеч в каждом дне
        LinkedHashMap<LocalDate, Integer> numberOfCandlesPerDayMap = countingNumberCandlesPerDay(source, series);
        // Вычисляем кол-во свеч в каждой неделе
        LinkedHashMap<WeekNumberAndYear, Integer> numberOfCandlesPerWeekMap = countingNumberCandlesOfWeeks(numberOfCandlesPerDayMap);
        WeekNumberAndYear firstWeekNumberAndYear = numberOfCandlesPerWeekMap.entrySet().iterator().next().getKey();

        WeekNumberAndYear prevWeekNumberAndYear = null;
        List<Double> valuesByPeriod = new ArrayList<>();
        XYSeries emaValues = new XYSeries(name);
        int period;
        double multiplier = 0;
        for (int i = 0; i < source.getItemCount(series); i++) {
            double current_time = source.getXValue(series, i);
            double currentValue = source.getYValue(series, i);
            Date currentDate = new Date(Math.round(current_time));
            LocalDate currentLocalDate = new Timestamp(currentDate.getTime()).toLocalDateTime().toLocalDate();
            int currentWeekNumber = currentLocalDate.get(WeekFields.ISO.weekOfWeekBasedYear());
            int currentYearOfWeek = currentLocalDate.get(WeekFields.ISO.weekBasedYear());
            WeekNumberAndYear currentWeekNumberAndYear = new WeekNumberAndYear(currentWeekNumber, currentYearOfWeek);
            if (i == 0) {
                prevWeekNumberAndYear = currentWeekNumberAndYear;
                valuesByPeriod.add(currentValue);
            } else {
                if (!currentWeekNumberAndYear.equals(prevWeekNumberAndYear)) {
                    period = numberOfCandlesPerWeekMap.get(prevWeekNumberAndYear);
                    multiplier = 2.0 / (period + 1);
                    if (prevWeekNumberAndYear.equals(firstWeekNumberAndYear)) {
                        // Первое значение EMA - простое среднее за первые period значений
//                    double firstSMA1 = valuesByPeriod.stream().mapToDouble(Double::doubleValue).average().orElseThrow();
                        // Первое значение EMA - медиана за первые period значений
                        double firstSMA = Median.calculateMedian(valuesByPeriod);
                        emaValues.add(current_time, firstSMA);
                        valuesByPeriod.clear();
                    } else {
                        /// Если это первая свеча нового дня
                        double prevEMA = emaValues.getY(emaValues.getItemCount() - 1).doubleValue();
                        double currentEMA = (currentValue - prevEMA) * multiplier + prevEMA;
                        emaValues.add(current_time, currentEMA);
                    }
                    prevWeekNumberAndYear = currentWeekNumberAndYear;
                } else {
                    if (prevWeekNumberAndYear.equals(firstWeekNumberAndYear)) {
                        valuesByPeriod.add(currentValue);
                    }
                    if (!currentWeekNumberAndYear.equals(firstWeekNumberAndYear)) {
                        // Расчет последующих значений EMA
                        /// Применим медианное сглаживание для того, чтобы игнорировать резкие ценовые выбросы
                        // выборка из текущего и 5 предыдущих значений
//                        List<Double> valuesForMedian = new ArrayList<>();
//                        valuesForMedian.add(currentValue);
//                        for (int j = 1; j <= numberForMedian; j++) {
//                            valuesForMedian.add(source.getYValue(series, i - j));
//                        }
//                        double currentMedianValue = Median.calculateMedian(valuesForMedian);
                        double prevEMA = emaValues.getY(emaValues.getItemCount() - 1).doubleValue();
                        double currentEMA = (currentValue - prevEMA) * multiplier + prevEMA;
//                        double currentEMA = (currentMedianValue- prevEMA) * multiplier + prevEMA;
                        emaValues.add(current_time, currentEMA);
                    }
                }
            }
        }
        result.addSeries(emaValues);
        return result;
    }

    /// Вычисляем кол-во свеч в каждой неделе
    private static LinkedHashMap<WeekNumberAndYear, Integer> countingNumberCandlesOfWeeks(
            LinkedHashMap<LocalDate, Integer> numberOfCandlesPerDayMap) {
        LinkedHashMap<WeekNumberAndYear, Integer> numberOfCandlesPerWeekMap = new LinkedHashMap<>();
        final WeekNumberAndYear[] prevWeekNumberAndYear = new WeekNumberAndYear[1];

        final int[] numberOfCandlesOfWeek = {0};
        numberOfCandlesPerDayMap.forEach((key, value) -> {
            int weekNumber = key.get(WeekFields.ISO.weekOfWeekBasedYear());
            int yearOfWeek = key.get(WeekFields.ISO.weekBasedYear());
            if (prevWeekNumberAndYear[0] == null) {
                prevWeekNumberAndYear[0] = new WeekNumberAndYear(weekNumber, yearOfWeek);
                numberOfCandlesOfWeek[0] += value;
            } else if (weekNumber > prevWeekNumberAndYear[0].getNumberOfWeek() ||
                    (weekNumber == 1 && weekNumber < prevWeekNumberAndYear[0].getNumberOfWeek())) {
//                numberOfCandlesPerWeekMap.put(prevWeekNumberAndYear[0], numberOfCandlesOfWeek[0]);
                numberOfCandlesPerWeekMap.put(new WeekNumberAndYear(prevWeekNumberAndYear[0]), numberOfCandlesOfWeek[0]);
                prevWeekNumberAndYear[0].setNumberOfWeek(weekNumber);
                prevWeekNumberAndYear[0].setYear(yearOfWeek);
//                prevWeekNumberAndYear[0].set(new WeekNumberAndYear(weekNumber, yearOfWeek));
                numberOfCandlesOfWeek[0] = value;
            } else if (weekNumber == prevWeekNumberAndYear[0].getNumberOfWeek()) {
                numberOfCandlesOfWeek[0] += value;
            } else {
                log.error("Incorrect number of week!!");
            }
        });
        return numberOfCandlesPerWeekMap;
    }

    /// Вычисляем кол-во свеч в каждом дне
    private static LinkedHashMap<LocalDate, Integer> countingNumberCandlesPerDay(XYDataset source, int series) {
        LinkedHashMap<LocalDate, Integer> numberOfCandlesPerDayMap = new LinkedHashMap<>();
        LocalDate prevDay = null;
        int numberOfCandles = 0;
        for (int i = 0; i < source.getItemCount(series); i++) {
            double current_time = source.getXValue(series, i);
            Date currentDate = new Date(Math.round(current_time));
            LocalDate currentLocalDate = new Timestamp(currentDate.getTime()).toLocalDateTime().toLocalDate();
            if (i == 0) {
                prevDay = currentLocalDate;
                numberOfCandles++;
            } else {
                if (currentLocalDate.equals(prevDay)) {
                    numberOfCandles++;
                } else {
                    numberOfCandlesPerDayMap.put(prevDay, numberOfCandles);
                    prevDay = currentLocalDate;
                    numberOfCandles = 1;
                }
            }
        }
        return numberOfCandlesPerDayMap;
    }


    /// Метод для вычисления обычной EMA по заданному периоду (кол-ву свеч)
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

    /// Метод для вычисления нижней и верхней полосы Боллинжера (+- 1 стандартное отклонение)
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
        return new XYDataset[]{result_1, result_2};
    }


    /**
     * // реализация вычисления обычной экспоненциальной скользящей средней (EMA)
     */
    public static XYSeries calculateEMA(XYDataset source, int series, String name, int period) {
        Args.nullNotPermitted(source, "source");
        if (period < Double.MIN_VALUE) {
            throw new IllegalArgumentException("period must be positive.");
        }
        XYSeries emaValues = new XYSeries(name);

        double multiplier = 2.0 / (period + 1);

        // Первое значение EMA - простое среднее за первые period значений
        double first_time = source.getXValue(series, period);

        List<Double> firstCloseList = new ArrayList<>();
        for (int i = 0; i < period - 1; i++) {
            firstCloseList.add(source.getYValue(0, i));
        }
        double firstSMA = firstCloseList.stream().mapToDouble(Double::doubleValue).average().orElseThrow();
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
     * Вычисляем экспоненциальное скользящее стандартное отклонение для периода D1
     */
    public static XYSeries calculateExponentialStdDevByD1(XYDataset ema, OHLCDataset source, String name) {
        int seriesCount = source.getSeriesCount();  // = 1 (содержится 1 серия)
        int series = 0;
        if (seriesCount > 1) {
            log.error("seriesCount > 1");
        }
        XYSeries stdDevValues = new XYSeries(name);

        // Вычисляем кол-во свеч в каждом дне
        LinkedHashMap<LocalDate, Integer> numberOfCandlesPerDayMap = countingNumberCandlesPerDay(source, series);
        LocalDate firstLocalDate = numberOfCandlesPerDayMap.entrySet().iterator().next().getKey();
        LocalDate prevDay = null;
        List<Double> valuesByPeriod = new ArrayList<>();
        int shiftEma = 0;
        int period;
        double multiplier = 0;
        for (int i = 0; i < source.getItemCount(series); i++) {
            double currentTime = source.getXValue(series, i);
            double currentHighValue = source.getHighValue(series, i);
            double currentLowValue = source.getLowValue(series, i);
            Date currentDate = new Date(Math.round(currentTime));
            LocalDate currentLocalDate = new Timestamp(currentDate.getTime()).toLocalDateTime().toLocalDate();
            if (i == 0) {
                prevDay = currentLocalDate;
                valuesByPeriod.add(currentHighValue);
                valuesByPeriod.add(currentLowValue);
            } else {
                if (currentLocalDate.isAfter(prevDay)) {
                    period = numberOfCandlesPerDayMap.get(prevDay);
                    multiplier = 2.0 / (period + 1);
                    if (prevDay.isEqual(firstLocalDate)) {
                        shiftEma = period;
                        // Первое значение времени для расчета STD - обычное стандартное отклонение
                        double firstStdDev = calculateStdDev(valuesByPeriod);
                        stdDevValues.add(currentTime, firstStdDev);
                        valuesByPeriod.clear();
                    } else {
                        int sequenceNumberEma = i - shiftEma;
                        /// Если это первая свеча нового дня
                        double highDeviation = Math.abs(source.getHighValue(0, i) - ema.getYValue(0, sequenceNumberEma));
                        double lowDeviation = Math.abs(source.getLowValue(0, i) - ema.getYValue(0, sequenceNumberEma));
                        double maxCurrentDeviation = Math.max(highDeviation, lowDeviation);
                        double prevStdDev = stdDevValues.getY(stdDevValues.getItemCount() - 1).doubleValue();
                        double currentStdDev = prevStdDev + multiplier * (maxCurrentDeviation - prevStdDev);
                        stdDevValues.add(currentTime, currentStdDev);
                    }
                    prevDay = currentLocalDate;
                } else if (currentLocalDate.equals(prevDay)) {
                    if (prevDay.isEqual(firstLocalDate)) {
                        valuesByPeriod.add(currentHighValue);
                        valuesByPeriod.add(currentLowValue);
                    }
                    if (currentLocalDate.isAfter(firstLocalDate)) {
//                        log.info("i = {}", i);
//                        log.info("period = {}", period);
                        int sequenceNumberEma = i - shiftEma;
                        // Расчет последующих значений STD
                        double highDeviation = Math.abs(source.getHighValue(0, i) - ema.getYValue(0, sequenceNumberEma));
                        double lowDeviation = Math.abs(source.getLowValue(0, i) - ema.getYValue(0, sequenceNumberEma));
                        double maxCurrentDeviation = Math.max(highDeviation, lowDeviation);
                        /// Применим медианное сглаживание для того, чтобы игнорировать резкие ценовые выбросы
                        // выборка из текущего и 5 предыдущих значений
//                        List<Double> deviationForMedian = new ArrayList<>();
//                        deviationForMedian.add(maxCurrentDeviation);
//                        for (int j = 1; j < numberForMedian; j++) {
//                            double emaValue;
//                            if (sequenceNumberEma >= j) {
//                               emaValue = ema.getYValue(0, sequenceNumberEma - j);
//                            } else {
//                                emaValue = ema.getYValue(0, 0);
//                            }
//                            double highDeviation_j = Math.abs(source.getHighValue(0, i - j) - emaValue);
//                            double lowDeviation_j = Math.abs(source.getLowValue(0, i - j) - emaValue);
//                            double maxCurrentDeviation_j = Math.max(highDeviation_j, lowDeviation_j);
//                            deviationForMedian.add(maxCurrentDeviation_j);
//                        }
//                        double medianCurrentDeviation = Median.calculateMedian(deviationForMedian);
                        double prevStdDev = stdDevValues.getY(stdDevValues.getItemCount() - 1).doubleValue();

                        double currentStdDev = prevStdDev + multiplier * (maxCurrentDeviation - prevStdDev);
//                        double currentStdDev = prevStdDev + multiplier * (medianCurrentDeviation - prevStdDev);
                        stdDevValues.add(currentTime, currentStdDev);
                    }
                } else {
                    log.error("incorrect LocalDate!!!");
                }
            }
        }
        return stdDevValues;
    }


    /**
     * Вычисляем экспоненциальное скользящее стандартное отклонение для периода W
     */
    public static XYSeries calculateExponentialStdDevByW(XYDataset ema, OHLCDataset source, String name) {
        int seriesCount = source.getSeriesCount();  // = 1 (содержится 1 серия)
        int series = 0;
        if (seriesCount > 1) {
            log.error("seriesCount > 1");
        }
        XYSeries stdDevValues = new XYSeries(name);

        // Вычисляем кол-во свеч в каждом дне
        LinkedHashMap<LocalDate, Integer> numberOfCandlesPerDayMap = countingNumberCandlesPerDay(source, series);
        // Вычисляем кол-во свеч в каждой неделе
        LinkedHashMap<WeekNumberAndYear, Integer> numberOfCandlesPerWeekMap = countingNumberCandlesOfWeeks(numberOfCandlesPerDayMap);
        WeekNumberAndYear firstWeekNumberAndYear = numberOfCandlesPerWeekMap.entrySet().iterator().next().getKey();
        WeekNumberAndYear prevWeekNumberAndYear = null;
        List<Double> valuesByPeriod = new ArrayList<>();
        int shiftEma = 0;
        int period;
        double multiplier = 0;


        for (int i = 0; i < source.getItemCount(series); i++) {
            double currentTime = source.getXValue(series, i);
            double currentHighValue = source.getHighValue(series, i);
            double currentLowValue = source.getLowValue(series, i);
            Date currentDate = new Date(Math.round(currentTime));
            LocalDate currentLocalDate = new Timestamp(currentDate.getTime()).toLocalDateTime().toLocalDate();
            int currentWeekNumber = currentLocalDate.get(WeekFields.ISO.weekOfWeekBasedYear());
            int currentYearOfWeek = currentLocalDate.get(WeekFields.ISO.weekBasedYear());
            WeekNumberAndYear currentWeekNumberAndYear = new WeekNumberAndYear(currentWeekNumber, currentYearOfWeek);
            if (i == 0) {
                prevWeekNumberAndYear = currentWeekNumberAndYear;
                valuesByPeriod.add(currentHighValue);
                valuesByPeriod.add(currentLowValue);
            } else {
                if (!currentWeekNumberAndYear.equals(prevWeekNumberAndYear)) {
                    period = numberOfCandlesPerWeekMap.get(prevWeekNumberAndYear);
                    multiplier = 2.0 / (period + 1);
                    if (prevWeekNumberAndYear.equals(firstWeekNumberAndYear)) {
                        shiftEma = period;
                        // Первое значение времени для расчета STD - обычное стандартное отклонение
                        double firstStdDev = calculateStdDev(valuesByPeriod);
                        stdDevValues.add(currentTime, firstStdDev);
                        valuesByPeriod.clear();
                    } else {
                        int sequenceNumberEma = i - shiftEma;
                        /// Если это первая свеча нового дня
                        double highDeviation = Math.abs(source.getHighValue(0, i) - ema.getYValue(0, sequenceNumberEma));
                        double lowDeviation = Math.abs(source.getLowValue(0, i) - ema.getYValue(0, sequenceNumberEma));
                        double maxCurrentDeviation = Math.max(highDeviation, lowDeviation);
                        double prevStdDev = stdDevValues.getY(stdDevValues.getItemCount() - 1).doubleValue();
                        double currentStdDev = prevStdDev + multiplier * (maxCurrentDeviation - prevStdDev);
                        stdDevValues.add(currentTime, currentStdDev);
                    }
                    prevWeekNumberAndYear = currentWeekNumberAndYear;
                } else {
                    if (prevWeekNumberAndYear.equals(firstWeekNumberAndYear)) {
                        valuesByPeriod.add(currentHighValue);
                        valuesByPeriod.add(currentLowValue);
                    }
                    if (!currentWeekNumberAndYear.equals(firstWeekNumberAndYear)) {
                        //                        log.info("i = {}", i);
//                        log.info("period = {}", period);
                        int sequenceNumberEma = i - shiftEma;
                        // Расчет последующих значений STD
                        double highDeviation = Math.abs(source.getHighValue(0, i) - ema.getYValue(0, sequenceNumberEma));
                        double lowDeviation = Math.abs(source.getLowValue(0, i) - ema.getYValue(0, sequenceNumberEma));
                        double maxCurrentDeviation = Math.max(highDeviation, lowDeviation);
                        /// Применим медианное сглаживание для того, чтобы игнорировать резкие ценовые выбросы
                        // выборка из текущего и 5 предыдущих значений
//                        List<Double> deviationForMedian = new ArrayList<>();
//                        deviationForMedian.add(maxCurrentDeviation);
//                        for (int j = 1; j < numberForMedian; j++) {
//                            double emaValue;
//                            if (sequenceNumberEma >= j) {
//                                emaValue = ema.getYValue(0, sequenceNumberEma - j);
//                            } else {
//                                emaValue = ema.getYValue(0, 0);
//                            }
//                            double highDeviation_j = Math.abs(source.getHighValue(0, i - j) - emaValue);
//                            double lowDeviation_j = Math.abs(source.getLowValue(0, i - j) - emaValue);
//                            double maxCurrentDeviation_j = Math.max(highDeviation_j, lowDeviation_j);
//                            deviationForMedian.add(maxCurrentDeviation_j);
//                        }
//                        double medianCurrentDeviation = Median.calculateMedian(deviationForMedian);

                        double prevStdDev = stdDevValues.getY(stdDevValues.getItemCount() - 1).doubleValue();
                        double currentStdDev = prevStdDev + multiplier * (maxCurrentDeviation - prevStdDev);
//                        double currentStdDev = prevStdDev + multiplier * (medianCurrentDeviation - prevStdDev);
                        stdDevValues.add(currentTime, currentStdDev);
                    }
                }
            }
        }
        return stdDevValues;
    }

    /**
     * Вычисляем экспоненциальное скользящее стандартное отклонение
     */
    public static XYSeries calculateExponentialStdDev(XYDataset ema, OHLCDataset source, String name, int period) {
        if (source.getItemCount(0) < period) {
            return new XYSeries(name);
        }
        XYSeries stdDevValues = new XYSeries(name);
//        XYDataset ema = createMovingAverage(source, suffix, period);
        double multiplier = 2.0 / (period + 1);

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
            double currentStdDev = prevStdDev + multiplier * (Math.max(highDeviation, lowDeviation) - prevStdDev);
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
        // Сначала расчитываем среднее хначение
//        double meanAverage = values.stream()
//                .mapToDouble(Double::doubleValue)
//                .average()
//                .orElse(0.0);
//        // Затем рассчитываем среднеквадратичное значение отклонения каждого значения от среднего,
//        // и вычисляем среднее значение этого отклонения
//        double variance = values.stream()
//                .mapToDouble(value -> Math.pow(value - meanAverage, 2))
//                .average()
//                .orElse(0.0);
//        return Math.sqrt(variance);

        // 1. Сначала расчитываем медианное хначение
        double meanMedian = Median.calculateMedian(values);
        // 2. Вычисляем абсолютные отклонения от медианы
        double[] absDeviations = values.stream()
                .mapToDouble(value -> Math.abs(value - meanMedian)).toArray();
        // и медиану абсолютных отклонений
        return Median.calculateMedian(absDeviations);
    }
}
