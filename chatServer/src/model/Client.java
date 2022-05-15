package model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Класс реализуеший модель клиента.
 * @author Зиняков Ефим
 * @version 1.0
 */
public class Client implements Serializable {
    /** Уникальный идентификатор клиента */
    private int id;
    /** Уникальный логин клиента */
    private String login;
    /** Пароль клиента */
    private String password;
    /** Хранилище сообщений определнного клиента */
    private ArrayList<Message> messages;
    /** Поле показывающие в сети ли клиент */
    private boolean isOnline = false;

    /**
     *  Конструктор - создание нового объекта клиента.
     *  @param id id клиента
     *  @param login логин клиента
     *  @param password пароль клиента
     */
    public Client (int id, String login, String password){
        this.id = id;
        this.login = login;
        this.password = password;
        messages = new ArrayList<>();
    }
    /**
     * Проверка для авторизации пользователя. Проверяет совпадение логина и пароля.
     * @param login логин клиента
     * @param password пароль клиента
     * @return Вернет id клиента, если пользователь не в сети и пароль и логин верны. Иначе вернет -1.
     */
    public int login(String login,String password){
        if(!isOnline)
            if (this.login.equals(login))
                if (this.password.equals(password)){
                    isOnline = true;
                    return id;
                }
        return -1;
    }

    /**
     * Добавление сообщение в коллекцию сообщений клиента
     * @param message сообщение которое будет добавленно в коллекцию
     */
    public void addMessage(Message message){
        messages.add(message);
    }

    /**
     * Вывести информацию о полях клиента
     * @return Вернет строку со всеме полями (оформленную)
     */
    public String getInfo(){
        return "id: "+ id +" login: " + login + " password: "+ password;
    }

    /**
     * Получение значение поля {@link Client#id}
     * @return вернет id клиента
     */
    public int getId() {
        return id;
    }

    /**
     * Получение значение поля {@link Client#isOnline}
     * @return вернет true если пользователь в сети, иначе false
     */
    public boolean isOnline() {
        return isOnline;
    }

    /**
     * Замена поля {@link Client#isOnline}
     * @param online значение на которое будет заменяться
     */
    public void setOnline(boolean online) {
        isOnline = online;
    }
    /**
     * Получение значение поля {@link Client#login}
     * @return вернет логин пользователя
     */
    public String getLogin() {
        return login;
    }
    /**
     * Получение значение поля {@link Client#messages}
     * @return вернет коллекцию всех сообщений пользователя
     */
    public ArrayList<Message> getMessages() {
        return messages;
    }

    /**
     * Заменяет поля {@link Client#messages}
     * @param messages коллекция на которую нужно заменять
     */
    public void setMessages(ArrayList<Message> messages){
        this.messages = messages;
    }
}
