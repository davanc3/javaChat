package network;

import controller.ServerFormListiner;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

/**
 * Многопоточный сервер
 * Сервер работает на синхронном взаимодействие
 * Хранит сессию клиента.
 * @author Зиняков Ефим
 * @version 1.0
 */
public class Server extends Thread{
    /** Сервер сокет  */
    private ServerSocket ss;
    /** Все сервер сокеты */
    public ArrayList<Client> clients = new ArrayList<>();
    /** Порт */
    public String port;

    /**
     * Конструктор - создание объекта сокет.
     * @param port порт для задния ServerSocket
     * @throws IOException Вызываеться если не получилось создать ServerSocket
     */
    public Server(int port) throws  IOException{
        ss = new ServerSocket(port);
        this.port = String.valueOf(port);
        connect();
    }

    @Override
    /**
     * Выполнение потока. Ожидание подлючение и создание слиента.
     */
    public void run() {
        Socket s;
        while (true) {
            try {
                s = ss.accept();
                clients.add(new Client(s));
            } catch (SocketException e){
                break;
            } catch (IOException e) {
                ServerFormListiner.errorMes(e.getMessage());
            }
        }
    }
    /** Запуск отдельного потока для сервера */
    public void connect(){
        start();
    }

    /**
     * Остановка сервера и отключение пользователей от сервера
     * @throws IOException Вызываеться если произошел сбой в отключение сокета
     */
    public void stopServer() throws IOException {
        sendAllMsg("STOP_SERVER");
        for (Client c : clients){
            c.disconnect();
        }
        clients.clear();
        ss.close();
    }

    /**
     * Отправка личного сообщения.
     * @param str сообщение
     * @param senderId отправитель
     * @param whomId получатель
     */
    public void sendPersonMsg(String str,int senderId,int whomId){
        for (Client c: clients){
            if (c.getClientId() == senderId || c.getClientId() == whomId)
                c.sendMsg(str);
        }
    }

    /**
     * Отправка сообщения всем
     * @param str сообщение
     */
    public void sendAllMsg(String str){
        for(Client i : clients){
            if(i.isAlive()){
                i.sendMsg(str);
            }
        }
    }
}
