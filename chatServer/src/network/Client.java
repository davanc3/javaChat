package network;


import controller.GeneralController;
import controller.ServerFormListiner;
import parsers.StringParser;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

/**
 * Серверны клиент. Принимает сообщение которые пришли с клиента и отпраляет {@link GeneralController} для обработки.
 * @author Зиняков Ефим
 * @version 1.0
 */
public class Client extends Thread{
    /** Сокет */
    private Socket s;
    /** id пользователя подключеного к этому сокету */
    private int clientId;
    /** Поток получение данных с клиента */
    private BufferedReader in;
    /** Поток отправки данных на клиента */
    private BufferedWriter out;
    /** Флаг для выхода из из потока клиента */
    private boolean exit = true;

    /**
     * Конструктор создание объекта {@link Client}
     * @param s сокет
     * @throws IOException Вызывается если что-то случилось с потоком передачи данных
     */
    public Client(Socket s) throws IOException {
        this.s = s;
        in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
        connect();
    }
    /** Подключение отдельного потока для пользователя */
    public void connect(){
        start();
    }
    @Override
    /**
     * Выполнение потока. Обработка данных которые приходят с клиента.
     */
    public void run() {
        try {
            String msg;
            while (exit) {
                msg = in.readLine();
                if (msg == null){
                    exit = false;
                    break;
                }
                else
                    StringParser.parsingString(msg);
                switch (StringParser.type.valueOf(StringParser.getProperty("type"))){
                    case LOGIN:
                        GeneralController.login(this);
                        break;
                    case SUBMIT_MESSAGE:
                        GeneralController.submitMessage(clientId,StringParser.getProperty("msg"), StringParser.getProperty("whom"));
                        break;
                    case GET_MESSAGES:
                        GeneralController.getMessages(this);
                        break;
                    case DISCONNECT:
                        exit = false;
                        GeneralController.disconnect(GeneralController.findLoginClientById(clientId),clientId);
                        break;
                }
                StringParser.flush();
            }
            s.close();
        } catch (SocketException e) {
            StringParser.flush();
        } catch (IOException exception) {
            ServerFormListiner.errorMes(exception.getMessage());
        }
    }

    /**
     * Отключение сокета.
     * @throws IOException Срабатывает если не получилось отключить сокет.
     */
    public void disconnect() throws IOException {
        s.close();
    }

    /**
     * Отправить сообщение по сокету.
     * @param str сообщение
     */
    public void sendMsg(String str){
        try {
            out.write(str+"\n");
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Получение поля {@link Client#clientId}
     * @return Вернет id {@link Client}
     */
    public int getClientId() {
        return clientId;
    }

    /**
     * Заменять или добавить в поле {@link Client#clientId}
     * @param clientId id клиента, кому пренадлежит этот сокет
     */
    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    /**
     * Изменяет флаг для выхода из потока
     * @param exit занчение которое нужно проставить
     */
    public void setExit(boolean exit) {
        this.exit = exit;
    }
}


