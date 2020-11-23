import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class MyCanvas extends JPanel {

    private BufferedImage image;

    private MainFrame mainFrame;

    public MyCanvas(int numImage, MainFrame kit) {
        try {
            mainFrame = kit;
            image = ImageIO.read(new File("D:\\ДАРЫНА\\УНІВЕР\\ВУЧОБА\\2 курс\\3 сем\\джава\\лабораторные\\2\\2.1\\src\\" + numImage + ".jpg"));
        } catch (IOException ex) {
            image = null;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        try {
            g.drawImage(image, 17, 0, mainFrame.getWidth() - 50,image.getHeight()* mainFrame.getWidth()/image.getWidth(),this);
        }
        catch(Exception e){}
    }
}