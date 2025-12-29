package stepanovvv.ru.strategyPanel;

import lombok.extern.slf4j.Slf4j;
import stepanovvv.ru.MyTerminal;
import stepanovvv.ru.models.instrument_info.StockInfoDto;
import stepanovvv.ru.models.native_moex_models.native_instrument_info.FutureMoex;
import stepanovvv.ru.pop_up_warnings.NoContent;
import stepanovvv.ru.service.MoexService;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class PanelHi2 extends StrategyPanel {
    private final MoexService moexService = new MoexService();

    private static final StrategyName name = StrategyName.STRATEGY_Hi2;
    private static final String strategyUrl = "hi2";
    private static final String[] list1 = new String[]{"Stocks", "Futures", "Currency"};
    private static final String[][] list2 = new String[][]{
            {"1 level", "2 level", "3 level"},                                // раскрытие list1[0] - для "Stocks"
            {"1 level", "2 level", "3 level", "индексы", "товары", "валюта"}, // раскрытие list1[1] - для "Futures"
            {"1 level", "2 level"}};                                          // раскрытие list1[2] - для "Currency"

    //    private final List<List<StockMoex>> stockListByLevels = new ArrayList<>();
    private final List<List<StockInfoDto>> stockListByLevels = new ArrayList<>();
    private final List<List<FutureMoex>> futuresByLevels = new ArrayList<>();

    private final List<List<FutureMoex>> currentsByLevels = new ArrayList<>();

    //    private final String[][] futuresStocksByLevels;
//    private final String[][] currentsByLevels;

    protected final JButton buttonBaseFutureSpread_Strategy;
    protected final JButton buttonFutureSpread_2_Strategy;
    protected final JButton buttonFutureSpread_3_Strategy;


    public PanelHi2(MyTerminal myTerminal) {
        super(myTerminal);
        // вызов метода для фильтрации акций по уровню листинга и добавление их в список по уровням листинга
        setStocksByLevels();

//        futuresStocksByLevels = setFuturesStocksByLevels();
//        currentsByLevels      = setCurrentsByLevels();
        setFuturesStocksByLevels();
        setCurrentsByLevels();

        buttonBaseFutureSpread_Strategy = new JButton("BaseFutureSpread_Strategy");
        buttonBaseFutureSpread_Strategy.addActionListener(e -> MyTerminal.main(new String[]{"BaseFutureSpread"}));
        add(buttonBaseFutureSpread_Strategy);

        buttonFutureSpread_2_Strategy = new JButton("FutureSpread_2_Strategy");
        buttonFutureSpread_2_Strategy.addActionListener(e -> MyTerminal.main(new String[]{"FutureSpread_2"}));
        add(buttonFutureSpread_2_Strategy);

        buttonFutureSpread_3_Strategy = new JButton("FutureSpread_3_Strategy");
        buttonFutureSpread_3_Strategy.addActionListener(e -> MyTerminal.main(new String[]{"FutureSpread_3"}));
        add(buttonFutureSpread_3_Strategy);
    }

    public void setStocksByLevels() {
        // Список всех акций, по которым Мосбиржа ведет расчет метрик HI2
        log.info("download stock info (DTO)");
        List<StockInfoDto> allStocksInfoDto = moexService.getStocksInfoDtoForHi2();
        if (allStocksInfoDto == null) {
            // окошко - предупреждение об отсутствии информации на сервере
            NoContent.main(new String[]{"No content of StockInfoDto on server!!!"});

        } else {
            // Фильтрация акций по уровням листинга
            log.info("filtering stocks by listing levels");
            List<StockInfoDto> stocksListLevel1 = allStocksInfoDto.stream()
                    .filter(stockInfoDto -> stockInfoDto.stockMoexListLevel() == 1)
                    .toList();
            List<StockInfoDto> stocksListLevel2 = allStocksInfoDto.stream()
                    .filter(stockInfoDto -> stockInfoDto.stockMoexListLevel() == 2)
                    .toList();
            List<StockInfoDto> stocksListLevel3 = allStocksInfoDto.stream()
                    .filter(stockInfoDto -> stockInfoDto.stockMoexListLevel() == 3)
                    .toList();

            // Добавление отфильтрованных акций в список (список списков)
            stockListByLevels.add(stocksListLevel1);
            stockListByLevels.add(stocksListLevel2);
            stockListByLevels.add(stocksListLevel3);
        }

    }

    public String[][] setFuturesStocksByLevels() {
        // Список всех фьючерсов, по которым Мосбиржа ведет расчет метрик HI2

//        List<FutureMoex> allFutures = repository.getFuturesForHi2();

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
    public String setStrategyUrl() {
        return strategyUrl;
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
                    // Получение выделенного элемента из окна 2 (порядковый номер уровня ликвидности)
                    int selected2 = list2.locationToIndex(e.getPoint());
                    switch (selected1) {
                        case 0:                     // если выбрано Stocks в list1
                            // список инструментов, отсортированный либо
                            // по алфавиту,
                            // либо  по объему торгов (ликвидности)
                            log.info("sort stocks");
                            // Создаем копию выбранного по уровню списка акций
                            List<StockInfoDto> listStocks = List.of();
                            //Создаем список (массив) названий акций
                            String[] stocksArrayLevel = new String[0];
                            if (!stockListByLevels.isEmpty()) {
                                listStocks = new ArrayList<>(stockListByLevels.get(selected2));
                                //список (массив) названий акций
                                stocksArrayLevel = new String[stockListByLevels.get(selected2).size()];
                                if (checkBox1.isSelected()) { // если выбрана сортировка по ликвидности
                                    // с сортировкой по объему торгов (ликвидности)
//                              listStocks.sort((o1, o2) -> (int) (o2.prevVolume() - o1.prevVolume())); // в лотах
                                    listStocks.sort((o1, o2) -> (int) (o2.prevValue() - o1.prevValue())); // в рублях
                                }
                            }
                            // если не выбрана сортировка по ликвидности (то будет по алфавиту)
                            log.info("get shortName from stocks");
                            listStocks.stream()
                                    .map(stockInfoDto -> stockInfoDto.secId() + " (" +
                                            stockInfoDto.stockMoexShortName() + ")")
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
