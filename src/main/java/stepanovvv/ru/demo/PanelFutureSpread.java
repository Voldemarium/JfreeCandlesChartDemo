package stepanovvv.ru.demo;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PanelFutureSpread extends StrategyPanel{
    private final String[][] stocksByLevels = {
            {"SBER (Сбербанк)", "ALRS (АО \"Алроса\")", "AFK (АО \"Система\")"},
            {"AFK (АО \"Система\")", "JHHS", "UUYUY"},
            {"HJHDSJHDF", "EDSD", "FDFDFF"}
    };
    private final String[][] futuresStocksByLevels = {
            {"FSBER (Сбербанк)", "FALRS (АО \"Алроса\")", "FAFK (АО \"Система\")"},
            {"FAFK (АО \"Система\")", "FJHHS", "FUUYUY"},
            {"FHJHDSJHDF", "FEDSD", "FDFDFF"},
            {"RTS", "MOEX"},
            {"GOLD", "BRENT"},
            {"USDRUB", "EUR"}
    };
    private final String[][] currentsByLevels = {
            {"USDRUB", "EURRUB", "CNYRUB"},
            {"CHFRUB", "GBRRUB", "BYNRUB"}
    };

    public PanelFutureSpread() {
      super();
      setVisible(true);
    }

    @Override
    StrategyName setStrategyName() {
        return StrategyName.STRATEGY_FUTURE_SPREAD;
    }

    @Override
    String[] setDataList1() {
        return new String[] {"Indexes", "Indexes futures", "Stocks", "Stocks Futures"};
    }

    @Override
    String[][] setDataList2() {
        return new String[][] {
                {"1 level", "2 level", "3 level"},
                {"1 level", "2 level", "3 level", "индексы", "товары", "валюта"},
                {"1 level", "2 level"}};
    }

    @Override
    JList<String> getStringJList1() {
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
