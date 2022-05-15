package controller;

import view.MessageHistory;
import view.View;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;

import static java.lang.System.exit;
/**
 * Класс является слушателем компонентов формы.
 * В себе содержит методы, которые будут срабатывать во время действий на форме.
 * @author Ванцын Дмитрий
 * @version 1.0
 */
public class ClientFormListiner implements ActionListener, MouseListener {
    /**
     * Интерфейс программы
     */
    private static View view = null;
    /**
     * Коллекция, содержащая в себе всех пользователей, находящихся онлайн
     */
    private static ArrayList<String> userIsOnline = new ArrayList<>();

    /**
     * Конструктор, который создаёт слушателя компонентов формы, а так же добавляющий этогос лушателя компонентам формы
     * @param view Интерфейс программы
     */
    public ClientFormListiner(View view) {
        this.view = view;
        view.getButtonExit().addActionListener(this);
        view.getButtonConnect().addActionListener(this);
        view.getButtonDisconnect().addActionListener(this);
        view.getButtonEnter().addActionListener(this);
        view.getButtonGetAllMessage().addActionListener(this);
        view.getTextFieldEnter().addMouseListener(this);
        view.getButtonIsOnline().addActionListener(this);
    }

    /**
     * Унаследованный метод, который реагирует на события на форме
     * @param e События на форме
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == view.getButtonExit()){
            exit(0);
        }
        else if(e.getSource() == view.getButtonDisconnect()){
            GeneralController.disconnect();
        }
        else if(e.getSource() == view.getButtonConnect()){
            String portStr = view.getTextFieldPort().getText().replaceAll("\\s+","");
            String hostStr = view.getTextFieldHost().getText().replaceAll("\\s+","");
            String login = view.getTextFieldLogin().getText().replaceAll("\\s+","");
            String password = view.getTextFieldPassword().getText().replaceAll("\\s+","");
            if(portStr.isEmpty()){
                view.sendMes("Введите порт");
                return;
            }
            if(hostStr.isEmpty()){
                view.sendMes("Введите хост");
                return;
            }
            if(!portStr.matches("\\d+")){
                view.sendMes("В поле порт записаны не числа");
                return;
            }
            int port = Integer.parseInt(portStr);
            if(port<=0||port>65536) {
                view.sendMes("Значение в поле порт не подходит для подключению к серверу");
                return;
            }
            if(login.isEmpty()){
                view.sendMes("Введите логин");
                return;
            }
            if(password.isEmpty()){
                view.sendMes("Введите пароль");
                return;
            }
            GeneralController.connect(hostStr,port);
        }
        else if(e.getSource() == view.getButtonEnter()){
            String message = view.getTextFieldEnter().getText();
            if(message.isEmpty() || message.equals("Введите сообщение")){
                view.sendMes("Сообщение не введено");
                return;
            }
            int lengthStr = 60;
            int count = message.length()/lengthStr;
            for (int i = 1; i <= count; i++)
                message = new StringBuilder(message).insert(lengthStr*i,"#n").toString();
            GeneralController.enter(message,view.getSelectUsers().getSelectedItem().toString());
            view.getTextFieldEnter().setText("");
        }
        else if(e.getSource() == view.getButtonGetAllMessage()){
            GeneralController.getMessageHistory();
            new MessageHistory("История переписки");
        }
        else if(e.getSource() == view.getButtonIsOnline()){
            if(userIsOnline.size() != 0)
                JOptionPane.showMessageDialog(view, userIsOnline.toArray(new String[0]), "Пользователи онлайн",
                    JOptionPane.INFORMATION_MESSAGE);
            else JOptionPane.showMessageDialog(view, "Подключённых пользователей нет",
                    "Пользователи онлайн", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    /**
     * Метод, который выводит ошибки в диалоговые окна
     * @param str Строка с ошибкой
     */
    public static void errorMes (String str){
        JOptionPane.showMessageDialog(view, str,
                "Ошибка", JOptionPane.INFORMATION_MESSAGE);
    }
    /**
     * Добавляет пользователей онлайн, в меню выбора кому отправить сообщения
     * @param user Пользователь, которого надо добавить
     */
    public static void addToUser(String user){
       for (int i = 0; i<view.getSelectUsers().getModel().getSize();i++){
           if(view.getSelectUsers().getModel().getElementAt(i).equals(user))
                return;
       }
       userIsOnline.add(user);
       view.getSelectUsers().addItem(user);
    }
    /**
     * Удаляет пользователей, которые отключились от сервера, из меню выбора кому отправить сообщения
     * @param user Пользователь, которого надо удалить
     */
    public static void removeUser(String user){
        view.sendMes("Пользователь" + user +"отключился");
        userIsOnline.remove(user);
        view.getSelectUsers().removeItem(user);
    }
    /**
     * Унаследованные методы, которые реагируют на дейтсвия мыши на форме
     * @param e Действия мыши на форме
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getComponent()==view.getTextFieldEnter())
            view.getTextFieldEnter().setText("");
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

}