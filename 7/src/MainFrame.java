import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Console;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;

public class MainFrame extends JFrame {
    //region Поля класса
    private static final String FRAME_TITLE = "Клиент мгновенных сообщений";
    private static final int FRAME_MINIMUM_WIDTH = 500;
    private static final int FRAME_MINIMUM_HEIGHT = 500;
    private static final int FROM_FIELD_DEFAULT_COLUMNS = 4;
    private static final int TO_FIELD_DEFAULT_COLUMNS = 4;
    private static final int PORT_FIELD_DEFAULT_COLUMNS = 4;
    private static final int INCOMING_AREA_DEFAULT_ROWS = 10;
    private static final int OUTGOING_AREA_DEFAULT_ROWS = 5;
    private static final int SMALL_GAP = 5;
    private static final int MEDIUM_GAP = 10;
    private static final int LARGE_GAP = 15;
    private final int SERVER_PORT;
    private final JTextField textFieldFrom;
    private final JTextField textFieldTo;
    private final JTextField textFieldPort;
    private final JTextPane textAreaIncoming;
    private final JTextArea textAreaOutgoing;
    //endregion

    //region Конструктор
    public MainFrame(int SERVER_PORT) throws UnknownHostException {
        super(FRAME_TITLE);
        this.SERVER_PORT = SERVER_PORT;
        //region Создание дизайна
        setMinimumSize(new Dimension(FRAME_MINIMUM_WIDTH, FRAME_MINIMUM_HEIGHT));
// Центрирование окна
        final Toolkit kit = Toolkit.getDefaultToolkit();
        setLocation((kit.getScreenSize().width - getWidth()) / 2,
                (kit.getScreenSize().height - getHeight()) / 2);
// Текстовая область для отображения полученных сообщений
        textAreaIncoming = new JTextPane();
        textAreaIncoming.setContentType("text/html");
        textAreaIncoming.setEditable(false);
// Контейнер, обеспечивающий прокрутку текстовой области
        final JScrollPane scrollPaneIncoming =
                new JScrollPane(textAreaIncoming);
// Подписи полей
        final JLabel labelFrom = new JLabel("От");
        final JLabel labelTo = new JLabel("IP получателя");
        final JLabel labelPort = new JLabel("Текущий порт " + String.valueOf(SERVER_PORT));
        final JLabel labelIp = new JLabel("Текущий ip " + InetAddress.getLocalHost().getHostAddress());
        final JLabel lableSendPort = new JLabel("Порт");
// Поля ввода имени пользователя и адреса получателя
        textFieldFrom = new JTextField(FROM_FIELD_DEFAULT_COLUMNS);
        textFieldTo = new JTextField(TO_FIELD_DEFAULT_COLUMNS);
        textFieldPort = new JTextField(PORT_FIELD_DEFAULT_COLUMNS);
// Текстовая область для ввода сообщения
        textAreaOutgoing = new JTextArea(OUTGOING_AREA_DEFAULT_ROWS, 0);
// Контейнер, обеспечивающий прокрутку текстовой области
        final JScrollPane scrollPaneOutgoing =
                new JScrollPane(textAreaOutgoing);
// Панель ввода сообщения
        final JPanel messagePanel = new JPanel();
        messagePanel.setBorder(
                BorderFactory.createTitledBorder("Сообщение"));
// Кнопка отправки сообщения
        final JButton sendButton = new JButton("Отправить");
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });
// Компоновка элементов панели "Сообщение"
        final GroupLayout layout2 = new GroupLayout(messagePanel);
        messagePanel.setLayout(layout2);
        layout2.setHorizontalGroup(layout2.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout2.createParallelGroup(GroupLayout.Alignment.TRAILING)
                        .addGroup(layout2.createSequentialGroup()
                                .addComponent(labelFrom)
                                .addGap(SMALL_GAP)
                                .addComponent(textFieldFrom)
                                .addGap(LARGE_GAP)
                                .addComponent(labelTo)
                                .addGap(SMALL_GAP)
                                .addComponent(textFieldTo)
                                .addGap(SMALL_GAP)
                                .addComponent(lableSendPort)
                                .addGap(SMALL_GAP)
                                .addComponent(textFieldPort))
                        .addGroup(layout2.createSequentialGroup()
                                .addComponent(labelPort)
                                .addGap(LARGE_GAP)
                                .addComponent(labelIp))
                        .addComponent(scrollPaneOutgoing)
                        .addComponent(sendButton))
                .addContainerGap());
        layout2.setVerticalGroup(layout2.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout2.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(labelFrom)
                        .addComponent(textFieldFrom)
                        .addComponent(labelTo)
                        .addComponent(textFieldTo)
                        .addComponent(lableSendPort)
                        .addComponent(textFieldPort))
                .addGap(MEDIUM_GAP)
                .addGroup(layout2.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(labelPort)
                        .addComponent(labelIp))
                .addGap(MEDIUM_GAP)
                .addComponent(scrollPaneOutgoing)
                .addGap(MEDIUM_GAP)
                .addComponent(sendButton)
                .addContainerGap());
// Компоновка элементов фрейма
        final GroupLayout layout1 = new GroupLayout(getContentPane());
        setLayout(layout1);
        layout1.setHorizontalGroup(layout1.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout1.createParallelGroup()
                        .addComponent(scrollPaneIncoming)
                        .addComponent(messagePanel))
                .addContainerGap());
        layout1.setVerticalGroup(layout1.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollPaneIncoming)
                .addGap(MEDIUM_GAP)
                .addComponent(messagePanel)
                .addContainerGap());
        //endregion
        //region Создание и запуск потока-обработчика запросов
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
                    while (!Thread.interrupted()) {
                        final Socket socket = serverSocket.accept();
                        final DataInputStream in = new DataInputStream(socket.getInputStream());
// Читаем имя отправителя
                        final String senderName = in.readUTF();
// Читаем сообщение
                        final String message = in.readUTF();
// Закрываем соединение
                        socket.close();
// Выделяем IP-адрес и порт
                        final String address = ((InetSocketAddress) socket.getRemoteSocketAddress())
                                .getAddress()
                                .getHostAddress();
// Выводим сообщение в текстовую область
                        var doc = (HTMLDocument)textAreaIncoming.getDocument();
                        doc.insertAfterEnd(doc.getCharacterElement(doc.getLength()),
                                senderName +
                                        " (" +
                                        address +":"+
                                        "): " + message + "<br />");
                    }
                } catch (IOException  e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(MainFrame.this,
                            "Ошибка в работе сервера",
                            "Ошибка",
                            JOptionPane.ERROR_MESSAGE);
                } catch (BadLocationException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(MainFrame.this,
                            "Ошибка в работе html",
                            "Ошибка",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }).start();
        //endregion
    }
    //endregion

    //region Функция отправки сообщений
    private void sendMessage() {
        try {
            // region Получаем необходимые параметры
            final String senderName = textFieldFrom.getText();

            String destinationAddress = textFieldTo.getText();
            final String message = textAreaOutgoing.getText();
            int clientPort;
            try {
                clientPort = Integer.parseInt(textFieldPort.getText());
            }catch (Exception ex){
                clientPort = SERVER_PORT;
            }
            //endregion
            //region Убеждаемся, что поля не пустые
            if (senderName.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Введите имя отправителя", "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (destinationAddress.isEmpty()) {
                destinationAddress = InetAddress.getLocalHost().getHostAddress();
            }
            if (message.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Введите текст сообщения", "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            //endregion
            // Создаем сокет для соединения
            final Socket socket = new Socket(destinationAddress, clientPort);
            // Открываем поток вывода данных
            final DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            // Записываем в поток имя
            out.writeUTF(senderName);
            // Записываем в поток сообщение
            out.writeUTF(message);
            // Закрываем сокет
            socket.close();
            // Помещаем сообщения в текстовую область выводa
            var doc = (HTMLDocument)textAreaIncoming.getDocument();
            doc.insertAfterEnd(doc.getCharacterElement(doc.getLength()),"Я -> "  +destinationAddress + ": " + message + "<br />");
            // Очищаем текстовую область ввода сообщения
            textAreaOutgoing.setText("");
        } catch (UnknownHostException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(MainFrame.this,
                    "Не удалось отправить сообщение: узел-адресат не найден",
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(MainFrame.this,
                    "Не удалось отправить сообщение",
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE);
        } catch (BadLocationException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(MainFrame.this,
                    "Ошибка в работе html",
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    //endregion

    //region Стандартная функция main
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                int port;
                try{
                    port = Integer.parseInt(args[0]);
                }
                catch (Exception ex){
                    port = 4567;
                }
                final MainFrame frame;
                try {
                    frame = new MainFrame(port);
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.setVisible(true);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    //endregion
}