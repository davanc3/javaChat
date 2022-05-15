package model;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Класс являет хранилещем всего чата. Реализующий паттерн проектирование SINGLETON
 * В себе содержит коллекцию всех клиентов {@link ChatStoreg#clients} (клиенты загружаются с файла после старта сервера)
 * Коллекцию сообщений  {@link ChatStoreg#message}, заполнение данной коллекции идет из файла (старые) и когда с сервера приходит сообщение(новые)
 * Статическое поле {@link ChatStoreg#COUNT_ID_MESSAGE} - хранит в себе id сообщений.
 * @author Зиняков Ефим
 * @version 1.0
 */
public class ChatStoreg implements Serializable {
    /** Коллекция которая хранит всех пользователей загружаемых из файла */
    private HashMap<Integer,Client> clients;        // все пользователи
    /** Коллекция которая все сообщения */
    private ArrayList<Message> message;         // история всех сообщений
    /** Хранит в себе id сообщений */
    public static int COUNT_ID_MESSAGE = 0;
    /** Поле для реализации паттерна SINGLETON */
    private static ChatStoreg chatStoreg = null;

    /**
     * Приватный конструктор для реализции SINGLETON.
     * Для создание объект нужно вызывать метод {@link ChatStoreg#getChatStore()}
     */
    private ChatStoreg() {
        clients = new HashMap<>();
        message = new ArrayList<>();
    }
    /**
     * Метод нужен для создание объекта данного класса.
     * Или же если объект создан верене ссылку на него
     * @return Вернет созданный объект {@link ChatStoreg}
     */
    public static ChatStoreg getChatStore(){
        if (chatStoreg == null)
            chatStoreg = new ChatStoreg();
        return chatStoreg;
    }

    /**
     * Добавление клиента в коллекцию клиентов.
     * @param id id клиента
     * @param login логин клиента
     * @param password пароль клиента
     */
    private void createClient(int id,String login,String password){
        clients.put(id,new Client(id,login,password));
    }

    /**
     * Создание сообщение {@link CommonMessage},{@link PersonMessage} и добавление его в коллекцию.
     * Если сообщение предназначено для всех передаваемое значение recipient должно быть null.
     * @param date Время отправление сообщения
     * @param message Сообщение
     * @param sender Отправитель
     * @param recipient Получатель, если получателя нет нужно отправиться передать null
     * @return Вернет созданое сообщение
     */
    public Message addMessage(Date date, String message, Client sender, Client recipient){
        if (message == null) throw new IllegalArgumentException("Нужно передать сообщение!");
        if (!clients.containsValue(sender)) throw new IllegalArgumentException("Такого пользователя нет!");
        Message msg;
        if (recipient == null)
            msg = new CommonMessage(COUNT_ID_MESSAGE++,message,sender,date);
        else
            msg = new PersonMessage(COUNT_ID_MESSAGE++,message,sender,recipient,date);
        clients.get(sender.getId()).addMessage(msg);
        this.message.add(msg);
        return msg;
    }

    /**
     * Меняет пользователю({@link Client}) поле isOnline = false (т.е не в сети).
     * @param id id пользователя которому нужно изменить онлайн.
     */
    public void logout(int id){
        clients.get(id).setOnline(false);
    }

    /**
     * Авторизация пользователя. Проверяте не в сети ли пользователь и правильно ли передан пароль и логин.
     * @param login логин для проверки
     * @param password пароль для проверки
     * @return Если авторизация успешна вернет id пользователя который прошел проверку, иначе вернет -1
     */
    public int login(String login, String password){
        for (Client c : clients.values()) {
            int clientId = c.login(login, password);
            if (clientId != -1){
                return clientId;
            }
        }
        return -1;
    }

    /**
     * Загрузка пользователь в коллекцию {@link ChatStoreg#clients} из файла.
     * @param path путь к файлу где храняться пользователи. Формат записи в файле id:login:password
     * @throws IOException Вызывает если не получилось открыть поток с файлом или не удалось считать данные с потока
     */
    private void loadClients(String path) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(path));
        while(true){
            String str = br.readLine();
            if (str == null) break;
            String[] buff;
            buff = str.split(":");
            createClient(Integer.parseInt(buff[0]),buff[1],buff[2]);
        }
    }

    /**
     * Сохранение сообщений определенного пользователя (только то что он отправлял) в файл
     * Сохранение происходит с помощью сериализации.
     * @param path путь к файлу.
     * @throws IOException Вызывает если не получилось открыть поток с файлом или не удалось записать данные в файл.
     */
    public void saveClientMsg(String path) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));
        for (int i = 0; i < clients.size(); i++)
            oos.writeObject(clients.get(i).getMessages());
        oos.close();
    }
    /**
     * Загрузка сообщений определенного пользователя (только то что он отправлял) из файла.
     * Загрузка происходит с помощью сериализации.
     * @param path путь к файлу.
     * @throws IOException Вызывает если не получилось открыть поток с файлом или не удалось считать данные в файл.
     * @throws ClassNotFoundException Если не удалось считать данные с файла.
     */
    private void loadClientMsg(String path) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));
        for (int i = 0; i<clients.size(); i++){
            ArrayList<Message> mes = (ArrayList<Message>) ois.readObject();
            if (!mes.isEmpty())
                clients.get(i).setMessages(mes);
        }
        ois.close();
    }
    /**
     * Сохранение сообщений из коллекци {@link ChatStoreg#message} в файл
     * Сохранение происходит с помощью сериализации.
     * @param path путь к файлу.
     * @throws IOException Вызывает если не получилось открыть поток с файлом или не удалось записать данные в файл.
     */
    private void saveMessage(String path) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));
        oos.writeObject(message);
        oos.writeInt(COUNT_ID_MESSAGE);
        oos.close();
    }
    /**
     * Загрузка сообщений в коллекцию {@link ChatStoreg#message} из файла.
     * Загрузка происходит с помощью сериализации.
     * @param path путь к файлу.
     * @throws IOException Вызывает если не получилось открыть поток с файлом или не удалось считать данные в файл.
     * @throws ClassNotFoundException Если не удалось считать данные с файла.
     */
    private void loadMessage(String path) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));
        message = (ArrayList<Message>) ois.readObject();
        COUNT_ID_MESSAGE = ois.readInt();
        ois.close();
    }

    /**
     * Метод сохраняет хранилище в файл. В методе вызываются методы
     * saveMessage - сохраняет всю историю сообщений в файл.
     * saveClientMsg - сохраняет сообщение каждого пользователя(т.е. каждое сообщение отправленное определенным пользователем)
     * Сохранение происходить с помощью сериализации.
     * @param pathMessage Путь к файлу где будут храниться все сообщения
     * @param pathClientMsg Путь к файлу где будут храниться сообщение каждого пользователя
     * @throws IOException Вызывает если не получилось открыть поток с файлом или не удалось записать данные в файл.
     */
    public void save(String pathMessage,String pathClientMsg) throws IOException {
        saveMessage(pathMessage);
        saveClientMsg(pathClientMsg);
    }
    /**
     * Метод загружает хранилище из файла. В методе вызываются методы
     * loadMessage - загружает всю историю сообщений из файл.
     * loadClientMsg - загружает сообщение каждого пользователя(т.е. каждое сообщение отправленное определенным пользователем) из файла
     * loadClient - загружает клиентов из файла в коллекцию {@link ChatStoreg#clients}. Формат записи в файле id:login:password
     * Загрузка происходить с помощью сериализации.
     * @param pathMessage Путь к файлу где будут храняться все сообщения
     * @param pathClientMsg Путь к файлу где будут храняться сообщение каждого пользователя
     * @param pathClient Путь к файлу где будут храняться клиенты.
     * @throws IOException Вызывает если не получилось открыть поток с файлом или не удалось записать данные в файл.
     * @throws ClassNotFoundException вызываеться если не удалось считать данные с файла.
     */
    public void load(String pathMessage,String pathClientMsg,String pathClient) throws IOException, ClassNotFoundException {
        loadClients(pathClient);
        loadMessage(pathMessage);
        loadClientMsg(pathClientMsg);
    }

    /**
     * Ищет клиента типа {@link Client} по его логину в коллекции.
     * @param login логин по которому нужно искать клиента
     * @return Если клиент найден вернет клиента, иначе null
     */
    public Client findClientByLogin(String login){
        for (Client c : clients.values()){
            if (c.getLogin().equals(login)) return c;
        }
        return null;
    }
    /**
     * Ищет в коллекции логин клиента по его id
     * @param id id по которому нужно искать
     * @return Вернет логин
     */
    public String findLoginClientById(int id){
        return clients.get(id).getLogin();
    }
    /**
     * Ищет клиентов типа {@link Client} которые онлайн в коллекции.
     * @return Вернет ArrayList клиентов которые в сети.
     */
    public ArrayList<String> findAllLoginIsOnline(){
        ArrayList<String> clientIsOnline = new ArrayList<>();
        for (Client c: clients.values())
            if (c.isOnline()) clientIsOnline.add(c.getLogin());
        return clientIsOnline;
    }

    /**
     * Достать клиента по его id
     * @param id id по которому нужно достать
     * @return Вернет клиента типа {@link Client}
     */
    public Client getClient(int id){
        return clients.get(id);
    }

    /**
     * Получить коллекцию всех сообщений.
     * @return Вернет коллекцию всех сообщений.
     */
    public ArrayList<Message> getMessage(){
        return message;
    }

}
