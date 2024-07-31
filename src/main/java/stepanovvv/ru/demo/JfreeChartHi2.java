package stepanovvv.ru.demo;

import stepanovvv.ru.candlestick.JfreeCandlesChart;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class JfreeChartHi2 extends JFrame {
//    private String ticker;
//    private boolean deletingHolidays = true;

    public JfreeChartHi2(String ticker, LocalDate fromLocalDate, LocalDate tillLocalDate, Timeframe timeframe,
                         boolean deletingHolidays) {
        super(ticker);
        JFrame.setDefaultLookAndFeelDecorated(true);
//        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Если это использовать, при нажатии на крестик
        // закроется не только окно с графиком, но и сама программв

        JfreeCandlesChart jfreeCandlesChart = new JfreeCandlesChart(ticker, fromLocalDate, tillLocalDate,
                timeframe, deletingHolidays);
        add(jfreeCandlesChart.getCommonChartPanel(), BorderLayout.CENTER);
        // Вспомогательная панель справа
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());

        // Панель внутри правой панели (верхняя)
        JPanel panel2 = new PanelHi2();
        rightPanel.add(panel2, BorderLayout.CENTER); // добавляем панель в правую панель в центр
        add(rightPanel, BorderLayout.EAST);    // добавляем правую панель справа

        //Disable the resizing feature
//        setResizable(true);
    }

    /*
    private void createAndShowGUI() {
        //Make sure we have nice window decorations.
        JFrame.setDefaultLookAndFeelDecorated(true);

        //Create and set up the window.
        JFrame frame = new JFrame("JfreeChartHi2");

//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Если это использовать, при нажатии на крестик
//        // закроется не только окно с графиком, но и сама программв


        //Create and set up the chart.
        JfreeCandlesChart jfreeCandlesChart = new JfreeCandlesChart(ticker, deletingHolidays);

        //Так добавляется  org.jfree.chart.ChartPanel, включающая в себя org.jfree.chart.ChartPanel (при этом при изменении
        // размера Frame с помощью мыши не будет меняться размер панели с графиками)
//        frame.setContentPane(jfreeCandlesChart);
        //Так добавляется org.jfree.chart.ChartPanel (при этом при изменении размера Frame с помощью мыши
        //  будет меняться размер панели с графиками)

        frame.add(jfreeCandlesChart.getCommonChartPanel(), BorderLayout.CENTER);

        // Вспомогательная панель справа
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());

        // Панель внутри правой панели (верхняя)
        JPanel panel2 = new PanelHi2();

        rightPanel.add(panel2, BorderLayout.CENTER); // добавляем панель в правую панель в центр
        frame.add(rightPanel, BorderLayout.EAST);    // добавляем правую панель справа

        //Disable the resizing feature
        frame.setResizable(true);
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
     */

    public static void addChart(String ticker,
                                LocalDate fromLocalDate,
                                LocalDate tillLocalDate,
                                Timeframe timeframe,
                                boolean deletingHolidays) {
        SwingUtilities.invokeLater(() -> {
            JfreeChartHi2 app = new JfreeChartHi2(ticker, fromLocalDate, tillLocalDate, timeframe, deletingHolidays);
            app.pack();
            app.setVisible(true);
        });
    }

    public static void main(String[] args) {
        String ticker = "SBER(Demo)";
        boolean deletingHolidays = true;
        LocalDate fromLocalDate = LocalDate.now();
        LocalDate tillLocalDate = LocalDate.now();
        SwingUtilities.invokeLater(() -> {
            JfreeChartHi2 app = new JfreeChartHi2(ticker, fromLocalDate, tillLocalDate, Timeframe.D1,
                     deletingHolidays);
            app.pack();
            app.setVisible(true);
        });

//        JfreeChartHi2 jfreeChartHi2 = new JfreeChartHi2();
//        //creating and showing this application's GUI.
//        SwingUtilities.invokeLater(jfreeChartHi2::createAndShowGUI);
    }
}
