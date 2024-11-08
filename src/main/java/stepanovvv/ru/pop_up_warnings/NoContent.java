package stepanovvv.ru.pop_up_warnings;

//Вплывающее диалоговое окно-подсказка

import javax.swing.*;

public class NoContent extends JFrame {

    public NoContent(String arg) {
        super("no content");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        JPanel contents = new JPanel();
        JTextField textField = new JTextField("   " + arg + "  ");
        contents.add(textField);
        setContentPane(contents);
        // Вывод окна на экран
        setLocation(1500, 500);
        setSize(250, 100);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new NoContent(args[0]));
    }

}


