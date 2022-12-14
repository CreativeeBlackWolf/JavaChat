package chat.Client;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.PrivateKey;
import java.security.PublicKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import chat.Shared.AuthencationResponse;
import chat.Shared.UserConsoleReader;
import chat.Shared.UserReader;
import chat.Shared.UserSystemInReader;
import chat.Shared.Exceptions.ServerVerifyException;
import chat.Shared.Security.DH;
import chat.Shared.Security.RSA;
import chat.Shared.Utils.KeyConverter;
import chat.Shared.Utils.DefaultUser;


public class Client {
    private static final Logger logger = LoggerFactory.getLogger(Client.class);

    public DefaultUser user = new DefaultUser();
    protected Socket clientSocket;
    private DH hell = new DH();
    protected PrintWriter clientWriter;
    protected ResponsePrinter securedPrinter;
    protected RSA security = new RSA();
    protected UserReader consoleReader = System.console() == null ? new UserSystemInReader() : new UserConsoleReader();

    public Client(String host, int port) throws UnknownHostException, IOException, ServerVerifyException {
        this.clientSocket = new Socket(host, port);
        logger.info("Connected to server on host `" + host + "` and port `" + port + "`");
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
        logger.debug("Started key exchange with server.");
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
            logger.debug("Finished key exchange with server.");
        } else {
            throw new ServerVerifyException("Сервер не может подтвердить свою личность. Строка: " + serverVerificationResponce);
        }
    }

    /** Авторизует пользователя.
     * @param login
     * @param password
     * @return {@code AuthencationResponse}
     */
    public AuthencationResponse login(String login, String password) {
        clientWriter.println(security.encrypt("LOG_ME_IN"));
        clientWriter.println(security.encrypt(login));
        clientWriter.println(security.encrypt(password));
        clientWriter.flush();
        try {
            return AuthencationResponse.valueOf(security.decrypt(securedPrinter.readLine()));
        } catch (IOException e) {
            logger.error(password, e);
            return null;
        }
    }
}
