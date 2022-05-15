package model;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Класс который определяет личные сообщения. Наследует класс Message {@link Message}
 * Отличие от других классов Message тут есть поле которые хранит в себе получателя.
 * @author Зиняков Ефим
 * @version 1.0
 */
public class PersonMessage extends Message implements Serializable {
    /** Поле получатель. Тип {@link Client}*/
    protected Client recipient;
    /**
     * Конструктор - создание объекта данного класса
     * Вызывает конструкот базового класса {@link Message#Message(int, String, Client, Date)},
     * а также заполняет поле {@link PersonMessage#recipient}     *
     * @param id уникальный идентификатор
     * @param message сообщение
     * @param sender отправитель
     * @param recipient получатель
     * @param date время отправление
     */
    public PersonMessage(int id, String message, Client sender, Client recipient, Date date){
        super(id,message,sender,date);
        this.recipient = recipient;
    }

    /**
     * Формирует строку для вывода информации об объекте.
     * Вызывает мето {@link Message#getInfo()} и добавляет к нему поле получателя {@link PersonMessage#recipient}
     * @return Вернет оформленную строку.
     */
    public String getInfo(){
        return super.getInfo()+ " recipient: " + recipient;
    }

    /**
     * Формирует строку для отправки ее на клиент.
     * Переопределяет метод {@link Message#sendMessage()}
     * @return Вернет оформленную строку
     */
    public String sendMessage(){
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        return  dateFormat.format(date) +" " + sender.getLogin() + " -> "+recipient.getLogin()+": " + message;
    }
    /**
     * Получить поле {@link PersonMessage#recipient}
     * @return Вернет поле {@link PersonMessage#recipient}. Тип {@link Client}
     */
    public Client getRecipient() {
        return recipient;
    }
}
