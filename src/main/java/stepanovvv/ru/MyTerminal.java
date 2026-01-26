package stepanovvv.ru;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jfree.chart.ChartPanel;
import stepanovvv.ru.candlestick.JfreeChartPanel;
import stepanovvv.ru.models.ParametersMA;
import stepanovvv.ru.strategyPanel.*;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;

@Slf4j
@Getter
@Setter
public class MyTerminal extends JFrame {
    private ChartPanel chartPanel; // ссылка на панель с графиком
//    public static final ZoneId zoneId = ZoneId.of("Europe/Moscow");
    public static final ZoneId zoneId = ZoneId.of("Europe/Saratov");
    public static final ZoneOffset zoneOffset = ZoneOffset.of("+4");


    public MyTerminal(StrategyName strategy, String selectedInstrumentOfList1, String selectedInstrumentOfList2,
                      String selectedTickerOrExpDateOfList3, LocalDate fromLocalDate, LocalDate tillLocalDate,
                      Timeframe timeframe, boolean deletingHolidays, boolean volume, boolean marketProfile, ParametersMA parametersMA) {
        // Создание надписи над графиком = ticker
        super(selectedInstrumentOfList2 + " (" + selectedTickerOrExpDateOfList3 + ")");
        // Если это использовать, то не будут передвигаться дополнительно открытые окна
//        JFrame.setDefaultLookAndFeelDecorated(true);
//        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Если это использовать, при нажатии на крестик
        // закроется не только окно с графиком, но и сама программв
        log.info("building \"jfreeChartPanel\"");


        ///  Выбор url для загрузки свечного графика спреда в зависимости от стратегии
        String strategyUrl = "";
        if (strategy.equals(StrategyName.STRATEGY_Hi2)) {

        } else if (strategy.equals(StrategyName.STRATEGY_FUTURE_SPREAD_2)) {

        } else if (strategy.equals(StrategyName.STRATEGY_FUTURE_SPREAD_3)) {
            strategyUrl = "/futuresSpread3";
        }


        // Создание графика
        JfreeChartPanel jfreeChartPanel = new JfreeChartPanel(strategyUrl, selectedInstrumentOfList1, selectedInstrumentOfList2,
                selectedTickerOrExpDateOfList3, fromLocalDate, tillLocalDate, timeframe, deletingHolidays, volume, marketProfile,
                parametersMA);
        ChartScrollBar chartScrollBar = new ChartScrollBar(Adjustable.HORIZONTAL, jfreeChartPanel.getCommonChart(),
                jfreeChartPanel.getCommonChart().getXYPlot());
        this.chartPanel = jfreeChartPanel.getCommonChartPanel();
        this.chartPanel.add(chartScrollBar, BorderLayout.SOUTH);;

        add(chartPanel, BorderLayout.CENTER);
        // Вспомогательная панель справа
        log.info("building \"richtPanel\"");
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());

        // Панель внутри правой панели (верхняя)
        JPanel panel2 = null;
        if (strategy == StrategyName.STRATEGY_Hi2) {
            log.info("building \"panelHi2\"");
            panel2 = new PanelHi2(this);
        }
       else if (strategy == StrategyName.STRATEGY_BASE_FUTURE_SPREAD) {
            log.info("building \"panelBaseFutureSpread\"");
            panel2 = new PanelBaseFutureSpread(this);
        }
        else if (strategy == StrategyName.STRATEGY_FUTURE_SPREAD_2) {
            log.info("building \"panelFutureSpread_2\"");
            panel2 = new PanelFutureSpread_2(this);
        }
        else if (strategy == StrategyName.STRATEGY_FUTURE_SPREAD_3) {
            log.info("building \"panelFutureSpread_3\"");
            panel2 = new PanelFutureSpread_3(this);
        }
        assert panel2 != null;
        rightPanel.add(panel2, BorderLayout.CENTER); // добавляем панель в правую панель в центр
        add(rightPanel, BorderLayout.EAST);    // добавляем правую панель справа
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
        ParametersMA parametersMA = null;

        if (args.length == 1 && args[0].equals("BaseFutureSpread")) {
            log.info("building \"BaseFutureSpread\"");
            String ticker2 = "BaseFutureSpread";
            SwingUtilities.invokeLater(() -> {
                MyTerminal app = new MyTerminal(StrategyName.STRATEGY_BASE_FUTURE_SPREAD, null,
                        null, ticker2, fromLocalDate, tillLocalDate,
                        Timeframe.D1, deletingHolidays, volume, marketProfile, parametersMA);
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
                MyTerminal app = new MyTerminal(StrategyName.STRATEGY_FUTURE_SPREAD_2, null,
                        null, ticker3, fromLocalDate, tillLocalDate,
                        Timeframe.D1, deletingHolidays, volume, marketProfile, parametersMA);
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
                MyTerminal app = new MyTerminal(StrategyName.STRATEGY_FUTURE_SPREAD_3, null,
                        null, ticker4, fromLocalDate, tillLocalDate,
                        Timeframe.D1, deletingHolidays, volume, marketProfile, parametersMA);
                //JFrame.DISPOSE_ON_CLOSE Убирает окно с экрана и освобождает все принадлежащие ему ресурсы
                app.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                app.pack();  // оптимальный размер окна с компонентами
                app.setVisible(true);
            });
        }
        else {
            SwingUtilities.invokeLater(() -> {
                log.info("building \"START PANEL\"");
                MyTerminal app = new MyTerminal(StrategyName.STRATEGY_Hi2, null,
                        null, ticker, fromLocalDate, tillLocalDate, Timeframe.D1_Hi2,
                        deletingHolidays, volume, marketProfile, parametersMA);
                // При закрытии стартовой панели приложение будет остановлено
                app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                app.pack();  // оптимальный размер окна с компонентами
                app.setVisible(true);
            });
        }
    }
}
