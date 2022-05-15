package view;

import parser.StringParser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Класс является интерфейсом диалогового окна, показывающего историю сообщений.
 * В себе содержит компонеты интерфейса диалогового окна, показывающего историю сообщений.
 * @author Ванцын Дмитрий
 * @version 1.0
 */
public class MessageHistory extends JFrame {
    /** Панель, содержащая в себе все компоненты формы */
    private JPanel mainPanel = new JPanel();
    /** Поле, содержащая в себе текстовую область, для вывода сообщений */
    private static JTextArea history = new JTextArea();
    /** Поле, добавляющеее к текстовой области, для вывода сообщений скролл */
    private JScrollPane scroll = new JScrollPane(history, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    /** Кнопка, для выхода из интерфеса диалогового окна */
    private JButton buttonClose = new JButton("Закрыть");

    /**
     * Конструктор, создающий форму, для просмотра истории сообщений
     * @param title задаёт имя формы
     * @exception HeadlessException Вызывается, когда код, зависящий от клавиатуры, дисплея или мыши, вызывается в среде, не поддерживающей клавиатуру, дисплей или мышь.
     */
    public MessageHistory(String title) throws HeadlessException {
        super(title);
        init();
    }
    /** Задаёт положение всех компонентов, распологающихся на форме */
    public void init(){
        setSize(400, 400);
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(scroll, BorderLayout.CENTER);
        history.setEditable(false);
        mainPanel.add(buttonClose, BorderLayout.SOUTH);
        buttonClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        scroll.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        add(mainPanel);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Изменяет историю сообщений
     * @param str Текст, содержащий историю сообщений
     */
    public static void setHistory(String str) {
        history.setText(str);
    }
}
