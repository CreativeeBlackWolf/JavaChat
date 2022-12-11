package chat.Client;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.PrivateKey;
import java.security.PublicKey;

import chat.Shared.AuthencationResponse;
import chat.Shared.UserConsoleReader;
import chat.Shared.UserReader;
import chat.Shared.UserSystemInReader;
import chat.Shared.Exceptions.ServerVerifyException;
import chat.Shared.Security.DH;
import chat.Shared.Security.RSA;
import chat.Shared.Utils.KeyConverter;
import chat.Shared.Utils.User;


public class Client {

    public User user = new User();
    protected Socket clientSocket;
    private DH hell = new DH();
    protected PrintWriter clientWriter;
    protected ResponsePrinter securedPrinter;
    protected RSA security = new RSA();
    protected UserReader consoleReader = System.console() == null ? new UserSystemInReader() : new UserConsoleReader();

    public Client(String host, int port) throws UnknownHostException, IOException, ServerVerifyException {
        this.clientSocket = new Socket(host, port);
        this.clientWriter = new PrintWriter(clientSocket.getOutputStream());
        
        exchangeKeys();
    }


    /** Обменивается ключами с сервером.
     * Инициализирует {@code securedPrinter} для возможности расшифровки сообщений, 
     * полученных от сервера. 
     * @throws IOException
     * @throws ServerVerifyException невозможно подтвердить личность сервера
     */
    public void exchangeKeys() throws IOException, ServerVerifyException {
        ResponsePrinter exchangingPrinter = new ResponsePrinter(clientSocket);
        PublicKey serverPublicKey = (PublicKey) KeyConverter.stringToKey(exchangingPrinter.readLine(), "RSA", false);
        String serverVerificationResponce = security.decrypt(exchangingPrinter.readLine(), serverPublicKey);

        if (serverVerificationResponce.equals("лъягушка")) {
            clientWriter.println(KeyConverter.keyToString(hell.getPublickey()));
            clientWriter.flush();

            hell.setReceiverPublicKey((PublicKey) KeyConverter.stringToKey(
                exchangingPrinter.readLine(), "EC", false));
            PrivateKey serverPrivateKey = (PrivateKey) KeyConverter.stringToKey(
                    hell.decrypt(exchangingPrinter.readLine()), "RSA", true);
            security.setPublicKey(serverPublicKey);
            security.setPrivateKey(serverPrivateKey);
            this.securedPrinter = new ResponsePrinter(clientSocket, security);
        } else {
            throw new ServerVerifyException("Сервер не может подтвердить свою личность. Строка: " + serverVerificationResponce);
        }
    }

    public AuthencationResponse login(String login, String password) {
        clientWriter.println(security.encrypt(login));
        clientWriter.println(security.encrypt(password));
        clientWriter.flush();
        try {
            return AuthencationResponse.valueOf(security.decrypt(securedPrinter.readLine()));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
