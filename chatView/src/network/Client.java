package network;


import controller.GeneralController;
import controller.ClientFormListiner;
import parser.StringParser;

import java.io.*;
import java.net.Socket;
/**
 * Клиент. Принимает сообщение которые пришли с сервера и отпраляет в {@link GeneralController} для обработки.
 * @author Зиняков Ефим
 * @version 1.0
 */
public class Client extends Thread{
    private String host ;
    private int port ;
    /** Сокет */
    private Socket s;
    /** Поток получение данных с клиента */
    private BufferedReader in;
    /** Поток отправки данных на клиента */
    private BufferedWriter out;
    /** Флаг для выхода из из потока клиента */
    private boolean exit = true;
    /**
     * Конструктор создание объекта
     * @param host host по которому нужно подключиться
     * @param port port по которому нужно подлючиться
     * @throws IOException Вызывается если что-то случилось с потоком передачи данных
     */
    public Client(String host,int port) throws IOException {
        this.host = host;
        this.port = port;
        s = new Socket(host,port);
        in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
        connect();
    }
    @Override
    /**
     * Выполнение потока. Обработка данных которые приходят с клиента.
     */
    public void run() {
        sendMsg("LOGIN#;login#:"+ GeneralController.view.getTextFieldLogin().getText()+"#;password#:"+
                GeneralController.view.getTextFieldPassword().getText());
        while (exit){
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
                            GeneralController.checkConnect(StringParser.getProperty("msg"));
                            break;
                        case STOP_SERVER:
                            GeneralController.stopServer(this);
                            break;
                        case DISCONNECT:
                            ClientFormListiner.removeUser(StringParser.getProperty("client"));
                            break;
                        case IS_ONLINE:
                            GeneralController.view.sendMes(StringParser.getProperty("msg"));
                            GeneralController.setOnline();
                            break;
                        case SUBMIT_MESSAGE:
                            GeneralController.view.sendMesNoTime(StringParser.getProperty("msg"));
                            break;
                        case MESSAGE:
                            GeneralController.setMessageHistory();
                            break;
                        case BASE:
                            GeneralController.view.sendMes(StringParser.getProperty("msg"));
                            break;
                    }
                    StringParser.flush();
                }
                s.close();
            } catch (IOException exception) {
                ClientFormListiner.errorMes(exception.getMessage());
            }
        }
    }
    /** Подключение отдельного потока для пользователя */
    public void connect() throws IOException{
        start();
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
            ClientFormListiner.errorMes(e.getMessage());
        }
    }
    /**
     * Изменяет флаг для выхода из потока
     * @param exit занчение которое нужно проставить
     */
    public void setExit(boolean exit) {
        this.exit = exit;
    }
}
