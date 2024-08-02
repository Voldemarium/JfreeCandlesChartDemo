package stepanovvv.ru.examples.examplesCharts;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.JFrame;
import javax.swing.JMenuBar;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryLabelPosition;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.CategoryLabelWidthType;
import org.jfree.chart.axis.SubCategoryAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.GroupedStackedBarRenderer;
import org.jfree.chart.text.TextBlockAnchor;
import org.jfree.chart.ui.RectangleAnchor;
import org.jfree.chart.ui.TextAnchor;
import org.jfree.data.KeyToGroupMap;
import org.jfree.data.category.DefaultCategoryDataset;

public class HorisontalGroupBars extends JFrame {
    private static final long serialVersionUID = 5309704066433738856L;
    private ChartPanel panel;

    public static void main(String[] args) {
        new HorisontalGroupBars();
    }

    public HorisontalGroupBars() {
        JMenuBar menuBar = new JMenuBar();

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JFreeChart chart = ChartFactory.createStackedBarChart("", "", "", null, PlotOrientation.HORIZONTAL, false, true, false);
        chart.setBackgroundPaint(getBackground());
        chart.setTextAntiAlias(true);
        panel = new ChartPanel(chart, 400, 400, 100, 100, 1000, 1000, true, false, false, true, false, false);

        Set<String> subCategories = new TreeSet<>();
        GroupedStackedBarRenderer groupedStackedBarRenderer = new GroupedStackedBarRenderer();
        groupedStackedBarRenderer.setDrawBarOutline(true);

        GradientPaint billPaint = new GradientPaint(0.0F, 0.0F, Color.GREEN.darker(), 0.0F, 0.0F, Color.GREEN);
        GradientPaint creditPaint = new GradientPaint(0.0F, 0.0F, Color.RED.darker(), 0.0F, 0.0F, Color.RED);

        SubCategoryAxis subCategoryAxis = new SubCategoryAxis(null);
        subCategoryAxis.setCategoryLabelPositions(CategoryLabelPositions.replaceLeftPosition( //
                subCategoryAxis.getCategoryLabelPositions(),
                new CategoryLabelPosition(RectangleAnchor.CENTER, TextBlockAnchor.BOTTOM_CENTER, TextAnchor.BOTTOM_CENTER, -Math.PI / 2,
                        CategoryLabelWidthType.CATEGORY, 1f)
        ));
        subCategoryAxis.setMinorTickMarksVisible(false);
        subCategoryAxis.setAxisLineVisible(false);
        subCategoryAxis.setTickLabelFont(subCategoryAxis.getLabelFont().deriveFont(Font.BOLD));

        KeyToGroupMap keyToGroupMap = new KeyToGroupMap("A");

        Map<String, Map<String, Map<String, Double>>> map = generateRandomSet(new HashSet<>(Arrays.asList("ab", "cd", "ef", "gh")),
                new HashSet<>(Arrays.asList("A", "B", "C", "D")));
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (String employee : map.keySet()) {
            Map<String, Map<String, Double>> classMap = map.get(employee);
            for (String serviceClass : classMap.keySet()) {
                Map<String, Double> typeMap = classMap.get(serviceClass);
                subCategories.add(serviceClass.toString());
                Double billValue = typeMap.containsKey("RE") ? (Double) typeMap.get("RE") : 0D;
                Double creditNoteValue = typeMap.containsKey("GU") ? (Double) typeMap.get("GU") : 0D;
                Double effectiveValue = Math.max(0, billValue - creditNoteValue);
                dataset.addValue(effectiveValue, serviceClass + " (RE)", employee);
                dataset.addValue(creditNoteValue, serviceClass + " (GU)", employee);
            }
        }
        CategoryPlot categoryPlot = (CategoryPlot) panel.getChart().getPlot();
        categoryPlot.setDataset(dataset);
        categoryPlot.setDomainAxis(subCategoryAxis);
        categoryPlot.setRenderer(groupedStackedBarRenderer);

        int counter = 0;
        for (String subCategory : subCategories) {
            groupedStackedBarRenderer.setSeriesPaint(counter * 2, billPaint);
            groupedStackedBarRenderer.setSeriesPaint(counter++ * 2 + 1, creditPaint);
            keyToGroupMap.mapKeyToGroup(subCategory + " (RE)", subCategory);
            keyToGroupMap.mapKeyToGroup(subCategory + " (GU)", subCategory);
            subCategoryAxis.addSubCategory(subCategory);
        }
        groupedStackedBarRenderer.setSeriesToGroupMap(keyToGroupMap);

        setJMenuBar(menuBar);
        add(panel);

        setTitle("main.title");

        pack();
        setVisible(true);

    }

    private Map<String, Map<String, Map<String, Double>>> generateRandomSet(Set<String> emplNames, Set<String> subNames) {
        Random rnd = new Random();
        Map<String, Map<String, Map<String, Double>>> result = new TreeMap<>();
        for (String empl : emplNames) {
            Map<String, Map<String, Double>> emplData = new TreeMap<>();
            for (String sub : subNames) {
                Map<String, Double> subData = new TreeMap<>();
                subData.put("RE", rnd.nextDouble() * 1000 + 1000);
                subData.put("GU", rnd.nextDouble() * 1000);
                emplData.put(sub, subData);
            }
            result.put(empl, emplData);
        }
        return result;
    }
}
