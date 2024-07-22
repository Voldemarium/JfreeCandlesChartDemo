package stepanovvv.ru.candlestick;

import lombok.Getter;
import org.jfree.chart.*;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.panel.CrosshairOverlay;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.ohlc.OHLCSeries;
import org.jfree.data.time.ohlc.OHLCSeriesCollection;
import org.jfree.data.xy.OHLCDataset;
import org.jfree.data.xy.XYDataset;
import stepanovvv.ru.models.CandleMoex;
import stepanovvv.ru.models.MockListCandles;
import stepanovvv.ru.oldJFreecart.SegmentedTimeline;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Getter
public class JfreeCandlesChart extends JPanel implements ChartMouseListener {
    // Формат времени для считывания данных из объекта свечи
    private static final DateFormat READABLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final String DISPLAY_DATE_FORMAT = "dd.MM.yy";
    private final String offsetId = "+04:00";

    private final JFreeChart commonChart; // общий график

    private OHLCDataset ohlcDataSet;      // набор данных для построения свечного графика
    private XYDataset metricHi2DataSet;   // набор данных для построения метрик
    private XYDataset volumeDataSet;      // набор данных для построения графика объемоа

    private final ChartPanel commonChartPanel;  // Панель для общего графика

    private final Crosshair xCrosshair;               // часть перекрестия по оси Х
    private final Float crosshairWidth = 0.4f;        // толщина линии перекрестия
    private final Color crosshairColor = Color.GRAY;  // цвет линии перектрестия


    public JfreeCandlesChart(String title, boolean deletingHolidays) {
        // Create new chart
        commonChart = createChart(title, deletingHolidays);
        // Create new chart panel
        commonChartPanel = new ChartPanel(commonChart);

        // Устанавливаем размер панели с графиком по умолчанию
        commonChartPanel.setPreferredSize(new Dimension(1600, 800));
        // Enable zooming
        commonChartPanel.setMouseZoomable(true);
        commonChartPanel.setMouseWheelEnabled(true);

        // Add crosshairOverlay (добавляем перекрестие при наведении курсора мыши)
        this.commonChartPanel.addChartMouseListener(this);                // добавляем на панель слушатель мыши
        CrosshairOverlay commonCrosshairOverlay = new CrosshairOverlay(); // Создание объекта перекрестия на общем графике
        // Установка начального значения, цвета и ширины линии перекрести по оси Х
        // По оси Y не устанавливаем, т.к. это сделать невозможно, потому что на общей панели общим является
        // только время - по оси X
        this.xCrosshair = new Crosshair(Double.NaN, crosshairColor, new BasicStroke(crosshairWidth));
        this.xCrosshair.setLabelVisible(true);                     // делаем перекрестие по оси Х видимым
        commonCrosshairOverlay.addDomainCrosshair(xCrosshair);
        // Добавляем перекрестие на панель с общим графиком
        commonChartPanel.addOverlay(commonCrosshairOverlay);
        // Добавляем панель с графиком в нашу панель (необязательно, только в случае ее использования без возможности
        // уменьшать размер графиков изменением размера окна Frame)
        add(commonChartPanel);
    }


    private JFreeChart createChart(String chartTitle, boolean deletingHolidays) {
        List<CandleMoex> candleMoexList = new MockListCandles().getCandleMoexList();
//        // Добавляем данные со свечей на таймсерии
        addCandles(candleMoexList);

       // 1. Создаем график свечей D1
        JFreeChart candlesChart = createCandlesChart("Candles");
        // 2. Создаем график метрик Hi2
        JFreeChart metricsHi2Chart = createMetricsHi2Chart("metrics Hi2 Chart");
        // 3. Создаем график объемов D1
        JFreeChart volumeChart = createVolumeChart("Volume chart");

        // 4. Создаем основной график диаграммы с тремя подграфиками (candlestickSubplot,VolumeSubplot, buySellMetricSubplot)
        //		и одной общей осью времени dateAxis
        // Creating charts common dateAxis
        DateAxis dateAxis = new DateAxis("Time");

        // Формат надписи по оси времени
        dateAxis.setDateFormatOverride(new SimpleDateFormat(DISPLAY_DATE_FORMAT));
//		 reduce the default left/right margin from 0.05 to 0.02
        dateAxis.setLowerMargin(0.02);
        dateAxis.setUpperMargin(0.02);

        // Create mainPlot
        CombinedDomainXYPlot mainPlot = new CombinedDomainXYPlot(dateAxis);
        mainPlot.setGap(5.0); // Отступ между подграфиками
        //Добавление и установка пропорций размеров подграфиков
        mainPlot.add(candlesChart.getXYPlot(), 4);
        mainPlot.add(metricsHi2Chart.getXYPlot(), 1);
        mainPlot.add(volumeChart.getXYPlot(), 1);

        // Ориентация графиков
        mainPlot.setOrientation(PlotOrientation.VERTICAL);
        // Построение общего графика
        JFreeChart commonChart = new JFreeChart(chartTitle, JFreeChart.DEFAULT_TITLE_FONT, mainPlot, true);
        commonChart.removeLegend();

        // Если нужно удалить с графика выходные дни (субботу и воскресение)
        if (deletingHolidays) {
            SegmentedTimeline timeline = SegmentedTimeline.newMondayThroughFridayTimeline();
            dateAxis.setTimeline(timeline);
        }

        return commonChart;
    }

    private JFreeChart createCandlesChart(String chartTitle) {
        // Создаем график фабричным методом
        JFreeChart candlestickChart = ChartFactory.createCandlestickChart(chartTitle, "time", "Price",
                ohlcDataSet, true);
        // Create candlestick chart renderer (Создание средства визуализации свечных диаграмм)
        CandlestickRenderer candlestickRenderer = new CandlestickRenderer(
                CandlestickRenderer.WIDTHMETHOD_AVERAGE,  // свечи средней ширины
                false,                                    // обьем на графике со свечами не рисовать
                // объект CustomHighLowItemLabelGenerator = чтобы всплывало окошко при наведении мышки на свечу
                new CustomHighLowItemLabelGenerator(READABLE_DATE_FORMAT, new DecimalFormat("0.000")));
        XYPlot plot = (XYPlot) candlestickChart.getPlot();
        plot.setBackgroundPaint(new Color(255, 227, 190, 100)); // Цвет фона свечного графика
        plot.setRenderer(candlestickRenderer);
        NumberAxis priceAxis = (NumberAxis) plot.getRangeAxis();
        priceAxis.setAutoRangeIncludesZero(false); // показывать ли диапазон значений начиная от НОЛ
        return candlestickChart;
    }

    private JFreeChart createVolumeChart(String chartTitle) {
        // Создаем график фабричным методом
        JFreeChart volumeChart = ChartFactory.createTimeSeriesChart(chartTitle, "time",
                "Volume", volumeDataSet);

        // Create volume chart renderer (Создание средства визуализации баров (столбчатых диаграмм))
        XYBarRenderer xyBarRenderer = new XYBarRenderer(); // Вид графика - бары (столбчатая диаграмма)
        // При наведении курсора мыши на элемент графика показываем значение Y рядом с курсором
        xyBarRenderer.setDefaultToolTipGenerator((xyDataset, i, i1) -> xyDataset.getY(i, i1).toString());
        //видимость тени на баре
        xyBarRenderer.setShadowVisible(false);
        xyBarRenderer.setSeriesPaint(0, Color.cyan);
        xyBarRenderer.setMargin(0.1);
        XYPlot plot = (XYPlot) volumeChart.getPlot();
        plot.setRenderer(xyBarRenderer);
        plot.setBackgroundPaint(new Color(255, 227, 190, 100)); // Цвет фона графика
        return volumeChart;
    }

    private JFreeChart createMetricsHi2Chart(String chartTitle) {
        // Создаем график фабричным методом
        JFreeChart metricsHi2Chart = ChartFactory.createTimeSeriesChart(chartTitle, "time",
                "MetricsHi2", metricHi2DataSet);

        NumberAxis buy_sellMetricAxis = new NumberAxis("BUY/SELL_metric");
        buy_sellMetricAxis.setAutoRangeIncludesZero(true);    // показывать ли диапазон значений начиная от НОЛЯ
        // Set to no decimal
        buy_sellMetricAxis.setNumberFormatOverride(new DecimalFormat("0"));

        // Create metric chart renderer (Создание средства визуализации - линий с точками)
//        XYBezierRenderer timeRenderer_Buy_Sell = new XYBezierRenderer();
//        XYLineAndShapeRenderer timeRenderer_Buy_Sell = new XYLineAndShapeRenderer();
        XYSplineRenderer rendererHi2Metrics = new XYSplineRenderer();
        // При наведении курсора мыши на элемент графика показываем название таймсерии значение Y рядом с курсором
        // xyDataset.getSeriesKey(i) и значение Y
        rendererHi2Metrics.setDefaultToolTipGenerator((xyDataset, i, i1) ->
                Arrays.stream(xyDataset.getSeriesKey(i).toString().split("_")).findFirst().orElse("") +
                        "=" + xyDataset.getY(i, i1).toString());
        //Делаем видимыми нужные серии
        rendererHi2Metrics.setSeriesVisible(0, true);
        rendererHi2Metrics.setSeriesVisible(1, true);
        // Цвет серий
        rendererHi2Metrics.setSeriesPaint(0, Color.GREEN);
        rendererHi2Metrics.setSeriesPaint(1, Color.RED);
        // Настройка штриховых линий (если необходимо)
        rendererHi2Metrics.setSeriesStroke(0,
                new BasicStroke(2.0f,                // line width
                        BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1.0f,    // end gap, line join, miter limit
                        new float[]{6.0f,         // stroke length (длина штриха)
                                4.0f},       // gap between strokes (разрыв между штрихами)
                        0.0f                       // dashPhase
                )
        );
        rendererHi2Metrics.setSeriesStroke(1,
                new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1.0f,
                        new float[]{6.0f, 4.0f}, 0.0f));
        XYPlot plot = (XYPlot) metricsHi2Chart.getPlot();
        plot.setRenderer(rendererHi2Metrics);
        plot.setBackgroundPaint(new Color(255, 227, 190, 100)); // Цвет фона графика
        return metricsHi2Chart;
    }


    public void addCandles(List<CandleMoex> candleMoexList) {
        OHLCSeries ohlcSeries = new OHLCSeries("candles");
        TimeSeries buyMetricSeries = new TimeSeries("BUY");
        TimeSeries sellMetricSeries = new TimeSeries("SELL");
        TimeSeries volumeSeries = new TimeSeries("volume");

        double prevBuy = 0;
        double prevSell = 0;
        // Добавляем свечи на графмк в цикле
        for (int i = 0; i < candleMoexList.size(); i++) {
            CandleMoex candleMoex = candleMoexList.get(i);
            LocalDateTime dateTimeOpen = candleMoex.getBegin();
            Date date = new Date(dateTimeOpen.toEpochSecond(ZoneOffset.of(offsetId)) * 1000);
            Day t = new Day(date);

            // добавление свечи
            ohlcSeries.add(t, candleMoex.getOpen(), candleMoex.getHigh(), candleMoex.getLow(), candleMoex.getClose());
            // добавление объема на свече
            volumeSeries.add(t, candleMoex.getVolume());
            // добавление метрик
            if (i == 0) {
                buyMetricSeries.add(t, candleMoex.getBuyMetric());
                sellMetricSeries.add(t, candleMoex.getSellMetric());
            } else if (i < candleMoexList.size() - 1) {
                buyMetricSeries.add(t, prevBuy);
                sellMetricSeries.add(t, prevSell);
            } else {
                buyMetricSeries.add(t, prevBuy);
                sellMetricSeries.add(t, prevSell);
                LocalDateTime dateTimeOpen2 = candleMoex.getBegin().plusDays(1);
                Date date2 = new Date(dateTimeOpen2.toEpochSecond(ZoneOffset.of(offsetId)) * 1000);
                Day t2 = new Day(date2);
                buyMetricSeries.add(t2, candleMoex.getBuyMetric());
                sellMetricSeries.add(t2, candleMoex.getSellMetric());
            }
            prevBuy = candleMoex.getBuyMetric();
            prevSell = candleMoex.getSellMetric();
        }
        OHLCSeriesCollection candlestickDataset = new OHLCSeriesCollection();
        candlestickDataset.addSeries(ohlcSeries);
        ohlcDataSet = candlestickDataset;
        volumeDataSet = new TimeSeriesCollection(volumeSeries);
        TimeSeriesCollection metricHi2Collection = new TimeSeriesCollection();
        metricHi2Collection.addSeries(buyMetricSeries);
        metricHi2Collection.addSeries(sellMetricSeries);
        metricHi2DataSet = metricHi2Collection;
    }


    @Override  // Что будет происходить при клике мышью
    public void chartMouseClicked(ChartMouseEvent event) {
//        if (event.getEntity() instanceof XYItemEntity) { //Если клик мышки попал на график
//            XYItemEntity entity = (XYItemEntity) event.getEntity();
//            System.out.println(entity);
//            int seriesIndex = entity.getSeriesIndex();
//        }
    }

    @Override  // Что будет происходить при перемещении мыши
    public void chartMouseMoved(ChartMouseEvent event) {
        //1. Положение курсора мыши на экране
        int mouseX = event.getTrigger().getX();
        int mouseY = event.getTrigger().getY();
        Point mousePoint = commonChartPanel.getMousePosition();
        if (mousePoint != null) {
            //2. Вычисляем и устанавливаем общую координату Х(время) для перекрестия общего графика
            // Общая координатная область графика Rectangle2D dataArea (вместе с подграфиками),
            // на ней будет общей только координата Х (время)
            Rectangle2D dataArea = this.commonChartPanel.getScreenDataArea();
            JFreeChart chart = event.getChart();
            // Представим комбинированный график как обычный координатный график XYPlot, чтобы взять с него ось Х
            XYPlot commonPlot = (XYPlot) chart.getPlot();
            // Ось Х - время берем с общего графика (она будет проходить через все подграфики)
            ValueAxis xAxis = commonPlot.getDomainAxis();
            double x = xAxis.java2DToValue(event.getTrigger().getX(), dataArea, RectangleEdge.TOP);

            // 3.  Вычисляем и устанавливаем координату Y для перекрестия того подграфика, на котором находится курсор мыши
            // convert the Java2D coordinate to axis coordinates
            ChartRenderingInfo chartInfo = this.commonChartPanel.getChartRenderingInfo();
            Point2D java2DPoint = this.commonChartPanel.translateScreenToJava2D(mousePoint);
            PlotRenderingInfo plotInfo = chartInfo.getPlotInfo();
            // Индекс подграфика, на котором находится курсор мыши
            int subplotIndex = plotInfo.getSubplotIndex(java2DPoint);

            if (subplotIndex >= 0)   // yep, position the crosshairs
            {
                // Координатная область подграфика Rectangle2D panelArea, у которой берем координату Y
                Rectangle2D panelArea = this.commonChartPanel.getScreenDataArea(mouseX, mouseY);
                int index = 0;
                //  Общий график представляем как комбинированный график, чтобы извлечь из него подграфики
                CombinedDomainXYPlot combinedDomainXYPlot = (CombinedDomainXYPlot) chart.getPlot();
                for (XYPlot subplot : combinedDomainXYPlot.getSubplots()) {
                    if (subplotIndex == index && panelArea != null) {
                        double y = subplot.getRangeAxis().java2DToValue(mousePoint.getY(), panelArea, subplot.getRangeAxisEdge());
                        // Делаем перекрестие по шкале Y на подграфике видимым и устанавливаем значение y
                        subplot.setRangeCrosshairVisible(true);
                        subplot.setRangeCrosshairValue(y, true);

                    } else {
                        subplot.setRangeCrosshairVisible(false);
                    }
                    index++;
                }
            }

            this.xCrosshair.setValue(x);
            // Преобразование значения x из формата double в LocalDate
            this.xCrosshair.setLabelGenerator(crosshair -> {
                Instant instant = Instant.ofEpochMilli((long) crosshair.getValue());
                LocalDate localDate = LocalDate.ofInstant(instant, ZoneId.of(offsetId));
                return DateTimeFormatter.ofPattern("dd.MM").format(localDate);
            });
        }


    }
}
