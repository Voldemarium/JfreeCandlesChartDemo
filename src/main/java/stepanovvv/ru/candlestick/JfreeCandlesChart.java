package stepanovvv.ru.candlestick;

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

import javax.swing.JPanel;

import org.jfree.chart.*;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.panel.CrosshairOverlay;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYBezierRenderer;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.ohlc.OHLCSeries;
import org.jfree.data.time.ohlc.OHLCSeriesCollection;

public class JfreeCandlesChart extends JPanel implements ChartMouseListener {
    // Формат времени для считывания данных из объекта свечи
    private static final DateFormat READABLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final String DISPLAY_DATE_FORMAT = "dd.MM.yy";
    private final String offsetId = "+04:00";

    private final JFreeChart candlestickChart;

    private OHLCSeries ohlcSeries;
    private TimeSeries volumeSeries;

    private TimeSeries sellMetricSeries;
    private TimeSeries buyMetricSeries;

    private final ChartPanel chartPanel;
    private final Crosshair xCrosshair;
//    private final Crosshair yCrosshair;
    private final Float crosshairWidth = 0.4f;
    private final Color crosshairColor = Color.GRAY;

    public JfreeCandlesChart(String title) {
        // Create new chart
        candlestickChart = createChart(title);
        // Create new chart panel
        chartPanel = new ChartPanel(candlestickChart);
        // Устанавливаем размер панели с графиком
        chartPanel.setPreferredSize(new Dimension(1600, 800));
        // Enable zooming
        chartPanel.setMouseZoomable(true);
        chartPanel.setMouseWheelEnabled(true);

        // Add crosshairOverlay (добавляем перекрестие при наведении курсора мыши)
        this.chartPanel.addChartMouseListener(this);                // добавляем на панель слушатель мыши
        CrosshairOverlay commonCrosshairOverlay = new CrosshairOverlay(); // Создание объекта перекрестия на общем графике
        // Установка начального значения, цвета и ширины линии перекрести по оси Х
        // По оси Y не устанавливаем, т.к. это сделать невозможно, потому что на общей панели общим является
        // только время - по оси X
        this.xCrosshair = new Crosshair(Double.NaN, crosshairColor, new BasicStroke(crosshairWidth));
        this.xCrosshair.setLabelVisible(true);                     // делаем перекрестие по оси Х видимым

//        this.yCrosshair = new Crosshair(Double.NaN, crosshairColor, new BasicStroke(crosshairWidth));
//        this.yCrosshair.setLabelVisible(true);                     // делаем перекрестие по оси Y видимым

        commonCrosshairOverlay.addDomainCrosshair(xCrosshair);
//        commonCrosshairOverlay.addRangeCrosshair(yCrosshair);
        chartPanel.addOverlay(commonCrosshairOverlay);
        add(chartPanel, BorderLayout.CENTER);
    }

    private JFreeChart createChart(String chartTitle) {

        //1.  Create OHLCSeriesCollection as a price dataset for candlestick chart
        // (Создание OHLCSeriesCollection в качестве набора ценовых данных для свечного графика).
        OHLCSeriesCollection candlestickDataset = new OHLCSeriesCollection();
        ohlcSeries = new OHLCSeries("Price");
        candlestickDataset.addSeries(ohlcSeries);
        // Create candlestick chart priceAxis (создание оси "Price")
        NumberAxis priceAxis = new NumberAxis("Price");
        priceAxis.setAutoRangeIncludesZero(false); // показывать ли диапазон значений начиная от НОЛЯ
        // Create candlestick chart renderer (Создание средства визуализации свечных диаграмм)
        CandlestickRenderer candlestickRenderer = new CandlestickRenderer(
                CandlestickRenderer.WIDTHMETHOD_AVERAGE,  // свечи средней ширины
                false,                                    // обьем на графике со свечами не рисовать
                // объект CustomHighLowItemLabelGenerator = чтобы всплывало окошко при наведении мышки на свечу
                new CustomHighLowItemLabelGenerator(READABLE_DATE_FORMAT, new DecimalFormat("0.000")));
        // Create candlestickSubplot
        XYPlot candlestickSubplot = new XYPlot(candlestickDataset, null, priceAxis, candlestickRenderer);
//        candlestickSubplot.setBackgroundPaint(Color.lightGray); // Цвет и степень прозрачности фона свечного графика RGB
        candlestickSubplot.setBackgroundPaint(new Color(255, 227, 190, 100)); // Цвет фона свечного графика

        //2. Creates TimeSeriesCollection as a volume dataset for volume chart
        TimeSeriesCollection volumeDataset = new TimeSeriesCollection();
        volumeSeries = new TimeSeries("Volume");
        volumeDataset.addSeries(volumeSeries);
        // Create volume chart volumeAxis
        NumberAxis volumeAxis = new NumberAxis("Volume");
        volumeAxis.setAutoRangeIncludesZero(true); // показывать ли диапазон значений начиная от НОЛЯ
        // Set to no decimal
        volumeAxis.setNumberFormatOverride(new DecimalFormat("0"));
        // Create volume chart renderer (Создание средства визуализации баров (столбчатых диаграмм))
        XYBarRenderer timeRenderer = new XYBarRenderer(); // Вид графика - бары (столбчатая диаграмма)
        // При наведении курсора мыши на элемент графика показываем значение Y рядом с курсором
        timeRenderer.setDefaultToolTipGenerator((xyDataset, i, i1) -> xyDataset.getY(i, i1).toString());
        //видимость тени на баре
        timeRenderer.setShadowVisible(false);
        // Create volumeSubplot
        XYPlot volumeSubplot = new XYPlot(volumeDataset, null, volumeAxis, timeRenderer);
//        volumeSubplot.setOrientation(PlotOrientation.VERTICAL);
        volumeSubplot.setBackgroundPaint(new Color(255, 227, 190, 100));

        // 3. Create TimeSeriesCollection as a BUY/SELL_metric dataset for BUY/SELL_metric chart
        TimeSeriesCollection buySellMetricDataset = new TimeSeriesCollection();
        buyMetricSeries = new TimeSeries("BUY_metric");
        sellMetricSeries = new TimeSeries("SELL_metric");
        buySellMetricDataset.addSeries(sellMetricSeries);
        buySellMetricDataset.addSeries(buyMetricSeries);
        NumberAxis buy_sellMetricAxis = new NumberAxis("BUY/SELL_metric");
        buy_sellMetricAxis.setAutoRangeIncludesZero(true);    // показывать ли диапазон значений начиная от НОЛЯ
        // Set to no decimal
        buy_sellMetricAxis.setNumberFormatOverride(new DecimalFormat("0"));
        // Create volume chart renderer (Создание средства визуализации - линий с точками)
//        XYBezierRenderer timeRenderer_Buy_Sell = new XYBezierRenderer();
        XYBezierRenderer timeRenderer_Buy_Sell = new XYBezierRenderer();
        // При наведении курсора мыши на элемент графика показываем название таймсерии значение Y рядом с курсором
        // xyDataset.getSeriesKey(i) и значение Y
        timeRenderer_Buy_Sell.setDefaultToolTipGenerator((xyDataset, i, i1) ->
                Arrays.stream(xyDataset.getSeriesKey(i).toString().split("_")).findFirst().orElse("") +
                        "=" + xyDataset.getY(i, i1).toString());
        //Делаем видимыми нужные серии
        timeRenderer_Buy_Sell.setSeriesVisible(0, true);
        timeRenderer_Buy_Sell.setSeriesVisible(1, true);
        // Create buySellMetricSubplot
        XYPlot buySellMetricSubplot = new XYPlot(buySellMetricDataset, null, buy_sellMetricAxis, timeRenderer_Buy_Sell);
        volumeSubplot.setBackgroundPaint(new Color(255, 227, 190, 100));

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
        mainPlot.add(candlestickSubplot, 4);
        mainPlot.add(buySellMetricSubplot, 1);
        mainPlot.add(volumeSubplot, 1);

        // Ориентация графиков
        mainPlot.setOrientation(PlotOrientation.VERTICAL);
        // Построение общего графика
        JFreeChart chart = new JFreeChart(chartTitle, JFreeChart.DEFAULT_TITLE_FONT, mainPlot, true);
        chart.removeLegend();
        return chart;
    }


    // метод добавление свечи и ее объема на график
    public void addCandel(LocalDateTime dateTimeOpen, double o, double h, double l, double c, double v,
                          double buyMetric, double sellMetric
    ) {
        Date date = new Date(dateTimeOpen.toEpochSecond(ZoneOffset.of(offsetId)) * 1000);
        Day t = new Day(date);
        // добавление свечи
        ohlcSeries.add(t, o, h, l, c);
        // добавление объема неа свече
        volumeSeries.add(t, v);
        // добавление метрик
        buyMetricSeries.add(t, buyMetric);
        sellMetricSeries.add(t, sellMetric);

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
        Point mousePoint = chartPanel.getMousePosition();

        //2. Вычисляем и устанавливаем общую координату Х(время) для перекрестия общего графика
        // Общая координатная область графика Rectangle2D dataArea (вместе с подграфиками),
        // на ней будет общей только координата Х (время)
        Rectangle2D dataArea = this.chartPanel.getScreenDataArea();
        JFreeChart chart = event.getChart();
        // Представим комбинированный график как обычный координатный график XYPlot, чтобы взять с него ось Х
        XYPlot commonPlot = (XYPlot) chart.getPlot();
        // Ось Х - время берем с общего графика (она будет проходить через все подграфики)
        ValueAxis xAxis = commonPlot.getDomainAxis();
        double x = xAxis.java2DToValue(event.getTrigger().getX(), dataArea, RectangleEdge.TOP);

        // 3.  Вычисляем и устанавливаем координату Y для перекрестия того подграфика, на котором находится курсор мыши
        // convert the Java2D coordinate to axis coordinates
        ChartRenderingInfo chartInfo = this.chartPanel.getChartRenderingInfo();
        Point2D java2DPoint = this.chartPanel.translateScreenToJava2D(mousePoint);
        PlotRenderingInfo plotInfo = chartInfo.getPlotInfo();
        // Индекс подграфика, на котором находится курсор мыши
        int subplotIndex = plotInfo.getSubplotIndex(java2DPoint);

        if (subplotIndex >= 0)   // yep, position the crosshairs
        {
            // Координатная область подграфика Rectangle2D panelArea, у которой берем координату Y
            Rectangle2D panelArea = this.chartPanel.getScreenDataArea(mouseX, mouseY);
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
