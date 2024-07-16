package stepanovvv.ru.demo;

import lombok.AllArgsConstructor;
import stepanovvv.ru.candlestick.JfreeCandlesChart;
import stepanovvv.ru.models.CandleMoex;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
public class JfreeCandlesChartDemo extends JPanel {
    String ticker;

    private void createAndShowGUI() {
        //Make sure we have nice window decorations.
        JFrame.setDefaultLookAndFeelDecorated(true);

        //Create and set up the window.
        JFrame frame = new JFrame("JfreeCandlestickChartDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the chart.
        JfreeCandlesChart jfreeCandlesChart = new JfreeCandlesChart(ticker);

        List<CandleMoex> candleMoexList = List.of(
                CandleMoex.builder()
                        .begin(LocalDateTime.of(2024, 7, 1, 10, 0))
                        .volume(153.32)
                        .close(301.1)
                        .open(298.5)
                        .high(301.4)
                        .low(298.0)
                        .buyMetric(200.5)
                        .sellMetric(456.3)
                        .build(),
                CandleMoex.builder()
                        .begin(LocalDateTime.of(2024, 7, 3, 10, 0))
                        .volume(154.1)
                        .close(302.1)
                        .open(299.5)
                        .high(302.4)
                        .low(297.0)
                        .buyMetric(178.5)
                        .sellMetric(234.0)
                        .build(),
                CandleMoex.builder()
                        .begin(LocalDateTime.of(2024, 7, 4, 10, 0))
                        .volume(135.1)
                        .close(290.1)
                        .open(302.5)
                        .high(303.4)
                        .low(287.0)
                        .buyMetric(278.5)
                        .sellMetric(189.6)
                        .build(),
                CandleMoex.builder()
                        .begin(LocalDateTime.of(2024, 8, 1, 10, 0))
                        .volume(135.1)
                        .close(290.1)
                        .open(302.5)
                        .high(303.4)
                        .low(287.0)
                        .buyMetric(456.5)
                        .sellMetric(345.3)
                        .build()

        );

        // Добавляем свечи на графмк в цикле
        for (CandleMoex candleMoex : candleMoexList) {
            jfreeCandlesChart.addCandel(
                    candleMoex.getBegin(),
                    candleMoex.getOpen(),
                    candleMoex.getHigh(),
                    candleMoex.getLow(),
                    candleMoex.getClose(),
                    candleMoex.getVolume(),
                    candleMoex.getBuyMetric(),
                    candleMoex.getSellMetric()
            );
        }


        frame.setContentPane(jfreeCandlesChart);

        //Disable the resizing feature
        frame.setResizable(true);
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        JfreeCandlesChartDemo jfreeCandlestickChartDemo = new JfreeCandlesChartDemo("SBER");
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(jfreeCandlestickChartDemo::createAndShowGUI);
    }
}
