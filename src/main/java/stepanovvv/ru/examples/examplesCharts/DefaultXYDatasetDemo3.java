package stepanovvv.ru.examples.examplesCharts;

import org.jfree.chart.*;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.panel.CrosshairOverlay;
import org.jfree.chart.plot.Crosshair;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.data.xy.DefaultXYDataset;
import stepanovvv.ru.models.native_moex_models.candles.CandleMoex;
import stepanovvv.ru.models.native_moex_models.candles.MockListCandles;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.List;

import static org.jfree.data.general.DatasetUtils.findYValue;


/**
 * A demo showing crosshairs that follow the data points on an XYPlot.
 */
public class DefaultXYDatasetDemo3 extends JFrame implements ChartMouseListener {

    private ChartPanel chartPanel;

    private Crosshair xCrosshair;

    private Crosshair yCrosshair;

    public DefaultXYDatasetDemo3(String title) {
        super(title);
        setContentPane(createContent());
    }

    private JPanel createContent() {
        JFreeChart chart = createChart(createDataset());
        this.chartPanel = new ChartPanel(chart);
        this.chartPanel.addChartMouseListener(this);
        CrosshairOverlay crosshairOverlay = new CrosshairOverlay();
        this.xCrosshair = new Crosshair(Double.NaN, Color.BLUE, new BasicStroke(0.2f));
        this.xCrosshair.setLabelVisible(true);
        this.yCrosshair = new Crosshair(Double.NaN, Color.BLUE, new BasicStroke(0.2f));
        this.yCrosshair.setLabelVisible(true);
        crosshairOverlay.addDomainCrosshair(xCrosshair);
        crosshairOverlay.addRangeCrosshair(yCrosshair);
        chartPanel.addOverlay(crosshairOverlay);
        return chartPanel;
    }

    private JFreeChart createChart(DefaultXYDataset dataset) {
        return ChartFactory.createXYLineChart("Crosshair Demo",
                "X", "Y", dataset, PlotOrientation.VERTICAL, false, false, true);
    }

    private DefaultXYDataset createDataset() {
        DefaultXYDataset dataset = new DefaultXYDataset();
        // Create candleMoexList
        List<CandleMoex> candleMoexList = new MockListCandles().getCandleMoexList();
        //  Заполняем двумерный массив данных
        double[][] data = new double[][] {{22.1, 22.2, 22.3, 22.5}, {1.3, 34.0, 45.3, 55.33}};
        dataset.addSeries("Volume", data);
        return dataset;
    }

    @Override
    public void chartMouseClicked(ChartMouseEvent event) {
        // ignore
    }

    @Override
    public void chartMouseMoved(ChartMouseEvent event) {
        Rectangle2D dataArea = this.chartPanel.getScreenDataArea();
        JFreeChart chart = event.getChart();
        XYPlot plot = (XYPlot) chart.getPlot();
        ValueAxis xAxis = plot.getDomainAxis();
        double x = xAxis.java2DToValue(event.getTrigger().getX(), dataArea,
                RectangleEdge.BOTTOM);
        double y = findYValue(plot.getDataset(), 0, x);
        this.xCrosshair.setValue(x);
        this.yCrosshair.setValue(y);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DefaultXYDatasetDemo3 app = new DefaultXYDatasetDemo3(
                    "JFreeChart: CrosshairOverlayDemo1.java");
            app.pack();
            app.setVisible(true);
        });
    }

}
