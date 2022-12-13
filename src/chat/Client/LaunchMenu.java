package chat.Client;

import chat.Shared.AuthencationResponse;
import chat.Shared.Exceptions.ServerVerifyException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

public class LaunchMenu extends JFrame implements KeyListener, ActionListener {
    private static JButton bLogin;
    private static JButton bRegistration;
    private static JLabel successMessage;
    private static JPasswordField passwordField;
    private static JTextField loginField;
    private static JCheckBox showPass;
    private Client client;
    
    public LaunchMenu() {
        try {
            this.client = new Client(Config.HOST, Config.PORT);
        } catch (IOException | ServerVerifyException e1) {
            e1.printStackTrace();
        }
        setSize(345, 210);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setTitle("Вход");

        ImageIcon image = new ImageIcon("src/icon.png");
        setIconImage(image.getImage());

        JPanel panel = new JPanel();
        panel.setLayout(null);
        add(panel);

        JLabel userLabel = new JLabel("Логин");
        userLabel.setBounds(25, 20, 80, 25);
        panel.add(userLabel);

        loginField = new JTextField(32);
        loginField.setBounds(100, 20, 165, 25);
        panel.add(loginField);
        loginField.addKeyListener(this);

        JLabel passwordLabel = new JLabel("Пароль");
        passwordLabel.setBounds(25, 60, 80, 25);
        panel.add(passwordLabel);

        passwordField = new JPasswordField(32);
        passwordField.setBounds(100, 60, 165, 25);
        panel.add(passwordField);
        passwordField.addKeyListener(this);

        bLogin = new JButton("Войти");
        bLogin.setFocusPainted(false);
        bLogin.addActionListener(this);
        bLogin.setBounds(90, 130, 80, 25);
        panel.add(bLogin);

        bRegistration = new JButton("Регистрация");
        bRegistration.setFocusPainted(false);
        bRegistration.addActionListener(this);
        bRegistration.setBounds(172, 130, 110, 25);
        panel.add(bRegistration);

        successMessage = new JLabel("");
        successMessage.setFont(new Font(null, Font.BOLD, 12));
        successMessage.setForeground(Color.RED);
        successMessage.setBounds(100, 80, 300, 25);
        panel.add(successMessage);

        showPass = new JCheckBox("Показать пароль");
        showPass.addActionListener(e -> {
            if(showPass.isSelected()){
                passwordField.setEchoChar((char)0);
                passwordField.grabFocus();
            }
            else{
                passwordField.setEchoChar('*');
                passwordField.grabFocus();
            }
        });
        showPass.setFocusPainted(false);
        showPass.setFont(new Font("Cambria",Font.PLAIN,12));
        showPass.setBounds(182, 102, 150, 15);
        panel.add(showPass);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode()==10) {
            String login = loginField.getText();
            String pass = new String(passwordField.getPassword());
            client.user.setUsername(login);
            AuthencationResponse authResponse = client.login(login, pass);
            if(authResponse == AuthencationResponse.LOGIN_SUCCESS){
                new ChatMenu(client);
                dispose();
            }
            else {
                successMessage.setText("Неверный пароль или логин");
            }
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource()==bLogin) {
            String login = loginField.getText();
            String pass = new String(passwordField.getPassword());
                client.user.setUsername(login);
                AuthencationResponse authResponse = client.login(login, pass);
                if(authResponse == AuthencationResponse.LOGIN_SUCCESS){
                    new ChatMenu(client);
                    dispose();
                }
                else {
                    successMessage.setText("Неверный пароль или логин");
                }
        }

        if (e.getSource()==bRegistration) {
            dispose();
            new RegistrationMenu(client);
        }
    }
}
