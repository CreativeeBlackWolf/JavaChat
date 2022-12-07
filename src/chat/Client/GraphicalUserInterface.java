package chat.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GraphicalUserInterface extends JFrame implements ActionListener {
    private JTextArea chatArea;
    private JTextArea messageArea;
    private JTextArea onlineUsersArea;
    private JButton sendMessage;
    public GraphicalUserInterface(){
        setTitle("Java Chat");
        setLayout(new BorderLayout(10, 10));
        setBounds(600, 300, 600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        add(centerPanel, BorderLayout.CENTER);
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setText("Test");
        centerPanel.add(new JScrollPane(chatArea), BorderLayout.CENTER);


        JPanel bottomCentralPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.add(bottomCentralPanel, BorderLayout.SOUTH);
        bottomCentralPanel.setPreferredSize(new Dimension(50,50));
        sendMessage = new JButton("Send");
        bottomCentralPanel.add(sendMessage, BorderLayout.EAST);
        sendMessage.setPreferredSize(new Dimension(150, 100));
        messageArea = new JTextArea();
        messageArea.setLineWrap(true);
        bottomCentralPanel.add(new JScrollPane(messageArea), BorderLayout.CENTER);

        JPanel rightCentralPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.add(rightCentralPanel, BorderLayout.EAST);
        JLabel onlineUsersText = new JLabel("Online Users");
        onlineUsersText.setHorizontalAlignment(JLabel.CENTER);
        rightCentralPanel.add(onlineUsersText, BorderLayout.NORTH);
        onlineUsersArea = new JTextArea();
        rightCentralPanel.add(new JScrollPane(onlineUsersArea), BorderLayout.CENTER);
        rightCentralPanel.setPreferredSize(new Dimension(150,150));


        add(new JPanel(), BorderLayout.WEST);
        add(new JPanel(), BorderLayout.SOUTH);
        add(new JPanel(), BorderLayout.EAST);
        add(new JPanel(), BorderLayout.NORTH);
        setVisible(true);


    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }
}
