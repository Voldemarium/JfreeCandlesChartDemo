package stepanovvv.ru.examples.examplesScrollBar;

import javax.swing.*;
import java.awt.*;

public class MainScroll {
    public static void main(String[] args) {
        final JFrame frame = new JFrame("JScrollbar Demo");
        JScrollBar scrollBarH = new JScrollBar(JScrollBar.HORIZONTAL, 30, 20, 0, 500);
        JScrollBar scrollBarV = new JScrollBar(JScrollBar.VERTICAL, 30, 40, 0, 500);
        frame.setSize(300, 200);
        frame.getContentPane().add(scrollBarH, BorderLayout.SOUTH);
        frame.getContentPane().add(scrollBarV, BorderLayout.EAST);
        frame.setVisible(true);
    }
}
