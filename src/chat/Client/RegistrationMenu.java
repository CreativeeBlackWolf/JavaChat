package chat.Client;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import chat.Shared.AuthencationResponse;

public class RegistrationMenu extends JFrame implements ActionListener{
    private static JTextField userNameField;
    private static JPasswordField passwordField;
    private static JTextField nameField;
    private static JTextField lastNameField;
    private static JTextField numberField;
    private static JButton bRegistration;
    private Client client;

    public RegistrationMenu (Client client) {
        this.client = client;
        setSize(320, 330);
        setResizable(false);
        setTitle("Регистрация");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        ImageIcon image = new ImageIcon("src/icon.png");
        setIconImage(image.getImage());

        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                new LaunchMenu();
            }
        });

        JPanel panel = new JPanel();
        panel.setLayout(null);
        add(panel);

        JLabel userNameLabel = new JLabel("Логин");
        userNameLabel.setBounds(25, 30, 80, 25);
        panel.add(userNameLabel);

        userNameField = new JTextField(32);
        userNameField.setBounds(100, 30, 165, 25);
        panel.add(userNameField);

        JLabel passwordFiled = new JLabel("Пароль");
        passwordFiled.setBounds(25, 70, 80, 25);
        panel.add(passwordFiled);

        passwordField = new JPasswordField(32);
        passwordField.setBounds(100, 70, 165, 25);
        panel.add(passwordField);

        JLabel nameLabel = new JLabel("Имя");
        nameLabel.setBounds(25, 110, 80, 25);
        panel.add(nameLabel);

        nameField = new JTextField(32);
        nameField.setBounds(100, 110, 165, 25);
        panel.add(nameField);

        JLabel lastNameLabel = new JLabel("Фамилия");
        lastNameLabel.setBounds(25, 150, 80, 25);
        panel.add(lastNameLabel);

        lastNameField = new JTextField(32);
        lastNameField.setBounds(100, 150, 165, 25);
        panel.add(lastNameField);

        JLabel numberLabel = new JLabel("Номер");
        numberLabel.setBounds(25, 190, 80, 25);
        panel.add(numberLabel);

        numberField = new JTextField(32);
        numberField.setBounds(100, 190, 165, 25);
        panel.add(numberField);

        bRegistration = new JButton("Зарегистрироваться");
        bRegistration.setFocusPainted(false);
        bRegistration.addActionListener(this);
        bRegistration.setBounds(100,240,155, 25);
        bRegistration.setFont(new Font(null,Font.BOLD,11));
        panel.add(bRegistration);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == bRegistration) {
            String username = userNameField.getText();
            String password = new String(passwordField.getPassword());
            String name = nameField.getText();
            String lastname = lastNameField.getText();
            String phoneNumber = numberField.getText();


            client.clientWriter.println(client.security.encrypt("REGISTER_ME"));
            client.clientWriter.println(client.security.encrypt(username));
            client.clientWriter.println(client.security.encrypt(password));
            client.clientWriter.println(client.security.encrypt(name));
            client.clientWriter.println(client.security.encrypt(lastname));
            client.clientWriter.println(client.security.encrypt(phoneNumber));
            client.clientWriter.flush();

            AuthencationResponse authResponse = null;
            try {
                authResponse = AuthencationResponse.valueOf(client.security.decrypt(client.securedPrinter.readLine()));
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            if (authResponse == AuthencationResponse.REGISTERED) {
                client.user.setUsername(username);
                dispose();
                new ChatMenu(client);
            } else {
                JOptionPane.showMessageDialog(null, authResponse.name());
            }
        }
    }
}
