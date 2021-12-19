package COMMONS.services;

import java.sql.Date;
import java.sql.ResultSet;

public class Stage extends forDB {


    protected int idstage;
    protected String stageName;
    protected String place;
    protected String city;
    protected int cupId;
    protected int firstGrid;
    protected int firstJudgeId;
    protected int secondJudgeId;
    protected int thirdJudgeId;
    protected int status;

    protected Date date;
    protected int daysDuration;

    public Stage() {
    }
    public Stage(ResultSet resSet){
        setFieldsByResSet(resSet);
    }
    public Stage(int idstage, String name, String place, String city, int cupId, int firstGrid, int firstJudgeId, int secondJudgeId, int thirdJudgeId) {
        this.idstage = idstage;
        this.stageName = name;
        this.place = place;
        this.city = city;
        this.cupId = cupId;
        this.firstGrid = firstGrid;
        this.firstJudgeId = firstJudgeId;
        this.secondJudgeId = secondJudgeId;
        this.thirdJudgeId = thirdJudgeId;
    }
    public Stage(String name, String place, String city, int cupId, int firstGrid, int firstJudgeId, int secondJudgeId, int thirdJudgeId) {
        this.idstage = idstage;
        this.stageName = name;
        this.place = place;
        this.city = city;
        this.cupId = cupId;
        this.firstGrid = firstGrid;
        this.firstJudgeId = firstJudgeId;
        this.secondJudgeId = secondJudgeId;
        this.thirdJudgeId = thirdJudgeId;
    }

    public int getIdstage() {
        return idstage;
    }
    public void setIdstage(int idstage) {
        this.idstage = idstage;
    }

    public String getName() {
        return stageName;
    }
    public void setName(String name) {
        this.stageName = name;
    }

    public String getPlace() {
        return place;
    }
    public void setPlace(String place) {
        this.place = place;
    }

    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }

    public int getCupId() {
        return cupId;
    }
    public void setCupId(int cupId) {
        this.cupId = cupId;
    }

    public int getFirstGrid() {
        return firstGrid;
    }
    public void setFirstGrid(int firstGrid) {
        this.firstGrid = firstGrid;
    }

    public int getFirstJudgeId() {
        return firstJudgeId;
    }
    public void setFirstJudgeId(int firstJudgeId) {
        this.firstJudgeId = firstJudgeId;
    }

    public int getSecondJudgeId() {
        return secondJudgeId;
    }
    public void setSecondJudgeId(int secondJudgeId) {
        this.secondJudgeId = secondJudgeId;
    }

    public int getThirdJudgeId() {
        return thirdJudgeId;
    }
    public void setThirdJudgeId(int thirdJudgeId) {
        this.thirdJudgeId = thirdJudgeId;
    }

    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }

    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }

    public int getDaysDuration() {
        return daysDuration;
    }
    public void setDaysDuration(int daysDuration) {
        this.daysDuration = daysDuration;
    }
}
