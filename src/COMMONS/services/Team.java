package COMMONS.services;

import java.sql.ResultSet;

public class Team extends forDB {

    protected int idteam;
    protected String name;
    protected String country;
    protected String manager;

    public Team() {
    }
    public Team(ResultSet resSet){
        setFieldsByResSet(resSet);
    }
    public Team(int idteam, String name, String country, String manager) {
        this.idteam = idteam;
        this.name = name;
        this.country = country;
        this.manager = manager;
    }
    public Team(String name, String country, String manager) {
        this.name = name;
        this.country = country;
        this.manager = manager;
    }

    public int getIdteam() {
        return idteam;
    }
    public void setIdteam(int idteam) {
        this.idteam = idteam;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }

    public String getManager() {
        return manager;
    }
    public void setManager(String manager) {
        this.manager = manager;
    }

}
