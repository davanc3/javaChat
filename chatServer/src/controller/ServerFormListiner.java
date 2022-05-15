package controller;

import view.View;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.lang.System.exit;
/**
 * Класс является слушателем компонентов формы.
 * В себе содержит методы, которые будут срабатывать во время действий на форме.
 * @author Ванцын Дмитрий
 * @version 1.0
 */
public class ServerFormListiner implements ActionListener {
    /**
     * Интерфейс программы
     */
    private static View view = null;
    /**
     * Конструктор, который создаёт слушателя компонентов формы, а так же добавляющий этогос лушателя компонентам формы
     * @param view Интерфейс программы
     */
    public ServerFormListiner(View view) {
        this.view = view;
        view.getButtonExit().addActionListener(this);
        view.getButtonStart().addActionListener(this);
        view.getButtonStop().addActionListener(this);
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
        if(e.getSource() == view.getButtonStop()){
            GeneralController.stopServer();
            view.sendMes("Сервер выключен!");
        }
        if(e.getSource() == view.getButtonStart()){
            String portStr = view.getTextFieldPort().getText().replaceAll("\\s+","");
            if(portStr.isEmpty()){
                errorMes("Введите порт");
                return;
            }
            if(!portStr.matches("\\d+")){
                errorMes("В поле порт введены не числа");
                return;
            }
            int port = Integer.parseInt(portStr);
            if(port<=0||port>65536) {
                errorMes("Значение в поле порт не подходит для подключения");
                return;
            }
            GeneralController.connect(port);
            view.connect();
            view.sendMes("Сервер запущен");
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
}
