package stepanovvv.ru.candlestick;

import java.awt.BorderLayout;
import java.awt.Color;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.ohlc.OHLCSeries;
import org.jfree.data.time.ohlc.OHLCSeriesCollection;


public class JfreeCandlesChart extends JPanel {
    // Формат времени для считывания данных из объекта свечи
    private static final DateFormat READABLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private OHLCSeries ohlcSeries;
    private TimeSeries volumeSeries;

    public JfreeCandlesChart(String title) {
        // Create new chart
        final JFreeChart candlestickChart = createChart(title);
        // Create new chart panel
        final ChartPanel chartPanel = new ChartPanel(candlestickChart);

//		chartPanel.setPreferredSize(new java.awt.Dimension(1800, 1000));
        chartPanel.setPreferredSize(new java.awt.Dimension(1800, 1000));
        // Enable zooming
        chartPanel.setMouseZoomable(true);
        chartPanel.setMouseWheelEnabled(true);
        add(chartPanel, BorderLayout.CENTER);
    }

    private JFreeChart createChart(String chartTitle) {
        // Create OHLCSeriesCollection as a price dataset for candlestick chart
        // (Создание OHLCSeriesCollection в качестве набора ценовых данных для свечного графика).
        OHLCSeriesCollection candlestickDataset = new OHLCSeriesCollection();
        ohlcSeries = new OHLCSeries("Price");
        candlestickDataset.addSeries(ohlcSeries);
        // Create candlestick chart priceAxis
        // (создание оси "Price")
        NumberAxis priceAxis = new NumberAxis("Price");
        priceAxis.setAutoRangeIncludesZero(false);
        // Create candlestick chart renderer ( Создание средства визуализации свечных диаграмм)
        CandlestickRenderer candlestickRenderer = new CandlestickRenderer(CandlestickRenderer.WIDTHMETHOD_AVERAGE,
                false, new CustomHighLowItemLabelGenerator(READABLE_DATE_FORMAT, new DecimalFormat("0.000")));
        // Create candlestickSubplot
        XYPlot candlestickSubplot = new XYPlot(candlestickDataset, null, priceAxis, candlestickRenderer);
        candlestickSubplot.setBackgroundPaint(Color.lightGray); // Цвет фона свечного графика

        // creates TimeSeriesCollection as a volume dataset for volume chart
        TimeSeriesCollection volumeDataset = new TimeSeriesCollection();
        volumeSeries = new TimeSeries("Volume");
        volumeDataset.addSeries(volumeSeries);
        // Create volume chart volumeAxis
        NumberAxis volumeAxis = new NumberAxis("Volume");
        volumeAxis.setAutoRangeIncludesZero(false);
        // Set to no decimal
        volumeAxis.setNumberFormatOverride(new DecimalFormat("0"));
        // Create volume chart renderer
        XYBarRenderer timeRenderer = new XYBarRenderer();
        timeRenderer.setShadowVisible(false);
        timeRenderer.setBaseToolTipGenerator(new StandardXYToolTipGenerator("Volume--> Time={1} Size={2}",
                new SimpleDateFormat("yyyy-MM-dd"), new DecimalFormat("0.00")));
        // Create volumeSubplot
        XYPlot volumeSubplot = new XYPlot(volumeDataset, null, volumeAxis, timeRenderer);
//        volumeSubplot.setOrientation(PlotOrientation.VERTICAL);
        volumeSubplot.setBackgroundPaint(Color.white);

//		Создаем основной график диаграммы с двумя подграфиками (candlestickSubplot,VolumeSubplot)
        //		и одной общей осью времени dateAxis
        // Creating charts common dateAxis
        DateAxis dateAxis = new DateAxis("Time");
        // Формат надписи по оси времени
        dateAxis.setDateFormatOverride(new SimpleDateFormat("dd.MM.yy"));
//		 reduce the default left/right margin from 0.05 to 0.02
        dateAxis.setLowerMargin(0.02);
        dateAxis.setUpperMargin(0.02);
        // Create mainPlot
        CombinedDomainXYPlot mainPlot = new CombinedDomainXYPlot(dateAxis);
        mainPlot.setGap(10.0); // Отступ между подграфиками
        //Добавление и установка пропорций размеров подграфиков
        mainPlot.add(candlestickSubplot, 4);
        mainPlot.add(volumeSubplot, 1);
        // Ориентация графиков
        mainPlot.setOrientation(PlotOrientation.VERTICAL);

        JFreeChart chart = new JFreeChart(chartTitle, JFreeChart.DEFAULT_TITLE_FONT, mainPlot, true);
        chart.removeLegend();
        return chart;
    }


    // метод добавление свечи М1 и ее объема на график
    public void addCandel(LocalDateTime dateTimeOpen, double o, double h, double l, double c, double v) {
        Date date = new Date(dateTimeOpen.toEpochSecond(ZoneOffset.of("+04:00")) * 1000);
        Day t = new Day(date);
        // добавление свечи М1
        ohlcSeries.add(t, o, h, l, c);
        // добавление объема М1
        volumeSeries.add(t, v);

    }

}
