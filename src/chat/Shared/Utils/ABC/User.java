package chat.Shared.Utils.ABC;

import chat.Shared.Exceptions.InvalidNameException;
import chat.Shared.Exceptions.InvalidPasswordException;
import chat.Shared.Utils.PhoneNumber;

public abstract class User {
    public String name;
    public String lastName;
    public String statusMessage;
    public String password;
    public String username;
    public PhoneNumber number;

    public User(String username,
                String name,
                String lastName,
                String statusMessage,
                String password,
                PhoneNumber number) throws InvalidNameException, InvalidPasswordException {
        this.username = username;
        this.number = number;
        setName(name);
        setLastName(lastName);
        setStatusMessage(statusMessage);
        setPassword(password);
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

    abstract public String setPassword(String password) throws InvalidPasswordException;

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

    public PhoneNumber getNumber() {
        return number;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setNumber(PhoneNumber number) {
        this.number = number;
    }
}
