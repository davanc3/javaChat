package model;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Класс, который отвечает за сообщение. В себе он хранит всю информацию о сообщение, такие как
 * уникальны id, рремя когда было отправленно сообщение, само сообщение, и кто откравил (отправителем считается клиент
 * {@link Client})
 * Являтся абстрактным.
 * @author Зиняков Ефим
 * @version 1.0
 */
public abstract class Message implements Serializable {
    /** Уникальный идентификатор клиента */
    protected int id;
    /** Время когда было отправленно сообщение */
    protected Date date;
    /** Сообщение */
    protected String message;
    /** Отправитель. Объект класса  */
    protected Client sender;
    /**
     * Конструктор - создает объект данного класса.
     * @param id уникальный идентификатор
     * @param message сообщение
     * @param sender отправитель
     * @param date время отправление
     */
    public Message(int id, String message, Client sender,Date date){
        this.id = id;
        this.message = message;
        this.sender = sender;
        this.date = date;
    }
    /**
     * Мутод, который дает информацию о сообщение (оформленную)
     * @return Вернет строку оформленную строку
     */
    public String getInfo(){
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        return "id: " + id + " date: " + dateFormat.format(date) + " message: " + message + " sender: " + sender;
    }
    /**
     * Метод который формирует строку для отправки ее на клиент
     * @return Вернет оформленную строку
     */
    public String sendMessage(){
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        return dateFormat.format(date) +" " + sender.getLogin() + ": " + message;
    }
    /**
     * Вернет поле отправителя {@link Message#sender}
     * @return Вернет поле отправителя {@link Message#sender}
     */
    public Client getSender() {
        return sender;
    }
}
