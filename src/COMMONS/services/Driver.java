package COMMONS.services;

import java.sql.ResultSet;

public class Driver extends forDB {

    protected int iddriver;
    protected String FIO;
    protected String nickname;
    protected String car;
    protected String country;
    protected String city;

    public Driver() {
    }
    public Driver(ResultSet resSet){
        setFieldsByResSet(resSet);
    }
    public Driver(int iddriver, String FIO, String nickname, String car, String country, String city) {
        this.iddriver = iddriver;
        this.FIO = FIO;
        this.nickname = nickname;
        this.car = car;
        this.country = country;
        this.city = city;
    }
    public Driver( String FIO, String nickname, String car, String country, String city) {
        this.FIO = FIO;
        this.nickname = nickname;
        this.car = car;
        this.country = country;
        this.city = city;
    }

    public int getIddriver() {
        return iddriver;
    }
    public void setIddriver(int iddriver) {
        this.iddriver = iddriver;
    }

    public String getFIO() {
        return FIO;
    }
    public void setFIO(String FIO) {
        this.FIO = FIO;
    }

    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getCar() {
        return car;
    }
    public void setCar(String car) {
        this.car = car;
    }

    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }

}
