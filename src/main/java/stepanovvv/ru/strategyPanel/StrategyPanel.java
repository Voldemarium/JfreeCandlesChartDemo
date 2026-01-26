package stepanovvv.ru.strategyPanel;

import lombok.extern.slf4j.Slf4j;
import org.jfree.chart.ChartPanel;
import stepanovvv.ru.MyTerminal;
import stepanovvv.ru.candlestick.JfreeChartPanel;
import stepanovvv.ru.models.ParametersMA;
import stepanovvv.ru.pop_up_warnings.WrongMove;

import javax.swing.*;
import javax.swing.text.DateFormatter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Arrays;
import java.util.Date;

@Slf4j
public abstract class StrategyPanel extends JPanel {
    protected StrategyName strategyName;
    protected String strategyUrl;
    protected int selected1;
    protected String[] dataList1;
    final JList<String> list1;
    protected String[][] dataList2;
    final JList<String> list2;
    final JList<String> list3;
    protected final JFormattedTextField fromDatesField;
    protected final JFormattedTextField tillDatesField;
    protected final JFormattedTextField periodEMAField;
    protected final ButtonGroup timeFramebuttonGroup;
    protected final JButton buttonCreateNewChart;
    protected MyTerminal myTerminal;

    final JCheckBox checkBox1;
    final JCheckBox checkBox2;
    final JCheckBox checkBox3;
    final JCheckBox checkBox4;
    final JCheckBox checkBox5;

    public StrategyPanel(MyTerminal myTerminal) {
        this.myTerminal = myTerminal;
        strategyName = setStrategyName();
        strategyUrl = setStrategyUrl();
        dataList1 = setDataList1();
        dataList2 = setDataList2();

        // Устанавливаем размер панели (заданные здесь ее размеры будут определять размеры всего окна Frame)
        this.setPreferredSize(new Dimension(180, 950));

        // 1. Создание списка 1 выбора типов инструментов (через абстрактный метод)
        list1 = getStringJList1();

        // 2. Создание списка 2 выбора типов инструментов
        list2 = new JList<>();
        //Метод setPrototypeCellValue() позволяет определить ширину JList по длине указанной строки.
        list2.setPrototypeCellValue("МСК_недвижимость");             // задаем длину строки
        list2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Выделение одного элемента списка.

        // 3. Кнопка выбора сортировки инструментов
        checkBox1 = new JCheckBox("sort by liquidity");

        // 4. Создание списка 3 выбора типов инструментов
        list3 = new JList<>();
        //Метод setPrototypeCellValue() позволяет определить ширину JList по длине указанной строки.
        list3.setPrototypeCellValue("SBER (Сбербанк)aaaaa");         // задаем длину строки
        list3.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Выделение одного элемента списка.

        // 5. Создание текстового поля
//        dateInfo = new JTextArea(3, 15); // от этих параметров зависит размер текстового окошка

        // 6. Создание полей для выбора дат
        // Определение маски и поля ввода даты
        DateFormat date = new SimpleDateFormat("dd MM yyyy");
        // Форматирующий объект даты
        DateFormatter dateFormatter = new DateFormatter(date);
        dateFormatter.setAllowsInvalid(false);
        dateFormatter.setOverwriteMode(true);
        // Создание форматированного текстового поля даты
        fromDatesField = new JFormattedTextField(dateFormatter);
        fromDatesField.setColumns(8);
        // Настоящее время МСК в миллисекундах
        long fromTime = LocalDateTime.now().minusYears(1).toEpochSecond(MyTerminal.zoneOffset) * 1000;
        Date fromDate = new Date(fromTime);
        Date tillDate = new Date();
        fromDatesField.setValue(fromDate);
        // Создание форматированного текстового поля даты
        tillDatesField = new JFormattedTextField(dateFormatter);
        tillDatesField.setColumns(8);
        tillDatesField.setValue(tillDate);

        // 7.1 Галочка-выбор убирать ли с графика выходные дни (субботу и воскресение)
        checkBox2 = new JCheckBox(" remove weekends ");
        // 7.2 Галочка-выбор добавлять ли график объемов
        checkBox3 = new JCheckBox(" add volume chart ");
        // 7.3 Галочка-выбор добавлять ли на график маркетпрофиль (горизонтальные объемы)
        checkBox4 = new JCheckBox("market profile");
        // 7.5. Галочка-выбор добавлять ли на график EMA (Экспоненциальная скользящая средняя)
        checkBox5 = new JCheckBox("    add EMA     ");

        // Создание форматированного текстового поля для параметров EMA
        periodEMAField = new JFormattedTextField();
        periodEMAField.setColumns(3);
        periodEMAField.setValue(20); // устанавливаем период EMA по умолчанию


        timeFramebuttonGroup = new ButtonGroup();
        JRadioButton button_M = new JRadioButton("M");
        timeFramebuttonGroup.add(button_M);
        JRadioButton button_W = new JRadioButton("W");
        timeFramebuttonGroup.add(button_W);
        JRadioButton button_D1 = new JRadioButton("D1");
        button_D1.setSelected(true);   // Выбрана по умолчанию
        timeFramebuttonGroup.add(button_D1);
        JRadioButton button_H4 = new JRadioButton("H4");
        timeFramebuttonGroup.add(button_H4);
        JRadioButton button_H1 = new JRadioButton("H1");
        timeFramebuttonGroup.add(button_H1);
        JRadioButton button_M30 = new JRadioButton("M30");
        timeFramebuttonGroup.add(button_M30);
        JRadioButton button_M15 = new JRadioButton("M15");
        timeFramebuttonGroup.add(button_M15);
        JRadioButton button_M10 = new JRadioButton("M10");
        timeFramebuttonGroup.add(button_M10);
        JRadioButton button_M5 = new JRadioButton("M5");
        timeFramebuttonGroup.add(button_M5);
        JRadioButton button_M1 = new JRadioButton("M1");
        timeFramebuttonGroup.add(button_M1);

        buttonCreateNewChart = new JButton("create new chart");

        // Подключение слушателя мыши на первый список
        addList1ListenerMoise(list1, list2, dataList2);
//
//        // Подключение слушателя мыши на 2 список
        addList2ListenerMoise(list2, list3, checkBox1);

        // Подключение слушателя мыши на 3 список
//        list3.addMouseListener(new MouseAdapter() {
//            public void mouseClicked(MouseEvent e) {
//                if (e.getClickCount() == 1) {    // при одинарном клике мышью
//                    //извлекаем даты из инструмента, по которым есть данные Hi2
//                    String fromDate = "12.04.2020";
//                    String tillDate = "now date";
//
//                    String builder = "   --possible dates Algopack-- \n" +
//                            "      from: " + fromDate + "\n" +
//                            "      till: " + tillDate;
//                    dateInfo.setText(builder);
//                }
//            }
//        });



        buttonCreateNewChart.addActionListener(e -> {
            Timeframe timeframe = null;
            if (button_M.isSelected()) {
                timeframe = Timeframe.M;
            } else if (button_W.isSelected()) {
                timeframe = Timeframe.W;
            } else if (button_D1.isSelected()) {
                timeframe = Timeframe.D1;
            } else if (button_H4.isSelected()) {
                timeframe = Timeframe.H4;
            } else if (button_H1.isSelected()) {
                timeframe = Timeframe.H1;
            } else if (button_M30.isSelected()) {
                timeframe = Timeframe.M30;
            } else if (button_M15.isSelected()) {
                timeframe = Timeframe.M15;
            } else if (button_M10.isSelected()) {
                timeframe = Timeframe.M10;
            } else if (button_M5.isSelected()) {
                timeframe = Timeframe.M5;
            } else if (button_M1.isSelected()) {
                timeframe = Timeframe.M1;
            }

            // Получение вида инструмента
            String selectedInstrumentOfList1 = list1.getSelectedValue();
            // Получение подвида инструмента
            String selectedInstrumentOfList2 = list2.getSelectedValue();
            // Получение тикера инструмента (или даты экспирации для Spread3)
            String selectedOfList3 = list3.getSelectedValue();

            if (selectedOfList3 != null) {
                /// Создание нового графика с заменой
                createNewChart(myTerminal, selectedOfList3, selectedInstrumentOfList1, selectedInstrumentOfList2,
                        timeframe, checkBox2, checkBox3, checkBox4, checkBox5);
            } else {
                //  всплывающее окно-подсказка "Выберите инструмент!"
                WrongMove.main(null);
            }
        });

        // Размещение компонентов в интерфейсе панели
        add(new Label("  " + strategyName + "  "));
        JScrollPane jScrollPane1 = new JScrollPane(list1);
        add(jScrollPane1);
        add(new JScrollPane(list2));
        add(checkBox1);
        add(new JScrollPane(list3));
//        add(new JScrollPane(dateInfo));
        add(new JLabel("from "));
        add(fromDatesField);
        add(new JLabel("        till "));
        add(tillDatesField);

        add(checkBox2);
        add(checkBox3);

       // добавляем кнопки из группы кнопок timeFramebuttonGroup
        add(button_M);
        add(button_W);
        add(button_D1);
        add(button_H4);
        add(button_H1);
        add(button_M30);
        add(button_M15);
        add(button_M10);
        add(button_M5);
        add(button_M1);

        add(checkBox5);
        add(new JLabel("period EMA"));
        add(periodEMAField);

        add(buttonCreateNewChart);
        setVisible(true);
    }

    /// Создание нового графика с заменой
    void createNewChart(MyTerminal myTerminal, String selectedOfList3, String selectedInstrumentOfList1,
                        String selectedInstrumentOfList2, Timeframe timeframe,
                        JCheckBox checkBox2, JCheckBox checkBox3, JCheckBox checkBox4, JCheckBox checkBox5) {
        String selectedTickerOrExpDateOfList3 = Arrays.stream(selectedOfList3.split(" ")).findFirst().orElse(null);
        // Получение дат инструмента
        Date fromDate = (Date) fromDatesField.getValue();
        Date tillDate = (Date) tillDatesField.getValue();
        LocalDate fromLocalDate = fromDate.toInstant().atZone(MyTerminal.zoneId).toLocalDate();
        LocalDate tillLocalDate = tillDate.toInstant().atZone(MyTerminal.zoneId).toLocalDate();

        boolean isMarketProfile;
        if (checkBox4 == null) {
            isMarketProfile = false;
        } else {
            isMarketProfile = checkBox4.isSelected();
        }

        ParametersMA parametersMA = null;
        if (checkBox5.isSelected()) {
            Integer periodMA = (Integer) periodEMAField.getValue();
            if (periodMA != null) {
                parametersMA = new ParametersMA();
                parametersMA.setPeriodMA(periodMA);
            }

        }

        // Создание нового графика с выбранными на панели параметрами
        JfreeChartPanel newJfreeChart = new JfreeChartPanel(strategyUrl, selectedInstrumentOfList1, selectedInstrumentOfList2,
                selectedTickerOrExpDateOfList3, fromLocalDate, tillLocalDate, timeframe,
                checkBox2.isSelected(), // Галочка-выбор убирать ли с графика выходные дни (субботу и воскресение)
                checkBox3.isSelected(), // Галочка-выбор добавлять ли график объемов
                isMarketProfile,        // Галочка-выбор добавлять ли на график маркетпрофиль (горизонтальные объемы)
                parametersMA);         // Добавлять на график EMA

        // Со Swing-компонентами лучше работать в диспетчерском потоке событий (EDT), чтобы избегать проблем
        // с конкурентным доступом. Для этого потребуется SwingUtilities.invokeLater()
        SwingUtilities.invokeLater(() -> {
            // Вносим изменения в интерфейс, всё под контролем...
            Container container = myTerminal.getContentPane();
            ChartPanel chartPanel = myTerminal.getChartPanel();
            container.remove(chartPanel);
            ChartPanel newChartPanel = newJfreeChart.getCommonChartPanel();
            container.add(newChartPanel, BorderLayout.CENTER);
            newChartPanel.revalidate(); // Подготавливаемся к обновлению макета
            myTerminal.setChartPanel(newChartPanel); // сохраняем ссылку на обновленную панель с графиком
            newChartPanel.repaint(); // Перерисовка - И теперь всё выглядит обновленным!
        });
    }

    public abstract StrategyName setStrategyName();

    public abstract String setStrategyUrl();

    public abstract String[] setDataList1();

    public abstract String[][] setDataList2();

    public abstract JList<String> getStringJList1();

    private void addList1ListenerMoise(JList<String> list1, JList<String> list2, String[][] dataList2) {
        list1.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {    // при одинарном клике мышью
                    // Получение выделенного элемента из окна 1
                    selected1 = list1.locationToIndex(e.getPoint());
                    list2.setListData(dataList2[selected1]);
                }
            }
        });
    }

    abstract void addList2ListenerMoise(JList<String> list2, JList<String> list3, JCheckBox checkBox1);
}





