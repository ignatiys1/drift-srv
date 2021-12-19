package COMMONS.phones;

import COMMONS.services.User;
import TCP_Server.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class ClientHandler implements Runnable {

    // экземпляр нашего сервера
    private Server server;
    // исходящее сообщение
    private PrintWriter outMessage;
    // входящее собщение
    private InputStream inMessage;

    // клиентский сокет
    private Socket clientSocket = null;
    // количество клиента в чате, статичное поле
    private static int clients_count = 0;

    private User thisUser;
    // конструктор, который принимает клиентский сокет и сервер
    public ClientHandler(Socket socket, Server server) {
        try {
            clients_count++;
            this.server = server;
            this.clientSocket = socket;
            this.outMessage = new PrintWriter(socket.getOutputStream());
            this.inMessage = socket.getInputStream();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public User getThisUser() {
        return thisUser;
    }

    public void setThisUser(User thisUser) {
        this.thisUser = thisUser;
    }

    // Переопределяем метод run(), который вызывается когда
    // мы вызываем new Thread(client).start();
    @Override
    public void run() {
        try {
            while (true) {
                // сервер отправляет сообщение
//                server.sendMessageToAllClients("Новый участник вошёл в чат!");
//                server.sendMessageToAllClients("Клиентов в чате = " + clients_count);
                    System.out.println("Клиент подключен");
                break;
            }

            while (true) {
                // Если от клиента пришло сообщение
                int size = inMessage.read();
                if (size > 0) {
                    byte[] bytes = new byte[size];
                    inMessage.read(bytes);

                    int sizeOfMSG = Integer.valueOf(new String(bytes));

                    byte[] bytesMSG = new byte[sizeOfMSG];
                    inMessage.read(bytesMSG);



                    boolean withError = false;
                    int COMMAND_ERROR = 0;
                    for (byte b : bytesMSG) {
                        if (b==0) {
                            withError = true;
                        }
                    }

                    if (withError) {
                        byte[] bytesMSG2 = new byte[inMessage.available()];

                        bytesMSG2 = inMessage.readNBytes(inMessage.available());

                        int indexInSecond = 0;
                        for (int i = 0; i < bytesMSG.length; i++) {
                            if (bytesMSG[i]==0 && bytesMSG2.length>indexInSecond) {
                                bytesMSG[i] = bytesMSG2[indexInSecond];
                                indexInSecond++;
                            }
                        }

                        System.out.println();
                    }


                    String clientMessage = new String(bytesMSG);
//                    if (withError) {
//                        COMMAND_ERROR = Integer.valueOf(clientMessage.substring(clientMessage.indexOf("COMMAND")+9, clientMessage.indexOf("COMMAND")+12));
//                    }
                     clientMessage.trim();

                    // выводим в консоль сообщение (для теста)
                    System.out.println("Сообщение от "+ this.toString()+": "+ clientMessage);
                    System.out.println("Количество клиентов: "+ clients_count);
                    // отправляем данное сообщение всем клиентам
                    Adapter.getInstance().processRequest(clientMessage, this, COMMAND_ERROR);
                } else {
                    break;
                }
                // останавливаем выполнение потока на 100 мс
                Thread.sleep(100);
            }
        } catch (InterruptedException | IOException | SQLException ex) {
            ex.printStackTrace();
        } finally {
            this.close();
        }
    }

    // отправляем сообщение
    public void sendMsg(String msg) {
        try {
            outMessage.println(msg);
            outMessage.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void sendMsgToAll(String msg) {
        try {
            ArrayList<ClientHandler> clients = server.getClients();
            for (ClientHandler client : clients) {
                if (client.getThisUser().getIdUser() != thisUser.getIdUser()) {
                    outMessage.println(msg);
                    outMessage.flush();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Server getServer() {
        return server;
    }

    // клиент выходит из чата
    public void close() {
        // удаляем клиента из списка
        System.out.println(this.toString()+" - отключен");

        server.removeClient(this);
        clients_count--;

    }
}
