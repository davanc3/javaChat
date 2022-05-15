package parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
/**
 * Парсер строки которая приходит с клиента.
 * Парсер разбивает строку на тип задачи и пременную по типу ключ значение.
 * Строку он заносить в HashMap, где ключ типа String и его значение тоже String.
 * В роли значение может быть массив.
 * Чтобы получить значение переменной нужно вызвать метод {@link StringParser#getProperty(String)} куда стоит
 * передать ключ(т.е название переменной).
 * Если переменная являеться массивом то нужно вызвать медот {@link StringParser#getPropertys(String)} куда также стоит
 * передать ключ.
 * @author Зиняков Ефим
 * @version 1.0
 */
public class StringParser {
    /** Коллекция которая хранит все переменные и ее значения */
    private static HashMap<String, ArrayList<String>> properties;
    /**
     * Enum, который хранит тип задачи <br>
     * LOGIN - проверка при подключение <br>
     * DISCONNECT - отключение пользователя <br>
     * SUBMIT_MESSAGE - отправка сообщений на сервер <br>
     * MESSAGE - истории сообщения <br>
     * STOP_SERVER - отключение сервера
     * BASE - вывод сообщение в лог
     * IS_ONLINE - пользователи онлайн
     */
    public enum type {LOGIN,DISCONNECT,IS_ONLINE,STOP_SERVER,SUBMIT_MESSAGE, MESSAGE,BASE}
    /**
     * Очищение хранилища переменных
     */
    public static void flush(){
        properties.clear();
    }
    /**
     * Метод который по ключу достает массивы значений
     * @param key ключ
     * @return Вернет ArrayList значений
     */
    public static ArrayList<String> getPropertys(String key){
        if (key == null) throw new IllegalArgumentException("Чтобы достать свойсво нужно передать строку!");
        return properties.get(key);
    }
    /**
     * Метод который по ключи вернет значение
     * @param key ключ
     * @return Вернет значение тип String
     */
    public static String getProperty(String key) {
        // Может вернуть просто Стринг.
        // А может вернуть массив стрингов, если под ключем лежит массив.
        if (key == null) throw new IllegalArgumentException("Чтобы достать свойсво нужно передать строку!");
        if(properties.get(key) == null) return null;
        if (properties.get(key).size() > 1) throw new IllegalArgumentException("Это массив элементов");
        return (properties.get(key)).get(0);
    }
    /**
     * Разбите строки и занесение ключей и значений в {@link StringParser#properties}<br>
     * Спец символы:<br>
     *  "#;" - разделитель для переменных или типа задачи.<br>
     *  "#:" - разделяет переменную и ее значение<br>
     *  "#,"- разделить для значения типа массива, перечесление элементов массива<br>
     * Как должна выглядить передоваемся строка:<br>
     *  [тип задачи]#;[имя переменной#:значение переменной]#; перменных может быть не ограниченое кол-во.<br>
     * Пример:<br>
     *  LOGIN#;msg#:LOGINFAIL<br>
     *  IS_ONLINE#;msg#:Пользователи онлайн#;client#: Efim#,Dima#,Lexa<br>
     * @param str Строка которую нужно запарсить.
     */
    public static void parsingString(String str){
        //      "type"#;msg#:dfsfs
        //      "type"#;msg#:sdad#;client#: Efim#,Dima#,Lexa
        if (str == null) throw new IllegalArgumentException("В парсер стоит передать строку!");
        properties = new HashMap<>();
        System.out.println(str);
        String[] buff = str.split("#;");
        for (String s : buff){
            ArrayList<String> arr = new ArrayList<>();
            if (!s.contains("#:")){
                arr.add(s);
                properties.put("type",arr);
            }
            else {
                String[] buff2 = s.split("#:");
                for (int i = 0; i < buff2.length; i+=2) {
                    if (buff2[i+1].contains("#,"))
                        arr.addAll(Arrays.asList(buff2[i+1].split("#,")));
                    else
                        arr.add(buff2[i+1]);
                    properties.put(buff2[i],arr);
                }
            }
        }
    }
    /**
     * Выводит в консоль читаемую информацию всех ключей и их значений
     */
    public static void info(){
        for (String s: properties.keySet()){
            System.out.print(s+": ");
            for (String s1 : properties.get(s))
                System.out.print(" "+s1);
            System.out.print("\n");
        }
    }
}
