package stepanovvv.ru.strategyPanel;

import lombok.extern.slf4j.Slf4j;
import stepanovvv.ru.MyTerminal;
import stepanovvv.ru.models.FutureMoex;
import stepanovvv.ru.models.StockMoex;
import stepanovvv.ru.repository.api_mosExchange.ServerRepository;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class PanelHi2 extends StrategyPanel {
    private static final StrategyName name = StrategyName.STRATEGY_Hi2;
    private static final String[] list1 = new String[]{"Stocks", "Futures", "Currency"};
    private static final String[][] list2 = new String[][]{
            {"1 level", "2 level", "3 level"},                                // раскрытие list1[0] - "Stocks"
            {"1 level", "2 level", "3 level", "индексы", "товары", "валюта"}, // раскрытие list1[1] - "Futures"
            {"1 level", "2 level"}};                                          // раскрытие list1[2] - "Currency"

    private final List<List<StockMoex>> stockListByLevels = new ArrayList<>();
    private final List<List<StockMoex>> futuresStocksByLevels = new ArrayList<>();

    private final List<List<StockMoex>> currentsByLevels = new ArrayList<>();

    //    private final String[][] futuresStocksByLevels;
//    private final String[][] currentsByLevels;
    protected final JButton buttonFutureSpread_Strategy;
    ServerRepository repository = new ServerRepository();

    public PanelHi2() {
        super();
        // вызов метода для фильтрации акций по уровню листинга и добавление их в список по уровням листинга
        setStocksByLevels();

//        futuresStocksByLevels = setFuturesStocksByLevels();
//        currentsByLevels      = setCurrentsByLevels();
        setFuturesStocksByLevels();
        setCurrentsByLevels();

        buttonFutureSpread_Strategy = new JButton("FutureSpread_Strategy");
        buttonFutureSpread_Strategy.addActionListener(e -> MyTerminal.main(new String[]{"Future Spread"}));
        add(buttonFutureSpread_Strategy);
    }

    public void setStocksByLevels() {
        // Список всех акций, по которым Мосбиржа ведет расчет метрик HI2
        List<StockMoex> allStocks = repository.getStocksForHi2();
        // Фильтрация акций по уровням листинга
        List<StockMoex> stocksListLevel1 = allStocks.stream()
                .filter(stockMoex -> stockMoex.getListLevel() == 1)
                .toList();
        List<StockMoex> stocksListLevel2 = allStocks.stream()
                .filter(stockMoex -> stockMoex.getListLevel() == 2)
                .toList();
        List<StockMoex> stocksListLevel3 = allStocks.stream()
                .filter(stockMoex -> stockMoex.getListLevel() == 3)
                .toList();
        // Добавление отфильтрованных акций в список (список списков)
        stockListByLevels.add(stocksListLevel1);
        stockListByLevels.add(stocksListLevel2);
        stockListByLevels.add(stocksListLevel3);
    }

    public String[][] setFuturesStocksByLevels() {
        // Список всех фьючерсов, по которым Мосбиржа ведет расчет метрик HI2
        List<FutureMoex> allFutures = repository.getFuturesForHi2();
        // Фильтрация фьючерсов на акции, индексные, товарные и валютные

        // Фильтрация фьючерсов на акции по уровням листинга




        return new String[][]{
                {"FSBER (Сбербанк)", "FALRS (АО \"Алроса\")", "FAFK (АО \"Система\")"}, // раскр. list2[1][0] - level 1
                {"FAFK (АО \"Система\")", "FJHHS", "FUUYUY"},                          // раскр. list2[1][1] - level 2
                {"FHJHDSJHDF", "FEDSD", "FDFDFF"},                                    // раскр. list2[1][2] - level 3
                {"RTS", "MOEX"},                                                     // раскр. list2[1][3] - indexes
                {"GOLD", "BRENT"},                                                  // раскр. list2[1][4] - commodities
                {"USDRUB", "EUR"}                                                  // раскр. list2[1][5] - currencies
        };
    }

    public String[][] setCurrentsByLevels() {
        return new String[][]{
                {"USDRUB", "EURRUB", "CNYRUB"},                             // раскр. list2[3][0] - level 1
                {"CHFRUB", "GBRRUB", "BYNRUB"}                             // раскр. list2[3][0] - level 2
        };
    }

    @Override
    public StrategyName setStrategyName() {
        return name;
    }

    @Override
    public String[] setDataList1() {
        return list1;
    }

    @Override
    public String[][] setDataList2() {
        return list2;
    }

    @Override
    public JList<String> getStringJList1() {
        return new JList<>(dataList1);
    }

    // Подключение слушателя мыши на второй список JList<String> list2
    @Override
    void addList2ListenerMoise(JList<String> list2, JList<String> list3, JCheckBox checkBox1) {
        list2.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {    // при одинарном клике мышью
                    // Получение выделенного элемента из окна 2
                    int selected2 = list2.locationToIndex(e.getPoint());
                    switch (selected1) {
                        case 0:                     // если выбрано Stocks в list1
                            // список инструментов, отсортированный либо
                            // по алфавиту,
                            // либо  по объему торгов (ликвидности)

                            // Создаем копию выбранного по уровню списка акций
                            List<StockMoex> listStocks = new ArrayList<>(stockListByLevels.get(selected2));
                            //Создаем список названий акций
                            String[] stocksArrayLevel = new String[stockListByLevels.get(selected2).size()];
                            if (checkBox1.isSelected()) { // если выбрана сортировка по ликвидности
                                // с сортировкой по объему торгов (ликвидности)
//                              listStocks.sort((o1, o2) -> (int) (o2.getPrevVolume() - o1.getPrevVolume())); // в лотах
                                listStocks.sort((o1, o2) -> (int) (o2.getPrevValue() - o1.getPrevValue())); // в рублях
                            }
                            // если не выбрана сортировка по ликвидности (то будет по алфавиту)
                            listStocks.stream()
                                    .map(stockMoex -> stockMoex.getSecId() + " (" + stockMoex.getShortName() + ")")
                                    .toList().toArray(stocksArrayLevel);
                            list3.setListData(stocksArrayLevel);
                            break;
                        case 1:
//                            list3.setListData(futuresStocksByLevels[selected2]);
                            break;
                        case 2:
//                            list3.setListData(currentsByLevels[selected2]);
                            break;
                    }
                }
            }
        });
    }
}
