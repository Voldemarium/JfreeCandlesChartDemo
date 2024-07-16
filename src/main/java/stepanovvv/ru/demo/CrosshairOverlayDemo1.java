package stepanovvv.ru.demo;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.geom.Rectangle2D;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.panel.CrosshairOverlay;
import org.jfree.chart.plot.Crosshair;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import static org.jfree.data.general.DatasetUtils.findYValue;


/**
 * A demo showing crosshairs that follow the data points on an XYPlot.
 */
public class CrosshairOverlayDemo1 extends JFrame implements ChartMouseListener {

    private ChartPanel chartPanel;

    private Crosshair xCrosshair;

    private Crosshair yCrosshair;

    public CrosshairOverlayDemo1(String title) {
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

    private JFreeChart createChart(XYDataset dataset) {
        return ChartFactory.createXYLineChart("Crosshair Demo",
                "X", "Y", dataset, PlotOrientation.VERTICAL, false, false, true);
    }

    private XYDataset createDataset() {
        XYSeries series = new XYSeries("S1");
        for (int x = 0; x < 10; x++) {
            series.add(x, x + Math.random() * 4.0);
        }
        return new XYSeriesCollection(series);
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
            CrosshairOverlayDemo1 app = new CrosshairOverlayDemo1(
                    "JFreeChart: CrosshairOverlayDemo1.java");
            app.pack();
            app.setVisible(true);
        });
    }

}
