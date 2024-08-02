package stepanovvv.ru.examples.examplesCharts;

import org.jfree.chart.*;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;

public class DefaultCategoryDatasetDemo2 extends JFrame {
    private ChartPanel chartPanel;
 // Перекрестие невозможно установить на данный график DefaultCategory, только на XY (DefaultXY)

    public DefaultCategoryDatasetDemo2(String title) {
        super(title);
        setContentPane(createContent());
    }

    private JPanel createContent() {
        DefaultCategoryDataset dataset = (DefaultCategoryDataset) createDataset();
        JFreeChart chart = createChart(dataset);
        this.chartPanel = new ChartPanel(chart);
        return chartPanel;
    }

    private JFreeChart createChart(DefaultCategoryDataset dataset) {
        return ChartFactory.createBarChart("VolumeChart", "time", "Volume",
                dataset);
    }

    private CategoryDataset createDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(343, "volumeDataset", "12.07");
        dataset.addValue(332, "volumeDataset", "13.07");
        dataset.addValue(145, "volumeDataset", "14.07");
        dataset.addValue(454, "volumeDataset", "17.07");
        return dataset;
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DefaultCategoryDatasetDemo2 app = new DefaultCategoryDatasetDemo2("JFreeChart: CrosshairOverlayDemo2.java");
            app.pack();
            app.setVisible(true);
        });
    }

}
