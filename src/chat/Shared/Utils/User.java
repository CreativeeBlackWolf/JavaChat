package chat.Shared.Utils;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import chat.Shared.Exceptions.InvalidNameException;
import chat.Shared.Exceptions.InvalidPasswordException;
import chat.Shared.Security.BCrypt;

public class User {
    private String name;
    private String lastName;
    private String statusMessage;
    private String password;
    private String username;

    public Number number;


    public User(String username,
                String name,
                String lastName,
                String statusMessage,
                String password,
                Number number) throws InvalidNameException, InvalidPasswordException {
        this.username = username;
        this.password = setPassword(password);
        this.number = number;
        this.name = setName(name);
        this.lastName = setLastName(lastName);
        this.statusMessage = setStatusMessage(statusMessage);
    }

    public User() {
        
    }

    public String setName(String name) throws InvalidNameException {
        if (name.isBlank() ||
            name.contains(" ") ||
            name.length() > 16 ||
            Character.isLowerCase((name.charAt(0))) ||
            name.replaceAll("\\D", "").length() != 0) {
            throw new InvalidNameException("Некорректное имя.");
        }
        this.name = name;
        return name;
    }

    public String setLastName(String lastName) throws InvalidNameException {
        if (lastName.isBlank() ||
            lastName.contains(" ") ||
            lastName.length() > 20 ||
            Character.isLowerCase((lastName.charAt(0))) ||
            lastName.replaceAll("\\D", "").length() != 0) {
            throw new InvalidNameException("Некорректная фамилия.");
        }
        this.lastName = lastName;
        return lastName;
    }

    public String setStatusMessage(String statusMessage) throws InvalidNameException {
        if (statusMessage.length() > 128) {
            throw new InvalidNameException("Длина статуса не должна привышать 128 символов.");
        }
        this.statusMessage = statusMessage;
        return statusMessage;
    }

    public String setPassword(String password) throws InvalidPasswordException {
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        Pattern p = Pattern.compile(regex);

        if (password.isBlank()) {
            throw new InvalidPasswordException("Пароль ОБЯЗАН НАХУЙ НЕ быть пустым.");
        }

        Matcher m = p.matcher(password);

        if (!(m.matches())) {
            throw new InvalidPasswordException("Пароль ОБЯЗАН НАХУЙ иметь как минимум ОДИН уникальный символ, " +
                                                "иметь как минимум один символ в верхнем и нижнем регистре и одну цифру, " +
                                                "а также быть от 8 до 32 символов.");
        }
        this.password = password;
        return password;
    }

    public void setHashedPassword(String password) {
        BCrypt crypto = new BCrypt();
        this.password = crypto.hash(10, password);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }
}