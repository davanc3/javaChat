package controller;

import network.Client;
import parser.StringParser;
import view.MessageHistory;
import view.View;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
/**
 * Главный контроллер приложения.
 * Связывает все части проекта, и обрабатывает их.
 * @author Ванцын Дмитрий
 * @version 1.0
 */
public class GeneralController {
    /** Путь где хранится файл с настройками */
    public static String FILE_SETTINGS_NAME = "source/config.properties";
    /** Коллекция настроек */
    public static Properties properties = new Properties();
    /** Интерфейс программы */
    public static View view = null;
    /** Слушатель интерфеса программы */
    public static ClientFormListiner kFL = null;
    /** Клиент */
    public static Client client = null;
    /**
     * Запуск всего приложение. Инициализация полей
     */
    public static void startApp (){
        try {
            properties.load(new FileInputStream(FILE_SETTINGS_NAME));
            view = View.getView();
            kFL = new ClientFormListiner(view);
        } catch (IOException exception) {
            ClientFormListiner.errorMes(exception.getMessage());
        }
    }
    /**
     * Обаботчки для кнопки 'Старт' из формы.
     * Задание сервера, загрузка в хранилище из файла.
     * @param port Порт, по которому будет подключаться сервер.
     * @param host Хост, по которому будет подключаться сервер.
     */
    public static void connect(String host,int port){
        try {
            client = new Client(host,port);
        } catch (IOException e) {
            ClientFormListiner.errorMes(e.getMessage());
        }
    }
    /**
     * Отлючение пользователя от сервера
     */
    public static void disconnect(){
        client.sendMsg("DISCONNECT");;
        view.disconnect();
    }
    /**
     * Отправка сообщений на сервер
     * @param msg сообщение
     * @param whom кому предназначено сообщение
     */
    public static void enter(String msg,String whom){
        client.sendMsg("SUBMIT_MESSAGE#;msg#:"+msg+"#;whom#:" +whom);
    }
    /**
     * Проверка подключения к серверу
     * @param str Строка, показывающая удачность или провальности подключения к серверу
     */
    public static void checkConnect(String str){
        if(str.equals("LOGINSUCCESS")){
            view.connect();
            JOptionPane.showMessageDialog(GeneralController.view, new String[]{
                            "Дражайше приветствуем тебя друже в нашем уютном чатике",
                            "Надеемся тебе понравится у нас", "Чувствуй себя как дома"},
                    "Добро пожаловать", JOptionPane.INFORMATION_MESSAGE);
        }
        else if (str.equals("LOGINFAIL")){
            JOptionPane.showMessageDialog(GeneralController.view,
                    "Не правильный логин или пароль!");
            client.setExit(false);
        }
    }
    /**
     * Изменение пользователей онлайн
     */
    public static void setOnline() {
        for (String s : StringParser.getPropertys("clientIsOnline"))
            ClientFormListiner.addToUser(s);
    }
    /**
     * Изменение истории сообщений
     */
    public static void setMessageHistory(){
        String history = StringParser.getProperty("msg");
        MessageHistory.setHistory(history.replaceAll("#n", "\n"));
    }
    /**
     * Получение пользователей онлайн
     */
    public static void getMessageHistory() {
        client.sendMsg("GET_MESSAGES#;");
    }
    /**
     * Остановка сервера
     */
    public static void stopServer(Client client) {
        client.setExit(false);
        view.disconnect();
        ClientFormListiner.removeUser(GeneralController.view.getTextFieldLogin().getText());
    }
}
