package stepanovvv.ru.examples.examplesCharts;

import lombok.Getter;
import org.jfree.chart.*;
import org.jfree.chart.axis.*;
import org.jfree.chart.panel.CrosshairOverlay;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.xy.*;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.ohlc.OHLCSeries;
import org.jfree.data.time.ohlc.OHLCSeriesCollection;
import org.jfree.data.xy.OHLCDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import stepanovvv.ru.candlestick.CustomHighLowItemLabelGenerator;
import stepanovvv.ru.candlestick.MarketProfileRenderer;
import stepanovvv.ru.examples.examlesModelTests.examlesModelTests.MyPanelHi2;
import stepanovvv.ru.models.native_moex_models.candles.CandleMoex;
import stepanovvv.ru.models.native_moex_models.candles.CandleMoexWithMetrics;
import stepanovvv.ru.models.native_moex_models.candles.MockListCandlesWithMetrics;
import stepanovvv.ru.candlestick.oldJFreecart.SegmentedTimeline;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * A demo showing crosshairs that follow the data points on an XYPlot.
 */
@Getter
public class CandlesVolMarketProfileDemo5 extends JFrame implements ChartMouseListener {
    private static final DateFormat READABLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final String DISPLAY_DATE_FORMAT = "dd.MM.yy";
    private final String offsetId = "+04:00";
    private final Float crosshairWidth = 0.4f;
    private final Color crosshairColor = Color.GRAY;

    private final ChartPanel chartPanel;

    private OHLCDataset ohlcDataSet;
    private XYDataset volumeDataset;
//    private XYDataset volumeDataset2;

    private Crosshair xCrosshair;
//    private Crosshair yCrosshair;

    public CandlesVolMarketProfileDemo5(String title) {
        super(title);
//        setContentPane(createContent(title));
        /// Добавление панели с графиком
        this.chartPanel = createContent(title, 0);
        chartPanel.setName("Chart");
        this.add(chartPanel, BorderLayout.CENTER);

        // Вспомогательная панель справа
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());

        // Панель с кнопками для выбора стратегии
        JPanel panel1 = new JPanel();
        panel1.setPreferredSize(new Dimension(180,120));
        panel1.add(new JLabel("-------------------------------"));
        JButton buttonHi2 = new JButton("Hi2");
        buttonHi2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {;
                main(null);
            }
        });
        panel1.add(buttonHi2);
        panel1.add(new JButton("Another strategy"));
        panel1.add(new JLabel("-------------------------------"));

       // Панель внутри правой панели (верхняя)
        JPanel myPanelHi2 = new MyPanelHi2(this);

        rightPanel.add(panel1, BorderLayout.NORTH); // добавляем панель с кнопками в правую панель сверху
        rightPanel.add(myPanelHi2, BorderLayout.CENTER); // добавляем панель в правую панель в центр
        this.add(rightPanel, BorderLayout.EAST);    // добавляем правую панель справа

    }

    /// Построение панели с графиком
    public ChartPanel createContent(String title, int var) {
        // Create candleMoexList
        List<CandleMoex> mockCandleMoexList = new MockListCandlesWithMetrics().getCandleMoexWithMetricsList().stream()
                .map(CandleMoexWithMetrics::getCandleMoex)
                .toList();
        List<CandleMoex> candleMoexList = new ArrayList<>();
        if (var == 0) {
            candleMoexList.add(mockCandleMoexList.get(0));
            candleMoexList.add(mockCandleMoexList.get(1));
        } else {
            candleMoexList.add(mockCandleMoexList.get(0));
            candleMoexList.add(mockCandleMoexList.get(1));
            candleMoexList.add(mockCandleMoexList.get(2));
            candleMoexList.add(mockCandleMoexList.get(3));
        }

        // Добавляем данные со свечей на таймсерию
        addCandles(candleMoexList);

        JFreeChart chart1 = createCandlesChart("Candles");
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


//        this.chartPanel = new ChartPanel(commonChart);
        ChartPanel chartPanel = new ChartPanel(commonChart);
        // Устанавливаем размер панели с графиком по умолчанию
        chartPanel.setPreferredSize(new Dimension(1600, 800));
        // Enable zooming
        chartPanel.setMouseZoomable(true);
        chartPanel.setMouseWheelEnabled(true);

        chartPanel.addChartMouseListener(this);
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

    private JFreeChart createCandlesChart(String chartTitle) {
        JFreeChart candlestickChart = ChartFactory.createCandlestickChart(chartTitle, "time", "Price",
                ohlcDataSet, true);

        // Create MarketProfile
        // Create horizontal volume chart renderer (Создание средства визуализации горизонтальных объемов)
        MarketProfileRenderer xyMarketProfileRenderer = new MarketProfileRenderer(); // Вид графика - бары (столбчатая диаграмма)
        // При наведении курсора мыши на элемент графика показываем значение Y рядом с курсором
        xyMarketProfileRenderer.setDefaultToolTipGenerator((xyDataset, i, i1) -> xyDataset.getY(i, i1).toString());
        xyMarketProfileRenderer.setSeriesPaint(0, Color.cyan);

        // add secondary axis
        XYPlot plot = candlestickChart.getXYPlot();
        plot.setDataset(1, createMarketProfileDataset());
        NumberAxis axisX2 = new NumberAxis("VolPrice");
        axisX2.setAutoRangeIncludesZero(false);
        plot.setDomainAxis(1, axisX2);
        plot.setDomainAxisLocation(1, AxisLocation.getOpposite(plot.getDomainAxisLocation(0)));
        plot.setRenderer(1, xyMarketProfileRenderer);
        plot.mapDatasetToDomainAxis(1, 1);


        // Create candlestick chart renderer (Создание средства визуализации свечных диаграмм)
        CandlestickRenderer candlestickRenderer = new CandlestickRenderer(
                CandlestickRenderer.WIDTHMETHOD_AVERAGE,  // свечи средней ширины
                false,                                    // обьем на графике со свечами не рисовать
                // объект CustomHighLowItemLabelGenerator = чтобы всплывало окошко при наведении мышки на свечу
                new CustomHighLowItemLabelGenerator(READABLE_DATE_FORMAT, new DecimalFormat("0.000")));
//        XYPlot plot = (XYPlot) candlestickChart.getPlot();
        plot.setRenderer(0, candlestickRenderer);
        NumberAxis priceAxis = (NumberAxis) plot.getRangeAxis();
        priceAxis.setAutoRangeIncludesZero(false); // показывать ли диапазон значений начиная от НОЛ

        return candlestickChart;
    }


    private XYDataset createMarketProfileDataset() {
        final XYSeries marketProfileSeries = new XYSeries("Series 2");
        marketProfileSeries.add(10.0, 301.2);
        marketProfileSeries.add(11.0, 300.5);
        marketProfileSeries.add(12.0, 300.1);
        marketProfileSeries.add(13.0, 299.7);
        marketProfileSeries.add(15.0, 299.3);
        marketProfileSeries.add(20.0, 297.3);
        marketProfileSeries.add(25.0, 295.3);
        marketProfileSeries.add(30.0, 294.3);
        marketProfileSeries.add(25.0, 293.3);
        marketProfileSeries.add(20.0, 292.3);
        marketProfileSeries.add(17.0, 290.3);
        return new XYSeriesCollection(marketProfileSeries);

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
            CandlesVolMarketProfileDemo5 app = new CandlesVolMarketProfileDemo5(
                    "JFreeChart: CrosshairOverlayDemo5.java");
            app.pack();
            app.setVisible(true);
        });

    }
}
