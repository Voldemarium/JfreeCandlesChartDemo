package stepanovvv.ru.demo;

import lombok.AllArgsConstructor;
import stepanovvv.ru.candlestick.JfreeCandlesChart;
import stepanovvv.ru.models.CandleMoex;
import stepanovvv.ru.models.MockListCandles;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

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

        // Create candleMoexList
        List<CandleMoex> candleMoexList = new MockListCandles().getCandleMoexList();

        // Добавляем свечи на графмк
        jfreeCandlesChart.addCandles(candleMoexList);

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
