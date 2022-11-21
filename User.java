import java.util.Date;
import java.text.SimpleDateFormat;
import java.io.*;


public class User {
    private String name;
    private String lastName;
    private int age;
    private String password;
    private String message;

    //сеттер для юзера
    public User(String name, String lastName, int age, String password, String message) throws IOException{
        ExceptionsForName forName = new ExceptionsForName();
        char firstSymbolName = name.charAt(0);
        char firstSymbolLastName = lastName.charAt(0);
        if(Character.isLowerCase(firstSymbolName) || Character.isLowerCase(firstSymbolLastName)
                || name.contains(" ") || name.isEmpty()
                || lastName.contains(" ") || lastName.isEmpty()){
            try{
                forName.NameCheck();
            }

            catch(ExceptionsForName e){
            }
        }
        else {
            this.name = name;
            this.lastName = lastName;
        }
        ExceptionsForChat forAge = new ExceptionsForChat();
        if(age < 0 || age > 120){
            try{
                forAge.ChatCheck();
            }
            catch(ExceptionsForChat e){
            }
        }
        else this.age = age;

        this.password = password;
        this.message = message;
    }
    public String OutName(){
        return name;
    }
    public String OutSecondName(){
        return lastName;
    }
    public String OutPassword(){
        return password;
    }
    public String OutMessage(){
        return message;
    }
    public void message(){
        Date date = new Date();
        SimpleDateFormat timeFormat = new SimpleDateFormat("dd.MM.YY HH:mm:ss");
        String time = timeFormat.format(date);
        try(FileWriter writer = new FileWriter("log.txt", false)){
            String text = "Время отправления: " + time + "\n" +
                    "Отправитель: " + name + " " + lastName + "\n" + "Сообщение: " + message;
            writer.write(text);
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    }
}