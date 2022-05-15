package view;

import controller.GeneralController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Класс является интерфейсом программы. Реализующий паттерн проектирование SINGLETON
 * В себе содержит компонеты интерфейса
 * @author Ванцын Дмитрий
 * @version 1.0
 */
public class View extends JFrame {
    /** Панель, содержащая в себе панели формы */
    private JPanel mainPanel = new JPanel();
    /** Панель, содержащая в себе компонеты для подключения к северу */
    private JPanel connectPanel = new JPanel();
    /** Панель, содержащая в себе кнопки подключиться, отключиться и выход */
    private JPanel buttonPanel = new JPanel();
    /** Панель, содержащая в себе текстовую область, в которую будут выводиться сообщения */
    private JPanel chatPanel = new JPanel();
    /** Панель, содержащая в себе компоненты для отправки сообщений, а так же для просмотра истории сообщений и пользователей онлайн */
    private JPanel enterPanel = new JPanel();
    /** Надпись, показывающая, куда вводить порт */
    private JLabel labelPort = new JLabel("Порт:");
    /** Текстовая панель, для ввода порта */
    private JTextField textFieldPort = new JTextField();
    /** Надпись, показывающая, куда вводить хост */
    private JLabel labelHost = new JLabel("Хост:");
    /** Текстовая панель, для ввода хоста */
    private JTextField textFieldHost = new JTextField();
    /** Надпись, показывающая, куда вводить логин */
    private JLabel labelLogin = new JLabel("Логин:");
    /** Текстовая панель, для ввода логина */
    private JTextField textFieldLogin = new JTextField();
    /** Надпись, показывающая, куда вводить пароль */
    private JLabel labelPassword = new JLabel("Пароль:");
    /** Текстовая панель, для ввода пароля */
    private JTextField textFieldPassword= new JTextField();
    /** Кнопка подключиться к серверу */
    private JButton buttonConnect = new JButton("Подключиться");
    /** Кнопка отключиться от сервера */
    private JButton buttonDisconnect = new JButton("Отключиться");
    /** Кнопка выход из приложения */
    private JButton buttonExit = new JButton("Выход");
    /** Текстовая область, в которую будут выводиться сообщения */
    private JTextArea chat = new JTextArea();
    /** Поле, прикрепляющие к текстовой области скрол */
    private JScrollPane scroll = new JScrollPane(chat, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    /** Поле, в которое надо ввести сообщение */
    private JTextField textFieldEnter = new JTextField("Введите сообщение");
    /** Кнопка отправить сообщение */
    private JButton buttonEnter = new JButton("Отправить");
    /** Поле для начальной инициализации поля выбора, кому отправить сообщение */
    String[] users = {"Всем"};
    /** Поле для выбора, кому отправить сообщение */
    private JComboBox <String> selectUsers = new JComboBox<String>(users);
    /** Кнорка, для просмотра истории переписки */
    private JButton buttonGetAllMessage = new JButton("История переписки");
    /** Кнопка для просмотра пользователей, которые подключены к сереверу */
    private JButton buttonIsOnline = new JButton("Пользователи онлайн");
    /** Поле для реализации паттерна SINGLETON */
    private static View view = null;

    /**
     * Приватный конструктор для реализции SINGLETON.
     * @param title задаёт имя формы
     * @exception HeadlessException Вызывается, когда код, зависящий от клавиатуры, дисплея или мыши, вызывается в среде, не поддерживающей клавиатуру, дисплей или мышь.
     * Для создание объект нужно вызывать метод {@link View#getView()}
     */
    private View(String title) throws HeadlessException {
        super(title);
        setSize(1000, 720);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        getRootPane().setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        init();
    }

    /**
     * Метод нужен для создание объекта данного класса.
     * Или же если объект создан вернёт ссылку на него
     * @return Вернет созданный объект {@link View}
     */
    public static View getView (){
        if(view == null)
            view = new View("Чат");
        return view;
    }

    /** Задаёт положение всех компонентов, распологающихся на форме */
    public void init (){
        textFieldHost.setText(GeneralController.properties.getProperty("host"));
        textFieldPort.setText(GeneralController.properties.getProperty("port"));
        mainPanel.setLayout(new BorderLayout());
        connectPanel.setLayout(new BoxLayout(connectPanel, BoxLayout.X_AXIS));
        labelHost.setFont(new Font(null, Font.BOLD, 13));
        connectPanel.add(Box.createHorizontalStrut(7));
        connectPanel.add(labelHost);
        connectPanel.add(Box.createHorizontalStrut(7));
        textFieldHost.setPreferredSize(new Dimension(70,10));
        connectPanel.add(textFieldHost);
        connectPanel.add(Box.createHorizontalStrut(7));
        labelPort.setFont(new Font(null, Font.BOLD, 13));
        connectPanel.add(labelPort);
        connectPanel.add(Box.createHorizontalStrut(7));
        textFieldPort.setPreferredSize(new Dimension(40,10));
        connectPanel.add(textFieldPort);
        connectPanel.add(Box.createHorizontalStrut(7));
        labelLogin.setFont(new Font(null, Font.BOLD, 13));
        connectPanel.add(labelLogin);
        connectPanel.add(Box.createHorizontalStrut(7));
        textFieldLogin.setPreferredSize(new Dimension(70,10));
        connectPanel.add(textFieldLogin);
        labelPassword.setFont(new Font(null, Font.BOLD, 13));
        labelPassword.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        connectPanel.add(labelPassword);
        textFieldPassword.setPreferredSize(new Dimension(70,10));
        connectPanel.add(textFieldPassword);
        buttonPanel.setLayout(new GridLayout(1,3,10,10));
        buttonPanel.setBorder(new EmptyBorder(0,10,0,0));
        buttonPanel.add(buttonConnect);
        buttonPanel.add(buttonDisconnect);
        buttonPanel.add(buttonExit);
        connectPanel.add(buttonPanel);
        connectPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        mainPanel.add(connectPanel, BorderLayout.NORTH);

        chatPanel.setLayout(new GridLayout());
        mainPanel.add(chatPanel, BorderLayout.CENTER);

        chat.setEditable(false);
        chatPanel.add(scroll);
        textFieldEnter.setFont(new Font(null, Font.BOLD, 14));
        enterPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 10;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(5,7,0,7);
        enterPanel.add(textFieldEnter, c);

        c.fill = GridBagConstraints.CENTER;
        c.weightx = 1;
        c.insets = new Insets(10,0,5,5);
        c.gridx = 1;
        c.gridy = 0;
        enterPanel.add(buttonEnter, c);

        c.fill = GridBagConstraints.PAGE_END;
        c.weightx = 1;
        c.insets = new Insets(10,0,5,5);
        c.gridx = 2;
        c.gridy = 0;
        enterPanel.add(buttonIsOnline, c);

        selectUsers.setBackground(Color.CYAN);
        selectUsers.setFont(new Font(null, Font.BOLD, 13));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(5,7,10,7);
        c.gridx = 0;

        c.gridy = 1;
        enterPanel.add(selectUsers, c);
        c.fill = GridBagConstraints.CENTER;

        c.insets = new Insets(10,0,10,0);
        c.gridx = 1;
        c.gridwidth = 2;
        c.gridy = 1;
        enterPanel.add(buttonGetAllMessage, c);

        mainPanel.add(enterPanel, BorderLayout.SOUTH);

        add(mainPanel);
        disconnect();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Переклчючает доступности кнопок на форме, если клиент подключился к серверу
     */
    public void connect(){
        buttonExit.setEnabled(false);
        buttonConnect.setEnabled(false);
        buttonDisconnect.setEnabled(true);
        buttonEnter.setEnabled(true);
        textFieldEnter.setEnabled(true);
        textFieldHost.setEnabled(false);
        textFieldLogin.setEnabled(false);
        textFieldPassword.setEnabled(false);
        textFieldPort.setEnabled(false);
        selectUsers.setEnabled(true);
        buttonGetAllMessage.setEnabled(true);
        buttonIsOnline.setEnabled(true);

    }

    /**
     *Переключает доступность кнопок на форме, если клиент отключился от сервера
     */
    public void disconnect(){
        buttonExit.setEnabled(true);
        buttonConnect.setEnabled(true);
        buttonDisconnect.setEnabled(false);
        buttonEnter.setEnabled(false);
        textFieldEnter.setEnabled(false);
        textFieldHost.setEnabled(true);
        textFieldLogin.setEnabled(true);
        textFieldPassword.setEnabled(true);
        textFieldPort.setEnabled(true);
        selectUsers.setEnabled(false);
        buttonGetAllMessage.setEnabled(false);
        buttonIsOnline.setEnabled(false);
    }

    /**
     * Функция, которая выводит заданное сообщение в форму, указывая время, в которое оно было отправлено
     * @param message сообщение, которое надо вывыести в форму
     */
    public void sendMes (String message){
        Date currentDate = new Date();
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String time = timeFormat.format(currentDate);
        chat.append(time + " " + message + "\n");
    }

    /**
     * Функция, которая выводит заданное сообщение в форму, не указывая время, в которое оно было отправлено
     * @param message сообщение, которое надо вывыести в форму
     */
    public void sendMesNoTime(String message){
        message = message.replaceAll("#n", "\n");
        chat.append(message + "\n");
    }

    /**
     * Получить доступ к кнопке История сообщений
     * @return вернёт компонент(кнопку История сообщений)
     */
    public JButton getButtonGetAllMessage() {
        return buttonGetAllMessage;
    }

    /**
     * Получить доступ к текстовому полю, в котором указан порт для подключения к серверу
     * @return вернёт компонент(текстовое поле с портом подключения к сереверу)
     */
    public JTextField getTextFieldPort() {
        return textFieldPort;
    }

    /**
     * Получить доступ к текстовому полю, в котором указан хост для подключения к серверу
     * @return вернёт компонент(текстовое поле с хостом подключения к сереверу)
     */
    public JTextField getTextFieldHost() {
        return textFieldHost;
    }
    /**
     * Получить доступ к текстовому полю, в котором указан логин для подключения к серверу
     * @return вернёт компонент(текстовое поле с логином подключения к сереверу)
     */
    public JTextField getTextFieldLogin() {
        return textFieldLogin;
    }
    /**
     * Получить доступ к текстовому полю, в котором указан пароль для подключения к серверу
     * @return вернёт компонент(текстовое поле с паролем подключения к сереверу)
     */
    public JTextField getTextFieldPassword() {
        return textFieldPassword;
    }
    /**
     * Получить доступ к кнопке подключиться
     * @return вернёт компонент(кнопку подключиться)
     */
    public JButton getButtonConnect() {
        return buttonConnect;
    }
    /**
     * Получить доступ к кнопке отключится
     * @return вернёт компонент(кнопку отключиться)
     */
    public JButton getButtonDisconnect() {
        return buttonDisconnect;
    }
    /**
     * Получить доступ к кнопке выход
     * @return вернёт компонент(кнопку выход)
     */
    public JButton getButtonExit() {
        return buttonExit;
    }
    /**
     * Получить доступ к текстовому полю, в котором указано сообщение
     * @return вернёт компонент(текстовое поле с сообщением)
     */
    public JTextField getTextFieldEnter() {
        return textFieldEnter;
    }
    /**
     * Получить доступ к кнопке отправить сообщение
     * @return вернёт компонент(кнопку отправить сообщение)
     */
    public JButton getButtonEnter() {
        return buttonEnter;
    }
    /**
     * Получить доступ к меню, в котором можно выбрать, кому отправить сообщение
     * @return вернёт компонент(меню, в котором можно выбрать, кому отправить сообщение)
     */
    public JComboBox<String> getSelectUsers() {
        return selectUsers;
    }
    /**
     * Получить доступ к кнопке посмотрть всех пользователей онлайн
     * @return вернёт компонент(кнопку посмотрть всех пользователей онлайн)
     */
    public JButton getButtonIsOnline() {
        return buttonIsOnline;
    }
}