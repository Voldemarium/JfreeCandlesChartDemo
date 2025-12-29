package stepanovvv.ru.examples.examplesCharts;

import org.jfree.chart.*;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.panel.CrosshairOverlay;
import org.jfree.chart.plot.Crosshair;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.data.time.Day;
import org.jfree.data.time.ohlc.OHLCSeries;
import org.jfree.data.time.ohlc.OHLCSeriesCollection;
import org.jfree.data.xy.*;
import stepanovvv.ru.candlestick.CustomHighLowItemLabelGenerator;
import stepanovvv.ru.models.native_moex_models.candles.CandleMoex;
import stepanovvv.ru.models.native_moex_models.candles.CandleMoexWithMetrics;
import stepanovvv.ru.models.native_moex_models.candles.MockListCandlesWithMetrics;
import stepanovvv.ru.candlestick.oldJFreecart.SegmentedTimeline;

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

import static org.jfree.data.general.DatasetUtils.findYValue;

/**
 * A demo showing crosshairs that follow the data points on an XYPlot.
 */
public class CandlestickChartDemo4 extends JFrame implements ChartMouseListener {
    private static final DateFormat READABLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final String DISPLAY_DATE_FORMAT = "dd.MM.yy";
    private final String offsetId = "+04:00";

    private ChartPanel chartPanel;

    private OHLCDataset ohlcDataSet;
    private XYDataset volumeSeries;

    private Crosshair xCrosshair;
    private Crosshair yCrosshair;

    public CandlestickChartDemo4(String title) {
        super(title);
        setContentPane(createContent(title));
    }

    private JPanel createContent(String title) {
        JFreeChart chart = createChart(title);
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


    private JFreeChart createChart(String chartTitle) {
        // Create candleMoexList
        List<CandleMoex> candleMoexList = new MockListCandlesWithMetrics().getCandleMoexWithMetricsList().stream()
                .map(CandleMoexWithMetrics::getCandleMoex).toList();
        // Добавляем данные со свечей на таймсерию
        addCandles(candleMoexList);
        // Создаем график фабричным методом
        JFreeChart candlestickChart = ChartFactory.createCandlestickChart(chartTitle, "time", "Price",
                ohlcDataSet, true);
//        JFreeChart commonChart = ChartFactory.createTimeSeriesChart("Volume", "time",
//                "Volume", volumeSeries);

        // Create volume chart renderer (Создание средства визуализации баров (столбчатых диаграмм))
//        XYBarRenderer xyBarRenderer = new XYBarRenderer(); // Вид графика - бары (столбчатая диаграмма)
//        // При наведении курсора мыши на элемент графика показываем значение Y рядом с курсором
//        xyBarRenderer.setDefaultToolTipGenerator((xyDataset, i, i1) -> xyDataset.getY(i, i1).toString());
//        //видимость тени на баре
//        xyBarRenderer.setShadowVisible(false);
//        xyBarRenderer.setSeriesPaint(0, Color.BLUE);
//        xyBarRenderer.setMargin(0.1);
//        XYPlot plot = (XYPlot) commonChart.getPlot();
//        plot.setRenderer(xyBarRenderer);

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

        // Формат надписи по оси времени
        DateAxis dateAxis = (DateAxis) plot.getDomainAxis();
                dateAxis.setDateFormatOverride(new SimpleDateFormat(DISPLAY_DATE_FORMAT));
//        // Если нужно удалить с графика выходные дни (субботу и воскресение)
        SegmentedTimeline timeline = SegmentedTimeline.newMondayThroughFridayTimeline();
        dateAxis.setTimeline(timeline);

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
        double y = findYValue(plot.getDataset(), 0, x);
        this.xCrosshair.setValue(x);
        // Преобразование значения x из формата double в LocalDate
        this.xCrosshair.setLabelGenerator(crosshair -> {
            Instant instant = Instant.ofEpochMilli((long) crosshair.getValue());
            LocalDate localDate = LocalDate.ofInstant(instant, ZoneId.of(offsetId));
            return DateTimeFormatter.ofPattern("dd.MM").format(localDate);
        });
        this.yCrosshair.setValue(y);
    }


    public void addCandles(List<CandleMoex> candleMoexList) {
        LocalDate prevDateTime = null;

        OHLCSeries ohlcSeries = new OHLCSeries("candles");
//        TimeSeries series = new TimeSeries("volume");
        // Добавляем свечи на графмк в цикле
        for (CandleMoex candleMoex : candleMoexList) {
            LocalDateTime dateTimeOpen = candleMoex.getBegin();
            Date date = new Date(dateTimeOpen.toEpochSecond(ZoneOffset.of(offsetId)) * 1000);
            Day t = new Day(date);

            // добавление свечи
            ohlcSeries.add(t, candleMoex.getOpen(), candleMoex.getHigh(), candleMoex.getLow(), candleMoex.getClose());
            // добавление объема на свече

//            series.add(t, candleMoex.getVolume());
            // добавление метрик
//            buyMetricSeries.add(t, candleMoex.getBuyMetric());
//            sellMetricSeries.add(t, candleMoex.getSellMetric());

            if (prevDateTime != null && !dateTimeOpen.toLocalDate().isEqual(prevDateTime.plusDays(1))) {
                System.out.println();
            }
            prevDateTime = dateTimeOpen.toLocalDate();
        }
        OHLCSeriesCollection candlestickDataset = new OHLCSeriesCollection();
        candlestickDataset.addSeries(ohlcSeries);
        ohlcDataSet = candlestickDataset;
//        volumeSeries = new TimeSeriesCollection(series);
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CandlestickChartDemo4 app = new CandlestickChartDemo4(
                    "JFreeChart: CrosshairOverlayDemo4.java");
            app.pack();
            app.setVisible(true);
        });

    }
}
