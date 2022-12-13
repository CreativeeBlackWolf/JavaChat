package chat.tests;

import chat.Shared.Exceptions.InvalidNameException;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import chat.Shared.Utils.Number;
import chat.Shared.Utils.User;
import chat.Shared.Exceptions.InvalidPasswordException;
import chat.Shared.Exceptions.InvalidPhoneNumberException;

import static org.junit.Assert.*;

public class Tests {
    @Test
    @DisplayName("Проверка валидации неправильных паролей")
    public void invalidPasswordValidationTest(){
        User user = new User();

        String[] wrongPasswords = {"q1w2e3r4t5y6", "Ad123456.", "Qwerty123456@"};
        
        for (String password : wrongPasswords) {
            assertThrows(InvalidPasswordException.class, () -> user.setPassword(password));
        }
    }

    @Test
    @DisplayName("Проверка валидации правильных паролей")
    public void validPasswordValidationTest() throws InvalidPasswordException {
        User user = new User();

        String[] validPasswords = {"wqjdJDK21#dja", "zhOpa12@12", "jdkKDHMDKI123&dmamdn"};
        
        for (String password : validPasswords) {
            assertEquals(password, user.setPassword(password));
        }
    }

    @Test
    @DisplayName("Проверка валидации неправильного номера телефона")
    public void invalidPhoneNumberValidationTest() {
        Number number = new Number();

        String[] invalidNumbers = {"8", "    +da", "chakwdjawffnf"};

        for (String num : invalidNumbers) {
            assertThrows(InvalidPhoneNumberException.class, () -> number.setNumber(num));
        }
    }

    @Test
    @DisplayName("Проверка валидации правильного номера телефона")
    public void validPhoneNumberValidationTest() throws InvalidPhoneNumberException {
        Number number = new Number();

        String[] validNumbers = {"89533470922", "+79171180241", "+7 (905) 093-61-93"};

        for (String num : validNumbers) {
            assertEquals(num, number.setNumber(num));
        }
    }

    @Test
    @DisplayName("Проверка валидации неправильного имени пользователя")
    public void invalidNameValidationTest() {
        User user = new User();

        String[] invalidNames = {"Григорий228", "", "   "};

        for (String name : invalidNames) {
            assertThrows(InvalidNameException.class, () -> user.setName(name));
        }
    }

    @Test
    @DisplayName("Проверка валидации правильного имени пользователя")
    public void validNameValidationTest() throws InvalidNameException {
        User user = new User();

        String[] validNames = {"Алексей", "Потап", "Donald"};

        for (String name : validNames) {
            assertEquals(name, user.setName(name));
        }
    }

}
