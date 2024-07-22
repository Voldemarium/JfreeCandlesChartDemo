package stepanovvv.ru.examples;

import org.jfree.chart.*;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.panel.CrosshairOverlay;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.ohlc.OHLCSeries;
import org.jfree.data.time.ohlc.OHLCSeriesCollection;
import org.jfree.data.xy.OHLCDataset;
import org.jfree.data.xy.XYDataset;
import stepanovvv.ru.candlestick.CustomHighLowItemLabelGenerator;
import stepanovvv.ru.models.CandleMoex;
import stepanovvv.ru.models.MockListCandles;
import stepanovvv.ru.oldJFreecart.SegmentedTimeline;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

/**
 * A demo showing crosshairs that follow the data points on an XYPlot.
 */
public class CandlesAndBarsDemo5 extends JFrame implements ChartMouseListener {
    private static final DateFormat READABLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final String DISPLAY_DATE_FORMAT = "dd.MM.yy";
    private final String offsetId = "+04:00";
    private final Float crosshairWidth = 0.4f;
    private final Color crosshairColor = Color.GRAY;

    private ChartPanel chartPanel;

    private OHLCDataset ohlcDataSet;
    private XYDataset volumeDataset;
//    private XYDataset volumeDataset2;

    private Crosshair xCrosshair;
//    private Crosshair yCrosshair;

    public CandlesAndBarsDemo5(String title) {
        super(title);
        setContentPane(createContent(title));
    }

    private JPanel createContent(String title) {
        // Create candleMoexList
        List<CandleMoex> candleMoexList = new MockListCandles().getCandleMoexList();
        // Добавляем данные со свечей на таймсерию
        addCandles(candleMoexList);

        JFreeChart chart1 = createChart2("Candles");
        JFreeChart chart2 = createVolumeChart("Volume");

        XYPlot plot1 = (XYPlot) chart1.getPlot();
        XYPlot plot2 = (XYPlot) chart2.getPlot();

        // Формат надписи по оси времени
        DateAxis dateAxis = (DateAxis) plot2.getDomainAxis();
        dateAxis.setDateFormatOverride(new SimpleDateFormat(DISPLAY_DATE_FORMAT));
//        // Если нужно удалить с графика выходные дни (субботу и воскресение)
        SegmentedTimeline timeline = SegmentedTimeline.newMondayThroughFridayTimeline();
        dateAxis.setTimeline(timeline);

        // Create mainPlot
        CombinedDomainXYPlot mainPlot = new CombinedDomainXYPlot(dateAxis);
        mainPlot.setGap(5.0); // Отступ между подграфиками
        //Добавление и установка пропорций размеров подграфиков
        mainPlot.add(plot1, 4);
        mainPlot.add(plot2, 1);
//        mainPlot.add(volumeSubplot, 1);
        // Ориентация графиков
        mainPlot.setOrientation(PlotOrientation.VERTICAL);
        // Построение общего графика
        JFreeChart commonChart = new JFreeChart("Title mainPlot", JFreeChart.DEFAULT_TITLE_FONT, mainPlot, true);
        commonChart.removeLegend();


        this.chartPanel = new ChartPanel(commonChart);

        this.chartPanel.addChartMouseListener(this);
        CrosshairOverlay crosshairOverlay = new CrosshairOverlay();
        this.xCrosshair = new Crosshair(Double.NaN, Color.BLUE, new BasicStroke(0.2f));
        this.xCrosshair.setLabelVisible(true);
//        this.yCrosshair = new Crosshair(Double.NaN, Color.BLUE, new BasicStroke(0.2f));
//        this.yCrosshair.setLabelVisible(true);
        crosshairOverlay.addDomainCrosshair(xCrosshair);
//        crosshairOverlay.addRangeCrosshair(yCrosshair);
        chartPanel.addOverlay(crosshairOverlay);
        return chartPanel;
    }


    private JFreeChart createVolumeChart(String chartTitle) {
        // Создаем график фабричным методом
        JFreeChart volumeChart = ChartFactory.createTimeSeriesChart("Volume", "time",
                "Volume", volumeDataset);

        // Create volume chart renderer (Создание средства визуализации баров (столбчатых диаграмм))
        XYBarRenderer xyBarRenderer = new XYBarRenderer(); // Вид графика - бары (столбчатая диаграмма)
        // При наведении курсора мыши на элемент графика показываем значение Y рядом с курсором
        xyBarRenderer.setDefaultToolTipGenerator((xyDataset, i, i1) -> xyDataset.getY(i, i1).toString());
        //видимость тени на баре
        xyBarRenderer.setShadowVisible(false);
        xyBarRenderer.setSeriesPaint(0, Color.BLUE);
        xyBarRenderer.setMargin(0.1);
        XYPlot plot = (XYPlot) volumeChart.getPlot();
        plot.setRenderer(xyBarRenderer);

        // Формат надписи по оси времени
//        DateAxis dateAxis = (DateAxis) plot.getDomainAxis();
//                dateAxis.setDateFormatOverride(new SimpleDateFormat(DISPLAY_DATE_FORMAT));
////        // Если нужно удалить с графика выходные дни (субботу и воскресение)
//        SegmentedTimeline timeline = SegmentedTimeline.newMondayThroughFridayTimeline();
//        dateAxis.setTimeline(timeline);

        return volumeChart;
    }

    private JFreeChart createChart2(String chartTitle) {
        JFreeChart candlestickChart = ChartFactory.createCandlestickChart(chartTitle, "time", "Price",
                ohlcDataSet, true);
        // Create candlestick chart renderer (Создание средства визуализации свечных диаграмм)
        CandlestickRenderer candlestickRenderer = new CandlestickRenderer(
                CandlestickRenderer.WIDTHMETHOD_AVERAGE,  // свечи средней ширины
                false,                                    // обьем на графике со свечами не рисовать
                // объект CustomHighLowItemLabelGenerator = чтобы всплывало окошко при наведении мышки на свечу
                new CustomHighLowItemLabelGenerator(READABLE_DATE_FORMAT, new DecimalFormat("0.000")));
        XYPlot plot = (XYPlot) candlestickChart.getPlot();
        plot.setRenderer(candlestickRenderer);
        NumberAxis priceAxis = (NumberAxis) plot.getRangeAxis();
        priceAxis.setAutoRangeIncludesZero(false); // показывать ли диапазон значений начиная от НОЛ

        return candlestickChart;
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
//        double y = findYValue(plot.getDataset(), 0, x);
        this.xCrosshair.setValue(x);
        // Преобразование значения x из формата double в LocalDate
        this.xCrosshair.setLabelGenerator(crosshair -> {
            Instant instant = Instant.ofEpochMilli((long) crosshair.getValue());
            LocalDate localDate = LocalDate.ofInstant(instant, ZoneId.of(offsetId));
            return DateTimeFormatter.ofPattern("dd.MM").format(localDate);
        });
//        this.yCrosshair.setValue(y);
    }


    public void addCandles(List<CandleMoex> candleMoexList) {
        LocalDate prevDateTime = null;

        TimeSeries series = new TimeSeries("volume");
        OHLCSeries ohlcSeries = new OHLCSeries("candles");
        // Добавляем свечи на графмк в цикле
        for (CandleMoex candleMoex : candleMoexList) {
            LocalDateTime dateTimeOpen = candleMoex.getBegin();
            Date date = new Date(dateTimeOpen.toEpochSecond(ZoneOffset.of(offsetId)) * 1000);
            Day t = new Day(date);

            // добавление свечи
            ohlcSeries.add(t, candleMoex.getOpen(), candleMoex.getHigh(), candleMoex.getLow(), candleMoex.getClose());
            // добавление объема на свече

            series.add(t, candleMoex.getVolume());
            // добавление метрик
//            buyMetricSeries.add(t, candleMoex.getBuyMetric());
//            sellMetricSeries.add(t, candleMoex.getSellMetric());

            if (prevDateTime != null && !dateTimeOpen.toLocalDate().isEqual(prevDateTime.plusDays(1))) {
                System.out.println();
            }
            prevDateTime = dateTimeOpen.toLocalDate();
        }
        volumeDataset = new TimeSeriesCollection(series);
        OHLCSeriesCollection candlestickDataset = new OHLCSeriesCollection();
        candlestickDataset.addSeries(ohlcSeries);
        ohlcDataSet = candlestickDataset;
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CandlesAndBarsDemo5 app = new CandlesAndBarsDemo5(
                    "JFreeChart: CrosshairOverlayDemo5.java");
            app.pack();
            app.setVisible(true);
        });

    }
}
