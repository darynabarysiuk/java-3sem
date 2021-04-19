import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class Field extends JPanel {
    // Флаг приостановленности движения
    private boolean paused;
    // Динамический список скачущих мячей
    private ArrayList<BouncingBall> balls = new ArrayList<BouncingBall>(10);
    // Класс таймер отвечает за регулярную генерацию событий ActionEvent
// При создании его экземпляра используется анонимный класс,
// реализующий интерфейс ActionListener

    private Timer repaintTimer = new Timer(10, new ActionListener() {
        public void actionPerformed(ActionEvent ev) {
// Задача обработчика события ActionEvent - перерисовка окна
            repaint();
        }
    });

    // Конструктор класса BouncingBall
    public Field() {
// Установить цвет заднего фона белым
        setBackground(Color.WHITE);
// Запустить таймер
        repaintTimer.start();
    }

    // Унаследованный от JPanel метод перерисовки компонента
    public void paintComponent(Graphics g) {
// Вызвать версию метода, унаследованную от предка
        super.paintComponent(g);
        Graphics2D canvas = (Graphics2D) g;
// Последовательно запросить прорисовку от всех мячей из списка
        for (BouncingBall ball: balls) {
            ball.paint(canvas);
        }
    }

    // Метод добавления нового мяча в список
    public void addBall() {
//Заключается в добавлении в список нового экземпляра BouncingBall
// Всю инициализацию положения, скорости, размера, цвета
// BouncingBall выполняет сам в конструкторе
        if(balls.size() <= 8) balls.add(new BouncingBall(this));
    }

    // Метод синхронизированный, т.е. только один поток может
// одновременно быть внутри
    public synchronized void pause() {
// Включить режим паузы
        paused = true;
    }

    // Метод синхронизированный, т.е. только один поток может
// одновременно быть внутри
    public synchronized void resume() {
// Выключить режим паузы
        paused = false;
// Будим все ожидающие продолжения потоки
        notifyAll();
    }

    // Синхронизированный метод проверки, может ли мяч двигаться
// (не включен ли режим паузы?)
    public synchronized void canMove(BouncingBall b2) throws InterruptedException {
        if (paused) {
// Если режим паузы включен, то поток, зашедший
// внутрь данного метода, засыпает
            wait();
        }
        for(BouncingBall b1 : balls) {
            if(b1 != b2){
                var x1 = b1.getX();
                var y1 = b1.getY();
                var x2 = b2.getX();
                var y2 = b2.getY();
                var rs = b2.getRadius()+b1.getRadius();
                if(Math.pow(x1-x2,2) + Math.pow(y1-y2,2) <= rs*rs) {
                    var s1 = b1.getSpeed();
                    var s2 = b2.getSpeed();
                    var sx1 = b1.getSpeedX()/3*s1;
                    var sx2 = b2.getSpeedX()/3*s2;
                    var sy1 = b1.getSpeedY()/3*s1;
                    var sy2 = b2.getSpeedY()/3*s2;
                    var alfa = Math.atan((y2 - y1)/(x1 - x2));
                    var sina = Math.sin(alfa);
                    var cosa = Math.cos(alfa);
                    var sxx1 = sx1*cosa-sy1*sina;
                    var syy1 = sx1*sina+sy1*cosa;
                    var sxx2 = -sx2*cosa+sy2*sina;
                    var syy2 = sx2*sina+sy2*cosa;
                    var k = Math.pow(b1.getRadius()/b2.getRadius(),3);
                    var sxxx1 = (2*sxx2+(k-1)*sxx1)/(1+k);
                    var sxxx2 = (2*sxx1+(1/k-1)*sxx2)/(1+1/k);
                    var ends1x = sxxx1*cosa+syy1*sina;
                    var ends1y = -sxxx1*sina+syy1*cosa;
                    var ends2x = sxxx2*cosa+syy2*sina;
                    var ends2y = -sxxx2*sina+syy2*cosa;
                    var ends1 = Math.sqrt(ends1x*ends1x+ends1y*ends1y);
                    var ends2 = Math.sqrt(ends2x*ends2x+ends2y*ends2y);
                    b1.setSpeedX(ends1x/ends1*3);
                    b1.setSpeedY(ends1y/ends1*3);
                    b2.setSpeedX(ends2x/ends2*3);
                    b2.setSpeedY(ends2y/ends2*3);
                    b1.setSpeed((int)ends1);
                    b2.setSpeed((int)ends2);
                    if(x2>x1)b2.PlusX(Math.abs(rs*cosa)-Math.abs(x1-x2)+1); else b2.PlusX(-Math.abs(rs*cosa)+Math.abs(x1-x2)-1);
                    if(y2>y1)b2.PlusY(Math.abs(rs*sina)-Math.abs(y1-y2)+1); else b2.PlusY(-Math.abs(rs*sina)+Math.abs(y1-y2)-1);
                }
            }
        }
    }
}