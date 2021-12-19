package TCP_Server;

import COMMONS.phones.ClientHandler;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Server {

    //отвечает за работу сервера
    private static boolean WORK = true;


    // порт, который будет прослушивать наш сервер
    // список клиентов, которые будут подключаться к серверу
    private ArrayList<ClientHandler> clients = new ArrayList<ClientHandler>();

    private String srvHost = "10.8.0.2";

    public Server(int port) {
        // сокет клиента, это некий поток, который будет подключаться к серверу
        // по адресу и порту
        Socket clientSocket = null;
        // серверный сокет
        ServerSocket serverSocket = null;
        try {
            // создаём серверный сокет на определенном порту
            serverSocket = new ServerSocket(port);
            System.out.println("Сервер запущен!");
            // запускаем бесконечный цикл
            while (true) {
                // таким образом ждём подключений от клиета
                clientSocket = serverSocket.accept();

                // создаём обработчик клиента, который подключился к серверу
                // this - это наш сервер
                ClientHandler client = new ClientHandler(clientSocket, this);
                clients.add(client);
                // каждое подключение клиента обрабатываем в новом потоке
                new Thread(client).start();

                Socket finalClientSocket = clientSocket;
                ServerSocket finalServerSocket = serverSocket;
                new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String close = new Scanner(System.in).nextLine();
                            if (close.equalsIgnoreCase("close")) {
                                try {
                                    WORK = false;
                                    finalClientSocket.close();
                                    finalServerSocket.close();
                                } catch (IOException exception) {
                                    exception.printStackTrace();
                                }
                            }
                        }
                    }).start();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                // закрываем подключение
                clientSocket.close();
                System.out.println("Сервер остановлен");
                serverSocket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    // удаляем клиента из коллекции при выходе из чата
    public void removeClient(ClientHandler client) {
        clients.remove(client);
    }


    // отправляем сообщение всем клиентам
    public void sendMessageToAllClients(String msg) {
        for (ClientHandler o : clients) {
            o.sendMsg(msg);
        }
    }

    public ArrayList<ClientHandler> getClients() {
        return clients;
    }
}
