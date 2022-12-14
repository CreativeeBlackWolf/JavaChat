package chat.Client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

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
        setIconImage(image.getImage());

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
                        null);

                if (result == JOptionPane.YES_OPTION)
                    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
        });

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu menu = new JMenu("Меню");
        menu.setFont(new Font("Segoe UI", Font.BOLD, 14));
        menuBar.add(menu);

        changeAccount = new JMenuItem("Сменить пользователя");
        changeAccount.setFont(new Font("Segoe UI", Font.BOLD, 14));
        exit = new JMenuItem("Выйти");
        exit.setFont(new Font("Segoe UI", Font.BOLD, 14));
        changeAccount.addActionListener(this);
        exit.addActionListener(this);
        menu.add(changeAccount);
        menu.add(exit);


        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        add(centerPanel, BorderLayout.CENTER);
        chatArea = new JTextArea();
        chatArea.setFont(new Font("Segoe UI", Font.PLAIN, 15));

        DefaultCaret caret = (DefaultCaret)chatArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        centerPanel.add(new JScrollPane(chatArea), BorderLayout.CENTER);

        JPanel rightCentralPanel = new JPanel(new BorderLayout(10, 10));
        rightCentralPanel.setPreferredSize(new Dimension(150,150));
        centerPanel.add(rightCentralPanel, BorderLayout.EAST);
        
        JLabel onlineUsersText = new JLabel("Участники");
        onlineUsersText.setFont(new Font("Segoe UI", Font.BOLD, 15));
        onlineUsersText.setHorizontalAlignment(JLabel.CENTER);
        rightCentralPanel.add(onlineUsersText, BorderLayout.NORTH);

        onlineUsersArea = new JTextArea();
        onlineUsersArea.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        onlineUsersArea.setEditable(false);
        rightCentralPanel.add(new JScrollPane(onlineUsersArea), BorderLayout.CENTER);

        try {
            ResponsePrinter printer = new ResponsePrinter(client.clientSocket, client.security, chatArea, onlineUsersArea);
            new Thread(printer).start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        JPanel bottomCentralPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.add(bottomCentralPanel, BorderLayout.SOUTH);
        bottomCentralPanel.setPreferredSize(new Dimension(50,50));

        sendMessageButton = new JButton("Отправить");
        sendMessageButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        sendMessageButton.setFocusPainted(false);
        sendMessageButton.setPreferredSize(new Dimension(150, 0));
        sendMessageButton.addActionListener(this);
        bottomCentralPanel.add(sendMessageButton, BorderLayout.EAST);

        messageArea = new JTextArea("Написать сообщение...");
        messageArea.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        messageArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode()==10 && !(messageArea.getText().isBlank())){
                    sendMessage();
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
        add(new JPanel(), BorderLayout.SOUTH);

        JPanel topPanel = new JPanel();
        topPanel.setPreferredSize(new Dimension(15, 25));
        topPanel.setLayout(null);
        add(topPanel, BorderLayout.NORTH);

        JLabel userNameLabel = new JLabel("Пользователь: " + client.user.getUsername());
        userNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        userNameLabel.setBounds(10,5,300,20);
        topPanel.add(userNameLabel);

        setLocationRelativeTo(null);
        setVisible(true);


    }

    public void sendMessage(){
        client.clientWriter.println(client.security.encrypt(messageArea.getText()));
        client.clientWriter.flush();
        chatArea.append(client.user.getUsername() + ": " + messageArea.getText() + "\n");
        messageArea.selectAll();
        messageArea.replaceSelection(null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==sendMessageButton && !(messageArea.getText().equals("Написать сообщение..."))){
            sendMessage();
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
        if(messageArea.getText().equals("Написать сообщение...")){
            messageArea.setText("");
            messageArea.setForeground(Color.black);
        }
    }

    @Override
    public void focusLost(FocusEvent e) {
        if(messageArea.getText().equals("")){
            messageArea.setForeground(Color.gray);
            messageArea.setText("Написать сообщение...");
        }
    }
}
