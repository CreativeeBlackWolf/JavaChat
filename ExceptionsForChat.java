public class ExceptionsForChat extends Exception{
    public void ChatCheck() throws ExceptionsForChat{
        System.out.println("Age should be between 14 and 120");
        throw new ExceptionsForChat();
    }
}

class ExceptionsForName extends Exception{
    public void NameCheck() throws ExceptionsForName{
        System.out.println("Name should not be empty");
        throw new ExceptionsForName();
    }
}

class ExceptionsForNumber extends Exception{
    public void NumberCheck() throws ExceptionsForNumber{
        System.out.println("Invalid number");
        throw new ExceptionsForNumber();
    }
}

class ExceptionsForPassword extends Exception{
    public void PasswordCheck() throws ExceptionsForPassword{
        System.out.println("Password should be at least 8 digits long");
        throw new ExceptionsForPassword();
    }
}