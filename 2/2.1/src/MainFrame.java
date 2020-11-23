// Импортируются классы, используемые в приложении
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

@SuppressWarnings("serial")
// Главный класс приложения, он же класс фрейма
public class MainFrame extends JFrame {
    private Box image;

    private Double mem1 = 0.0, mem2 = 0.0, mem3 = 0.0;
    // Размеры окна приложения в виде констант
    static final int WIDTH = 500;
    static final int HEIGHT = 400;
    // Текстовые поля для считывания значений переменных,
// как компоненты, совместно используемые в различных методах
    private JTextField textFieldX;
    private JTextField textFieldY;
    private JTextField textFieldZ;

    private JTextField textFieldMem1;
    private JTextField textFieldMem2;
    private JTextField textFieldMem3;
    // Текстовое поле для отображения результата,
// как компонент, совместно используемый в различных методах
    private JLabel labelFieldResult;
    // Группа радио-кнопок для обеспечения уникальности выделения в группе
    private ButtonGroup radioButtons = new ButtonGroup();
    // Контейнер для отображения радио-кнопок
    private Box hboxFormulaType = Box.createHorizontalBox();

    private Box vboxRadioMems = Box.createVerticalBox();
    private ButtonGroup radioButtonsMems = new ButtonGroup();

    private int formulaId = 1;
    private int memId = 1;
    // Формула No1 для рассчѐта
    public Double calculate1(Double x, Double y, Double z) {
        return Math.pow(Math.log((1+x)*(1+x))+Math.cos(Math.PI*z*z*z),Math.sin(y)) +
                Math.pow(Math.exp(x*x)+Math.cos(Math.exp(z))+Math.pow(y,-1.0/2),1/x);
    }
    // Формула No2 для рассчѐта
    public Double calculate2(Double x, Double y, Double z) {
        return Math.pow(Math.cos(Math.PI*x*x*x)+Math.log((1+y)*(1+y)),1.0/4) *
                (Math.exp(z*z) + Math.pow(x,-1.0/2) + Math.cos(Math.exp(y)));
    }
    // Вспомогательный метод для добавления кнопок на панель
    private void addRadioButton(String buttonName, final int formulaId) {
        JRadioButton button = new JRadioButton(buttonName);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                MainFrame.this.formulaId = formulaId;
                if(formulaId == 1)
                {
                    image.getComponent(0).setVisible(true);
                    image.getComponent(1).setVisible(false);
                }
                else
                {
                    image.getComponent(0).setVisible(false);
                    image.getComponent(1).setVisible(true);
                }
                image.repaint();
            }
        });
        radioButtons.add(button);
        hboxFormulaType.add(button);
    }

    private void addRadioButtonMems(String buttonName, final int memId) {
        JRadioButton button = new JRadioButton(buttonName);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                MainFrame.this.memId = memId;
            }
        });
        radioButtonsMems.add(button);
        vboxRadioMems.add(button);
    }

    // Конструктор класса
    public MainFrame() {
        super("Вычисление формулы");
        this.setSize(WIDTH, HEIGHT);
        Toolkit kit = Toolkit.getDefaultToolkit();
// Отцентрировать окно приложения на экране
        setLocation((kit.getScreenSize().width - WIDTH)/2,
                (kit.getScreenSize().height - HEIGHT)/2);

        image = Box.createHorizontalBox();
        MyCanvas _1 = new MyCanvas(1, this);
        MyCanvas _2 = new MyCanvas(2, this);
        image.add(_1);
        _2.setVisible(false);
        image.add(_2);

// Создать область с полями ввода для X и Y, Z
        JLabel labelForX = new JLabel("X:");
        textFieldX = new JTextField("0", 10);
        textFieldX.setMaximumSize(textFieldX.getPreferredSize());
        JLabel labelForY = new JLabel("Y:");
        textFieldY = new JTextField("0", 10);
        textFieldY.setMaximumSize(textFieldY.getPreferredSize());
        JLabel labelForZ = new JLabel("Z:");
        textFieldZ = new JTextField("0", 10);
        textFieldZ.setMaximumSize(textFieldZ.getPreferredSize());
        Box hboxVariables = Box.createHorizontalBox();
        hboxVariables.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        hboxVariables.add(Box.createHorizontalGlue());
        hboxVariables.add(labelForX);
        hboxVariables.add(Box.createHorizontalStrut(10));
        hboxVariables.add(textFieldX);
        hboxVariables.add(Box.createHorizontalStrut(30));
        hboxVariables.add(labelForY);
        hboxVariables.add(Box.createHorizontalStrut(10));
        hboxVariables.add(textFieldY);
        hboxVariables.add(Box.createHorizontalStrut(30));
        hboxVariables.add(labelForZ);
        hboxVariables.add(Box.createHorizontalStrut(10));
        hboxVariables.add(textFieldZ);
        hboxVariables.add(Box.createHorizontalGlue());

        hboxFormulaType.add(Box.createHorizontalGlue());
        addRadioButton("Формула 1", 1);
        addRadioButton("Формула 2", 2);
        radioButtons.setSelected(radioButtons.getElements().nextElement().getModel(), true);
        hboxFormulaType.add(Box.createHorizontalGlue());
        hboxFormulaType.setBorder(BorderFactory.createLineBorder(Color.BLACK));

// Создать область для вывода результата
        JLabel labelForResult = new JLabel("Результат:");
        labelFieldResult = new JLabel("0", 10);
        labelFieldResult.setMaximumSize(labelFieldResult.getPreferredSize());
        Box hboxResult = Box.createHorizontalBox();
        hboxResult.add(Box.createHorizontalGlue());
        hboxResult.add(labelForResult);
        hboxResult.add(Box.createHorizontalStrut(10));
        hboxResult.add(labelFieldResult);
        hboxResult.add(Box.createHorizontalGlue());
        hboxResult.setBorder(BorderFactory.createLineBorder(Color.BLACK));

// Создать область для кнопок
        JButton buttonCalc = new JButton("Вычислить");
        buttonCalc.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                try {
                    Double x = Double.parseDouble(textFieldX.getText());
                    Double y = Double.parseDouble(textFieldY.getText());
                    Double z = Double.parseDouble(textFieldZ.getText());
                    Double result;
                    if (formulaId==1)
                        result = calculate1(x, y, z);
                    else
                        result = calculate2(x, y, z);
                    labelFieldResult.setText(result.toString());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(MainFrame.this,
                            "Ошибка в формате записи числа с плавающей точкой", "Ошибочный формат числа",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        JButton buttonReset = new JButton("Очистить поля");
        buttonReset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                textFieldX.setText("0");
                textFieldY.setText("0");
                textFieldZ.setText("0");
                labelFieldResult.setText("0");
            }
        });
        Box hboxButtons = Box.createHorizontalBox();
        hboxButtons.add(Box.createHorizontalGlue());
        hboxButtons.add(buttonCalc);
        hboxButtons.add(Box.createHorizontalStrut(30));
        hboxButtons.add(buttonReset);
        hboxButtons.add(Box.createHorizontalGlue());
        hboxButtons.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        Box hboxMems = Box.createHorizontalBox();
        hboxMems.add(Box.createHorizontalGlue());
        addRadioButtonMems("Переменная 1", 1);
        addRadioButtonMems("Переменная 2", 2);
        addRadioButtonMems("Переменная 3", 3);
        radioButtonsMems.setSelected(radioButtonsMems.getElements().nextElement().getModel(), true);


        textFieldMem1 = new JTextField("0", 30);
        textFieldMem1.setMaximumSize(textFieldMem1.getPreferredSize());
        textFieldMem2 = new JTextField("0", 30);
        textFieldMem2.setMaximumSize(textFieldMem2.getPreferredSize());
        textFieldMem3 = new JTextField("0", 30);
        textFieldMem3.setMaximumSize(textFieldMem3.getPreferredSize());
        Box vboxTextMems = Box.createVerticalBox();
        vboxTextMems.add(textFieldMem1);
        vboxTextMems.add(Box.createVerticalStrut(5));
        vboxTextMems.add(textFieldMem2);
        vboxTextMems.add(Box.createVerticalStrut(5));
        vboxTextMems.add(textFieldMem3);

        hboxMems.add(vboxRadioMems);
        hboxMems.add(Box.createHorizontalGlue());
        hboxMems.add(vboxTextMems);
        hboxMems.add(Box.createHorizontalGlue());
        hboxMems.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JButton buttonMC = new JButton("Очистить переменную");
        buttonMC.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                if(memId == 1) {
                    mem1 = 0.0;
                    textFieldMem1.setText("0");
                }
                else if(memId == 2){
                    mem2 = 0.0;
                    textFieldMem2.setText("0");
                }
                else{
                    mem3 = 0.0;
                    textFieldMem3.setText("0");
                }
            }
        });

        JButton buttonMPlus = new JButton("Суммирование с результатом");
        buttonMPlus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                if(memId == 1){
                    textFieldMem1.setText(String.valueOf((Double.parseDouble(labelFieldResult.getText()) + mem1)));
                    mem1 = Double.parseDouble(textFieldMem1.getText());
                }
                else if(memId == 2){
                    textFieldMem2.setText(String.valueOf((Double.parseDouble(labelFieldResult.getText()) + mem2)));
                    mem2 = Double.parseDouble(textFieldMem2.getText());
                }
                else{
                    textFieldMem3.setText(String.valueOf((Double.parseDouble(labelFieldResult.getText()) + mem3)));
                    mem3 = Double.parseDouble(textFieldMem2.getText());
                }
            }
        });

        Box hboxButtonsMem = Box.createHorizontalBox();
        hboxButtonsMem.add(Box.createHorizontalGlue());
        hboxButtonsMem.add(buttonMC);
        hboxButtonsMem.add(Box.createHorizontalStrut(30));
        hboxButtonsMem.add(buttonMPlus);
        hboxButtonsMem.add(Box.createHorizontalGlue());
        hboxButtonsMem.setBorder(BorderFactory.createLineBorder(Color.BLACK));

// Связать области воедино в компоновке BoxLayout
        Box contentBox = Box.createVerticalBox();
        contentBox.add(Box.createVerticalGlue());
        contentBox.add(image);
        contentBox.add(hboxVariables);
        contentBox.add(hboxFormulaType);
        contentBox.add(hboxResult);
        contentBox.add(hboxButtons);
        contentBox.add(hboxMems);
        contentBox.add(hboxButtonsMem);
        contentBox.add(Box.createVerticalGlue());
        this.getContentPane().add(contentBox, BorderLayout.CENTER);
    }
    // Главный метод класса
    public static void main(String[] args) {
        MainFrame frame = new MainFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}