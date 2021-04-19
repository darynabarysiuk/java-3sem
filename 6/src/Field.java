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
        if (paused && b2.getStatusPause()) {
// Если режим паузы включен, то поток, зашедший
// внутрь данного метода, засыпает
            wait();
        }
        for(BouncingBall b1 : balls) {///нужно сделать правильный обмен скоростей
            if(b1 != b2){
                var x1 = b1.getX();
                var y1 = b1.getY();
                var x2 = b2.getX();
                var y2 = b2.getY();
                var rs = b2.getRadius()+b1.getRadius();
                if(Math.pow(x1-x2,2) + Math.pow(y1-y2,2) <= rs*rs) {
                    b1.setStatusPause(true);
                    var sx1 = b1.getSpeedX();
                    var sx2 = b2.getSpeedX();
                    var sy1 = b1.getSpeedY();
                    var sy2 = b2.getSpeedY();
                    var alfa = Math.atan((y2 - y1)/(x1 - x2));///
                    var sina = Math.sin(alfa);
                    var cosa = Math.cos(alfa);
                    var sxx1 = sx1*cosa-sy1*sina;
                    var syy1 = sx1*sina+sy1*cosa;
                    var sxx2 = -sx2*cosa+sy2*sina;
                    var syy2 = sx2*sina+sy2*cosa;
                    var k = Math.pow(b1.getRadius()/b2.getRadius(),3);
                    var sxxx1 = (2*sxx2+(k-1)*sxx1)/(1+k);
                    var sxxx2 = (2*sxx1+(1/k-1)*sxx2)/(1+1/k);
                    b1.setSpeedX(sxxx1*cosa+syy1*sina);
                    b1.setSpeedY(-sxxx1*sina+syy1*cosa);
                    b2.setSpeedX(sxxx2*cosa+syy2*sina);
                    b2.setSpeedY(-sxxx2*sina+syy2*cosa);
                    b2.setSpeed((int) Math.round(Math.sqrt(b2.getSpeedY()*b2.getSpeedY()+b2.getSpeedX()*b2.getSpeedX())));
                    b1.setSpeed((int) Math.round(Math.sqrt(b1.getSpeedY()*b1.getSpeedY()+b1.getSpeedX()*b1.getSpeedX())));
                    if(x2>x1)b2.PlusX(Math.abs(rs*cosa)-Math.abs(x1-x2)+1); else b2.PlusX(-Math.abs(rs*cosa)+Math.abs(x1-x2)-1);
                    if(y2>y1)b2.PlusY(Math.abs(rs*sina)-Math.abs(y1-y2)+1); else b2.PlusY(-Math.abs(rs*sina)+Math.abs(y1-y2)-1);
                    b1.setStatusPause(false);
                }
            }
        }
    }
}