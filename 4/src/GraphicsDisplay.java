import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.*;
import java.lang.constant.Constable;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

@SuppressWarnings("serial")
public class GraphicsDisplay extends JPanel {
    // Список координат точек для построения графика
    private Double[][] graphicsData;
    // Флаговые переменные, задающие правила отображения графика
    private boolean showAxis = true;
    private boolean showMarkers = true;
    private boolean rotate = false;
    // Границы диапазона пространства, подлежащего отображению
    private double minX;
    private double maxX;
    private double minY;
    private double maxY;
    // Используемый масштаб отображения
    private double scale;
    // Различные стили черчения линий
    private BasicStroke graphicsStroke;
    private BasicStroke axisStroke;
    private BasicStroke markerStroke;
    private BasicStroke gridStroke;
    private double radiusOfMarker;
    // Различные шрифты отображения надписей
    private Font axisFont;

    public GraphicsDisplay() {
        radiusOfMarker = 5.5;
// Цвет заднего фона области отображения - белый
        setBackground(Color.WHITE);
// Сконструировать необходимые объекты, используемые в рисовании
// Перо для рисования графика
        graphicsStroke = new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 10.0f, new float[]{8,2,2,2,4,2,2,2,8,5}, 0.0f);
// Перо для рисования осей координат
        axisStroke = new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, null, 0.0f);
// Перо для рисования контуров маркеров
        markerStroke =  new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 10.0f, null, 0.0f);
//Перо для прорисовки текста
        gridStroke = new BasicStroke(0.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 10.0f, null, 0.0f);
    }

// Данный метод вызывается из обработчика элемента меню "Открыть файл c графиком"
    // главного окна приложения в случае успешной загрузки данных
    public void showGraphics(Double[][] graphicsData) {
// Сохранить массив точек во внутреннем поле класса
        this.graphicsData = graphicsData;
// Запросить перерисовку компонента, т.е. неявно вызвать paintComponent()
        repaint();
    }

    // Методы-модификаторы для изменения параметров отображения графика
// Изменение любого параметра приводит к перерисовке области
    public void setShowAxis(boolean showAxis) {
        this.showAxis = showAxis;
        repaint();
    }

    public void setShowMarkers(boolean showMarkers) {
        this.showMarkers = showMarkers;
        repaint();
    }

    public void setRotate(boolean rotate) {
        this.rotate = rotate;
        repaint();
    }

    // Метод отображения всего компонента, содержащего график
    public void paintComponent(Graphics g) {
        /* Шаг 1 - Вызвать метод предка для заливки области цветом заднего фона
         * Эта функциональность - единственное, что осталось в наследство от
         * paintComponent класса JPanel
         */
        super.paintComponent(g);
        if(rotate) {
            ((Graphics2D)g).rotate(-Math.PI/2);
            ((Graphics2D)g).translate(-getSize().getHeight(), 0);
        }
// Шаг 2 - Если данные графика не загружены (при показе компонента при запуске программы) - ничего не делать
        if (graphicsData==null || graphicsData.length==0) return;
// Шаг 3 - Определить минимальное и максимальное значения для координат X и Y
// Это необходимо для определения области пространства, подлежащей отображению
// Еѐ верхний левый угол это (minX, maxY) - правый нижний это (maxX, minY)
        minX = graphicsData[0][0];
        maxX = graphicsData[graphicsData.length-1][0];
        minY = graphicsData[0][1];
        maxY = minY;
// Найти минимальное и максимальное значение функции
        for (int i = 1; i<graphicsData.length; i++) {
            if (graphicsData[i][1]<minY) {
                minY = graphicsData[i][1];
            }
            if (graphicsData[i][1]>maxY) {
                maxY = graphicsData[i][1];
            }
        }
/* Шаг 4 - Определить (исходя из размеров окна) масштабы по осям X
и Y - сколько пикселов
* приходится на единицу длины по X и по Y
*/
        double scaleX;
        double scaleY;
        if(rotate) {
            scaleX = Math.abs(getSize().getHeight() / (maxX - minX));
            scaleY = Math.abs(getSize().getWidth() / (maxY - minY));
        }
        else{
            scaleX = Math.abs(getSize().getWidth() / (maxX - minX));
            scaleY = Math.abs(getSize().getHeight() / (maxY - minY));
        }
// Шаг 5 - Чтобы изображение было неискажѐнным - масштаб должен быть одинаков
// Выбираем за основу минимальный
        scale = Math.min(scaleX, scaleY);
// Шаг 6 - корректировка границ отображаемой области согласно выбранному масштабу
        if (scale==scaleX) {
/* Если за основу был взят масштаб по оси X, значит по оси Y
делений меньше,
* т.е. подлежащий визуализации диапазон по Y будет меньше
высоты окна.
* Значит необходимо добавить делений, сделаем это так:
* 1) Вычислим, сколько делений влезет по Y при выбранном
масштабе - getSize().getHeight()/scale
* 2) Вычтем из этого сколько делений требовалось изначально
* 3) Набросим по половине недостающего расстояния на maxY и
minY
*/
            double yIncrement = ((rotate ? getSize().getWidth() : getSize().getHeight() )/ scale - (maxY - minY)) / 2;
            maxY += yIncrement;
            minY -= yIncrement;
        }
        if (scale==scaleY) {
// Если за основу был взят масштаб по оси Y, действовать по аналогии
            double xIncrement = ((rotate ? getSize().getHeight() : getSize().getWidth()) / scale - (maxX - minX)) / 2;
            maxX += xIncrement;
            minX -= xIncrement;
        }
// Шаг 7 - Сохранить текущие настройки холста
        Graphics2D canvas = (Graphics2D) g;
        Stroke oldStroke = canvas.getStroke();
        Color oldColor = canvas.getColor();
        Paint oldPaint = canvas.getPaint();
        Font oldFont = canvas.getFont();
// Шаг 8 - В нужном порядке вызвать методы отображения элементов графика
// Порядок вызова методов имеет значение, т.к. предыдущий рисунок будет затираться последующим
// Первыми (если нужно) отрисовываются оси координат.
        if (showAxis) {
            paintAxis(canvas);
        }
// Затем отображается сам график
        paintGraphics(canvas);
// Затем (если нужно) отображаются маркеры точек, по которым строился график.
        if (showMarkers) {
            paintMarkers(canvas);
        }
        canvas.setFont(oldFont);
        canvas.setPaint(oldPaint);
        canvas.setColor(oldColor);
        canvas.setStroke(oldStroke);
    }

    // Отрисовка графика по прочитанным координатам
    protected void paintGraphics(Graphics2D canvas) {
// Выбрать линию для рисования графика
        canvas.setStroke(graphicsStroke);
// Выбрать цвет линии
        canvas.setColor(Color.RED);
/* Будем рисовать линию графика как путь, состоящий из множества
сегментов (GeneralPath)
* Начало пути устанавливается в первую точку графика, после чего
прямой соединяется со
* следующими точками
*/GeneralPath graphics = new GeneralPath();
        for (int i=0; i<graphicsData.length; i++) {
// Преобразовать значения (x,y) в точку на экране point
            Point2D.Double point = xyToPoint(graphicsData[i][0],
                    graphicsData[i][1]);
            if (i>0) {
// Не первая итерация цикла - вести линию в точку point
                graphics.lineTo(point.getX(), point.getY());
            } else {
// Первая итерация цикла - установить начало пути в точку point
                graphics.moveTo(point.getX(), point.getY());
            }
        }
// Отобразить график
        canvas.draw(graphics);
    }

    // Отображение маркеров точек, по которым рисовался график
    protected void paintMarkers(Graphics2D canvas) {
// Выбрать красный цвета для контуров маркеров
        canvas.setStroke(markerStroke);
// Шаг 1 - Организовать цикл по всем точкам графика
        for (Double[] point: graphicsData) {
// Центр - в точке (x,y)
            int temp = (int) (Math.floor(Math.abs(point[1])));
            for(; temp % 2 == 0 && temp > 0; temp = temp /10);

            if(temp == 0) {
                canvas.setColor(Color.BLUE);
            }
            else {
                canvas.setColor(Color.RED);
            }
            Point2D.Double center = xyToPoint(point[0], point[1]);
            //Задание двух частей внутренних линий
            canvas.draw(new Line2D.Double(center.x, center.y-radiusOfMarker,
                    center.x,center.y+radiusOfMarker));
            canvas.draw(new Line2D.Double(center.x - radiusOfMarker,
                    center.y,center.x+radiusOfMarker,center.y));
            canvas.draw(new Line2D.Double(center.x - radiusOfMarker / 2, center.y - radiusOfMarker,
                    center.x + radiusOfMarker / 2,center.y - radiusOfMarker));
            canvas.draw(new Line2D.Double(center.x - radiusOfMarker / 2, center.y + radiusOfMarker,
                    center.x + radiusOfMarker / 2,center.y + radiusOfMarker));
            canvas.draw(new Line2D.Double(center.x - radiusOfMarker, center.y - radiusOfMarker / 2,
                    center.x - radiusOfMarker,center.y + radiusOfMarker / 2));
            canvas.draw(new Line2D.Double(center.x + radiusOfMarker, center.y - radiusOfMarker / 2,
                    center.x + radiusOfMarker,center.y + radiusOfMarker / 2));
        }
    }

    // Метод, обеспечивающий отображение осей координат
    protected void paintAxis(Graphics2D canvas) {
        canvas.setStroke(gridStroke);
        double scaleFont = Math.min(getSize().getWidth()/1280, getSize().getHeight()/720);
        axisFont = new Font("Serif", Font.BOLD, (int) Math.round(18 * scaleFont));
// Подписи к координатным осям делаются специальным шрифтом
        canvas.setFont(axisFont);
        //выбор сетки для x и y
        var lamdaGenerate = new Object() {
          double generateStep(double step) {
              if (step < 1) {
                  for (double temp = 1 ;; temp /= 10) {
                      if (step > temp) {
                          step = Math.round(step / temp) * temp;
                          break;
                      }
                  }
              } else {
                  for (double temp = 1; ; temp *= 10) {
                      if (step > temp) {
                          step = Math.round(step / temp) * temp;
                          break;
                      }
                  }
              }
              return step;
          }
        };
        double stepX = lamdaGenerate.generateStep(Math.abs((maxX - minX) / 10));
        double stepY = lamdaGenerate.generateStep(Math.abs((maxY - minY) / 10));

        //Добавление сетки и делений
        Line2D line = new Line2D.Double();

        canvas.setPaint(Color.GRAY);
        {
            for (double i = stepY; i < maxY; i += stepY) {
                line.setLine(xyToPoint(minX, i), xyToPoint(maxX, i));
                canvas.draw(line);
            }
            for (double i = stepY; i > minY; i -= stepY) {
                line.setLine(xyToPoint(minX, i), xyToPoint(maxX, i));
                canvas.draw(line);
            }
            for (double i = stepX; i < maxX; i += stepX) {
                line.setLine(xyToPoint(i, minY), xyToPoint(i, maxY));
                canvas.draw(line);
            }
            for (double i = stepX; i > minX; i -= stepX) {
                line.setLine(xyToPoint(i, minY), xyToPoint(i, maxY));
                canvas.draw(line);
            }
        }
// Установить особое начертание для осей
        canvas.setStroke(axisStroke);
// Оси рисуются чѐрным цветом
        canvas.setColor(Color.BLACK);
        //Прорисовка делений
        double stepSX = stepX/10;
        double stepSY = stepY/10;
        double height = (stepSX+stepSY)/6;

        var paintS = new Object(){
            void paintS(double height, double a, double b)
            {
                for (double i = stepSY * a; i <= maxY - b * stepSY / 2; i += b * stepSY) {
                    line.setLine(xyToPoint(-height, i), xyToPoint(height, i));
                    canvas.draw(line);
                }
                for (double i = - stepSY * a; i > minY; i -= b * stepSY) {
                    line.setLine(xyToPoint(-height, i), xyToPoint(height, i));
                    canvas.draw(line);
                }
                for (double i = stepSX * a; i <= maxX - b * stepSX / 2; i += b * stepSX) {
                    line.setLine(xyToPoint(i, -height), xyToPoint(i, height));
                    canvas.draw(line);
                }
                for (double i = - stepSX * a; i > minX; i -= b * stepSX) {
                    line.setLine(xyToPoint(i, -height), xyToPoint(i, height));
                    canvas.draw(line);
                }
            }
        };

        //Прорисовка всех делений
        paintS.paintS(height, 1, 1);
        //Средние штрихи
        height = (stepSX+stepSY)/3;
        paintS.paintS(height, 5, 10);
        //Краевые штрихи
        height = (stepSX+stepSY)/2;
        paintS.paintS(height, 10, 10);
// Стрелки заливаются чѐрным цветом
        canvas.setPaint(Color.BLACK);
// Создать объект контекста отображения текста - для получения характеристик устройства (экрана)
        FontRenderContext context = canvas.getFontRenderContext();
        DecimalFormat df = new DecimalFormat("#.####");
        df.setRoundingMode(RoundingMode.HALF_UP);
        for (double i = stepSY * 10; i < maxY - 10 * stepSY / 2; i += 10 * stepSY) {
            Rectangle2D bounds = axisFont.getStringBounds(Double.toString(i), context);
            Point2D.Double labelPos = xyToPoint(0, i);
            canvas.drawString(df.format(i), (float)labelPos.getX() + 10, (float)(labelPos.getY() - bounds.getY()/2));
        }
        for (double i = - stepSY * 10; i > minY; i -= 10 * stepSY) {
            Rectangle2D bounds = axisFont.getStringBounds(Double.toString(i), context);
            Point2D.Double labelPos = xyToPoint(0, i);
            canvas.drawString(df.format(i), (float)labelPos.getX() + 10, (float)(labelPos.getY() - bounds.getY()/2));
        }
        for (double i = stepSX * 10; i < maxX - 10 * stepSX / 2; i += 10 * stepSX) {
            Rectangle2D bounds = axisFont.getStringBounds(Double.toString(i), context);
            Point2D.Double labelPos = xyToPoint(i, 0);
            canvas.drawString(df.format(i), (float)(labelPos.getX() - bounds.getWidth()/2), (float)(labelPos.getY() + 25));
        }
        for (double i = - stepSX * 10; i > minX; i -= 10 * stepSX) {
            Rectangle2D bounds = axisFont.getStringBounds(Double.toString(i), context);
            Point2D.Double labelPos = xyToPoint(i, 0);
            canvas.drawString(df.format(i), (float)(labelPos.getX() - bounds.getWidth()/2), (float)(labelPos.getY() + 25));
        }
// Определить, должна ли быть видна ось Y на графике
        if (minX<=0.0 && maxX>=0.0) {
// Она должна быть видна, если левая граница показываемой области (minX) <= 0.0,
// а правая (maxX) >= 0.0
// Сама ось - это линия между точками (0, maxY) и (0, minY)
            canvas.draw(new Line2D.Double(xyToPoint(0, maxY),
                    xyToPoint(0, minY)));
// Стрелка оси Y
            GeneralPath arrow = new GeneralPath();
// Установить начальную точку ломаной точно на верхний конец оси Y
            Point2D.Double lineEnd = xyToPoint(0, maxY);
            arrow.moveTo(lineEnd.getX(), lineEnd.getY());
// Вести левый "скат" стрелки в точку с относительными координатами (5,20)
            arrow.lineTo(arrow.getCurrentPoint().getX()+5,
                    arrow.getCurrentPoint().getY()+20);
// Вести нижнюю часть стрелки в точку с относительными координатами (-10, 0)
            arrow.lineTo(arrow.getCurrentPoint().getX()-10,
                    arrow.getCurrentPoint().getY());
// Замкнуть треугольник стрелки
            arrow.closePath();
            canvas.draw(arrow); // Нарисовать стрелку
            canvas.fill(arrow);
// Закрасить стрелку
// Нарисовать подпись к оси Y
// Определить, сколько места понадобится для надписи "y"
            Rectangle2D bounds = axisFont.getStringBounds("y", context);
            Point2D.Double labelPos = xyToPoint(0, maxY);
// Вывести надпись в точке с вычисленными координатами
            canvas.drawString("y", (float)labelPos.getX() + 10,
                    (float)(labelPos.getY() - bounds.getY()));
        }
// Определить, должна ли быть видна ось X на графике
        if (minY<=0.0 && maxY>=0.0) {
// Она должна быть видна, если верхняя граница показываемой области (maxX) >= 0.0,
// а нижняя (minY) <= 0.0
            canvas.draw(new Line2D.Double(xyToPoint(minX, 0),
                    xyToPoint(maxX, 0)));
// Стрелка оси X
            GeneralPath arrow = new GeneralPath();
// Установить начальную точку ломаной точно на правый конец оси X
            Point2D.Double lineEnd = xyToPoint(maxX, 0);
            arrow.moveTo(lineEnd.getX(), lineEnd.getY());
// Вести верхний "скат" стрелки в точку с относительными координатами (-20,-5)
            arrow.lineTo(arrow.getCurrentPoint().getX()-20,
                    arrow.getCurrentPoint().getY()-5);
// Вести левую часть стрелки в точку с относительными координатами (0, 10)
            arrow.lineTo(arrow.getCurrentPoint().getX(),
                    arrow.getCurrentPoint().getY()+10);
// Замкнуть треугольник стрелки
            arrow.closePath();
            canvas.draw(arrow); // Нарисовать стрелку
            canvas.fill(arrow);
// Закрасить стрелку
// Нарисовать подпись к оси X
// Определить, сколько места понадобится для надписи "x"
            Rectangle2D bounds = axisFont.getStringBounds("x", context);
            Point2D.Double labelPos = xyToPoint(maxX, 0);
// Вывести надпись в точке с вычисленными координатами
            canvas.drawString("x", (float)(labelPos.getX() -
                    bounds.getWidth() - 10), (float)(labelPos.getY() + bounds.getY()));}
    }

    /* Метод-помощник, осуществляющий преобразование координат.
    * Оно необходимо, т.к. верхнему левому углу холста с координатами
    * (0.0, 0.0) соответствует точка графика с координатами (minX, maxY),
    где
    * minX - это самое "левое" значение X, а
    * maxY - самое "верхнее" значение Y.
    */
    protected Point2D.Double xyToPoint(double x, double y) {
        if(rotate) {
            // Вычисляем смещение X от самой левой точки (minX)
            double deltaX = x - minX;
// Вычисляем смещение Y от точки верхней точки (maxY)
            double deltaY = maxY - y;
            return new Point2D.Double(deltaX*scale, deltaY*scale);
        }
        else{

// Вычисляем смещение X от самой левой точки (minX)
            double deltaX = x - minX;
// Вычисляем смещение Y от точки верхней точки (maxY)
            double deltaY = maxY - y;
            return new Point2D.Double(deltaX*scale, deltaY*scale);
        }
    }

    /* Метод-помощник, возвращающий экземпляр класса Point2D.Double
     * смещѐнный по отношению к исходному на deltaX, deltaY
     * К сожалению, стандартного метода, выполняющего такую задачу, нет.
     */
    protected Point2D.Double shiftPoint(Point2D.Double src, double deltaX, double deltaY) {
// Инициализировать новый экземпляр точки
        Point2D.Double dest = new Point2D.Double();
// Задать еѐ координаты как координаты существующей точки + заданные смещения
        dest.setLocation(src.getX() + deltaX, src.getY() + deltaY);
        return dest;
    }
}