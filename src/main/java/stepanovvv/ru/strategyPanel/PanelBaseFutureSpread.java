package stepanovvv.ru.strategyPanel;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PanelBaseFutureSpread extends StrategyPanel {
    private static final StrategyName name = StrategyName.STRATEGY_BASE_FUTURE_SPREAD;
    private static final String[] list1 = new String[]{
            "Indexes",
            "Indexes futures",
            "Stocks",
            "Stocks Futures",
            "commodities spot",
            "commodity futures",
            "currencies",
            "currency futures"};
    private static final String[][] list2 = new String[][]{
            {"MOEX/RTS", "отраслевые", "МСК_недвижимость", "RGBI"},   // раскрытие list1[0] - "Indexes"
            {"MOEX/RTS", "отраслевые", "МСК_недвижимость", "RGBI"},   // раскрытие list1[1] - "Indexes futures"
            {"1 level", "2 level", "3 level"},                        // раскрытие list1[2] - "Stocks",
            {"1 level", "2 level", "3 level"},                        // раскрытие list1[3] - "Stocks Futures",
            // раскрытие list1[4] - "commodities spot",
            {"Нефть и газ", "драг_металлы", "цвет_металлы", "продовольствие"},
            // раскрытие list1[5] - "commodities spot",
            {"Нефть и газ", "драг_металлы", "цвет_металлы", "продовольствие"},
            {"1 level", "2 level"},                                   // раскрытие list1[6] - "currencies",
            {"1 level", "2 level"}                                    // раскрытие list1[7] - "currency futures"
    };

    private final String[][] indexes;
    private final String[][] futuresIndexes;
    private final String[][] stocksByLevels;
    private final String[][] futuresStocksByLevels;
    private final String[][] commodities;
    private final String[][] futuresCommodities;
    private final String[][] currentsByLevels;
    private final String[][] futuresCurrenciesByLevels;

    public PanelBaseFutureSpread() {
        super();
        indexes = setIndexes(false);
        futuresIndexes = setIndexes(true);
        stocksByLevels = setStocksByLevels(false);
        futuresStocksByLevels = setStocksByLevels(true);
        commodities = setCommodities(false);
        futuresCommodities = setCommodities(true);
        currentsByLevels = setCurrenciesByLevels(false);
        futuresCurrenciesByLevels = setCurrenciesByLevels(true);
    }

    public String[][] setIndexes(boolean isFutures) {
        String[][] result;
        if (isFutures) {
            result = new String[][]{
                    // Инструменты сохранять в БД (в БД хранить информацию о последнем обновлении, если новый день -
                    // обновить БД)
                    {"FMOEX", "FRTS"},                // раскрытие list2[1][0] - "MOEX/RTS"
                    {"FOGI", "FMMI", "FFNI", "FCNI"}, // раскрытие list2[1][1] - "отраслевые"
                    {"FHOME"},                        // раскрытие list2[1][2] - "МСК_недвижимость"
                    {"FRGBI"}                         // раскрытие list2[1][3] - "RGBI"
            };
        } else {
            result = new String[][]{
                    // Инструменты сохранять в БД (в БД хранить информацию о последнем обновлении, если новый день -
                    // обновить БД)
                    {"MOEX", "RTS"},               // раскрытие list2[0][0] - "MOEX/RTS"
                    {"OGI", "MMI", "FNI", "CNI"},  // раскрытие list2[0][1] - "отраслевые"
                    {"HOME"},                      // раскрытие list2[0][2] - "МСК_недвижимость"
                    {"RGBI"}                       // раскрытие list2[0][3] - "RGBI"
            };
        }
        return result;
    }

    public String[][] setStocksByLevels(boolean isFutures) {
        String[][] result;
        if (isFutures) {
            result = new String[][]{
                    {"FSBER (Сбербанк)", "FALRS (АО \"Алроса\")"},  // раскрытие list2[3][0] - "1 level"
                    {"FAFK (АО \"Система\")", "FJHHS", "FUUYUY"},   // раскрытие list2[3][1] - "2 level"
                    {"FHJHDSJHDF", "FEDSD", "FDFDFF"}};             // раскрытие list2[3][2] - "3 level"
        } else {
            result = new String[][]{
                    {"SBER (Сбербанк)", "ALRS (АО \"Алроса\")"},  // раскрытие list2[2][0] - "1 level"
                    {"AFK (АО \"Система\")", "JHHS", "UUYUY"},    // раскрытие list2[2][1] - "2 level"
                    {"HJHDSJHDF", "EDSD", "DFDFF"}};              // раскрытие list2[2][2] - "3 level"
        }
        return result;
    }

    public String[][] setCommodities(boolean isFutures) {
        String[][] result;
        if (isFutures) {
            result = new String[][]{
                    {"FBrent", "FWTI"},             // раскрытие list2[5][0] - "Нефть и газ"
                    {"FGOLD", "FSILVER"},           // раскрытие list2[5][1] - "драг_металлы"
                    {"FAL", "FCUPRUM"},             // раскрытие list2[5][2] - "цвет_металлы"
                    {"Ф_Пшеница"}                   // раскрытие list2[5][3] - "продовольствие"
            };
        } else {
            result = new String[][]{
                    {"Brent", "WTI"},              // раскрытие list2[4][0] - "Нефть и газ"
                    {"GOLD", "SILVER"},            // раскрытие list2[4][1] - "драг_металлы"
                    {"AL", "CUPRUM"},              // раскрытие list2[4][2] - "цвет_металлы"
                    {"Ф_Пшеница"}                  // раскрытие list2[4][3] - "продовольствие"
            };
        }
        return result;
    }

    public String[][] setCurrenciesByLevels(boolean isFutures) {
        String[][] result;
        if (isFutures) {
            result = new String[][]{
                    {"FUSDRUB", "FEURRUB", "FCNYRUB"},   // раскрытие list2[7][0] - "1 level"
                    {"FCHFRUB", "FGBRRUB", "FBYNRUB"}    // раскрытие list2[7][1] - "2 level"
            };
        } else {
            result = new String[][]{
                    {"USDRUB", "EURRUB", "CNYRUB"},     // раскрытие list2[6][0] - "1 level"
                    {"CHFRUB", "GBRRUB", "BYNRUB"}      // раскрытие list2[6][1] - "2 level"
            };
        }
        return result;
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

    @Override
    public void addList2ListenerMoise(JList<String> list2, JList<String> list3, JCheckBox checkBox1) {
        list2.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {    // при одинарном клике мышью
                    // Получение выделенного элемента из окна 2
                    int selected2 = list2.locationToIndex(e.getPoint());
                    switch (selected1) {
                        case 0:
                            list3.setListData(indexes[selected2]);
                            break;
                        case 1:
                            list3.setListData(futuresIndexes[selected2]);
                            break;
                        case 2:
                            list3.setListData(stocksByLevels[selected2]);
                            // В реальности здесь будет список инструментов, отсортированный либо
                            // по алфавиту,
                            // либо  по объему торгов (ликвидности)
                            if (checkBox1.isSelected()) {
                                // с сортировкой по объему торгов (ликвидности)
                            } else {
                                //с сортировкой по алфавиту
                            }
                            break;
                        case 3:
                            list3.setListData(futuresStocksByLevels[selected2]);
                            break;
                        case 4:
                            list3.setListData(commodities[selected2]);
                            break;
                        case 5:
                            list3.setListData(futuresCommodities[selected2]);
                            break;
                        case 6:
                            list3.setListData(currentsByLevels[selected2]);
                            break;
                        case 7:
                            list3.setListData(futuresCurrenciesByLevels[selected2]);
                            break;
                    }
                }
            }
        });
    }

}
