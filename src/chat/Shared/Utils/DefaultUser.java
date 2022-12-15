package chat.Shared.Utils;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import chat.Shared.Exceptions.InvalidNameException;
import chat.Shared.Exceptions.InvalidPasswordException;
import chat.Shared.Utils.ABC.User;

public class DefaultUser extends User {

    public DefaultUser(String username, String name, String lastName, String statusMessage, String password,
            PhoneNumber number) throws InvalidNameException, InvalidPasswordException {
        super(username, name, lastName, statusMessage, password, number);
    }

    public DefaultUser() {
        super();
    }

    @Override
    public String setPassword(String password) throws InvalidPasswordException {
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        Pattern p = Pattern.compile(regex);

        if (password.isBlank()) {
            throw new InvalidPasswordException("Пароль ОБЯЗАН НЕ быть пустым.");
        }

        Matcher m = p.matcher(password);

        if (!(m.matches())) {
            throw new InvalidPasswordException("Пароль ОБЯЗАН иметь как минимум ОДИН уникальный символ, " +
                                                "иметь как минимум один символ в верхнем и нижнем регистре и одну цифру, " +
                                                "а также быть от 8 до 32 символов.");
        }
        return this.password = password;
    }
}