package COMMONS.services;

import java.sql.ResultSet;

public class Cup extends forDB {

    protected int idcup;
    protected String cupName;
    protected int year;
    protected int organizerId;

    public Cup(){
        super();
    }
    public Cup(ResultSet resSet){
        setFieldsByResSet(resSet);
    }
    public Cup(String name, int year, int organizerId) {
        this();
        this.idcup = idcup;
        this.cupName = name;
        this.year = year;
        this.organizerId = organizerId;
    }

    public int getIdcup() {
        return idcup;
    }
    public void setIdcup(int idcup) {
        this.idcup = idcup;
    }

    public String getName() {
        return cupName;
    }
    public void setName(String name) {
        this.cupName = name;
    }

    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
    }

    public int getOrganizerId() {
        return organizerId;
    }
    public void setOrganizerId(int organizerId) {
        this.organizerId = organizerId;
    }
}
