package view;

import controller.GeneralController;

import javax.swing.*;
import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Класс является интерфейсом программы.
 * В себе содержит компонеты интерфейса
 * @author Ванцын Дмитрий
 * @version 1.0
 */
public class View extends JFrame {
    /** Панель, содержащая в себе панели формы */
    private JPanel mainPanel = new JPanel();
    /** Панель, содержащая в себе тексовую обоасть, для вывода логов */
    private JPanel logPanel = new JPanel();
    /** Панель, содержащая в себе компонеты для запуска севера */
    private JPanel loginPanel = new JPanel();
    /** Надпись, показывающая, куда вводить порт */
    private JLabel labelPort = new JLabel("Порт:");
    /** Текстовая панель, для ввода порта */
    private JTextField textFieldPort = new JTextField();
    /** Кнопка старта сервера */
    private JButton buttonStart = new JButton("Старт");
    /** Кнопка остановки сервера */
    private JButton buttonStop = new JButton("Стоп");
    /** Кнопка выхода из прилодения */
    private JButton buttonExit = new JButton("Выход");
    /** Текстовая область, для вывода логов */
    private JTextArea textLog = new JTextArea();
    /** Панель, для добавления на текстовую область, для вывода логов скролл*/
    private JScrollPane scroll = new JScrollPane(textLog, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    /**
     * Конструктор, создающий интерфес серверной части программы.
     * @param title задаёт имя формы
     * @exception HeadlessException Вызывается, когда код, зависящий от клавиатуры, дисплея или мыши, вызывается в среде, не поддерживающей клавиатуру, дисплей или мышь.
     */
    public View(String title) throws HeadlessException {
        super(title);
        disconnect();
        init();
    }
    /** Задаёт положение всех компонентов, распологающихся на форме */
    public void init (){
        setSize(600, 450);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        loginPanel.setLayout(new GridLayout(1,4,10,10));
        labelPort.setFont(new Font(null, Font.BOLD, 13));
        labelPort.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        loginPanel.add(labelPort);
        loginPanel.add(textFieldPort);
        loginPanel.add(buttonStart);
        loginPanel.add(buttonStop);
        loginPanel.add(buttonExit);
        loginPanel.setBorder(BorderFactory.createEmptyBorder(0,0,10,0 ));
        logPanel.setLayout(new GridLayout());
        textLog.setEditable(false);
        logPanel.add(scroll);
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(loginPanel, BorderLayout.NORTH);
        mainPanel.add(logPanel, BorderLayout.CENTER);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        add(mainPanel);
        textFieldPort.setText(GeneralController.properties.getProperty("port"));
        setLocationRelativeTo(null);
        setVisible(true);
    }
    /**
     * Переклчючает доступности кнопок на форме, если клиент подключился к серверу
     */
    public void connect(){
        buttonExit.setEnabled(false);
        buttonStart.setEnabled(false);
        buttonStop.setEnabled(true);
        textFieldPort.setEnabled(false);
    }
    /**
     *Переключает доступность кнопок на форме, если клиент отключился от сервера
     */
    public void disconnect(){
        buttonExit.setEnabled(true);
        buttonStart.setEnabled(true);
        buttonStop.setEnabled(false);
        textFieldPort.setEnabled(true);

    }
    /**
     * Функция, которая выводит заданное сообщение в форму, указывая время, в которое оно было отправлено
     * @param message сообщение, которое надо вывыести в форму
     */
    public void sendMes (String message){
        Date currentDate = new Date();
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String timeText = timeFormat.format(currentDate);
        textLog.append(timeText + " " + message + "\n");
    }
    /**
     * Получить доступ к текстовому полю, в котором указан порт для подключения к серверу
     * @return вернёт компонент(текстовое поле с портом подключения к сереверу)
     */
    public JTextField getTextFieldPort() {
        return textFieldPort;
    }
    /**
     * Получить доступ к кнопке подключиться
     * @return вернёт компонент(кнопку старт)
     */
    public JButton getButtonStart() {
        return buttonStart;
    }
    /**
     * Получить доступ к кнопке подключиться
     * @return вернёт компонент(кнопку стоп)
     */
    public JButton getButtonStop() {
        return buttonStop;
    }
    /**
     * Получить доступ к кнопке подключиться
     * @return вернёт компонент(кнопку выход)
     */
    public JButton getButtonExit() {
        return buttonExit;
    }

}
