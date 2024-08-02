package stepanovvv.ru.examples.examlesModelTests.examlesModelTests;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Date;

import javax.swing.*;
import javax.swing.text.DateFormatter;

public class MyPanelHi2 extends JPanel {
    // Данные списка
    private final String[] dataList = {"Stocks", "Futures", "Currency"};
    private int selected1;
    private final String[][] dataText = {
            {"1 уровень", "2 уровень", "3 уровень"},
            {"1 уровень", "2 уровень", "3 уровень", "индексы", "товары", "валюта"},
            {"1 уровень", "2 уровень"}
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
    private final JButton button;


    public MyPanelHi2() {
//        super("Выбор инструмента");
//        setDefaultCloseOperation(EXIT_ON_CLOSE);
        // Создание панели
//        JPanel contents = new JPanel();
//        contents.setSize(180, 400);
//        super();

        this.setPreferredSize(new Dimension(180,500));

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
        JCheckBox checkBox = new JCheckBox("sort by liquidity");

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


        // 7. Создание кнопки отрисовки графиков
        button = new JButton("Rendering");

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
                            if (checkBox.isSelected()) {
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

        // Подключение слушателя мыши на кнопку
        button.addActionListener(e -> {
            // Действие при нажатии кнопки
            // Получение тикера инструмента
            String selectedTicker = Arrays.stream(list3.getSelectedValue().split(" "))
                    .findFirst().orElse(null);
            // Получение дат инструмента
            Date fromDate = (Date) fromDatesField.getValue();
            Date tillDate = (Date) tillDatesField.getValue();
            LocalDate fromLocalDate = LocalDate.ofEpochDay(fromDate.getTime() / 86_400_000);
            LocalDate tillLocalDate = LocalDate.ofEpochDay(tillDate.getTime() / 86_400_000);
            System.out.println(selectedTicker);
            System.out.println(fromLocalDate);
            System.out.println(tillLocalDate);
        });

        // Размещение компонентов в интерфейсе
//        contents.add(checkBox);
//        contents.add(new JScrollPane(list1));
//        contents.add(new JScrollPane(list2));
//        contents.add(new JScrollPane(list3));
//        contents.add(new JScrollPane(dateInfo));
//        contents.add(new JLabel("from "));
//        contents.add(fromDatesField);
//        contents.add(new JLabel("    till "));
//        contents.add(tillDatesField);
//        contents.add(button);
        add(new Label("STRATEGY Hi2"));
        add(new JScrollPane(list1));
        add(new JScrollPane(list2));
        add(checkBox);
        add(new JScrollPane(list3));
        add(new JScrollPane(dateInfo));
        add(new JLabel("from "));
        add(fromDatesField);
        add(new JLabel("    till "));
        add(tillDatesField);
        add(button);
        // Вывод окна
//        setContentPane(contents);
//        setSize(180, 500); // Размер окна Frame
        setVisible(true);
    }


//    public static void main(String[] args) {
//        new MyPanel();
//    }
}




