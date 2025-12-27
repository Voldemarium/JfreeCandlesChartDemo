package stepanovvv.ru.strategyPanel;

import lombok.extern.slf4j.Slf4j;
import stepanovvv.ru.service.MoexService;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@Slf4j
public class PanelFutureSpread_2 extends StrategyPanel {
    private final MoexService moexService = new MoexService();
    private static final StrategyName name = StrategyName.STRATEGY_FUTURE_SPREAD_2;
    private static final String[] list1 = new String[]{
            "calendar_future_spread", "between_simple_and_privileged"};
    private static final String[][] list2 = new String[][]{
            {"stocks", "indexes"},   // раскрытие list1[0] - "calendar_future_spread"
            {"stocks"},   // раскрытие list1[1] - "between_simple_and_privileged"
    };

    private final String[][] calendar_future_spread_2;
    private final String[][] between_simple_and_privileged_2;

    public PanelFutureSpread_2() {
        super();
        calendar_future_spread_2 = setCalendarFutureSpread_2();
        between_simple_and_privileged_2 = setBetweenSimpleAndPrivilegedSpread_2();

    }

    public String[][] setCalendarFutureSpread_2() {
        // Получаем из API MOEX даты экспирации
        return new String[][]{
                // Данные сохранены в БД (в БД хранить информацию о последнем обновлении, если новый день - обновить БД)
                {"AL",  "Stock_2"},   // раскрытие list2[0][0] - "calendar_future_spread"
                {"RTS", "MX"}   // раскрытие list2[0][1] - "calendar_future_spread" - "indexes"
        };
    }

    public String[][] setBetweenSimpleAndPrivilegedSpread_2() {
        return new String[][]{
                // Инструменты сохранять в БД (в БД хранить информацию о последнем обновлении, если новый день -
                // обновить БД)
                {"TATP" , "SBERP"}   // раскрытие list2[1][0] - "between_simple_and_privileged" - "stocks"
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
    public void addList2ListenerMoise(JList<String> list2, JList<String> list3, JCheckBox checkBox1) {
        list2.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {    // при одинарном клике мышью
                    // Получение выделенного элемента из окна 2
                    int selected2 = list2.locationToIndex(e.getPoint());
                    switch (selected1) {
                        case 0:
                            list3.setListData(calendar_future_spread_2[selected2]);
                            break;
                        case 1:
                            list3.setListData(between_simple_and_privileged_2[selected2]);
                            break;
                    }
                }
            }
        });
    }

}
