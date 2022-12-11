package chat.Client;

import chat.Shared.AuthencationResponse;
import chat.Shared.Exceptions.ServerVerifyException;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class LaunchMenu extends JFrame{
    private static JButton bLogin;
    private static JButton bRegistration;
    private static JLabel successMessage;
    private static JPasswordField passwordField;
    private static JTextField loginField;
    private static JCheckBox showPass;

    public LaunchMenu() {
        setSize(345, 210);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setTitle("Вход");

        ImageIcon image = new ImageIcon("src/icon.png");
        this.setIconImage(image.getImage());

        JPanel panel = new JPanel();
        panel.setLayout(null);
        add(panel);

        JLabel userLabel = new JLabel("Логин");
        userLabel.setBounds(25, 30, 80, 25);
        panel.add(userLabel);

        loginField = new JTextField(32);
        loginField.setBounds(100, 30, 165, 25);
        panel.add(loginField);

        JLabel passwordLabel = new JLabel("Пароль");
        passwordLabel.setBounds(25, 70, 80, 25);
        panel.add(passwordLabel);

        passwordField = new JPasswordField(32);
        passwordField.setBounds(100, 70, 165, 25);
        panel.add(passwordField);

        bLogin = new JButton("Войти");
        bLogin.setFocusPainted(false);
        bLogin.addActionListener(e -> {
            String login = loginField.getText();
            String pass = passwordField.getPassword().toString();
            try {
                Client client = new Client("localhost", 2727);
                client.user.setUsername(login);
                AuthencationResponse authResponse = client.login(login, pass);
                if(authResponse == AuthencationResponse.LOGIN_SUCCESS){
                    new ChatMenu(client);
                    dispose();
                }
                else {
                    successMessage.setText("Неверный пароль или логин");
                }
            } catch (IOException | ServerVerifyException ex) {
                throw new RuntimeException(ex);
            }
        });
        bLogin.setBounds(100, 130, 80, 25);
        panel.add(bLogin);

        bRegistration = new JButton("Регистрация");
        bRegistration.setFocusPainted(false);
        bRegistration.addActionListener(e -> {
            dispose();
            new RegistrationMenu();
        });
        bRegistration.setBounds(182, 130, 110, 25);
        panel.add(bRegistration);

        successMessage = new JLabel("");
        successMessage.setForeground(Color.RED);
        successMessage.setBounds(90, 3, 300, 25);
        panel.add(successMessage);

        showPass = new JCheckBox("Показать пароль");
        showPass.addActionListener(e -> {
            if(showPass.isSelected()){
                passwordField.setEchoChar((char)0);
            }
            else{
                passwordField.setEchoChar('*');
            }
        });
        showPass.setFocusPainted(false);
        showPass.setFont(new Font("Cambria",Font.PLAIN,12));
        showPass.setBounds(182, 100, 150, 25);
        panel.add(showPass);

        setLocationRelativeTo(null);
        setVisible(true);
    }
}
