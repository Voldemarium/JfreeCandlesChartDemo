package stepanovvv.ru.tools;

import java.util.Arrays;
import java.util.List;

public class Median {
    public static double calculateMedian(double[] data) {
        double[] sorted = data.clone();
        Arrays.sort(sorted);
        int n = sorted.length;
        if (n % 2 == 0) {
            // Четное количество элементов: среднее двух центральных
            return (sorted[n/2 - 1] + sorted[n/2]) / 2.0;
        } else {
            // Нечетное количество: центральный элемент
            return sorted[n/2];
        }
    }

    public static double calculateMedian(List<Double> doubleList) {
        double[] doubleArray = doubleList.stream()
                .mapToDouble(Double::doubleValue)
                .toArray();
        return calculateMedian(doubleArray);
    }
}

