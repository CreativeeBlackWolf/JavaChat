package chat.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ChatMenu extends JFrame implements ActionListener, FocusListener{
    private static JTextArea chatArea;
    private static JTextArea messageArea;
    private static JTextArea onlineUsersArea;
    private static JButton sendMessageButton;
    private static JMenuItem exit;
    private static JMenuItem changeAccount;
    public ChatMenu(){
        setTitle("Java Chat");
        setLayout(new BorderLayout(10, 10));
        setBounds(600, 300, 600, 500);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                int result = JOptionPane.showOptionDialog(
                        null,
                        "Вы уверены, что хотите закрыть приложение?",
                        "Выйти из приложения",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE,
                        null,
                        new String[]{"Да", "Нет"},
                        0);

                if (result == JOptionPane.YES_OPTION)
                    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
        });

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu menu = new JMenu("Меню");
        menuBar.add(menu);

        changeAccount = new JMenuItem("Сменить пользователя");
        exit = new JMenuItem("Выйти");
        changeAccount.addActionListener(this);
        exit.addActionListener(this);
        menu.add(changeAccount);
        menu.add(exit);


        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        add(centerPanel, BorderLayout.CENTER);
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        centerPanel.add(new JScrollPane(chatArea), BorderLayout.CENTER);


        JPanel bottomCentralPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.add(bottomCentralPanel, BorderLayout.SOUTH);
        bottomCentralPanel.setPreferredSize(new Dimension(50,50));

        sendMessageButton = new JButton("Отправить");
        sendMessageButton.setFocusPainted(false);
        sendMessageButton.setPreferredSize(new Dimension(150, 100));
        sendMessageButton.addActionListener(this);
        bottomCentralPanel.add(sendMessageButton, BorderLayout.EAST);

        messageArea = new JTextArea("Написать сообщение...");
        messageArea.setForeground(Color.gray);
        messageArea.addFocusListener(this);
        messageArea.setLineWrap(true);
        bottomCentralPanel.add(new JScrollPane(messageArea), BorderLayout.CENTER);

        JPanel rightCentralPanel = new JPanel(new BorderLayout(10, 10));
        rightCentralPanel.setPreferredSize(new Dimension(150,150));
        centerPanel.add(rightCentralPanel, BorderLayout.EAST);

        JLabel onlineUsersText = new JLabel("Участники");
        onlineUsersText.setHorizontalAlignment(JLabel.CENTER);
        rightCentralPanel.add(onlineUsersText, BorderLayout.NORTH);

        onlineUsersArea = new JTextArea();
        onlineUsersArea.setEditable(false);
        rightCentralPanel.add(new JScrollPane(onlineUsersArea), BorderLayout.CENTER);


        add(new JPanel(), BorderLayout.WEST);
        add(new JPanel(), BorderLayout.SOUTH);
        add(new JPanel(), BorderLayout.EAST);
        add(new JPanel(), BorderLayout.NORTH);
        setLocationRelativeTo(null);
        setVisible(true);


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==sendMessageButton && !(messageArea.getText().equals("Написать сообщение..."))){
            chatArea.append(messageArea.getText() + "\n");
            messageArea.selectAll();
            messageArea.replaceSelection(null);
            messageArea.setForeground(Color.gray);
            messageArea.setText("Написать сообщение...");
        }
        if(e.getSource()==exit){
            System.exit(0);
        }
        if(e.getSource()==changeAccount){
            dispose();
            new LaunchMenu();
        }
    }

    @Override
    public void focusGained(FocusEvent e) {
        messageArea.setText("");
        messageArea.setForeground(Color.black);
    }

    @Override
    public void focusLost(FocusEvent e) {
        if(messageArea.getText().equals("")){
            messageArea.setForeground(Color.gray);
            messageArea.setText("Написать сообщение...");
        }
    }
}
