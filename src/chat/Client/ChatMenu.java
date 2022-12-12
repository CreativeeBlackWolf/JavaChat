package chat.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class ChatMenu extends JFrame implements ActionListener, FocusListener{
    private static JTextArea chatArea;
    private static JTextArea messageArea;
    private static JTextArea onlineUsersArea;
    private static JButton sendMessageButton;
    private static JMenuItem exit;
    private static JMenuItem changeAccount;
    private Client client;

    public ChatMenu(Client client){
        this.client = client;

        setTitle(String.format("Java Chat [%s]", client.user.getUsername()));
        setLayout(new BorderLayout(10, 10));
        setBounds(600, 300, 600, 500);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        ImageIcon image = new ImageIcon("src/icon.png");
        this.setIconImage(image.getImage());

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

        JLabel userNameLable = new JLabel("Пользователь: " + client.user.getUsername());
        userNameLable.setFont(new Font(null, Font.BOLD, 12));
        centerPanel.add(userNameLable, BorderLayout.NORTH);

        JPanel rightCentralPanel = new JPanel(new BorderLayout(10, 10));
        rightCentralPanel.setPreferredSize(new Dimension(150,150));
        centerPanel.add(rightCentralPanel, BorderLayout.EAST);
        
        JLabel onlineUsersText = new JLabel("Участники");
        onlineUsersText.setHorizontalAlignment(JLabel.CENTER);
        rightCentralPanel.add(onlineUsersText, BorderLayout.NORTH);

        onlineUsersArea = new JTextArea();
        onlineUsersArea.setEditable(false);
        rightCentralPanel.add(new JScrollPane(onlineUsersArea), BorderLayout.CENTER);

        try {
            ResponsePrinter printer = new ResponsePrinter(client.clientSocket, client.security, chatArea);
            new Thread(printer).start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        JPanel bottomCentralPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.add(bottomCentralPanel, BorderLayout.SOUTH);
        bottomCentralPanel.setPreferredSize(new Dimension(50,50));

        sendMessageButton = new JButton("Отправить");
        sendMessageButton.setFocusPainted(false);
        sendMessageButton.setPreferredSize(new Dimension(150, 0));
        sendMessageButton.addActionListener(this);
        bottomCentralPanel.add(sendMessageButton, BorderLayout.EAST);

        messageArea = new JTextArea("Написать сообщение...");
        messageArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode()==10 && !(messageArea.getText().isBlank())){
                    client.clientWriter.println(client.security.encrypt(messageArea.getText()));
                    client.clientWriter.flush();
                    chatArea.append(client.user.getUsername() + ": " + messageArea.getText() + "\n");
                    messageArea.selectAll();
                    messageArea.replaceSelection(null);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode()==10 && !(messageArea.getText().equals(""))){
                    messageArea.selectAll();
                    messageArea.replaceSelection(null);
                }
            }
        });
        messageArea.setForeground(Color.gray);
        messageArea.addFocusListener(this);
        messageArea.setLineWrap(true);
        bottomCentralPanel.add(new JScrollPane(messageArea), BorderLayout.CENTER);

        add(new JPanel(), BorderLayout.WEST);
        add(new JPanel(), BorderLayout.EAST);
        add(new JPanel(), BorderLayout.NORTH);
        add(new JPanel(), BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setVisible(true);


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==sendMessageButton && !(messageArea.getText().equals("Написать сообщение..."))){
            client.clientWriter.println(client.security.encrypt(messageArea.getText()));
            client.clientWriter.flush();
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
