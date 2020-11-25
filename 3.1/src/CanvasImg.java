import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;

public class CanvasImg extends JPanel {

    private BufferedImage image;
    private int _x;
    private int _y;

    private JFrame _kit;
    public CanvasImg(String nameImage, JFrame kit, int x, int y) {
        try {
            _x = x;
            _y = y;
            _kit = kit;
            image = ImageIO.read(new File("./" + nameImage));
        } catch (IOException ex) {

        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        try{
        g.drawImage(image, _x, _y, _kit.getWidth(),image.getHeight()*_kit.getWidth()/image.getWidth(),this);
    }catch (Exception e){}}
}