package chat.tests;

import chat.Shared.Exceptions.InvalidNameException;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import chat.Shared.Utils.PhoneNumber;
import chat.Shared.Utils.DefaultUser;
import chat.Shared.Exceptions.InvalidPasswordException;
import chat.Shared.Exceptions.InvalidPhoneNumberException;

import static org.junit.Assert.*;

// TODO Fix tests
public class Tests {
    @Test
    @DisplayName("Проверка валидации неправильных паролей")
    public void invalidPasswordValidationTest(){
        DefaultUser user = new DefaultUser();

        String[] wrongPasswords = {"q1w2e3r4t5y6", "Ad123456.", "Qwerty123456@"};
        
        for (String password : wrongPasswords) {
            assertThrows(InvalidPasswordException.class, () -> user.setPassword(password));
        }
    }

    @Test
    @DisplayName("Проверка валидации правильных паролей")
    public void validPasswordValidationTest() throws InvalidPasswordException {
        DefaultUser user = new DefaultUser();

        String[] validPasswords = {"wqjdJDK21#dja", "zhOpa12@12", "jdkKDHMDKI123&dmamdn"};
        
        for (String password : validPasswords) {
            assertEquals(password, user.setPassword(password));
        }
    }

    @Test
    @DisplayName("Проверка валидации неправильного номера телефона")
    public void invalidPhoneNumberValidationTest() {
        PhoneNumber number = new PhoneNumber();

        String[] invalidNumbers = {"8", "    +da", "chakwdjawffnf", "8)953)))347))09))22)"};

        for (String num : invalidNumbers) {
            assertThrows(InvalidPhoneNumberException.class, () -> number.setNumber(num));
        }
    }

    @Test
    @DisplayName("Проверка валидации правильного номера телефона")
    public void validPhoneNumberValidationTest() throws InvalidPhoneNumberException {
        PhoneNumber number = new PhoneNumber();

        String[] validNumbers = {"89533470922", "+79171180241", "+7 (905) 093-61-93"};

        for (String num : validNumbers) {
            assertEquals(num, number.setNumber(num));
        }
    }

    @Test
    @DisplayName("Проверка валидации неправильного имени пользователя")
    public void invalidNameValidationTest() {
        DefaultUser user = new DefaultUser();

        String[] invalidNames = {"Григорий228", "", "   "};

        for (String name : invalidNames) {
            assertThrows(InvalidNameException.class, () -> user.setName(name));
        }
    }

    @Test
    @DisplayName("Проверка валидации правильного имени пользователя")
    public void validNameValidationTest() throws InvalidNameException {
        DefaultUser user = new DefaultUser();

        String[] validNames = {"Алексей", "Потап", "Donald"};

        for (String name : validNames) {
            assertEquals(name, user.setName(name));
        }
    }

}
