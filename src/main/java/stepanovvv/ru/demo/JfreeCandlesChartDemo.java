package stepanovvv.ru.demo;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import stepanovvv.ru.candlestick.JfreeCandlesChart;

import javax.swing.*;
import java.awt.*;


@AllArgsConstructor
@NoArgsConstructor
public class JfreeCandlesChartDemo  {
    private String ticker;
    private boolean deletingHolidays = true;
    private Strategy strategy = Strategy.HI2;


    private void createAndShowGUI() {
        //Make sure we have nice window decorations.
        JFrame.setDefaultLookAndFeelDecorated(true);

        //Create and set up the window.
        JFrame frame = new JFrame("JfreeCandlestickChartDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the chart.
        JfreeCandlesChart jfreeCandlesChart = new JfreeCandlesChart(ticker, deletingHolidays);

        //Так добавляется  org.jfree.chart.ChartPanel, включающая в себя org.jfree.chart.ChartPanel (при этом при изменении
        // размера Frame с помощью мыши не будет меняться размер панели с графиками)
//        frame.setContentPane(jfreeCandlesChart);
        //Так добавляется org.jfree.chart.ChartPanel (при этом при изменении размера Frame с помощью мыши
        //  будет меняться размер панели с графиками)

//        frame.setContentPane(jfreeCandlesChart.getCommonChartPanel());
        frame.add(jfreeCandlesChart.getCommonChartPanel(), BorderLayout.CENTER);

        // Вспомогательная панель справа
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());

        // Панель с кнопками для выбора стратегии
        JPanel panel1 = new JPanel();
        panel1.setPreferredSize(new Dimension(180,120));
        panel1.add(new JLabel("-------------------------------"));
        JButton buttonHi2 = new JButton("Hi2");
        buttonHi2.addActionListener(e -> main(null)); // новый запуск отрисовки графика по стратегии Hi2
        panel1.add(buttonHi2);
        panel1.add(new JButton("Another strategy"));  // запуск отрисовки графика по другой стратегии
        panel1.add(new JLabel("-------------------------------"));

        JPanel panel2 = null;
        if (strategy == Strategy.HI2) {
            panel2 = new PanelHi2();
        }
        // Панель внутри правой панели (верхняя)
//        JPanel panelHi2 = new PanelHi2();

        rightPanel.add(panel1, BorderLayout.NORTH); // добавляем панель с кнопками в правую панель сверху
        assert panel2 != null;
        rightPanel.add(panel2, BorderLayout.CENTER); // добавляем панель в правую панель в центр
        frame.add(rightPanel, BorderLayout.EAST);    // добавляем правую панель справа


        //Disable the resizing feature
        frame.setResizable(true);
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        JfreeCandlesChartDemo jfreeCandlestickChartDemo = new JfreeCandlesChartDemo();
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(jfreeCandlestickChartDemo::createAndShowGUI);
    }
}
