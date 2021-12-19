package Work_with_DB;

import COMMONS.commands.DifObjectTypes;
import COMMONS.services.*;
import COMMONS.services.Driver;



import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;


public class DataBaseHandler {

    private String dbHost = "localhost";
    private String dbPort = "3306";
    private String dbUser = "root";
    private String dbPass = "";
    private String dbName = "drift_app";


    public String getDbHost() {
        return dbHost;
    }

    public void setDbHost(String dbHost) {
        this.dbHost = dbHost;
    }

    public String getDbPort() {
        return dbPort;
    }

    public void setDbPort(String dbPort) {
        this.dbPort = dbPort;
    }

    public String getDbUser() {
        return dbUser;
    }

    public void setDbUser(String dbUser) {
        this.dbUser = dbUser;
    }

    public String getDbPass() {
        return dbPass;
    }

    public void setDbPass(String dbPass) {
        this.dbPass = dbPass;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }


    private static DataBaseHandler dataBaseHandler;

    Connection dbConnection;

    public static synchronized DataBaseHandler getInstance() {
        if (dataBaseHandler == null) {
            try {
                dataBaseHandler = new DataBaseHandler();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return dataBaseHandler;
    }

    public Connection getDbConnection() throws SQLException, ClassNotFoundException {
        String connectionString = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName + "?useSSL=false&serverTimezone=UTC";

        Class.forName("com.mysql.cj.jdbc.Driver");

        dbConnection = DriverManager.getConnection(connectionString, dbUser, dbPass);
        return dbConnection;
    }

    private DataBaseHandler() throws SQLException {

    }

    /**
     * Методы работы с БД
     */

    //возвращает список всех пользователей
    public ArrayList<User> GetUsers() throws SQLException {
        ResultSet resSet = null;
        ArrayList<User> users = new ArrayList<>();
        String select = "SELECT * FROM " + Const.USERS_TABEL;
        try (PreparedStatement prSt = getDbConnection().prepareStatement(select)) {

            resSet = prSt.executeQuery();

            while (resSet.next()) {
                users.add(new User(resSet));
            }
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }

        dbConnection.close();
        return users;
    }

    public User GetUserById(int id, boolean withPass) throws SQLException {
        ResultSet resSet = null;
        User user = null;
        String select = "SELECT * FROM " + Const.USERS_TABEL +
                " WHERE " + Const.USERS_ID + "=?;";
        try (PreparedStatement prSt = getDbConnection().prepareStatement(select)) {

            prSt.setInt(1, id);

            resSet = prSt.executeQuery();
            while (resSet.next()) {
                if (withPass) {
                    user = new User(resSet, withPass);
                } else  {
                    user = new User(resSet);
                }
            }

        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        dbConnection.close();

        return user;
    }

    //возвращает список пользователей с заданными логином и паролем
    public User CheckUserInDB(String login, String password) {
        ResultSet resSet = null;

        String select = "SELECT * FROM " + Const.USERS_TABEL + " WHERE " + Const.USERS_LOGIN + "=? AND " + Const.USERS_PASSWORD + "=?";

        try {

            PreparedStatement prSt = getDbConnection().prepareStatement(select);
            prSt.setString(1, login);
            prSt.setString(2, password);

            resSet = prSt.executeQuery();
            while (resSet.next()) {
                return new User(resSet, true);
            }
            return null;

        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        try {
            dbConnection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;
    }

    //возвращает список пользователей с заданными логином
    public User CheckUserInDB(String login) {
        ResultSet resSet = null;

        String select = "SELECT * FROM " + Const.USERS_TABEL + " WHERE " + Const.USERS_LOGIN + "=?";

        try {

            PreparedStatement prSt = getDbConnection().prepareStatement(select);
            prSt.setString(1, login);

            resSet = prSt.executeQuery();
            while (resSet.next()) {
                return new User(resSet);
            }
            return null;

        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        try {
            dbConnection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;
    }

    //регестрирует нового пользователя
    public User SignUpUser(User user) {
        if (CheckUserInDB(user.getLogin()) != null) {
            return null;
        }
        User user1 = null;
        try (PreparedStatement prSt = getDbConnection().prepareStatement(user.getInsertQuery())) {


            prSt.executeUpdate();

            user1 = CheckUserInDB(user.getLogin());
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        try {
            dbConnection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return user1;
    }

    //Удаляет пользователя
    public void DeleteUser(String login) {
        String query = "DELETE FROM " + Const.USERS_TABEL +
                " WHERE " +
                Const.USERS_LOGIN +
                "= ?";

        try (PreparedStatement prSt = getDbConnection().prepareStatement(query)) {

            prSt.setString(1, login);
            prSt.executeUpdate();
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        try {
            dbConnection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    //редактирует пользователя
    public boolean EditUser(User userToEdit, User newUser) throws SQLException {
        boolean success = false;
        User user = CheckUserInDB(userToEdit.getLogin(), userToEdit.getPassword());
        if (user != null) {
            String update = "UPDATE " + Const.USERS_TABEL + " SET " +
                    Const.USERS_FIRSTNAME + " =?, " +
                    Const.USERS_LASTNAME + " =?, " +
                    Const.USERS_LOGIN + " =?, " +
                    Const.USERS_PASSWORD + " =?, " +
                    Const.USERS_TYPE + " =? " +
                    " WHERE " + Const.USERS_ID + " =? ";
            try (PreparedStatement prSt = getDbConnection().prepareStatement(update)) {


                if (!newUser.getFirstName().equals("")) {
                    prSt.setString(1, newUser.getFirstName());
                } else {
                    prSt.setString(1, userToEdit.getFirstName());
                }
                if (!newUser.getLastName().equals("")) {
                    prSt.setString(2, newUser.getLastName());
                } else {
                    prSt.setString(2, userToEdit.getLastName());
                }
                if (!newUser.getLogin().equals("")) {
                    prSt.setString(3, newUser.getLogin());
                } else {
                    prSt.setString(3, userToEdit.getLogin());
                }
                if (!newUser.getPassword().equals("")) {
                    prSt.setString(4, newUser.getPassword());
                } else {
                    prSt.setString(4, userToEdit.getPassword());
                }
                if (!newUser.getType().equals(userToEdit.getType())) {
                    prSt.setString(5, newUser.getType());
                } else {
                    prSt.setString(5, userToEdit.getType());
                }

                prSt.setInt(6, userToEdit.getIdUser());

                prSt.executeUpdate();
                success = true;
            } catch (SQLException | ClassNotFoundException throwables) {
                throwables.printStackTrace();
            }
        }
        try {
            dbConnection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return success;

    }


//////////////////////////////////////////////////////////////////////////////////////////////////////


//    public ArrayList<Cup> GetCups() throws SQLException {
//        ResultSet resSet = null;
//        ArrayList<Cup> cups = new ArrayList<>();
//        String select = "SELECT * FROM " + Const.CUPS_TABEL;
//        try (PreparedStatement prSt = getDbConnection().prepareStatement(select)){
//
//
//            resSet = prSt.executeQuery();
//            while (resSet.next()) {
//                cups.add(new Cup(resSet.getInt(Const.CUPS_ID),
//                        resSet.getString(Const.CUPS_NAME),
//                        resSet.getInt(Const.CUPS_YEAR),
//                        resSet.getInt(Const.CUPS_ORGANIZER)
//                ));
//            }
//
//        } catch (SQLException | ClassNotFoundException throwables) {
//            throwables.printStackTrace();
//        }
//        try {
//            dbConnection.close();
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//
//        return cups;
//    }

    //ПОЛУЧЕНИЕ ТАБЛИЦ ЦЕЛИКОМ


    public ArrayList<Cup> GetCups() throws SQLException {
        ResultSet resSet = null;
        ArrayList<Cup> cups = new ArrayList<>();
        String select = "SELECT * FROM " + Const.CUPS_TABEL;
        try (PreparedStatement prSt = getDbConnection().prepareStatement(select)) {

            resSet = prSt.executeQuery();
            while (resSet.next()) {
                cups.add(new Cup(resSet));
            }

        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        try {
            dbConnection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return cups;
    }

    public ArrayList<Driver> GetDrivers() throws SQLException {
        ResultSet resSet = null;
        ArrayList<Driver> drivers = new ArrayList<>();
        String select = "SELECT * FROM " + Const.DRIVERS_TABEL;
        try (PreparedStatement prSt = getDbConnection().prepareStatement(select)) {

            resSet = prSt.executeQuery();
            while (resSet.next()) {
                drivers.add(new Driver(resSet));
            }

        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        try {
            dbConnection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return drivers;
    }

    public ArrayList<Grid> GetGrids() throws SQLException {
        ResultSet resSet = null;
        ArrayList<Grid> grids = new ArrayList<>();
        String select = "SELECT * FROM " + Const.GRIDS_TABEL;
        try (PreparedStatement prSt = getDbConnection().prepareStatement(select)) {

            resSet = prSt.executeQuery();
            while (resSet.next()) {
                grids.add(new Grid(resSet));
            }

        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        try {
            dbConnection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return grids;
    }

    public ArrayList<ResultTable> GetResultTable() throws SQLException {
        ResultSet resSet = null;
        ArrayList<ResultTable> resultTable = new ArrayList<>();
        String select = "SELECT * FROM " + Const.RESULTTABLE_TABEL;
        try (PreparedStatement prSt = getDbConnection().prepareStatement(select)) {

            resSet = prSt.executeQuery();
            while (resSet.next()) {
                resultTable.add(new ResultTable(resSet));
            }

        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        try {
            dbConnection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return resultTable;
    }

    public ArrayList<Stage> GetStage() throws SQLException {
        ResultSet resSet = null;
        ArrayList<Stage> stages = new ArrayList<>();
        String select = "SELECT * FROM " + Const.STAGE_TABEL;
        try (PreparedStatement prSt = getDbConnection().prepareStatement(select)) {

            resSet = prSt.executeQuery();
            while (resSet.next()) {
                stages.add(new Stage(resSet));
            }

        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        try {
            dbConnection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return stages;
    }

    public ArrayList<StageTableTemp> GetStageTableTemp() throws SQLException {
        ResultSet resSet = null;
        ArrayList<StageTableTemp> stages = new ArrayList<>();
        String select = "SELECT * FROM " + Const.STAGETABLETEMP_TABEL;
        try (PreparedStatement prSt = getDbConnection().prepareStatement(select)) {

            resSet = prSt.executeQuery();
            while (resSet.next()) {
                stages.add(new StageTableTemp(resSet));
            }

        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        try {
            dbConnection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return stages;
    }

    public ArrayList<Team> GetTeam() throws SQLException {
        ResultSet resSet = null;
        ArrayList<Team> teams = new ArrayList<>();
        String select = "SELECT * FROM " + Const.CUPS_TABEL;
        try (PreparedStatement prSt = getDbConnection().prepareStatement(select)) {


            resSet = prSt.executeQuery();
            while (resSet.next()) {
                teams.add(new Team(resSet));
            }

        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        try {
            dbConnection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return teams;
    }


    //ПОЛУЧЕНИЕ ОБЪЕКТА ПО ID
    public forDB GetObjById(int id) {
        forDB obj = new forDB();
        ResultSet resultSet = null;
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(obj.getSelectByIdQuery());

            resultSet = prSt.executeQuery();
            if (resultSet.next()) {
                return obj.setFieldsByResSet(resultSet);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            dbConnection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;
    }


    //ПОЛУЧЕНИЕ ТАБЛИЦ С ПАРАМЕТРАМИ

    //получение этапной решетки
    public ArrayList<Grid> GetGrids(int idStage) throws SQLException {
        ResultSet resSet = null;
        ArrayList<Grid> grids = new ArrayList<>();
        String select = "SELECT * FROM " + Const.GRIDS_TABEL +
                " WHERE " + Const.GRIDS_STAGE + "=? ";
        try (PreparedStatement prSt = getDbConnection().prepareStatement(select)) {
            prSt.setInt(1, idStage);

            resSet = prSt.executeQuery();
            while (resSet.next()) {
                grids.add(new Grid(resSet.getInt(Const.GRIDS_ID),
                        resSet.getInt(Const.GRIDS_STAGE),
                        resSet.getInt(Const.GRIDS_DRIVER),
                        resSet.getInt(Const.GRIDS_POSITION),
                        resSet.getInt(Const.GRIDS_TYPE)
                ));
            }

        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        try {
            dbConnection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return grids;
    }

    //получение турнийрной таблицы по чемпионату (по пилотам)
    public ArrayList<CupTableByPilots> GetCupTableByPilots(int idcup) throws SQLException {
        ResultSet resSet = null;
        ArrayList<CupTableByPilots> resultTable = new ArrayList<>();
        String select = "SELECT " +
                Const.RESULTTABLE_TABEL + "." + Const.RESULTTABLE_CUP + ", " +
                Const.RESULTTABLE_TABEL + "." + Const.RESULTTABLE_DRIVER + ", " +
                Const.CUPS_TABEL + "." + Const.CUPS_NAME + ", " +
                Const.DRIVERS_TABEL + "." + Const.DRIVERS_FIO + ", " +
                Const.DRIVERS_TABEL + "." + Const.DRIVERS_NICKNAME + ", " +
                Const.DRIVERS_TABEL + "." + Const.DRIVERS_COUNTRY + ", " +
                "SUM(" + Const.RESULTTABLE_TABEL + "." + Const.RESULTTABLE_SCORE + ") " +
                "FROM " + Const.RESULTTABLE_TABEL + " INNER JOIN " + Const.DRIVERS_TABEL +
                " ON " + Const.RESULTTABLE_TABEL + "." + Const.RESULTTABLE_DRIVER + " = " + Const.DRIVERS_TABEL + "." + Const.DRIVERS_ID +
                " INNER JOIN " + Const.CUPS_TABEL +
                " ON " + Const.RESULTTABLE_TABEL + "." + Const.RESULTTABLE_CUP + " = " + Const.CUPS_TABEL + "." + Const.CUPS_ID +
                " WHERE " + Const.RESULTTABLE_TABEL + "." + Const.RESULTTABLE_CUP + "=? " +
                "GROUP BY " +
                Const.RESULTTABLE_TABEL + "." + Const.RESULTTABLE_CUP + ", " +
                Const.RESULTTABLE_TABEL + "." + Const.RESULTTABLE_DRIVER + ", " +
                Const.CUPS_TABEL + "." + Const.CUPS_NAME + ", " +
                Const.DRIVERS_TABEL + "." + Const.DRIVERS_FIO + ", " +
                Const.DRIVERS_TABEL + "." + Const.DRIVERS_NICKNAME + ", " +
                Const.DRIVERS_TABEL + "." + Const.DRIVERS_COUNTRY;
        try (PreparedStatement prSt = getDbConnection().prepareStatement(select)) {
            prSt.setInt(1, idcup);

            resSet = prSt.executeQuery();
            while (resSet.next()) {
                resultTable.add(new CupTableByPilots(resSet));
            }

        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        try {
            dbConnection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return resultTable;
    }

    //получение турнийрной таблицы по чемпионату (по командам)
    public ArrayList<CupTableByTeams> GetCupTableByTeams(int idcup) throws SQLException {
        ResultSet resSet = null;
        ArrayList<CupTableByTeams> resultTable = new ArrayList<>();
        String select = "SELECT " +
                Const.RESULTTABLE_TABEL + "." + Const.RESULTTABLE_CUP + ", " +
                Const.RESULTTABLE_TABEL + "." + Const.RESULTTABLE_TEAM + ", " +
                Const.CUPS_TABEL + "." + Const.CUPS_NAME + " AS cupName, " +
                Const.TEAMS_TABEL + "." + Const.TEAMS_NAME + " AS teamName, " +
                Const.TEAMS_TABEL + "." + Const.TEAMS_COUNTRY + ", " +
                "SUM(" + Const.RESULTTABLE_TABEL + "." + Const.RESULTTABLE_SCORE + ") " +
                "FROM " + Const.RESULTTABLE_TABEL + " INNER JOIN " + Const.TEAMS_TABEL +
                " ON " + Const.RESULTTABLE_TABEL + "." + Const.RESULTTABLE_TEAM + " = " + Const.TEAMS_TABEL + "." + Const.TEAMS_ID +
                " INNER JOIN " + Const.CUPS_TABEL +
                " ON " + Const.RESULTTABLE_TABEL + "." + Const.RESULTTABLE_CUP + " = " + Const.CUPS_TABEL + "." + Const.CUPS_ID +
                " WHERE " + Const.RESULTTABLE_TABEL + "." + Const.RESULTTABLE_CUP + "=? " +
                "GROUP BY " +
                Const.RESULTTABLE_TABEL + "." + Const.RESULTTABLE_CUP + ", " +
                Const.RESULTTABLE_TABEL + "." + Const.RESULTTABLE_DRIVER + ", " +
                Const.CUPS_TABEL + "." + Const.CUPS_NAME + ", " +
                Const.TEAMS_TABEL + "." + Const.TEAMS_NAME + ", " +
                Const.TEAMS_TABEL + "." + Const.TEAMS_COUNTRY;
        try (PreparedStatement prSt = getDbConnection().prepareStatement(select)) {
            prSt.setInt(1, idcup);

            resSet = prSt.executeQuery();
            while (resSet.next()) {
                resultTable.add(new CupTableByTeams(resSet));
            }

        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        try {
            dbConnection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return resultTable;
    }

    //получение турнийрной таблицы по этапу (по пилотам)
    public ArrayList<StageTableByPilots> GetStageTableByPilots(int idstage) throws SQLException {
        ResultSet resSet = null;
        ArrayList<StageTableByPilots> resultTable = new ArrayList<>();
        String select = "SELECT " +
                Const.RESULTTABLE_TABEL + "." + Const.RESULTTABLE_STAGE + ", " +
                Const.RESULTTABLE_TABEL + "." + Const.RESULTTABLE_DRIVER + ", " +
                Const.STAGE_TABEL + "." + Const.STAGE_NAME + ", " +
                Const.DRIVERS_TABEL + "." + Const.DRIVERS_FIO + ", " +
                Const.DRIVERS_TABEL + "." + Const.DRIVERS_NICKNAME + ", " +
                Const.DRIVERS_TABEL + "." + Const.DRIVERS_COUNTRY + ", " +
                "SUM(" + Const.RESULTTABLE_TABEL + "." + Const.RESULTTABLE_SCORE + ") " +
                "FROM " + Const.RESULTTABLE_TABEL + " INNER JOIN " + Const.DRIVERS_TABEL +
                " ON " + Const.RESULTTABLE_TABEL + "." + Const.RESULTTABLE_DRIVER + " = " + Const.DRIVERS_TABEL + "." + Const.DRIVERS_ID +
                " INNER JOIN " + Const.CUPS_TABEL +
                " ON " + Const.RESULTTABLE_TABEL + "." + Const.RESULTTABLE_STAGE + " = " + Const.STAGE_TABEL + "." + Const.STAGE_ID +
                " WHERE " + Const.RESULTTABLE_TABEL + "." + Const.RESULTTABLE_STAGE + "=? " +
                "GROUP BY " +
                Const.RESULTTABLE_TABEL + "." + Const.RESULTTABLE_STAGE + ", " +
                Const.RESULTTABLE_TABEL + "." + Const.RESULTTABLE_DRIVER + ", " +
                Const.STAGE_TABEL + "." + Const.STAGE_NAME + ", " +
                Const.DRIVERS_TABEL + "." + Const.DRIVERS_FIO + ", " +
                Const.DRIVERS_TABEL + "." + Const.DRIVERS_NICKNAME + ", " +
                Const.DRIVERS_TABEL + "." + Const.DRIVERS_COUNTRY;
        try (PreparedStatement prSt = getDbConnection().prepareStatement(select)) {
            prSt.setInt(1, idstage);

            resSet = prSt.executeQuery();
            while (resSet.next()) {
                resultTable.add(new StageTableByPilots(resSet));
            }

        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        try {
            dbConnection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return resultTable;
    }

    //получение турнийрной таблицы по этапу (по командам)
    public ArrayList<StageTableByTeams> GetStageTableByTeams(int idstage) throws SQLException {
        ResultSet resSet = null;
        ArrayList<StageTableByTeams> resultTable = new ArrayList<>();
        String select = "SELECT " +
                Const.RESULTTABLE_TABEL + "." + Const.RESULTTABLE_CUP + ", " +
                Const.RESULTTABLE_TABEL + "." + Const.RESULTTABLE_TEAM + ", " +
                Const.STAGE_TABEL + "." + Const.STAGE_NAME + " AS stageName, " +
                Const.TEAMS_TABEL + "." + Const.TEAMS_NAME + " AS teamName, " +
                Const.TEAMS_TABEL + "." + Const.TEAMS_COUNTRY + ", " +
                "SUM(" + Const.RESULTTABLE_TABEL + "." + Const.RESULTTABLE_SCORE + ") " +
                "FROM " + Const.RESULTTABLE_TABEL + " INNER JOIN " + Const.TEAMS_TABEL +
                " ON " + Const.RESULTTABLE_TABEL + "." + Const.RESULTTABLE_TEAM + " = " + Const.TEAMS_TABEL + "." + Const.TEAMS_ID +
                " INNER JOIN " + Const.STAGE_TABEL +
                " ON " + Const.RESULTTABLE_TABEL + "." + Const.RESULTTABLE_STAGE + " = " + Const.STAGE_TABEL + "." + Const.STAGE_ID +
                " WHERE " + Const.RESULTTABLE_TABEL + "." + Const.RESULTTABLE_STAGE + "=? " +
                "GROUP BY " +
                Const.RESULTTABLE_TABEL + "." + Const.RESULTTABLE_STAGE + ", " +
                Const.RESULTTABLE_TABEL + "." + Const.RESULTTABLE_DRIVER + ", " +
                Const.STAGE_TABEL + "." + Const.STAGE_NAME + ", " +
                Const.TEAMS_TABEL + "." + Const.TEAMS_NAME + ", " +
                Const.TEAMS_TABEL + "." + Const.TEAMS_COUNTRY;
        try (PreparedStatement prSt = getDbConnection().prepareStatement(select)) {
            prSt.setInt(1, idstage);

            resSet = prSt.executeQuery();
            while (resSet.next()) {
                resultTable.add(new StageTableByTeams(resSet));
            }

        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        try {
            dbConnection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return resultTable;
    }

    //получение этапов чемпионата
    public ArrayList<Stage> GetStage(Cup cup) throws SQLException {
        ResultSet resSet = null;
        ArrayList<Stage> stages = new ArrayList<>();
        String select = "SELECT * FROM " + Const.STAGE_TABEL + " WHERE "+
                Const.STAGE_CUP + " = ?";
        try (PreparedStatement prSt = getDbConnection().prepareStatement(select)) {
            prSt.setInt(1, cup.getIdcup());

            resSet = prSt.executeQuery();
            while (resSet.next()) {
                stages.add(new Stage(resSet));
            }

        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        try {
            dbConnection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return stages;
    }

    //получение этапной сетки
    public GridWithDrivers GetGridForStage(Stage stage){

        ResultSet resSet = null;
        String select = "SELECT * FROM " + Const.GRIDS_TABEL +
                " LEFT JOIN " + Const.DRIVERS_TABEL + " ON "+
                Const.GRIDS_TABEL+"."+Const.GRIDS_DRIVER+" = "+Const.DRIVERS_TABEL+"."+Const.DRIVERS_ID +
                " WHERE "+
                Const.GRIDS_STAGE + " = ?";
        try (PreparedStatement prSt = getDbConnection().prepareStatement(select)) {
            prSt.setInt(1, stage.getIdstage());

            resSet = prSt.executeQuery();
            return new GridWithDrivers(resSet, stage.getIdstage());

        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        try {
            dbConnection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        return null;
    }

    //получение чемпионатов для организатора
    public ArrayList<Cup> GetCups(User user) throws SQLException {
        ResultSet resSet = null;
        ArrayList<Cup> cups = new ArrayList<>();
        String select = "SELECT * FROM " + Const.CUPS_TABEL + " WHERE " +
                Const.CUPS_ORGANIZER + "=?";
        try (PreparedStatement prSt = getDbConnection().prepareStatement(select)) {

            prSt.setInt(1,user.getIdUser());
            resSet = prSt.executeQuery();
            while (resSet.next()) {
                cups.add(new Cup(resSet));
            }

        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        try {
            dbConnection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return cups;
    }

    //получение чемпионата для этапа
    public Cup GetCup(int id) throws SQLException {
        ResultSet resSet = null;

        String select = "SELECT * FROM " + Const.CUPS_TABEL + " WHERE " + Const.CUPS_ID + "=?";
        try (PreparedStatement prSt = getDbConnection().prepareStatement(select)) {

            prSt.setInt(1,id);
            resSet = prSt.executeQuery();
            while (resSet.next()) {
                return new Cup(resSet);
            }

        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        try {
            dbConnection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;
    }

    //получение пилотов с USER
    public ArrayList<DriverWithUser> GetDriversWithUser() throws SQLException {
        ResultSet resSet = null;
        ArrayList<DriverWithUser> drivers = new ArrayList<>();
        String select = "SELECT * FROM " + Const.DRIVERS_TABEL +
                " Left JOIN " + Const.USERS_TABEL +
                " ON " + Const.DRIVERS_TABEL+"."+Const.DRIVERS_ID+"="+Const.USERS_TABEL+"."+Const.USERS_DRIVER;
        try (PreparedStatement prSt = getDbConnection().prepareStatement(select)) {

            resSet = prSt.executeQuery();

            //int columns = resSet.getMetaData().;
//            // Перебор строк с данными
//            while(resSet.next()){
//                for (int i = 1; i <= columns; i++){
//                    System.out.print(resSet.getString(i) + "\t");
//                }
//                System.out.println();
//            }
            int num = 0;
            while (resSet.next()) {
                drivers.add(new DriverWithUser(resSet));
                num++;
//                if (num == columns-1) {
//                    return drivers;
//                }
            }

        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        try {
            dbConnection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return drivers;
    }


    //получение заездов этапа
    public ArrayList<StageTableTemp> GetRunsWithStatus(int stageId, boolean qual)  throws SQLException{
        ResultSet resSet = null;
        ArrayList<StageTableTemp> stageTableTemps = new ArrayList<>();
        String select = "SELECT * FROM " + Const.STAGETABLETEMP_TABEL +
                " WHERE " +
                Const.STAGETABLETEMP_STAGE + "=? AND "+
                Const.STAGETABLETEMP_QUAL + "=?";
        try (PreparedStatement prSt = getDbConnection().prepareStatement(select)) {

            prSt.setInt(1,stageId);
            prSt.setBoolean(2, qual);
            resSet = prSt.executeQuery();
            while (resSet.next()) {
                stageTableTemps.add(new StageTableTemp(resSet));
            }

        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        try {
            dbConnection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }



        return stageTableTemps;
    }

    //получение заездов этапа
    public ArrayList<StageTableTemp> GetFinalRunsWithStatus(int stageId, boolean qual)  throws SQLException{
        ResultSet resSet = null;
        ArrayList<StageTableTemp> stageTableTemps = new ArrayList<>();



        String select = "SELECT * FROM " + Const.STAGETABLETEMP_TABEL +
                " INNER JOIN (SELECT MAX("+Const.STAGETABLETEMP_SCORE+") AS MAX_SCORE, "+
                    Const.STAGETABLETEMP_FIRSTDRIVER+
                    " FROM " + Const.STAGETABLETEMP_TABEL +
                    " GROUP BY "+
                    Const.STAGETABLETEMP_FIRSTDRIVER+", "+
                    Const.STAGETABLETEMP_SECONDDRIVER +
                ") AS MAX_SCORE ON "+
                " MAX_SCORE."+Const.STAGETABLETEMP_FIRSTDRIVER+"="+Const.STAGETABLETEMP_TABEL+"."+ Const.STAGETABLETEMP_FIRSTDRIVER +
                " AND MAX_SCORE.MAX_SCORE = "+Const.STAGETABLETEMP_TABEL+"."+ Const.STAGETABLETEMP_SCORE +
                " WHERE " +
                Const.STAGETABLETEMP_STAGE + "=? AND "+
                Const.STAGETABLETEMP_QUAL + "=?";
        try (PreparedStatement prSt = getDbConnection().prepareStatement(select)) {

            prSt.setInt(1,stageId);
            prSt.setBoolean(2, qual);
            resSet = prSt.executeQuery();
            while (resSet.next()) {
                stageTableTemps.add(new StageTableTemp(resSet));
            }


        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        try {
            dbConnection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }



        return stageTableTemps;
    }

    public StageTableTemp GetRunWithStatus(int stageId, int status)  throws SQLException{
        ResultSet resSet = null;

        String select = "SELECT * FROM " + Const.STAGETABLETEMP_TABEL +
                " WHERE " +
                Const.STAGETABLETEMP_STAGE + "=? AND " +
                Const.STAGETABLETEMP_STATUS + "=?";
        try (PreparedStatement prSt = getDbConnection().prepareStatement(select)) {

            prSt.setInt(1,stageId);
            prSt.setInt(2,status);
            resSet = prSt.executeQuery();
            while (resSet.next()) {
                return new StageTableTemp(resSet);
            }

        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        try {
            dbConnection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }



        return null;
    }



    public StageTableTemp GetRun(int runId)  throws SQLException{
        ResultSet resSet = null;

        StageTableTemp run = null;

        String select = "SELECT * FROM " + Const.STAGETABLETEMP_TABEL +
                " WHERE " +
                Const.STAGETABLETEMP_ID + "=?";
        try (PreparedStatement prSt = getDbConnection().prepareStatement(select)) {

            prSt.setInt(1,runId);
            resSet = prSt.executeQuery();
            while (resSet.next()) {
                run = new StageTableTemp(resSet);
            }

        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        try {
            dbConnection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return run;
    }

    public StageTableTemp GetRun(int driverId, int attempt)  throws SQLException{
        ResultSet resSet = null;

        StageTableTemp run = null;

        String select = "SELECT * FROM " + Const.STAGETABLETEMP_TABEL +
                " WHERE " +
                Const.STAGETABLETEMP_FIRSTDRIVER + "=? AND " +
                Const.STAGETABLETEMP_ATTEMPT + "=?";
        try (PreparedStatement prSt = getDbConnection().prepareStatement(select)) {

            prSt.setInt(1,driverId);
            prSt.setInt(1,attempt);

            resSet = prSt.executeQuery();
            while (resSet.next()) {
                run = new StageTableTemp(resSet);
            }

        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        try {
            dbConnection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return run;
    }


    //получение этапов чемпионата
    public StageWithCup GetOnlineStage() throws SQLException {
        ResultSet resSet = null;
        String select = "SELECT * FROM " + Const.STAGE_TABEL +
                " LEFT JOIN " + Const.CUPS_TABEL + " ON " +
                Const.STAGE_TABEL+"."+Const.STAGE_CUP +"="+Const.CUPS_TABEL+"."+Const.CUPS_ID +
                " WHERE "+
                Const.STAGE_STATUS + " >= ?" +
                " AND " + Const.STAGE_STATUS + " < ?";
        try (PreparedStatement prSt = getDbConnection().prepareStatement(select)) {
            prSt.setInt(1, DifObjectTypes.STAGE_STATUS_QUAL);
            prSt.setInt(2, DifObjectTypes.STAGE_STATUS_IN_PAST);

            resSet = prSt.executeQuery();
            while (resSet.next()) {
                return new StageWithCup(resSet);
            }

        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        try {
            dbConnection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;
    }

    //получение этапов чемпионата
    public StageWithCup GetStage(int id) throws SQLException {
        ResultSet resSet = null;
        String select = "SELECT * FROM " + Const.STAGE_TABEL +
                " LEFT JOIN " + Const.CUPS_TABEL + " ON " +
                Const.STAGE_TABEL+"."+Const.STAGE_CUP +"="+Const.CUPS_TABEL+"."+Const.CUPS_ID +
                " WHERE "+
                Const.STAGE_ID + " = ?";
        try (PreparedStatement prSt = getDbConnection().prepareStatement(select)) {
            prSt.setInt(1, id);

            resSet = prSt.executeQuery();
            while (resSet.next()) {
                return new StageWithCup(resSet);
            }

        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        try {
            dbConnection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;
    }

    //получение пилота по id
    public Driver GetDriverById(int id) throws SQLException {
        ResultSet resSet = null;
        Driver driver = null;
        String select = "SELECT * FROM " + Const.DRIVERS_TABEL;

        if (id!=0) {
            select += " WHERE " + Const.DRIVERS_ID + "=?;";
        }
        try (PreparedStatement prSt = getDbConnection().prepareStatement(select)) {
            if (id!=0) {
                prSt.setInt(1, id);
            }

            resSet = prSt.executeQuery();
            while (resSet.next()) {
                driver = new Driver(resSet);
            }

        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        dbConnection.close();

        return driver;
    }

    //возвращает список всех судей
    public ArrayList<User> GetJudges() throws SQLException {
        ResultSet resSet = null;
        ArrayList<User> users = new ArrayList<>();
        String select = "SELECT * FROM " + Const.USERS_TABEL +
                " WHERE " + Const.USERS_TYPE + "=?";
        try (PreparedStatement prSt = getDbConnection().prepareStatement(select)) {
            prSt.setString(1, DifObjectTypes.USER_JUDGE);
            resSet = prSt.executeQuery();

            while (resSet.next()) {
                users.add(new User(resSet));
            }
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }

        dbConnection.close();
        return users;
    }

    public ArrayList<Participation> GetStageParticipiants(int idStage) throws SQLException {
        ResultSet resSet = null;
        ArrayList<Participation> participations = new ArrayList<>();
        String select = "SELECT * FROM " + Const.PARTICIOIANTS_TABLE +
                " WHERE " + Const.PARTICIOIANTS_STAGE + "=?" +
                " ORDER BY " + Const.PARTICIOIANTS_SERIALNUMBER;
        try (PreparedStatement prSt = getDbConnection().prepareStatement(select)) {
            prSt.setInt(1, idStage);
            resSet = prSt.executeQuery();

            while (resSet.next()) {
                participations.add(new Participation(resSet));
            }
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }

        dbConnection.close();
        return participations;
    }

    //ЗАПОЛНЕНИЕ ТАБЛИЦ

    public boolean AddObjectInDB(forDB obj) {
        String insert = obj.getInsertQuery();
        try (PreparedStatement prSt = getDbConnection().prepareStatement(insert)) {
            prSt.executeUpdate();
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        try {
            dbConnection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return true;
    }

    public boolean AddJudgeDecision(JudgeDecision decision) {
        ResultSet resultSet = null;
        String insert = "UPDATE " + Const.STAGETABLETEMP_TABEL + " SET ";

        switch (decision.judgeNum) {
            case 1:
                insert+= Const.STAGETABLETEMP_FIRSTJUDGESOLUTION;
                break;
            case 2:
                insert+= Const.STAGETABLETEMP_SECONDJUDGESOLUTION;
                break;
            case 3:
                insert+= Const.STAGETABLETEMP_THIRDJUDGESOLUTION;
                break;
            default:
                return false;
        }
        insert+= " =? " + " WHERE " + Const.STAGETABLETEMP_ID + "=?;";
        try (PreparedStatement prSt = getDbConnection().prepareStatement(insert)) {
            prSt.setInt(1, decision.solution);
            prSt.setInt(2, decision.idstagetable);
            prSt.executeUpdate();
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        try {
            dbConnection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return true;

    }

    //сохранение изменений в чемпионате
    //в случае отсутствия в базе - создаем новый
    public boolean SaveCup(Cup cup){
        boolean success = false;
        String query = "";

        Cup cupInDB = new Cup();

        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(cup.getSelectByIdQuery());
            ResultSet resSet = prSt.executeQuery();
            while (resSet.next()) {
                cupInDB.setFieldsByResSet(resSet);
            }
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }

        if (cupInDB.getIdcup() == cup.getIdcup() && cup.getIdcup() != 0) {
            query = cup.getUpdateQuery();
        } else {
            query = cup.getInsertQuery();
        }

        try (PreparedStatement prSt = getDbConnection().prepareStatement(query)) {
                prSt.executeUpdate();
                success = true;
        } catch (SQLException | ClassNotFoundException throwables) {
                throwables.printStackTrace();
            }


        try {
            dbConnection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return success;
    }

    //сохранение изменений в чемпионате
    //в случае отсутствия в базе - создаем новый
    public boolean SaveStage(Stage stage){
        boolean success = false;
        String query = "";

        Stage stageInDB = new Stage();

        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(stage.getSelectByIdQuery());
            ResultSet resSet = prSt.executeQuery();
            while (resSet.next()) {
                stageInDB.setFieldsByResSet(resSet);
            }
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }

        if (stageInDB.getIdstage() == stage.getIdstage() && stage.getIdstage() != 0) {
            query = stage.getUpdateQuery();
        } else {
            query = stage.getInsertQuery();
        }

        try (PreparedStatement prSt = getDbConnection().prepareStatement(query)) {
            prSt.executeUpdate();
            success = true;
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }


        try {
            dbConnection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return success;
    }

    //сохранение изменений в пилоте
    //в случае отсутствия в базе - создаем новый
    public boolean SaveDriver(Driver driver){
        boolean success = false;
        String query = "";

        Driver driverInDB = new Driver();

        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(driver.getSelectByIdQuery());
            ResultSet resSet = prSt.executeQuery();
            while (resSet.next()) {
                driverInDB.setFieldsByResSet(resSet);
            }
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }

        if (driverInDB.getIddriver() == driver.getIddriver() && driver.getIddriver() != 0) {
            query = driver.getUpdateQuery();
        } else {
            query = driver.getInsertQuery();
        }

        try (PreparedStatement prSt = getDbConnection().prepareStatement(query)) {
            prSt.executeUpdate();
            success = true;
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }


        try {
            dbConnection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return success;
    }

    //сохранение изменений в чемпионате
    //в случае отсутствия в базе - создаем новый
    public boolean SaveParticipiant(Participation part){
        boolean success = false;
        String query = "";

        Participation partInDB = null;

        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(part.getSelectByIdQuery());
            ResultSet resSet = prSt.executeQuery();
            while (resSet.next()) {
                partInDB = new Participation(resSet);
            }
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }

        query = part.getInsertQuery();

        if (partInDB != null) {
            if (partInDB.getIdStageParticipiants() == part.getIdStageParticipiants() && part.getIdStageParticipiants() != 0) {
                query = part.getUpdateQuery();
            }
        }


        try (PreparedStatement prSt = getDbConnection().prepareStatement(query)) {
            prSt.executeUpdate();
            success = true;
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }


        try {
            dbConnection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return success;
    }


    //сохранение изменений в чемпионате
    //в случае отсутствия в базе - создаем новый
    public boolean SaveStageTableTemp(StageTableTemp stageTableTemp){
        boolean success = false;
        String query = "";

        StageTableTemp stageInDB = new StageTableTemp();

        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(stageTableTemp.getSelectByIdQuery());
            ResultSet resSet = prSt.executeQuery();
            while (resSet.next()) {
                stageInDB.setFieldsByResSet(resSet);
            }
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }

        if (stageInDB.getIdstagetable() == stageTableTemp.getIdstagetable() && stageTableTemp.getIdstagetable() != 0) {
            query = stageTableTemp.getUpdateQuery();
        } else {
            query = stageTableTemp.getInsertQuery();
        }

        try (PreparedStatement prSt = getDbConnection().prepareStatement(query)) {
            prSt.executeUpdate();
            success = true;
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }

        return success;
    }

    public boolean SaveStageStatus(Stage stage){
        boolean success = false;
        String query = "";

        Stage stageInDB = new Stage();

        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(stage.getSelectByIdQuery());
            ResultSet resSet = prSt.executeQuery();
            while (resSet.next()) {
                stageInDB.setFieldsByResSet(resSet);
            }
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }

        if (stageInDB.getIdstage() == stage.getIdstage() && stage.getIdstage() != 0) {

            query = "UPDATE " + Const.STAGE_TABEL + " SET " + Const.STAGE_STATUS + "=? " +
                    " WHERE " + Const.STAGE_ID + "=?";


            try (PreparedStatement prSt = getDbConnection().prepareStatement(query)) {
                prSt.setInt(1, stage.getStatus());
                prSt.setInt(2, stage.getIdstage());
                prSt.executeUpdate();
                success = true;
            } catch (SQLException | ClassNotFoundException throwables) {
                throwables.printStackTrace();
            }

        }
        try {
            dbConnection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return success;
    }

    public boolean SaveRunStatus(StageTableTemp run){
        boolean success = false;
        String query = "";

        StageTableTemp runInDB = new StageTableTemp();

        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(run.getSelectByIdQuery());
            ResultSet resSet = prSt.executeQuery();
            while (resSet.next()) {
                runInDB.setFieldsByResSet(resSet);
            }
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }

        if (runInDB.getIdstagetable() == run.getIdstagetable() && run.getIdstagetable() != 0) {

            query = "UPDATE " + Const.STAGETABLETEMP_TABEL + " SET " + Const.STAGETABLETEMP_STATUS + "=? " +
                    " WHERE " + Const.STAGETABLETEMP_ID + "=?";


            try (PreparedStatement prSt = getDbConnection().prepareStatement(query)) {
                prSt.setInt(1, run.getStatus());
                prSt.setInt(2, run.getIdstagetable());
                prSt.executeUpdate();
                success = true;
            } catch (SQLException | ClassNotFoundException throwables) {
                throwables.printStackTrace();
            }

        }
        try {
            dbConnection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return success;
    }

    public void CalculateDecision(int idstagetable){

        ResultSet resSet = null;
        StageTableTemp stageTableTemp = null;

        String select = "SELECT * FROM " + Const.STAGETABLETEMP_TABEL +
                " WHERE " +
                Const.STAGETABLETEMP_ID + "=?";
        try (PreparedStatement prSt = getDbConnection().prepareStatement(select)) {

            prSt.setInt(1,idstagetable);
            resSet = prSt.executeQuery();
            while (resSet.next()) {
                stageTableTemp = new StageTableTemp(resSet);
            }

            if (stageTableTemp != null) {
                if (stageTableTemp.getFirstJudgeSolution() != -1 &&
                        stageTableTemp.getSecondJudgeSolution() != -1 &&
                        stageTableTemp.getThirdJudgeSolution() != -1) {
                    if (stageTableTemp.isQual()) {
                        int score = (stageTableTemp.getFirstJudgeSolution()+stageTableTemp.getSecondJudgeSolution()+stageTableTemp.getThirdJudgeSolution())/3;
                        stageTableTemp.setScore(score);
                        if (stageTableTemp.getAttempt()<3) {
                            StageTableTemp nextRun = GetRun(stageTableTemp.getFirstDriverId(), stageTableTemp.getAttempt());
                            if (nextRun!=null) {
                                nextRun.setStatus(DifObjectTypes.RUN_STATUS_NOW);
                                SaveStageTableTemp(nextRun);
                            }
                        }
                        } else {
                        int firstWin = 0;
                        if (stageTableTemp.getFirstJudgeSolution() == stageTableTemp.getFirstDriverId()) {
                            firstWin++;
                        } else if (stageTableTemp.getFirstJudgeSolution() == stageTableTemp.getSecondDriverId()) {
                            firstWin--;
                        }
                        if (stageTableTemp.getSecondJudgeSolution() == stageTableTemp.getFirstDriverId()) {
                            firstWin++;
                        } else if (stageTableTemp.getSecondJudgeSolution() == stageTableTemp.getSecondDriverId()) {
                            firstWin--;
                        }
                        if (stageTableTemp.getThirdJudgeSolution() == stageTableTemp.getFirstDriverId()) {
                            firstWin++;
                        } else if (stageTableTemp.getThirdJudgeSolution() == stageTableTemp.getSecondDriverId()) {
                            firstWin--;
                        }

                        stageTableTemp.setWinnerId(0);
                        if (firstWin>0) {
                            stageTableTemp.setWinnerId(stageTableTemp.getFirstDriverId());
                        } else if (firstWin<0) {
                            stageTableTemp.setWinnerId(stageTableTemp.getSecondDriverId());
                        }
                    }
                    stageTableTemp.setStatus(DifObjectTypes.RUN_STATUS_LATEST);
                    SaveStageTableTemp(stageTableTemp);
                }
            }

        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        try {
            dbConnection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public boolean SetQualStageTableTemp(Stage stage, ArrayList<Participation> partsOfStage){
        boolean success = true;

        for (Participation part : partsOfStage) {

//            StageTableTemp stageTableTemp = new StageTableTemp(stage.getIdstage(), part.driverId, true);
//            if (!SaveStageTableTemp(stageTableTemp)) {
//                success = false;
//            }
            String query = "";

            ResultSet resSet = null;

            StageTableTemp stageTableTempFromDB = null;

            String select = "SELECT * FROM " + Const.STAGETABLETEMP_TABEL +
                    " WHERE " +
                    Const.STAGETABLETEMP_STAGE + "=? AND " +
                    Const.STAGETABLETEMP_FIRSTDRIVER + "=? AND "+
                    Const.STAGETABLETEMP_QUAL + "=?";
            try (PreparedStatement prSt = getDbConnection().prepareStatement(select)) {

                prSt.setInt(1,stage.getIdstage());
                prSt.setInt(2,part.driverId);
                prSt.setBoolean(3,true);

                resSet = prSt.executeQuery();
                while (resSet.next()) {
                    stageTableTempFromDB = new StageTableTemp(resSet);
                }

            } catch (SQLException | ClassNotFoundException throwables) {
                throwables.printStackTrace();
            }

            if (stageTableTempFromDB == null) {
                query = "INSERT INTO " + Const.STAGETABLETEMP_TABEL + " (" + Const.STAGETABLETEMP_STAGE + ", " + Const.STAGETABLETEMP_FIRSTDRIVER + ", " + Const.STAGETABLETEMP_NUM + ", "+ Const.STAGETABLETEMP_QUAL + ", "+Const.STAGETABLETEMP_ATTEMPT+") VALUES (?,?,?,?,?)";

                try (PreparedStatement prSt = getDbConnection().prepareStatement(query)) {
                    prSt.setInt(1, stage.getIdstage());
                    prSt.setInt(2, part.driverId);
                    prSt.setInt(3, part.serialNumber);
                    prSt.setBoolean(4, true);
                    prSt.setInt(5, 1);

                    prSt.executeUpdate();

                } catch (SQLException | ClassNotFoundException throwables) {
                    success = false;
                    throwables.printStackTrace();
                }

                query = "INSERT INTO " + Const.STAGETABLETEMP_TABEL + " (" + Const.STAGETABLETEMP_STAGE + ", " + Const.STAGETABLETEMP_FIRSTDRIVER + ", " + Const.STAGETABLETEMP_NUM + ", "+ Const.STAGETABLETEMP_QUAL + ", "+Const.STAGETABLETEMP_ATTEMPT+") VALUES (?,?,?,?,?)";

                try (PreparedStatement prSt = getDbConnection().prepareStatement(query)) {
                    prSt.setInt(1, stage.getIdstage());
                    prSt.setInt(2, part.driverId);
                    prSt.setInt(3, part.serialNumber);
                    prSt.setBoolean(4, true);
                    prSt.setInt(5, 2);

                    prSt.executeUpdate();

                } catch (SQLException | ClassNotFoundException throwables) {
                    success = false;
                    throwables.printStackTrace();
                }

                query = "INSERT INTO " + Const.STAGETABLETEMP_TABEL + " (" + Const.STAGETABLETEMP_STAGE + ", " + Const.STAGETABLETEMP_FIRSTDRIVER + ", " + Const.STAGETABLETEMP_NUM + ", "+ Const.STAGETABLETEMP_QUAL + ", "+Const.STAGETABLETEMP_ATTEMPT+") VALUES (?,?,?,?,?)";

                try (PreparedStatement prSt = getDbConnection().prepareStatement(query)) {
                    prSt.setInt(1, stage.getIdstage());
                    prSt.setInt(2, part.driverId);
                    prSt.setInt(3, part.serialNumber);
                    prSt.setBoolean(4, true);
                    prSt.setInt(5, 3);

                    prSt.executeUpdate();

                } catch (SQLException | ClassNotFoundException throwables) {
                    success = false;
                    throwables.printStackTrace();
                }
            } else {

            }

        }
        try {
            dbConnection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return success;
    }

    public boolean ChageStatusesForRuns(int from, int to, int stageId) throws  SQLException {
        boolean success = false;

        String query = "";
        query = "UPDATE " + Const.STAGETABLETEMP_TABEL + " SET " +
                Const.STAGETABLETEMP_STATUS + "=? " +
                " WHERE " + Const.STAGETABLETEMP_STAGE + "=? AND "+
                Const.STAGETABLETEMP_STATUS + "=?";

        try (PreparedStatement prSt = getDbConnection().prepareStatement(query)) {

            prSt.setInt(1,to);
            prSt.setInt(2,stageId);
            prSt.setInt(3,from);

            prSt.executeUpdate();

        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }


        try {
            dbConnection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return success;
    }

    public boolean AddPositionIntoGrid(int stageId, int type, int driverId, int position) {
        boolean success = true;

        String query = "";
        query = "INSERT INTO " + Const.GRIDS_TABEL + " (" + Const.GRIDS_STAGE + ", " + Const.GRIDS_TYPE + ", " + Const.GRIDS_DRIVER + ", "+ Const.GRIDS_POSITION+") VALUES (?,?,?,?)";

        try (PreparedStatement prSt = getDbConnection().prepareStatement(query)) {

            prSt.setInt(1,stageId);
            prSt.setInt(2,type);
            prSt.setInt(3,driverId);
            prSt.setInt(4,position);

            prSt.executeUpdate();

        } catch (SQLException | ClassNotFoundException throwables) {
            success = false;
            throwables.printStackTrace();
        }


        try {
            dbConnection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return success;

    }
}


//    public ArrayList<Cup> GetAutos(String brand, String model) throws SQLException {
//        ResultSet resSet = null;
//        ArrayList<Cup> cups = new ArrayList<>();
//        String select = "SELECT * FROM " + Const.AUTOS_TABEL +
//                " WHERE " + Const.AUTOS_BRAND + "=? AND " +
//                Const.AUTOS_MODEL + "=?;";
//        try (PreparedStatement prSt = getDbConnection().prepareStatement(select)){
//
//
//            prSt.setInt(1, GetIdBrand(brand));
//            prSt.setInt(2, GetIdModel(model));
//
//            resSet = prSt.executeQuery();
//            while (resSet.next()) {
//                cups.add(new Cup(resSet.getInt(Const.AUTOS_VIN),
//                        resSet.getInt(Const.AUTOS_BRAND),
//                        resSet.getInt(Const.AUTOS_MODEL),
//                        resSet.getInt(Const.AUTOS_YEAR),
//                        resSet.getDouble(Const.AUTOS_ENGINE_CAPACITY),
//                        resSet.getInt(Const.AUTOS_ENGINE_TYPE),
//                        resSet.getInt(Const.AUTOS_TRANSMISSION),
//                        resSet.getInt(Const.AUTOS_DRIVE),
//                        resSet.getInt(Const.AUTOS_PRICE),
//                        resSet.getInt(Const.AUTOS_NUMBER)
//                ));
//            }
//
//        } catch (SQLException | ClassNotFoundException throwables) {
//            throwables.printStackTrace();
//        }
//        try {
//            dbConnection.close();
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//
//        return cups;
//    }
//
//    public ArrayList<Cup> GetAutos(int vin) throws SQLException {
//        ResultSet resSet = null;
//        ArrayList<Cup> cups = new ArrayList<>();
//        String select = "SELECT * FROM " + Const.AUTOS_TABEL +
//                " WHERE " + Const.AUTOS_VIN + "=?;";
//        try (PreparedStatement prSt = getDbConnection().prepareStatement(select)){
//
//
//            prSt.setInt(1, vin);
//
//            resSet = prSt.executeQuery();
//            while (resSet.next()) {
//                cups.add(new Cup(resSet.getInt(Const.AUTOS_VIN),
//                        resSet.getInt(Const.AUTOS_BRAND),
//                        resSet.getInt(Const.AUTOS_MODEL),
//                        resSet.getInt(Const.AUTOS_YEAR),
//                        resSet.getDouble(Const.AUTOS_ENGINE_CAPACITY),
//                        resSet.getInt(Const.AUTOS_ENGINE_TYPE),
//                        resSet.getInt(Const.AUTOS_TRANSMISSION),
//                        resSet.getInt(Const.AUTOS_DRIVE),
//                        resSet.getInt(Const.AUTOS_PRICE),
//                        resSet.getInt(Const.AUTOS_NUMBER)
//                ));
//            }
//
//        } catch (SQLException | ClassNotFoundException throwables) {
//            throwables.printStackTrace();
//        }
//        try {
//            dbConnection.close();
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//
//        return cups;
//    }
//
//    private int GetIdModel(String model) {
//        ResultSet resSet = null;
//        String select = "SELECT * FROM " + Const.MODELS_TABEL +
//                " WHERE " + Const.MODELS_MODEL + "=? ";
//        try (PreparedStatement prSt = getDbConnection().prepareStatement(select)){
//
//
//            prSt.setString(1, model);
//
//            resSet = prSt.executeQuery();
//            while (resSet.next()) {
//                return resSet.getInt(Const.MODELS_idMODEL);
//            }
//        } catch (SQLException | ClassNotFoundException throwables) {
//            throwables.printStackTrace();
//        }
//        try {
//            dbConnection.close();
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//
//        return 0;
//    }
//
//    private int GetIdBrand(String brand) {
//        try {
//            ResultSet resSet = null;
//            String select = "SELECT * FROM " + Const.BRANDS_TABEL +
//                    " WHERE " + Const.BRANDS_BRAND + "=? ";
//            try {
//                PreparedStatement prSt = getDbConnection().prepareStatement(select);
//
//                prSt.setString(1, brand);
//
//                resSet = prSt.executeQuery();
//            } catch (SQLException | ClassNotFoundException throwables) {
//                throwables.printStackTrace();
//            }
//            while (resSet.next()) {
//                return resSet.getInt(Const.BRANDS_idBRAND);
//            }
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//        try {
//            dbConnection.close();
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//
//        return 0;
//    }
//
//    public ResultSet CheckAutos(Cup cup)  {
//        String select = "SELECT * FROM " + Const.AUTOS_TABEL +
//                " WHERE " +
//                Const.AUTOS_BRAND + "=? AND " +
//                Const.AUTOS_MODEL + "=? AND " +
//                Const.AUTOS_ENGINE_CAPACITY + "=? AND " +
//                Const.AUTOS_ENGINE_TYPE + "=? AND " +
//                Const.AUTOS_TRANSMISSION + "=? AND " +
//                Const.AUTOS_DRIVE + "=? AND " +
//                Const.AUTOS_YEAR + "=?;";
//        ResultSet resultSet =null;
//        try {
//            PreparedStatement prSt = getDbConnection().prepareStatement(select);
//            prSt.setInt(1, cup.getIdBrand());
//            prSt.setInt(2, cup.getIdModel());
//            prSt.setDouble(3, cup.getEngineCapacity());
//            prSt.setInt(4, cup.getIdEnginetype());
//            prSt.setInt(5, cup.getIdTransmission());
//            prSt.setInt(6, cup.getIdDrive());
//            prSt.setInt(7, cup.getYearOfIssue());
//
//            resultSet = prSt.executeQuery();
//            return resultSet;
//
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//        try {
//            dbConnection.close();
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//
//        return null;
//    }
//
//    public ResultSet CheckAutos(int vin)  {
//        String select = "SELECT * FROM " + Const.AUTOS_TABEL +
//                " WHERE " +
//                Const.AUTOS_VIN + "=?;";
//        ResultSet resultSet = null;
//        try (PreparedStatement prSt = getDbConnection().prepareStatement(select)){
//
//            prSt.setInt(1, vin);
//
//             resultSet = prSt.executeQuery();
//            return resultSet;
//
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//        try {
//            dbConnection.close();
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//
//        return null;
//    }
//
//    public boolean NewAuto(Cup cup) {
//        ResultSet resultSet = null;
//        String insert = "UPDATE " + Const.AUTOS_TABEL + " SET " +
//                Const.AUTOS_NUMBER + "=?, " +
//                Const.AUTOS_PRICE + "=? " +
//                " WHERE " + Const.AUTOS_VIN + "=?;";
//        try (PreparedStatement prSt = getDbConnection().prepareStatement(insert)) {
//            resultSet = CheckAutos(cup);
//            if (resultSet.next()) {
//                prSt.setInt(1, resultSet.getInt(Const.AUTOS_NUMBER) + cup.getNumber());
//                prSt.setInt(2, cup.getPrice());
//                prSt.setInt(3, resultSet.getInt(Const.AUTOS_VIN));
//
//                prSt.executeUpdate();
//            } else {
//                String insert1 = "INSERT INTO " + Const.AUTOS_TABEL + "(" +
//                        Const.AUTOS_BRAND + "," +
//                        Const.AUTOS_MODEL + "," +
//                        Const.AUTOS_YEAR + "," +
//                        Const.AUTOS_ENGINE_CAPACITY + "," +
//                        Const.AUTOS_ENGINE_TYPE + "," +
//                        Const.AUTOS_TRANSMISSION + "," +
//                        Const.AUTOS_DRIVE + "," +
//                        Const.AUTOS_PRICE + "," +
//                        Const.AUTOS_NUMBER + ")" +
//                        "VALUES(?,?,?,?,?,?,?,?,?)";
//                try (PreparedStatement prSt1 = getDbConnection().prepareStatement(insert1)) {
//                    prSt1.setInt(1, cup.getIdBrand());
//                    prSt1.setInt(2, cup.getIdModel());
//                    prSt1.setInt(3, cup.getYearOfIssue());
//                    prSt1.setDouble(4, cup.getEngineCapacity());
//                    prSt1.setInt(5, cup.getIdEnginetype());
//                    prSt1.setInt(6, cup.getIdTransmission());
//                    prSt1.setInt(7, cup.getIdDrive());
//                    prSt1.setInt(8, cup.getPrice());
//                    prSt1.setInt(9, cup.getNumber());
//
//                    prSt1.executeUpdate();
//                }
//            }
//        } catch (SQLException | ClassNotFoundException throwables) {
//            throwables.printStackTrace();
//        }
//        try {
//            dbConnection.close();
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//        return true;
//    }
//
//    public boolean EditAuto(Cup newCup) throws SQLException, ClassNotFoundException {
//        boolean success = false;
//        ResultSet resSet = CheckAutos(newCup.getVin());
//        String update = "UPDATE " + Const.AUTOS_TABEL + " SET " +
//                Const.AUTOS_BRAND + " =?, " +
//                Const.AUTOS_MODEL + " =?, " +
//                Const.AUTOS_YEAR + " =?, " +
//                Const.AUTOS_ENGINE_CAPACITY + " =?, " +
//                Const.AUTOS_ENGINE_TYPE + " =?, " +
//                Const.AUTOS_TRANSMISSION + " =?, " +
//                Const.AUTOS_DRIVE + " =?, " +
//                Const.AUTOS_PRICE + " =?, " +
//                Const.AUTOS_NUMBER + " =? " +
//                " WHERE " + Const.AUTOS_VIN + "=?;";
//        if (resSet.next()) {
//            Cup cupToEdit = new Cup(resSet.getInt(1), resSet.getInt(2), resSet.getInt(3), resSet.getInt(4), resSet.getDouble(5), resSet.getInt(6), resSet.getInt(7), resSet.getInt(8), resSet.getInt(9), resSet.getInt(10));
//
//
//            try ( PreparedStatement prSt = getDbConnection().prepareStatement(update)){
//
//                if (newCup.getIdBrand() != 0) {
//                    prSt.setInt(1, newCup.getIdBrand());
//                } else {
//                    prSt.setInt(1, cupToEdit.getIdBrand());
//                }
//
//                if (newCup.getIdModel() != 0) {
//                    prSt.setInt(2, newCup.getIdModel());
//                } else {
//                    prSt.setInt(2, cupToEdit.getIdModel());
//                }
//
//                if (newCup.getYearOfIssue() != 0) {
//                    prSt.setInt(3, newCup.getYearOfIssue());
//                } else {
//                    prSt.setInt(3, cupToEdit.getYearOfIssue());
//                }
//
//                if (newCup.getEngineCapacity() != 0) {
//                    prSt.setDouble(4, newCup.getEngineCapacity());
//                } else {
//                    prSt.setDouble(4, cupToEdit.getEngineCapacity());
//                }
//
//                if (newCup.getIdEnginetype() != 0) {
//                    prSt.setInt(5, newCup.getIdEnginetype());
//                } else {
//                    prSt.setInt(5, cupToEdit.getIdEnginetype());
//                }
//
//                if (newCup.getIdTransmission() != 0) {
//                    prSt.setInt(6, newCup.getIdTransmission());
//                } else {
//                    prSt.setInt(6, cupToEdit.getIdTransmission());
//                }
//
//                if (newCup.getIdDrive() != 0) {
//                    prSt.setInt(7, newCup.getIdDrive());
//                } else {
//                    prSt.setInt(7, cupToEdit.getIdDrive());
//                }
//
//                if (newCup.getPrice() != 0) {
//                    prSt.setInt(8, newCup.getPrice());
//                } else {
//                    prSt.setInt(8, cupToEdit.getPrice());
//                }
//
//                if (newCup.getNumber() != 0) {
//                    prSt.setInt(9, newCup.getNumber());
//                } else {
//                    prSt.setInt(9, cupToEdit.getNumber());
//                }
//
//                prSt.setInt(10, cupToEdit.getVin());
//                prSt.executeUpdate();
//                success = true;
//            } catch (SQLException | ClassNotFoundException throwables) {
//                throwables.printStackTrace();
//            }
//        }
//        try {
//            dbConnection.close();
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//
//        return success;
//    }
//
//    public boolean DeleteAuto(Cup cup) {
//        String query = "DELETE FROM " + Const.AUTOS_TABEL +
//                " WHERE " +
//                Const.AUTOS_VIN +
//                "= ?";
//
//        try (PreparedStatement prSt = getDbConnection().prepareStatement(query)){
//
//            prSt.setInt(1, cup.getVin());
//            prSt.executeUpdate();
//            return true;
//        } catch (SQLException | ClassNotFoundException throwables) {
//            throwables.printStackTrace();
//        }
//        try {
//            dbConnection.close();
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//
//        return false;
//    }
//
//    ////////////////////////////////////////////////////////////////////
//
//
//    public void SetIdInfo() throws SQLException {
//        SetBrandTable();
//        SetModelTable();
//        SetEngTypeTable();
//        SetTransTable();
//        SetDriveTable();
//        SetPaymentTable();
//
//    }
//
//    public void SetBrandTable() throws SQLException {
//        ResultSet resSet = null;
//        String select = "SELECT * FROM " + Const.BRANDS_TABEL + " ORDER BY " + Const.BRANDS_idBRAND;
//        try (PreparedStatement prSt = getDbConnection().prepareStatement(select)){
//
//            resSet = prSt.executeQuery();
//            IdControls.getInstance().SetBrands(resSet);
//
//        } catch (SQLException | ClassNotFoundException throwables) {
//            throwables.printStackTrace();
//        }
//        try {
//            dbConnection.close();
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//
//    }
//
//    public void SetModelTable() throws SQLException {
//        ResultSet resSet = null;
//        String select = "SELECT * FROM " + Const.MODELS_TABEL + " ORDER BY " + Const.MODELS_idMODEL;
//        try (PreparedStatement prSt = getDbConnection().prepareStatement(select)){
//
//            resSet = prSt.executeQuery();
//            IdControls.getInstance().SetModels(resSet);
//
//        } catch (SQLException | ClassNotFoundException throwables) {
//            throwables.printStackTrace();
//        }
//        try {
//            dbConnection.close();
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//
//    }
//
//    public void SetEngTypeTable() throws SQLException {
//        ResultSet resSet = null;
//        String select = "SELECT * FROM " + Const.ENGINE_TYPES_TABEL + " ORDER BY " + Const.ENGINE_TYPES_idTYPE;
//        try (PreparedStatement prSt = getDbConnection().prepareStatement(select)){
//
//            resSet = prSt.executeQuery();
//            IdControls.getInstance().SetEngineTypes(resSet);
//
//        } catch (SQLException | ClassNotFoundException throwables) {
//            throwables.printStackTrace();
//        }
//        try {
//            dbConnection.close();
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//
//    }
//
//    public void SetTransTable() throws SQLException {
//        ResultSet resSet = null;
//        String select = "SELECT * FROM " + Const.TRANSMISSIONS_TABEL + " ORDER BY " + Const.TRANSMISSIONS_idTRANSMISSION;
//        try (PreparedStatement prSt = getDbConnection().prepareStatement(select)){
//
//
//            resSet = prSt.executeQuery();
//            IdControls.getInstance().SetTransmissions(resSet);
//
//        } catch (SQLException | ClassNotFoundException throwables) {
//            throwables.printStackTrace();
//        }
//        try {
//            dbConnection.close();
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//
//    }
//
//    public void SetDriveTable() throws SQLException {
//        ResultSet resSet = null;
//        String select = "SELECT * FROM " + Const.DRIVES_TABEL + " ORDER BY " + Const.DRIVES_idDRIVE;
//        try (PreparedStatement prSt = getDbConnection().prepareStatement(select)){
//
//
//            resSet = prSt.executeQuery();
//            IdControls.getInstance().SetDrives(resSet);
//
//        } catch (SQLException | ClassNotFoundException throwables) {
//            throwables.printStackTrace();
//        }
//        try {
//            dbConnection.close();
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//
//    }
//
//
//    public void SetPaymentTable() throws SQLException {
//        ResultSet resSet = null;
//        String select = "SELECT * FROM " + Const.PAYMENT_TABLE + " ORDER BY " + Const.PAYMENT_idPAYMENT;
//        try (PreparedStatement prSt = getDbConnection().prepareStatement(select)){
//            resSet = prSt.executeQuery();
//            IdControls.getInstance().SetPayment(resSet);
//
//        } catch (SQLException | ClassNotFoundException throwables) {
//            throwables.printStackTrace();
//        }
//        try {
//            dbConnection.close();
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//
//    }
//
//    //////////////////////////////////////////////////////////////////////
//
//
//    public boolean AddCharacteristics(NewCharacteristics characteristics) throws SQLException {
//        boolean success = false;
//            if (!characteristics.getModel().equals("")) {
//                String addModel = "INSERT INTO " + Const.MODELS_TABEL + "(" +
//                        Const.MODELS_idBRAND + ", " +
//                        Const.MODELS_MODEL + ") " +
//                        "VALUES(?,?);";
//                try (PreparedStatement statement = getDbConnection().prepareStatement(addModel)){
//
//                statement.setInt(1, characteristics.getIdBrand());
//                statement.setString(2, characteristics.getModel());
//
//                statement.executeUpdate();
//                } catch (ClassNotFoundException e) {
//                    e.printStackTrace();
//                }
//            }
//            if (!characteristics.getBrand().equals("")) {
//                String addBrand = "INSERT INTO " + Const.BRANDS_TABEL + "(" +
//                        Const.BRANDS_BRAND + ") " +
//                        "VALUES(?);";
//
//                try (PreparedStatement statement = getDbConnection().prepareStatement(addBrand)){
//
//                    statement.setString(1, characteristics.getBrand());
//
//                statement.executeUpdate();
//                } catch (ClassNotFoundException e) {
//                    e.printStackTrace();
//                }
//            }
//            if (!characteristics.getDrive().equals("")) {
//                String addDrive = "INSERT INTO " + Const.DRIVES_TABEL + "(" +
//                        Const.DRIVES_DRIVE + ") " +
//                        "VALUES(?);";
//
//                try (PreparedStatement statement = getDbConnection().prepareStatement(addDrive)){
//
//                    statement.setString(1, characteristics.getDrive());
//
//                    statement.executeUpdate();
//
//                } catch (ClassNotFoundException e) {
//                    e.printStackTrace();
//                }
//
//            }
//            if (!characteristics.getTransmission().equals("")) {
//                String addTransmission = "INSERT INTO " + Const.TRANSMISSIONS_TABEL + "(" +
//                        Const.TRANSMISSIONS_TRANSMISSION + ") " +
//                        "VALUES(?);";
//
//                try (PreparedStatement statement = getDbConnection().prepareStatement(addTransmission)){
//                    statement.setString(1, characteristics.getTransmission());
//
//                    statement.executeUpdate();
//
//                } catch (ClassNotFoundException e) {
//                    e.printStackTrace();
//                }
//
//            }
//            if (!characteristics.getEngineType().equals("")) {
//                String addEngineType = "INSERT INTO " + Const.ENGINE_TYPES_TABEL + "(" +
//                        Const.ENGINE_TYPES_TYPE + ") " +
//                        "VALUES(?);";
//
//                try (PreparedStatement statement = getDbConnection().prepareStatement(addEngineType)){
//                    statement.setString(1, characteristics.getEngineType());
//
//                    statement.executeUpdate();
//                } catch (ClassNotFoundException e) {
//                    e.printStackTrace();
//                }
//
//            }
//            success = true;
//        try {
//            dbConnection.close();
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//
//        return success;
//    }
//
//    public boolean DeleteCharacteristics(NewCharacteristics characteristics) throws SQLException {
//        boolean success = false;
//        try {
//            PreparedStatement statement = null;
//            PreparedStatement statementGet = null;
//            PreparedStatement statementCheck = null;
//
//            if (!characteristics.getModel().equals("")) {
//                String getId = "SELECT * FROM "+ Const.MODELS_TABEL +
//                        " WHERE "+ Const.MODELS_MODEL + "=?;";
//                statementGet = getDbConnection().prepareStatement(getId);
//                statementGet.setString(1, characteristics.getModel());
//                int id = 0;
//                ResultSet resultSet = statementGet.executeQuery();
//                while (resultSet.next()){
//                    id = resultSet.getInt(Const.MODELS_idMODEL);
//                }
//
//                String getAuto = "SELECT * FROM "+ Const.AUTOS_TABEL +
//                        " WHERE "+ Const.AUTOS_MODEL + "=?;";
//                statementCheck = getDbConnection().prepareStatement(getAuto);
//                statementCheck.setInt(1, id);
//
//                ResultSet resultSet2 = statementCheck.executeQuery();
//
//                if(!resultSet2.next()) {
//
//                    String addModel = "DELETE FROM " + Const.MODELS_TABEL + " WHERE " +
//                            Const.MODELS_idBRAND + "=? && " +
//                            Const.MODELS_MODEL + "=?;";
//                    statement = getDbConnection().prepareStatement(addModel);
//
//
//                    statement.setInt(1, characteristics.getIdBrand());
//                    statement.setString(2, characteristics.getModel());
//
//                    statement.executeUpdate();
//                }
//                statement.close();
//                statementCheck.close();
//                statementGet.close();
//            }
//            if (!characteristics.getBrand().equals("")) {
//                String getId = "SELECT * FROM "+ Const.BRANDS_TABEL +
//                        " WHERE "+ Const.BRANDS_BRAND + "=?;";
//                statementGet = getDbConnection().prepareStatement(getId);
//                statementGet.setString(1, characteristics.getBrand());
//                int id = 0;
//                ResultSet resultSet = statementGet.executeQuery();
//                while (resultSet.next()){
//                    id = resultSet.getInt(Const.BRANDS_idBRAND);
//                }
//
//                String getAuto = "SELECT * FROM "+ Const.AUTOS_TABEL +
//                        " WHERE "+ Const.AUTOS_BRAND + "=?;";
//                statementCheck = getDbConnection().prepareStatement(getAuto);
//                statementCheck.setInt(1, id);
//
//                ResultSet resultSet2 = statementCheck.executeQuery();
//
//                if(!resultSet2.next()) {
//
//                    String addBrand = "DELETE FROM " + Const.BRANDS_TABEL + "WHERE " +
//                            Const.BRANDS_BRAND + "=?; ";
//
//                    statement = getDbConnection().prepareStatement(addBrand);
//
//                    statement.setString(1, characteristics.getBrand());
//
//                    statement.executeUpdate();
//                }
//                statement.close();
//                statementCheck.close();
//                statementGet.close();
//            }
//            if (!characteristics.getDrive().equals("")) {
//                String getId = "SELECT * FROM "+ Const.DRIVES_TABEL +
//                        " WHERE "+ Const.DRIVES_DRIVE + "=?;";
//                statementGet = getDbConnection().prepareStatement(getId);
//                statementGet.setString(1, characteristics.getDrive());
//                int id = 0;
//                ResultSet resultSet = statementGet.executeQuery();
//                while (resultSet.next()){
//                    id = resultSet.getInt(Const.DRIVES_idDRIVE);
//                }
//
//                String getAuto = "SELECT * FROM "+ Const.AUTOS_TABEL +
//                        " WHERE "+ Const.AUTOS_DRIVE + "=?;";
//                statementCheck = getDbConnection().prepareStatement(getAuto);
//                statementCheck.setInt(1, id);
//
//                ResultSet resultSet2 = statementCheck.executeQuery();
//
//                if(!resultSet2.next()) {
//
//                    String addDrive = "DELETE FROM " + Const.DRIVES_TABEL + "WHERE " +
//                            Const.DRIVES_DRIVE + "=?;";
//
//                    statement = getDbConnection().prepareStatement(addDrive);
//
//                    statement.setString(1, characteristics.getDrive());
//
//                    statement.executeUpdate();
//                }
//                statement.close();
//                statementCheck.close();
//                statementGet.close();
//            }
//            if (!characteristics.getTransmission().equals("")) {
//                String getId = "SELECT * FROM "+ Const.TRANSMISSIONS_TABEL +
//                        " WHERE "+ Const.TRANSMISSIONS_TRANSMISSION + "=?;";
//                statementGet = getDbConnection().prepareStatement(getId);
//                statementGet.setString(1, characteristics.getTransmission());
//                int id = 0;
//                ResultSet resultSet = statementGet.executeQuery();
//                while (resultSet.next()){
//                    id = resultSet.getInt(Const.TRANSMISSIONS_idTRANSMISSION);
//                }
//
//                String getAuto = "SELECT * FROM "+ Const.AUTOS_TABEL +
//                        " WHERE "+ Const.AUTOS_TRANSMISSION + "=?;";
//                statementCheck = getDbConnection().prepareStatement(getAuto);
//                statementCheck.setInt(1, id);
//
//                ResultSet resultSet2 = statementCheck.executeQuery();
//
//                if(!resultSet2.next()) {
//
//                    String addTransmission = "DELETE FROM " + Const.TRANSMISSIONS_TABEL + "WHERE " +
//                            Const.TRANSMISSIONS_TRANSMISSION + "=?;";
//
//                    statement = getDbConnection().prepareStatement(addTransmission);
//
//                    statement.setString(1, characteristics.getTransmission());
//
//                    statement.executeUpdate();
//                }
//                if (statement!=null)statement.close();
//                if (statementCheck!=null)statementCheck.close();
//                if (statementGet!=null)statementGet.close();
//            }
//            if (!characteristics.getEngineType().equals("")) {
//                String getId = "SELECT * FROM "+ Const.ENGINE_TYPES_TABEL +
//                        " WHERE "+ Const.ENGINE_TYPES_TYPE + "=?;";
//                statementGet = getDbConnection().prepareStatement(getId);
//                statementGet.setString(1, characteristics.getEngineType());
//                int id = 0;
//                ResultSet resultSet = statementGet.executeQuery();
//                while (resultSet.next()){
//                    id = resultSet.getInt(Const.ENGINE_TYPES_idTYPE);
//                }
//
//                String getAuto = "SELECT * FROM "+ Const.AUTOS_TABEL +
//                        " WHERE "+ Const.AUTOS_ENGINE_TYPE + "=?;";
//                statementCheck = getDbConnection().prepareStatement(getAuto);
//                statementCheck.setInt(1, id);
//
//                ResultSet resultSet2 = statementCheck.executeQuery();
//
//                if(!resultSet2.next()) {
//
//                    String addEngineType = "DELETE FROM " + Const.ENGINE_TYPES_TABEL + "WHERE " +
//                            Const.ENGINE_TYPES_TYPE + "=?;";
//
//                    statement = getDbConnection().prepareStatement(addEngineType);
//
//                    statement.setString(1, characteristics.getEngineType());
//
//                    statement.executeUpdate();
//                }
//                statement.close();
//                statementCheck.close();
//                statementGet.close();
//            }
//            success = true;
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//        try {
//            dbConnection.close();
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//
//        return success;
//    }
//
//    //////////////////////////////////////////////////////////////////////////
//
//    public boolean AddOrder(Order order) throws SQLException, ClassNotFoundException {
//        String add = "INSERT INTO " + Const.ORDERS_TABEL + "(" +
//                Const.ORDERS_BUYER + ", " +
//                Const.ORDERS_DATE + ", "+
//                Const.ORDERS_IDPAYMENT + ", " +
//                Const.ORDERS_idUSERS + ", " +
//                Const.ORDERS_VIN + ") " +
//                "VALUES (?,?,?,?,?);";
//
//        PreparedStatement statement = getDbConnection().prepareStatement(add);
//        statement.setString(1, order.getBuyer());
//        statement.setLong(2, new Date().getTime());
//        statement.setInt(3, order.getIdPayment());
//        statement.setInt(4, order.getIdusers());
//        statement.setInt(5, order.getVin());
//
//        statement.executeUpdate();
//
//        ///////////////////////////////////////////////////
//
//        String getNumber = "SELECT * FROM " + Const.AUTOS_TABEL +
//                " WHERE " + Const.AUTOS_VIN + "=?;";
//
//        PreparedStatement statementGetNumber = getDbConnection().prepareStatement(getNumber);
//        statementGetNumber.setInt(1, order.getVin());
//
//        ResultSet resultSet = statementGetNumber.executeQuery();
//        int number=0;
//        while (resultSet.next()){
//            number = resultSet.getInt(Const.AUTOS_NUMBER);
//        }
//
//
//        String update = "UPDATE " + Const.AUTOS_TABEL + " SET " +
//                Const.AUTOS_NUMBER + "=?" +
//                " WHERE " + Const.AUTOS_VIN + "=?;";
//
//        PreparedStatement statementUpdate = getDbConnection().prepareStatement(update);
//        statementUpdate.setInt(1, (number-1));
//        statementUpdate.setInt(2, order.getVin());
//
//        statementUpdate.executeUpdate();
//
//        statement.close();
//        statementGetNumber.close();
//        statementUpdate.close();
//        try {
//            dbConnection.close();
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//
//        return true;
//    }
//
//    public ArrayList<Order> GetOrders() throws SQLException, ClassNotFoundException {
//        ArrayList<Order> orders = new ArrayList<>();
//        String select = "SELECT * FROM " + Const.ORDERS_TABEL;
//
//        PreparedStatement statement = getDbConnection().prepareStatement(select);
//
//        ResultSet resultSet = statement.executeQuery();
//
//        while (resultSet.next()) {
//            orders.add(new Order(
//                    resultSet.getInt(Const.ORDERS_idORDERS),
//                    resultSet.getInt(Const.ORDERS_VIN),
//                    resultSet.getInt(Const.ORDERS_idUSERS),
//                    resultSet.getString(Const.ORDERS_BUYER),
//                    resultSet.getInt(Const.ORDERS_IDPAYMENT),
//                    new Date(resultSet.getLong(Const.ORDERS_DATE))));
//        }
//
//        statement.close();
//        try {
//            dbConnection.close();
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//
//        return orders;
//    }
//
//    public boolean DeleteOrder(int id) throws SQLException, ClassNotFoundException {
//        String getVin = "SELECT * FROM " + Const.ORDERS_TABEL +
//                " WHERE " + Const.ORDERS_idORDERS + "=?;";
//
//        PreparedStatement statementGetVin = getDbConnection().prepareStatement(getVin);
//        statementGetVin.setInt(1, id);
//
//        ResultSet resultSetVin = statementGetVin.executeQuery();
//        int vin=0;
//        while (resultSetVin.next()){
//            vin = resultSetVin.getInt(Const.ORDERS_VIN);
//        }
//
//
//        /////////////////////////////////////////////
//
//        String delete = "DELETE FROM " + Const.ORDERS_TABEL +
//                " WHERE "+ Const.ORDERS_idORDERS + "=?;";
//
//        PreparedStatement statement = getDbConnection().prepareStatement(delete);
//
//        statement.setInt(1, id);
//
//        statement.executeUpdate();
//
//        ///////////////////////////////////////////////////
//
//        String getNumber = "SELECT * FROM " + Const.AUTOS_TABEL +
//                " WHERE " + Const.AUTOS_VIN + "=?;";
//
//        PreparedStatement statementGetNumber = getDbConnection().prepareStatement(getNumber);
//        statementGetNumber.setInt(1, vin);
//
//        ResultSet resultSetNumber = statementGetNumber.executeQuery();
//        int number=0;
//        while (resultSetNumber.next()){
//            number = resultSetNumber.getInt(Const.AUTOS_NUMBER);
//        }
//        ////////////////////////////////////////////////////
//
//        String update = "UPDATE " + Const.AUTOS_TABEL + " SET " +
//                Const.AUTOS_NUMBER + "=?" +
//                " WHERE " + Const.AUTOS_VIN + "=?;";
//
//        PreparedStatement statementUpdate = getDbConnection().prepareStatement(update);
//        statementUpdate.setInt(1, (number+1));
//        statementUpdate.setInt(2, vin);
//
//        statementUpdate.executeUpdate();
//
//        statement.close();
//        statementGetNumber.close();
//        statementGetVin.close();
//        statementUpdate.close();
//        try {
//            dbConnection.close();
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//
//        return true;
//
//
//    }
//
//    public boolean EditOrder(int id, String names) throws SQLException, ClassNotFoundException {
//        String update = "UPDATE " + Const.ORDERS_TABEL + " SET " +
//                Const.ORDERS_BUYER + "=? " +
//                " WHERE " + Const.ORDERS_idORDERS +"=?;";
//
//        PreparedStatement statement = getDbConnection().prepareStatement(update);
//
//        statement.setString(1,names);
//        statement.setInt(2, id);
//
//        statement.executeUpdate();
//
//        statement.close();
//        try {
//            dbConnection.close();
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//
//        return true;
//    }
//}
