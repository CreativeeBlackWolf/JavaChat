import Exceptions.InvalidNameException;
import Exceptions.InvalidPasswordException;
import Exceptions.InvalidMessageException;
import java.util.regex.*;
import java.util.Date;
import chatLogger.Logger;

public class User {
    private String name;
    private String lastName;
    private String statusMessage;
    private String password;
    public Number number;


    public User(String name, String lastName, String statusMessage, String password, Number number) throws InvalidNameException, InvalidPasswordException{
        this.password = setPassword(password);
        this.number = number;
        this.name = setName(name);
        this.lastName = setLastName(lastName);
        this.statusMessage = setStatusMessage(statusMessage);
    }

    public String setName(String name) throws InvalidNameException {
        if (name.isBlank() || name.contains(" ") || name.length() > 16) {
            throw new InvalidNameException("Имя не должно быть пустым, не должно содержать пробелов и быть длиннее 16 символов.");
        }
        this.name = name;
        return name;
    }

    public String setLastName(String lastName) throws InvalidNameException {
        if (lastName.isBlank() || lastName.contains(" ") || lastName.length() > 20) {
            throw new InvalidNameException("Фамилия не должна быть пустой, не должна содержать пробелов и быть длиннее 20 символов.");
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
        String regex = "(?=^.{8,32}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$";
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

    public void sendMessage(String message) throws InvalidMessageException{
        if (message.isBlank()){
            throw new InvalidMessageException("Message cannot be empty.");
        }
        else{
            Logger log = new Logger();
            Date date = new Date();
            String text = (name + " " + lastName + "\n " + date + ":\n " + message + "\n");
            log.WriteLog(text);
        }
    }

}