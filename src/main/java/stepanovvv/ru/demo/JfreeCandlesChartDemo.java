package stepanovvv.ru.demo;

import lombok.AllArgsConstructor;
import stepanovvv.ru.candlestick.JfreeCandlesChart;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

@AllArgsConstructor
public class JfreeCandlesChartDemo  {
    private String ticker;
    private boolean deletingHolidays;

    private void createAndShowGUI() {
        //Make sure we have nice window decorations.
        JFrame.setDefaultLookAndFeelDecorated(true);

        //Create and set up the window.
        JFrame frame = new JFrame("JfreeCandlestickChartDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the chart.
        JfreeCandlesChart jfreeCandlesChart = new JfreeCandlesChart(ticker, deletingHolidays);

        //Так добавляется JPanel, включающая в себя ChartPanel (при этом при изменении размера Frame с помощью мыши
        // не будет меняться размер панели с графиками)
//        frame.setContentPane(jfreeCandlesChart);
        //Так добавляется ChartPanel (при этом при изменении размера Frame с помощью мыши
        //  будет меняться размер панели с графиками)
        frame.setContentPane(jfreeCandlesChart.getCommonChartPanel());

        //Disable the resizing feature
        frame.setResizable(true);
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        JfreeCandlesChartDemo jfreeCandlestickChartDemo = new JfreeCandlesChartDemo("SBER", true);
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(jfreeCandlestickChartDemo::createAndShowGUI);
    }
}
