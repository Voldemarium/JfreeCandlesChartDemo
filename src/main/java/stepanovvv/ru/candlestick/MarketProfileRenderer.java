package stepanovvv.ru.candlestick;

import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.chart.plot.CrosshairState;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYItemRendererState;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYDataset;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.Serial;

public class MarketProfileRenderer extends XYBarRenderer {
    @Serial
    private static final long serialVersionUID = 770559577251370036L;
    private final double base;
    private final boolean useYInterval;
    private final XYBarPainter barPainter;
    private final double barAlignmentFactor;

    public MarketProfileRenderer() {
        this(0.0);
    }

    public MarketProfileRenderer(double margin) {
        this.base = 0.0;
        this.useYInterval = false;
        this.barPainter = getDefaultBarPainter();
        this.barAlignmentFactor = -1.0;
    }

    @Override
    public void drawItem(Graphics2D g2, XYItemRendererState state, Rectangle2D dataArea, PlotRenderingInfo info,
                         XYPlot plot, ValueAxis domainAxis, ValueAxis rangeAxis, XYDataset dataset, int series,
                         int item, CrosshairState crosshairState, int pass) {
        if (this.getItemVisible(series, item)) {
            IntervalXYDataset intervalDataset = (IntervalXYDataset)dataset;
            double value0;
            double value1;
            if (this.useYInterval) {
                value0 = intervalDataset.getStartXValue(series, item);
                value1 = intervalDataset.getEndXValue(series, item);
            } else {
                value0 = this.base;
                value1 = intervalDataset.getXValue(series, item);
            }

            if (!Double.isNaN(value0) && !Double.isNaN(value1)) {
                if (value0 <= value1) {
                    if (!domainAxis.getRange().intersects(value0, value1)) {
                        return;
                    }
                } else if (!domainAxis.getRange().intersects(value1, value0)) {
                    return;
                }

                double translatedValue0 = domainAxis.valueToJava2D(value0, dataArea, plot.getDomainAxisEdge());
                double translatedValue1 = domainAxis.valueToJava2D(value1, dataArea, plot.getDomainAxisEdge());
                double bottom = Math.min(translatedValue0, translatedValue1); //дно бара
                double top = Math.max(translatedValue0, translatedValue1);    // верх бара
                double startY = intervalDataset.getStartYValue(series, item);

                if (!Double.isNaN(startY)) {
                    double endY = intervalDataset.getEndYValue(series, item);
                    if (!Double.isNaN(endY)) {
                        if (startY <= endY) {
                            if (!rangeAxis.getRange().intersects(startY, endY)) {
                                return;
                            }
                        } else if (!rangeAxis.getRange().intersects(endY, startY)) {
                            return;
                        }

                        if (this.barAlignmentFactor >= 0.0 && this.barAlignmentFactor <= 1.0) {
                            double y = intervalDataset.getYValue(series, item);
                            double interval = endY - startY;
                            startY = y - interval * this.barAlignmentFactor;
                            endY = startY + interval;
                        }

                        // Вычисление толщины горизонтального бара
                        RectangleEdge location = plot.getRangeAxisEdge();
                        double translatedStartY = rangeAxis.valueToJava2D(startY, dataArea, location);
                        double translatedEndY =   rangeAxis.valueToJava2D(endY, dataArea, location);
                        double startWidth = 5.0; // толщина бара по умолчанию
                        double translatedWidth = Math.max(startWidth, Math.abs(translatedEndY - translatedStartY));
                        double left = Math.min(translatedStartY, translatedEndY);
                        if (this.getMargin() > 0.0) {
                            double cut = translatedWidth * this.getMargin();
                            translatedWidth -= cut;
                            left += cut / 2.0;
                        }

                        // Финальная отрисовка горизонтального бара
                        Rectangle2D bar = null;
                        PlotOrientation orientation = plot.getOrientation();
                        if (orientation.isHorizontal()) {
                            bottom = Math.max(bottom, dataArea.getMinY());  // дно
                            top = Math.min(top, dataArea.getMaxY());        // вершина
                            bar = new Rectangle2D.Double(left, bottom, translatedWidth, top - bottom);
                        } else if (orientation.isVertical()) {
                            bottom = Math.max(bottom, dataArea.getMinX());
                            top = Math.min(top, dataArea.getMaxX());
                            bar = new Rectangle2D.Double(bottom, left, top - bottom, translatedWidth);
                        }

                        boolean positive = value1 > 0.0;
                        boolean inverted = rangeAxis.isInverted();
                        RectangleEdge barBase;
                        if (orientation.isHorizontal()) {
                            if ((!positive || !inverted) && (positive || inverted)) {
                                barBase = RectangleEdge.LEFT;
                            } else {
                                barBase = RectangleEdge.RIGHT;
                            }
                        } else if ((!positive || inverted) && (positive || !inverted)) {
                            barBase = RectangleEdge.TOP;
                        } else {
                            barBase = RectangleEdge.BOTTOM;
                        }

                        if (state.getElementHinting()) {
                            this.beginElementGroup(g2, dataset.getSeriesKey(series), item);
                        }

                        if (this.getShadowsVisible()) {
                            this.barPainter.paintBarShadow(g2, this, series, item, bar, barBase, !this.useYInterval);
                        }

                        this.barPainter.paintBar(g2, this, series, item, bar, barBase);
                        if (state.getElementHinting()) {
                            this.endElementGroup(g2);
                        }

                        if (this.isItemLabelVisible(series, item)) {
                            XYItemLabelGenerator generator = this.getItemLabelGenerator(series, item);
                            this.drawItemLabel(g2, dataset, series, item, plot, generator, bar, value1 < 0.0);
                        }

                        double x1 = (startY + endY) / 2.0;
                        double y1 = dataset.getYValue(series, item);
                        double transX1 = domainAxis.valueToJava2D(x1, dataArea, location);
                        double transY1 = rangeAxis.valueToJava2D(y1, dataArea, plot.getRangeAxisEdge());
                        int datasetIndex = plot.indexOf(dataset);
                        this.updateCrosshairValues(crosshairState, x1, y1, datasetIndex, transX1, transY1, plot.getOrientation());
                        EntityCollection entities = state.getEntityCollection();
                        if (entities != null) {
                            this.addEntity(entities, bar, dataset, series, item, 0.0, 0.0);
                        }

                    }
                }
            }
        }
    }
}
