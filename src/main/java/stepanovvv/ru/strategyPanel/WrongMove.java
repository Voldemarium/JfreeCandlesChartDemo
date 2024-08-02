package stepanovvv.ru.strategyPanel;

//Вплывающее диалоговое окно-подсказка

import javax.swing.*;

public class WrongMove extends JFrame {

    public WrongMove() {
        super("wrong move");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        JPanel contents = new JPanel();
        JTextField textField = new JTextField("   Select instrument!!!  ");
        contents.add(textField);
        setContentPane(contents);
        // Вывод окна на экран
        setLocation(1500, 500);
        setSize(200, 130);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(WrongMove::new);
    }

}


