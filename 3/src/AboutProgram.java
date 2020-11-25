import javax.swing.*;
import java.awt.*;

public class AboutProgram extends JFrame {
    static int WIDTH = 280;
    static int HEIGHT = 520;
    AboutProgram(){
        super("О программе");
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        Toolkit kit = Toolkit.getDefaultToolkit();
// Отцентрировать окно приложения на экране
        setLocation((kit.getScreenSize().width - WIDTH)/2,
                (kit.getScreenSize().height - HEIGHT)/2);
        Box h = Box.createVerticalBox();
        Box img = Box.createHorizontalBox();
        JLabel FIO = new JLabel();
        FIO.setText("Борисюк Дарина Сергеевна");
        JLabel Variant = new JLabel();
        Variant.setText("Задание 3. Вариант С-2");
        CanvasImg pic = new CanvasImg("1.jpg", this, 0, 20);
        img.add(pic);
        h.add(FIO);
        h.add(Variant);
        h.add(img);
        h.add(Box.createHorizontalGlue());
        getContentPane().add(h);
    }
}
