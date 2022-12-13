package chat.tests;

import static org.junit.Assert.assertThrows;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import chat.Shared.Utils.Number;
import chat.Shared.Utils.User;
import chat.Shared.Exceptions.InvalidPasswordException;
import chat.Shared.Exceptions.InvalidPhoneNumberException;

public class Tests {
    @Test
    @DisplayName("Проверка валидации неправильных паролей")
    public void wrongPasswordValidationTest() {
        User user = new User();

        String[] wrongPasswords = {"q1w2e3r4t5y6", "Ad123456.", "Qwerty123456@"};
        
        for (String password : wrongPasswords) {
            assertThrows(InvalidPasswordException.class, () -> user.setPassword(password));
        }
    }

    @Test
    @DisplayName("Проверка валидации правильных паролей")
    public void validPasswordValidationTest() {
        User user = new User();

        String[] validPasswords = {};
        
        for (String password : validPasswords) {
            
        }
    }
}
