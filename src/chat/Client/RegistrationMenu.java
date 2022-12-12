package chat.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RegistrationMenu extends JFrame implements ActionListener{
    private static JTextField userNameField;
    private static JPasswordField passwordField;
    private static JTextField nameField;
    private static JTextField lastNameField;
    private static JTextField number;
    private static JButton bRegistration;
    public RegistrationMenu(){
        setSize(320, 315);
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

        number = new JTextField(32);
        number.setBounds(100, 190, 165, 25);
        panel.add(number);

        bRegistration = new JButton("Зарегистрироваться");
        bRegistration.setFocusPainted(false);
        bRegistration.addActionListener(this);
        bRegistration.setBounds(100,230,155, 25);
        bRegistration.setFont(new Font(null,Font.BOLD,11));
        panel.add(bRegistration);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==bRegistration){

        }
    }
}
