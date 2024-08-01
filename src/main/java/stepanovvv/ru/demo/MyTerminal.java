package stepanovvv.ru.demo;

import stepanovvv.ru.candlestick.JfreeChartPanel;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class MyTerminal extends JFrame {
//    private String ticker;
//    private boolean deletingHolidays = true;

    public MyTerminal(StrategyName strategy, String ticker, LocalDate fromLocalDate, LocalDate tillLocalDate,
                      Timeframe timeframe, boolean deletingHolidays, boolean volume, boolean marketProfile) {
        super(ticker);
        JFrame.setDefaultLookAndFeelDecorated(true);
//        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Если это использовать, при нажатии на крестик
        // закроется не только окно с графиком, но и сама программв

        JfreeChartPanel jfreeChartPanel = new JfreeChartPanel(ticker, fromLocalDate, tillLocalDate, timeframe,
                deletingHolidays, volume, marketProfile);
        add(jfreeChartPanel.getCommonChartPanel(), BorderLayout.CENTER);
        // Вспомогательная панель справа
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());

        // Панель внутри правой панели (верхняя)
        JPanel panel2 = null;
        if (strategy == StrategyName.SRATEGY_Hi2) {
            panel2 = new PanelHi2();
        }
        if (strategy == StrategyName.STRATEGY_FUTURE_SPREAD) {
            panel2 = new PanelFutureSpread();
            System.out.println("asd");
        }
        assert panel2 != null;
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

    public static void addChart(StrategyName strategy, String ticker,
                                LocalDate fromLocalDate,
                                LocalDate tillLocalDate,
                                Timeframe timeframe,
                                boolean deletingHolidays, boolean volume, boolean marketProfile) {
        SwingUtilities.invokeLater(() -> {
            MyTerminal app = new MyTerminal(strategy, ticker, fromLocalDate, tillLocalDate, timeframe,
                    deletingHolidays, volume, marketProfile);
            app.pack();
            app.setVisible(true);
        });
    }

    public static void main(String[] args) {

        String ticker = "START PANEL";
        boolean deletingHolidays = true;
        LocalDate fromLocalDate = LocalDate.now();
        LocalDate tillLocalDate = LocalDate.now();
        boolean volume = true;
        boolean marketProfile = true;

        if (args.length == 1 && args[0].equals("Future Spread")) {
            String ticker2 = "START PANEL2";
            SwingUtilities.invokeLater(() -> {
                MyTerminal app = new MyTerminal(StrategyName.STRATEGY_FUTURE_SPREAD,
                        ticker2, fromLocalDate, tillLocalDate,
                        Timeframe.D1, deletingHolidays, volume, marketProfile);
                //JFrame.DISPOSE_ON_CLOSE Убирает окно с экрана и освобождает все принадлежащие ему ресурсы
                app.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                app.pack();  // оптимальный размер окна с компонентами
                app.setVisible(true);
            });
        } else {
            SwingUtilities.invokeLater(() -> {
                MyTerminal app = new MyTerminal(StrategyName.SRATEGY_Hi2,
                        ticker, fromLocalDate, tillLocalDate, Timeframe.D1_Hi2,
                        deletingHolidays, volume, marketProfile);
                // При закрытии стартовой панели приложение будет остановлено
                app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                app.pack();  // оптимальный размер окна с компонентами
                app.setVisible(true);
            });
        }





//        JfreeChartHi2 jfreeChartHi2 = new JfreeChartHi2();
//        //creating and showing this application's GUI.
//        SwingUtilities.invokeLater(jfreeChartHi2::createAndShowGUI);
    }


}
