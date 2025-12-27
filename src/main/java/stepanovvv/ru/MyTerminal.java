package stepanovvv.ru;

import lombok.extern.slf4j.Slf4j;
import stepanovvv.ru.candlestick.JfreeChartPanel;
import stepanovvv.ru.strategyPanel.*;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

@Slf4j
public class MyTerminal extends JFrame {


    public MyTerminal(StrategyName strategy, String ticker, LocalDate fromLocalDate, LocalDate tillLocalDate,
                      Timeframe timeframe, boolean deletingHolidays, boolean volume, boolean marketProfile) {
        // Создание надписи над графиком = ticker
        super(ticker);
        // Если это использовать, то не будут передвигаться дополнительно открытые окна
//        JFrame.setDefaultLookAndFeelDecorated(true);
//        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Если это использовать, при нажатии на крестик
        // закроется не только окно с графиком, но и сама программв
        log.info("building \"jfreeChartPanel\"");


        ///  Выбор url для загрузки свечного графика спреда в зависимости от стратегии
        String url = "";
        if (strategy.equals(StrategyName.STRATEGY_Hi2)) {

        } else if (strategy.equals(StrategyName.STRATEGY_FUTURE_SPREAD_2)) {

        } else if (strategy.equals(StrategyName.STRATEGY_FUTURE_SPREAD_3)) {

        }



        // Создание графика
        JfreeChartPanel jfreeChartPanel = new JfreeChartPanel(url, ticker, fromLocalDate, tillLocalDate, timeframe,
                deletingHolidays, volume, marketProfile);
        add(jfreeChartPanel.getCommonChartPanel(), BorderLayout.CENTER);
        // Вспомогательная панель справа
        log.info("building \"richtPanel\"");
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());

        // Панель внутри правой панели (верхняя)
        JPanel panel2 = null;
        if (strategy == StrategyName.STRATEGY_Hi2) {
            log.info("building \"panelHi2\"");
            panel2 = new PanelHi2();
        }
       else if (strategy == StrategyName.STRATEGY_BASE_FUTURE_SPREAD) {
            log.info("building \"panelBaseFutureSpread\"");
            panel2 = new PanelBaseFutureSpread();
        }
        else if (strategy == StrategyName.STRATEGY_FUTURE_SPREAD_2) {
            log.info("building \"panelFutureSpread_2\"");
            panel2 = new PanelFutureSpread_2();
        }
        else if (strategy == StrategyName.STRATEGY_FUTURE_SPREAD_3) {
            log.info("building \"panelFutureSpread_3\"");
            panel2 = new PanelFutureSpread_3();
        }
        assert panel2 != null;
        rightPanel.add(panel2, BorderLayout.CENTER); // добавляем панель в правую панель в центр
        add(rightPanel, BorderLayout.EAST);    // добавляем правую панель справа
    }

    // Добавление свечного графика (по умолчанию, при старте приложения (на панели "Start Panel")
    // и нажатии кнопок "BaseFutureSpreadStrategy", "FutureSpread2_3_Strategy" подтягивается график для демонстрации),
    // При нажатии на кнопку "create new chart" создается нужный график с подтягиванием данных по выбранному ticker инструмента
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

    /// -------------------------------------------------------------------------------
    /// -----------------------------------MAIN-------------------------------------------
    /// ------------------------------------------------------------------------------

    public static void main(String[] args) {
        log.info("input parameters");
        String ticker = "START PANEL";
        boolean deletingHolidays = true;
        LocalDate fromLocalDate = LocalDate.now();
        LocalDate tillLocalDate = LocalDate.now();
        boolean volume = true;
        boolean marketProfile = true;

        if (args.length == 1 && args[0].equals("BaseFutureSpread")) {
            log.info("building \"BaseFutureSpread\"");
            String ticker2 = "BaseFutureSpread";
            SwingUtilities.invokeLater(() -> {
                MyTerminal app = new MyTerminal(StrategyName.STRATEGY_BASE_FUTURE_SPREAD,
                        ticker2, fromLocalDate, tillLocalDate,
                        Timeframe.D1, deletingHolidays, volume, marketProfile);
                //JFrame.DISPOSE_ON_CLOSE Убирает окно с экрана и освобождает все принадлежащие ему ресурсы
                app.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                app.pack();  // оптимальный размер окна с компонентами
                app.setVisible(true);
            });
        }
        else if (args.length == 1 && args[0].equals("FutureSpread_2")) {
            log.info("building \"FutureSpread_2\"");
            String ticker3 = "FutureSpread_2";
            SwingUtilities.invokeLater(() -> {
                MyTerminal app = new MyTerminal(StrategyName.STRATEGY_FUTURE_SPREAD_2,
                        ticker3, fromLocalDate, tillLocalDate,
                        Timeframe.D1, deletingHolidays, volume, marketProfile);
                //JFrame.DISPOSE_ON_CLOSE Убирает окно с экрана и освобождает все принадлежащие ему ресурсы
                app.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                app.pack();  // оптимальный размер окна с компонентами
                app.setVisible(true);
            });
        }
        else if (args.length == 1 && args[0].equals("FutureSpread_3")) {
            log.info("building \"FutureSpread_3\"");
            String ticker4 = "FutureSpread_3";
            SwingUtilities.invokeLater(() -> {
                MyTerminal app = new MyTerminal(StrategyName.STRATEGY_FUTURE_SPREAD_3,
                        ticker4, fromLocalDate, tillLocalDate,
                        Timeframe.D1, deletingHolidays, volume, marketProfile);
                //JFrame.DISPOSE_ON_CLOSE Убирает окно с экрана и освобождает все принадлежащие ему ресурсы
                app.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                app.pack();  // оптимальный размер окна с компонентами
                app.setVisible(true);
            });
        }
        else {
            SwingUtilities.invokeLater(() -> {
                log.info("building \"START PANEL\"");
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
