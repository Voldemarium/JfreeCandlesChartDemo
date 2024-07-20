package stepanovvv.ru.demo;

import org.jfree.chart.*;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.panel.CrosshairOverlay;
import org.jfree.chart.plot.Crosshair;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
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

import static org.jfree.data.general.DatasetUtils.findYValue;

/**
 * A demo showing crosshairs that follow the data points on an XYPlot.
 */
public class TimeSeriesXYDatasetDemo4 extends JPanel implements ChartMouseListener {
    private static final DateFormat READABLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final String DISPLAY_DATE_FORMAT = "dd.MM.yy";
    private final String offsetId = "+04:00";
    private final Float crosshairWidth = 0.4f;
    private final Color crosshairColor = Color.GRAY;

    private final JFreeChart volumeChart;

    private ChartPanel commonChartPanel;

    private TimeSeries volumeSeries;

    private Crosshair xCrosshair;
    private Crosshair yCrosshair;

    public TimeSeriesXYDatasetDemo4(String title) {
        // Create new chart
        volumeChart = createChart(title);

        // Create new chart panel
        commonChartPanel = new ChartPanel(volumeChart);

        // Устанавливаем размер панели с графиком
        commonChartPanel.setPreferredSize(new Dimension(1600, 800));
        // Enable zooming
        commonChartPanel.setMouseZoomable(true);
        commonChartPanel.setMouseWheelEnabled(true);

        // Add crosshairOverlay (добавляем перекрестие при наведении курсора мыши)
        this.commonChartPanel.addChartMouseListener(this);                // добавляем на панель слушатель мыши
        CrosshairOverlay commonCrosshairOverlay = new CrosshairOverlay(); // Создание объекта перекрестия на общем графике
        // Установка начального значения, цвета и ширины линии перекрести по оси Х
        this.xCrosshair = new Crosshair(Double.NaN, crosshairColor, new BasicStroke(crosshairWidth));
        this.xCrosshair.setLabelVisible(true);                     // делаем перекрестие по оси Х видимым

        this.yCrosshair = new Crosshair(Double.NaN, crosshairColor, new BasicStroke(crosshairWidth));
        this.yCrosshair.setLabelVisible(true);                     // делаем перекрестие по оси Y видимым

        commonCrosshairOverlay.addDomainCrosshair(xCrosshair);
        commonCrosshairOverlay.addRangeCrosshair(yCrosshair);
        commonChartPanel.addOverlay(commonCrosshairOverlay);
        add(commonChartPanel, BorderLayout.CENTER);
    }


    private JFreeChart createChart(String chartTitle) {
        //2. Creates TimeSeriesCollection as a volume dataset for volume chart
        TimeSeriesCollection volumeDataset = new TimeSeriesCollection();
        volumeSeries = new TimeSeries("Volume");
        volumeDataset.addSeries(volumeSeries);
        // Create volume chart volumeAxis
        NumberAxis volumeAxis = new NumberAxis("Volume");
        volumeAxis.setAutoRangeIncludesZero(true); // показывать ли диапазон значений начиная от НОЛЯ
        // Set to no decimal
        volumeAxis.setNumberFormatOverride(new DecimalFormat("0"));

        //  Creating dateAxis
        DateAxis dateAxis = new DateAxis("Time");
        // Формат надписи по оси времени
        dateAxis.setDateFormatOverride(new SimpleDateFormat(DISPLAY_DATE_FORMAT));
//		 reduce the default left/right margin from 0.05 to 0.02
        dateAxis.setLowerMargin(0.02);
        dateAxis.setUpperMargin(0.02);

        // Create volume chart renderer (Создание средства визуализации баров (столбчатых диаграмм))
        XYBarRenderer xyBarRenderer = new XYBarRenderer(); // Вид графика - бары (столбчатая диаграмма)
        // При наведении курсора мыши на элемент графика показываем значение Y рядом с курсором
        xyBarRenderer.setDefaultToolTipGenerator((xyDataset, i, i1) -> xyDataset.getY(i, i1).toString());
        //видимость тени на баре
        xyBarRenderer.setShadowVisible(false);
        xyBarRenderer.setSeriesPaint(0, Color.BLUE);
        // Create volumeSubplot
        XYPlot volumeSubplot = new XYPlot(volumeDataset, dateAxis, volumeAxis, xyBarRenderer);
//        volumeSubplot.setOrientation(PlotOrientation.VERTICAL);
        volumeSubplot.setBackgroundPaint(new Color(255, 227, 190, 100));

        // Ориентация графиков
        volumeSubplot.setOrientation(PlotOrientation.VERTICAL);
        // Построение общего графика
        JFreeChart commonChart = new JFreeChart("Volume", JFreeChart.DEFAULT_TITLE_FONT, volumeSubplot, true);
        commonChart.removeLegend();

        // Если нужно удалить с графика выходные дни (субботу и воскресение)
        SegmentedTimeline timeline = SegmentedTimeline.newMondayThroughFridayTimeline();
        dateAxis.setTimeline(timeline);

        return commonChart;
    }


    @Override
    public void chartMouseClicked(ChartMouseEvent event) {
        // ignore
    }

    @Override
    public void chartMouseMoved(ChartMouseEvent event) {
        Rectangle2D dataArea = this.commonChartPanel.getScreenDataArea();
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
        // Добавляем свечи на графмк в цикле
        for (CandleMoex candleMoex : candleMoexList) {
            LocalDateTime dateTimeOpen = candleMoex.getBegin();
            Date date = new Date(dateTimeOpen.toEpochSecond(ZoneOffset.of(offsetId)) * 1000);
            Day t = new Day(date);

            // добавление свечи
//            ohlcSeries.add(t, candleMoex.getOpen(), candleMoex.getHigh(), candleMoex.getLow(), candleMoex.getClose());
            // добавление объема на свече
            volumeSeries.add(t, candleMoex.getVolume());
            // добавление метрик
//            buyMetricSeries.add(t, candleMoex.getBuyMetric());
//            sellMetricSeries.add(t, candleMoex.getSellMetric());

            if (prevDateTime != null &&  !dateTimeOpen.toLocalDate().isEqual(prevDateTime.plusDays(1))) {
                System.out.println();
            }
            prevDateTime = dateTimeOpen.toLocalDate();
        }
    }

    private void createAndShowGUI() {
        String ticker = "SBER";
        //Make sure we have nice window decorations.
        JFrame.setDefaultLookAndFeelDecorated(true);
        //Create and set up the window.
        JFrame frame = new JFrame("JfreeCandlestickChartDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Create and set up the chart.
        TimeSeriesXYDatasetDemo4 jfreeCandlesChart = new TimeSeriesXYDatasetDemo4(ticker);
        // Create candleMoexList
        List<CandleMoex> candleMoexList = new MockListCandles().getCandleMoexList();
        // Добавляем свечи на графмк
        jfreeCandlesChart.addCandles(candleMoexList);
        frame.setContentPane(jfreeCandlesChart);
        //Disable the resizing feature
        frame.setResizable(true);
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        TimeSeriesXYDatasetDemo4 jfreeCandlestickChartDemo = new TimeSeriesXYDatasetDemo4("SBER");
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(jfreeCandlestickChartDemo::createAndShowGUI);
        }


}
