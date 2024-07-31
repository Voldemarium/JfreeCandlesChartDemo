package stepanovvv.ru.demo;

import javax.swing.*;
import javax.swing.text.DateFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Date;

public class PanelHi2 extends JPanel {
    // Данные списка
    private final String[] dataList = {"Stocks", "Futures", "Currency"};
    private int selected1;
    private final String[][] dataText = {
            {"1 level", "2 level", "3 level"},
            {"1 level", "2 level", "3 level", "индексы", "товары", "валюта"},
            {"1 level", "2 level"}
    };
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
    private final JTextArea dateInfo;
    private final JFormattedTextField fromDatesField;
    private final JFormattedTextField tillDatesField;
    private final JButton buttonD1_Hi2;
    private final ButtonGroup timeFramebuttonGroup;
    private final JButton buttonAddChart;

    public PanelHi2() {
        // Устанавливаем размер панели
        this.setPreferredSize(new Dimension(180, 500));

        // 1. Создание списка 1 выбора типов инструментов
        final JList<String> list1 = new JList<>(dataList);
        //Метод setPrototypeCellValue() позволяет определить ширину JList по длине указанной строки.
        list1.setPrototypeCellValue("Фьючерсы");
        list1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Выделение одного элемента списка.

        // 2. Создание списка 2 выбора типов инструментов
        final JList<String> list2 = new JList<>();
        //Метод setPrototypeCellValue() позволяет определить ширину JList по длине указанной строки.
        list2.setPrototypeCellValue("1 уровень");
        list2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Выделение одного элемента списка.

        // 3. Кнопка выбора сортировки инструментов
        JCheckBox checkBox1 = new JCheckBox("sort by liquidity");

        // 4. Создание списка 3 выбора типов инструментов
        final JList<String> list3 = new JList<>();
        //Метод setPrototypeCellValue() позволяет определить ширину JList по длине указанной строки.
        list3.setPrototypeCellValue("SBER (Сбербанк)aaaaa");
        list3.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Выделение одного элемента списка.

        // 5. Создание текстового поля для выбора инструментов
        dateInfo = new JTextArea(3, 15); // от этих параметров зависит размер текстового окошка

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
        long time = LocalDateTime.now().minusYears(1).toEpochSecond(ZoneOffset.of("+3")) * 1000;
        fromDatesField.setValue(new Date(time));
        // Создание форматированного текстового поля даты
        tillDatesField = new JFormattedTextField(dateFormatter);
        tillDatesField.setColumns(8);
        tillDatesField.setValue(new Date());

        // 7. Кнопка выбора сортировки инструментов
        JCheckBox checkBox2 = new JCheckBox("remove weekends");

        // 8. Создание кнопки отрисовки графиков
        buttonD1_Hi2 = new JButton("Rendering Hi2 D1");

        timeFramebuttonGroup = new ButtonGroup();
        JRadioButton button_M = new JRadioButton("M");
        timeFramebuttonGroup.add(button_M);
        JRadioButton button_W = new JRadioButton("W");
        timeFramebuttonGroup.add(button_W);
        JRadioButton button_D1 = new JRadioButton("D1");
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

        buttonAddChart = new JButton("add chart");

        // Подключение слушателя мыши на первый список
        list1.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {    // при одинарном клике мышью
                    // Получение выделенного элемента из окна 1
                    selected1 = list1.locationToIndex(e.getPoint());
                    list2.setListData(dataText[selected1]);
                }
            }
        });

        // Подключение слушателя мыши на 2 список
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

        // Подключение слушателя мыши на 3 список
        list3.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {    // при одинарном клике мышью
                    //извлекаем даты из инструмента, по которым есть данные Hi2
                    String fromDate = "12.04.2020";
                    String tillDate = "15.07.2024";

                    String builder = "   -----possible dates----- \n" +
                            "      from: " + fromDate + "\n" +
                            "      till:     " + tillDate;
                    dateInfo.setText(builder);
                }
            }
        });

        // Подключение слушателя мыши на кнопку запуска графика D1
        buttonD1_Hi2.addActionListener(e -> {
            // Действие при нажатии кнопки
            // Получение тикера инструмента
            String selected = list3.getSelectedValue();
            if (selected != null) {
                String selectedTicker = Arrays.stream(selected.split(" ")).findFirst().orElse(null);
                // Получение дат инструмента
                Date fromDate = (Date) fromDatesField.getValue();
                Date tillDate = (Date) tillDatesField.getValue();
                LocalDate fromLocalDate = LocalDate.ofEpochDay(fromDate.getTime() / 86_400_000);
                LocalDate tillLocalDate = LocalDate.ofEpochDay(tillDate.getTime() / 86_400_000);
                //Запуск программы с выбранными на панели параметрами
                JfreeChartHi2.addChart(selectedTicker, fromLocalDate, tillLocalDate, Timeframe.D1,
                        checkBox2.isSelected());
            } else {
                //  всплывающее окно-подсказка "Выберите инструмент!"
                System.out.println("Выберите инструмент");
            }
        });


        buttonAddChart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
                    timeframe = Timeframe.M30;
                } else if (button_M10.isSelected()) {
                    timeframe = Timeframe.M10;
                } else if (button_M5.isSelected()) {
                    timeframe = Timeframe.M5;
                } else if (button_M1.isSelected()) {
                    timeframe = Timeframe.M1;
                }
                // Получение тикера инструмента
                String selected = list3.getSelectedValue();
                if (selected != null) {
                    String selectedTicker = Arrays.stream(selected.split(" ")).findFirst().orElse(null);
                    // Получение дат инструмента
                    Date fromDate = (Date) fromDatesField.getValue();
                    Date tillDate = (Date) tillDatesField.getValue();
                    LocalDate fromLocalDate = LocalDate.ofEpochDay(fromDate.getTime() / 86_400_000);
                    LocalDate tillLocalDate = LocalDate.ofEpochDay(tillDate.getTime() / 86_400_000);
                    //Запуск программы с выбранными на панели параметрами
                    JfreeChartHi2.addChart(selectedTicker, fromLocalDate, tillLocalDate, timeframe,
                            checkBox2.isSelected());
                } else {
                    //  всплывающее окно-подсказка "Выберите инструмент!"
                    System.out.println("Выберите инструмент");
                }
            }
        });


        // Размещение компонентов в интерфейсе панели
        add(new Label("STRATEGY Hi2"));
        add(new JScrollPane(list1));
        add(new JScrollPane(list2));
        add(checkBox1);
        add(new JScrollPane(list3));
        add(new JScrollPane(dateInfo));
        add(new JLabel("from "));
        add(fromDatesField);
        add(new JLabel("    till "));
        add(tillDatesField);
        add(checkBox2);
        add(buttonD1_Hi2);
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
        add(buttonAddChart);
        setVisible(true);
    }
}





