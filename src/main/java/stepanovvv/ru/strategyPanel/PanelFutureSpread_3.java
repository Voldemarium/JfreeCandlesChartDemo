package stepanovvv.ru.strategyPanel;

import lombok.extern.slf4j.Slf4j;
import stepanovvv.ru.MyTerminal;
import stepanovvv.ru.service.MoexService;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

@Slf4j
public class PanelFutureSpread_3 extends StrategyPanel {
    private final MoexService moexService = new MoexService();
    private static final StrategyName name = StrategyName.STRATEGY_FUTURE_SPREAD_3;
    private static final String strategyUrl = "futuresSpread3";
    private static final String[] list1 = new String[]{
            "currency_future_spread_3", "metal_future_spread_3", "index_future_spread_3"};
    private static final String[][] list2 = new String[][]{
            {"CN,Si,UC", "asdddd1"},   // раскрытие list1[0] - "currency_future_spread" (RUS:SI1!/RUS:CR1!/RUS:UC1!)
            {"GL,Si,GD", "asdddd2"},   // раскрытие list1[1] - "metal_future_spread" (100*(31103.4768*RUS:GL1!/RUS:SI1!-RUS:GD1!)/RUS:GD1!)
            {"Ri,Mx,Si", "asdddd3"},   // раскрытие list1[2] - "index_future_spread" (100*(RUS:SI1!/1000*RUS:RI1!/50/0.63-RUS:MX1!)/RUS:MX1!)
    };

    private final String[][] currency_future_spread_3;
    private final String[][] metal_future_spread_3;
    private final String[][] index_future_spread_3;


    public PanelFutureSpread_3(MyTerminal myTerminal) {
        super(myTerminal);
        currency_future_spread_3 = setCurrencyFutureSpread_3();
        metal_future_spread_3 = setMetalFutureSpread_3();
        index_future_spread_3 = setIndexFutureSpread_3();

//        Галочка-выбор добавлять ли на график EMA (Экспоненциальная скользящая средняя)
        add(checkBox5);
    }

    public String[][] setCurrencyFutureSpread_3() {
        // Получаем из API MOEX даты экспирации
        List<String> listOfExpirationDates = moexService.getExpirationDateForCurrenciesFuturesSpread_3();

        String[] arrayOfExpirationDates = listOfExpirationDates.toArray(String[]::new);
        return new String[][]{
                // Данные сохранены в БД (в БД хранить информацию о последнем обновлении, если новый день - обновить БД)
                arrayOfExpirationDates,   // раскрытие list2[0][0] - "currency_future_spread_3" (RUS:SI1!/RUS:CR1!/RUS:UC1!)
                {"exp_date_1" , "exp_date_2", "exp_date_3" , "exp_date_4"}   // раскрытие list2[0][1] - "currency_future_spread_3" - "asdddd1"
        };
    }

    public String[][] setMetalFutureSpread_3() {
        return new String[][]{
                // Инструменты сохранять в БД (в БД хранить информацию о последнем обновлении, если новый день -
                // обновить БД)
                {"exp_date_1" , "exp_date_2", "exp_date_3" , "exp_date_4"},   // раскрытие list2[1][0] - "metal_future_spread" (100*(31103.4768*RUS:GL1!/RUS:SI1!-RUS:GD1!)/RUS:GD1!)
                {"exp_date_1" , "exp_date_2", "exp_date_3" , "exp_date_4"}   // раскрытие list2[1][1] - "metal_future_spread" - "asdddd2"
        };
    }

    public String[][] setIndexFutureSpread_3() {
        return new String[][]{
                // Инструменты сохранять в БД (в БД хранить информацию о последнем обновлении, если новый день -
                // обновить БД)
                {"CN,Si,UC"},   // раскрытие list2[0][0] - "currency_future_spread" (RUS:SI1!/RUS:CR1!/RUS:UC1!)
                {"GL,Si,GD"},   // раскрытие list2[0][1] - "metal_future_spread" (100*(31103.4768*RUS:GL1!/RUS:SI1!-RUS:GD1!)/RUS:GD1!)
                {"Ri,Mx,Si"},   // раскрытие list2[0][2] - "index_future_spread" (100*(RUS:SI1!/1000*RUS:RI1!/50/0.63-RUS:MX1!)/RUS:MX1!)
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

    @Override
    public void addList2ListenerMoise(JList<String> list2, JList<String> list3, JCheckBox checkBox1) {
        list2.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {    // при одинарном клике мышью
                    // Получение выделенного элемента из окна 2
                    int selected2 = list2.locationToIndex(e.getPoint());
                    switch (selected1) {
                        case 0:
                            list3.setListData(currency_future_spread_3[selected2]);
                            break;
                        case 1:
                            list3.setListData(metal_future_spread_3[selected2]);
                            break;
                        case 2:
                            list3.setListData(index_future_spread_3[selected2]);
                            break;
                    }
                }
            }
        });
    }

}
