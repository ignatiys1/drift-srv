package TCP_Server;


import COMMONS.services.Stage;
import COMMONS.services.StageWithCup;
import Work_with_DB.DataBaseHandler;

import java.sql.ResultSet;
import java.util.Scanner;

public class ForStart {

    public static void main(String[] args) {

        System.out.println("Пароль от бд:");
        Scanner scanner = new Scanner(System.in);
        DataBaseHandler.getInstance().setDbPass(scanner.nextLine());

       Server server = new Server(8090);

    }
}
