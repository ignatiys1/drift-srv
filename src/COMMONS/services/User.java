package COMMONS.services;

import java.io.PrintWriter;
import java.sql.ResultSet;
import java.util.Scanner;


public class User extends forDB {
    protected int iduser;
    protected String login;
    protected String password;
    protected String firstName;
    protected String lastName;
    protected String type;
    protected int driverId;

    public User(){
        iduser = 0;
    }
    public User(ResultSet resSet){
         setFieldsByResSet(resSet);
        if (firstName == null) {firstName = "";}
        if (lastName == null) {lastName = "";}
        if (type == null) {type = "";}

        login = "";
        password = "";
    }
    public User(ResultSet resSet, Boolean withpass){
        setFieldsByResSet(resSet);
    }

    public User(int iduser, String login, String password, String firstName, String lastName, String type) {
        this.iduser = iduser;
        this.login = login;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.type = type;
    }
    public User(String login, String password, String firstName, String lastName, String type) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.type = type;
    }

    public int getIdUser() {
        return iduser;
    }
    public void setIdUser(int iduser) {
        this.iduser = iduser;
    }

    public String getLogin() {
        return login;
    }
    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Для передачи через сокет
     * @return
     */


}
