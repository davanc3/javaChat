package controller;

import model.*;
import network.Server;
import parsers.StringParser;
import view.View;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

/**
 * Главный контроллер приложения.
 * Связывает все части проекта, и обрабатывает их.
 * @author Зиняков Ефим
 * @version 1.0
 */
public class GeneralController {
    /** Путь где хранится файл с настройками */
    public static String FILE_SETTINGS_NAME = "source/config.properties";
    /** Коллекция настроек */
    public static Properties properties = new Properties();
    /** Хранилище  */
    public static ChatStoreg chatStoreg = null;
    /** Сервер  */
    public static Server server = null;
    /** Форма  */
    public static View view = null;
    /** Обработчик формы  */
    public static ServerFormListiner sfl = null;

    /**
     * Запуск всего приложение. Инициализация полей
     */
    public static void startApp(){
        try{
            properties.load(new FileInputStream(FILE_SETTINGS_NAME));
            chatStoreg = ChatStoreg.getChatStore();
            view = new View("Сервер");
            sfl = new ServerFormListiner(view);
        } catch (IOException exception) {
            ServerFormListiner.errorMes(exception.getMessage());
        }
    }

    /**
     * Обаботчки для кнопки 'Старт' из формы.
     * Задание сервера, загрузка в хранилище из файла.
     * @param port Порт, по которому будет подключаться сервер.
     */
    public static void connect(int port){
        try {
            server = new Server(port);
            chatStoreg.load(properties.getProperty("pathOldMessage"),properties.getProperty("pathClientMsg"),properties.getProperty("pathClients"));
        }catch (ClassNotFoundException e){
            ServerFormListiner.errorMes(e.getMessage());
        } catch (IOException exception) {
            ServerFormListiner.errorMes(exception.getMessage());
        }
    }

    /**
     * Отправка сообщений на клиент. Добавление сообщения в хранилище
     * @param senderID отправитель
     * @param msg сообщение
     * @param whom кому
     */
    public static void submitMessage(int senderID,String msg,String whom){
        if (whom.equals("Всем")){
            Message message = chatStoreg.addMessage(new Date(),msg, chatStoreg.getClient(senderID),null);
            GeneralController.server.sendAllMsg("SUBMIT_MESSAGE#;msg#:" + message.sendMessage());
        }
        else{
            Message message = chatStoreg.addMessage(new Date(),msg,chatStoreg.getClient(senderID), chatStoreg.findClientByLogin(whom));
            server.sendPersonMsg("SUBMIT_MESSAGE#;msg#:"+message.sendMessage(),senderID,chatStoreg.findClientByLogin(whom).getId());
        }
    }

    /**
     * Авторизация пользователя.
     * @param client клиент, который пытаеться подключиться
     */
    public static void login(network.Client client){
        client.setClientId(chatStoreg.login(StringParser.getProperty("login"),StringParser.getProperty("password")));
        if (client.getClientId() != -1) {
            client.sendMsg("LOGIN#;msg#:LOGINSUCCESS");
            String clientName = findLoginClientById(client.getClientId());
            String str = "IS_ONLINE#;msg#:Пользователь " + clientName + " подлючился#;clientIsOnline#:";
            for (String s : findAllLoginIsOnline()){
                str += s+"#,";
            }
            server.sendAllMsg(str);
            view.sendMes("Пользователь "+ clientName + " подключился!");
        } else{
            view.sendMes("Совершена неудачная попытка зайти в аккаунт " +
                    StringParser.getProperty("login"));
            client.sendMsg("LOGIN#;msg#:LOGINFAIL");
            client.setExit(false);
        }
    }

    /**
     * Отлючение пользователя от сервера
     * @param client пользователь который отключается
     * @param id его id
     */
    public static void disconnect(String client, int id){
        server.sendAllMsg("DISCONNECT#;client#:"+ client);
        view.sendMes("Пользователь "+ client + " отключился!");
        chatStoreg.logout(id);
    }

    /**
     * Отключение сервера. (т.е закрытие всех сокетов, и самого сервера)
     * Сохранение данных из хранилища в файл
     */
    public static void stopServer() {
        try {
            chatStoreg.save(properties.getProperty("pathOldMessage"), properties.getProperty("pathClientMsg"));
            server.stopServer();
            view.disconnect();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Поиск в хранилище логин клиента по id
     * @param id id, по которому нужно искать
     * @return Вернет логин
     */
    public static String findLoginClientById(int id){
        return chatStoreg.findLoginClientById(id);
    }

    /**
     * Поиск в хранилище всех пользоватей которые в сети
     * @return Вернет коллекцию всех пользователей онлайн
     */
    public static ArrayList<String> findAllLoginIsOnline(){
        return chatStoreg.findAllLoginIsOnline();
    }

    /**
     * Получение истории сообщений из хранилища
     * @param client клиент который запросил историю.
     */
    public static void getMessages(network.Client client) {
        String str = "";
        for (Message message : chatStoreg.getMessage()) {
            if(message instanceof CommonMessage)
                str += message.sendMessage() + ("#n");
            else if (message instanceof PersonMessage){
                if (message.getSender().getId() == client.getClientId() ||
                        ((PersonMessage) message).getRecipient().getId() == client.getClientId())
                    str += message.sendMessage() + ("#n");
            }
        }
        client.sendMsg("MESSAGE#;msg#:" + (str.equals("")?"История сообщений пуста!":str));
    }
}
