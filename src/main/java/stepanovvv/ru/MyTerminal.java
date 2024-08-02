package stepanovvv.ru;

import stepanovvv.ru.candlestick.JfreeChartPanel;
import stepanovvv.ru.strategyPanel.PanelFutureSpread;
import stepanovvv.ru.strategyPanel.PanelHi2;
import stepanovvv.ru.strategyPanel.StrategyName;
import stepanovvv.ru.strategyPanel.Timeframe;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class MyTerminal extends JFrame {

    public MyTerminal(StrategyName strategy, String ticker, LocalDate fromLocalDate, LocalDate tillLocalDate,
                      Timeframe timeframe, boolean deletingHolidays, boolean volume, boolean marketProfile) {
        super(ticker);
        // Если это использовать, то не будут передвигаться дополнительно открытые окна
//        JFrame.setDefaultLookAndFeelDecorated(true);
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
        if (strategy == StrategyName.STRATEGY_Hi2) {
            panel2 = new PanelHi2();
        }
        if (strategy == StrategyName.STRATEGY_FUTURE_SPREAD) {
            panel2 = new PanelFutureSpread();
        }
        assert panel2 != null;
        rightPanel.add(panel2, BorderLayout.CENTER); // добавляем панель в правую панель в центр
        add(rightPanel, BorderLayout.EAST);    // добавляем правую панель справа

    }

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
            String ticker2 = "PANEL Future Spread";
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
                MyTerminal app = new MyTerminal(StrategyName.STRATEGY_Hi2,
                        ticker, fromLocalDate, tillLocalDate, Timeframe.D1_Hi2,
                        deletingHolidays, volume, marketProfile);
                // При закрытии стартовой панели приложение будет остановлено
                app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                app.pack();  // оптимальный размер окна с компонентами
                app.setVisible(true);
            });
        }
    }
}
