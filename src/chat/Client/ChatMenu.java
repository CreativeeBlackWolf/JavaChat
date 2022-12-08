package chat.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class ChatMenu extends JFrame implements ActionListener, FocusListener {
    private JTextArea chatArea;
    private JTextArea messageArea;
    private JTextArea onlineUsersArea;
    private JButton sendMessageButton;
    public ChatMenu(){
        setTitle("Java Chat");
        setLayout(new BorderLayout(10, 10));
        setBounds(600, 300, 600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        add(centerPanel, BorderLayout.CENTER);
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        centerPanel.add(new JScrollPane(chatArea), BorderLayout.CENTER);


        JPanel bottomCentralPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.add(bottomCentralPanel, BorderLayout.SOUTH);
        bottomCentralPanel.setPreferredSize(new Dimension(50,50));

        sendMessageButton = new JButton("Send");
        sendMessageButton.setPreferredSize(new Dimension(150, 100));
        sendMessageButton.addActionListener(this);
        bottomCentralPanel.add(sendMessageButton, BorderLayout.EAST);

        messageArea = new JTextArea("Write a message...");
        messageArea.setForeground(Color.gray);
        messageArea.addFocusListener(this);
        messageArea.setLineWrap(true);
        bottomCentralPanel.add(new JScrollPane(messageArea), BorderLayout.CENTER);

        JPanel rightCentralPanel = new JPanel(new BorderLayout(10, 10));
        rightCentralPanel.setPreferredSize(new Dimension(150,150));
        centerPanel.add(rightCentralPanel, BorderLayout.EAST);

        JLabel onlineUsersText = new JLabel("Online Users");
        onlineUsersText.setHorizontalAlignment(JLabel.CENTER);
        rightCentralPanel.add(onlineUsersText, BorderLayout.NORTH);

        onlineUsersArea = new JTextArea();
        onlineUsersArea.setEditable(false);
        rightCentralPanel.add(new JScrollPane(onlineUsersArea), BorderLayout.CENTER);


        add(new JPanel(), BorderLayout.WEST);
        add(new JPanel(), BorderLayout.SOUTH);
        add(new JPanel(), BorderLayout.EAST);
        add(new JPanel(), BorderLayout.NORTH);
        setVisible(true);


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==sendMessageButton){
            chatArea.append(messageArea.getText() + "\n");
            messageArea.selectAll();
            messageArea.replaceSelection(null);
            messageArea.setForeground(Color.gray);
            messageArea.setText("Write a message...");
        }
    }

    @Override
    public void focusGained(FocusEvent e) {
        messageArea.setText("");
        messageArea.setForeground(Color.black);
    }

    @Override
    public void focusLost(FocusEvent e) {
    }
}
