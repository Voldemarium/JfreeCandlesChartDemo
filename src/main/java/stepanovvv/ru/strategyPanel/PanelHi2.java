package stepanovvv.ru.strategyPanel;

import stepanovvv.ru.MyTerminal;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PanelHi2 extends StrategyPanel {
    private static final StrategyName name = StrategyName.STRATEGY_Hi2;
    private static final String[] list1 = new String[]{"Stocks", "Futures", "Currency"};
    private static final String[][] list2 = new String[][]{
            {"1 level", "2 level", "3 level"},                                // раскрытие list1[0] - "Stocks"
            {"1 level", "2 level", "3 level", "индексы", "товары", "валюта"}, // раскрытие list1[1] - "Futures"
            {"1 level", "2 level"}};                                          // раскрытие list1[2] - "Currency"
    private final String[][] stocksByLevels;
    private final String[][] futuresStocksByLevels;
    private final String[][] currentsByLevels;
    protected final JButton buttonFutureSpread_Strategy;

    public PanelHi2() {
        super();
        stocksByLevels = setStocksByLevels();
        futuresStocksByLevels = setFuturesStocksByLevels();
        currentsByLevels = setCurrentsByLevels();
        buttonFutureSpread_Strategy = new JButton("FutureSpread_Strategy");
        buttonFutureSpread_Strategy.addActionListener(e -> MyTerminal.main(new String[]{"Future Spread"}));
        add(buttonFutureSpread_Strategy);
    }

    public String[][] setStocksByLevels() {
        return new String[][]{
                // Инструменты сохранять в БД (в БД хранить информацию о последнем обновлении, если новый день -
                // обновить БД)
                {"SBER (Сбербанк)", "ALRS (АО \"Алроса\")", "AFK (АО \"Система\")"}, //раскрытие list2[0][0] - level 1
                {"AFK (АО \"Система\")", "JHHS", "UUYUY"},                           //раскрытие list2[0][1] - level 2
                {"HJHDSJHDF", "EDSD", "FDFDFF"}                                      //раскрытие list2[0][2] - level 3
        };
    }

    public String[][] setFuturesStocksByLevels() {
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

    @Override
    void addList2ListenerMoise(JList<String> list2, JList<String> list3, JCheckBox checkBox1) {
        list2.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {    // при одинарном клике мышью
                    // Получение выделенного элемента из окна 2
                    int selected2 = list2.locationToIndex(e.getPoint());
                    switch (selected1) {
                        case 0:
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
                        case 1:
                            list3.setListData(futuresStocksByLevels[selected2]);
                            break;
                        case 2:
                            list3.setListData(currentsByLevels[selected2]);
                            break;
                    }
                }
            }
        });
    }
}
