package model;

import java.io.Serializable;
import java.util.Date;

/**
 * Класс который определяет сообщение для всех. Наследует класс Message {@link Message} *
 * @author Зиняков Ефим
 * @version 1.0
 */
public class CommonMessage extends Message implements Serializable {
    /**
     * Конструктор - создание объекта данного класса
     * Вызывает конструкот базового класса {@link Message#Message(int, String, Client, Date)}
     * @param id уникальный идентификатор
     * @param message сообщение
     * @param sender отправитель
     * @param date время отправление
     */
    public CommonMessage(int id, String message, Client sender, Date date){
        super(id,message,sender,date);
    }

}
