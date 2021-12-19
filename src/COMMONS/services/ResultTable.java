package COMMONS.services;

import java.sql.ResultSet;

public class ResultTable extends forDB {

    protected int idresultTable;
    protected int driverId;
    protected int cupId;
    protected int stageId;
    protected int teamId;
    protected int score;

    public ResultTable() {
    }
    public ResultTable(ResultSet resSet){
        setFieldsByResSet(resSet);
    }
    public ResultTable(int idresultTable, int driverId, int cupId, int stageId, int teamId, int score) {
        this.idresultTable = idresultTable;
        this.driverId = driverId;
        this.cupId = cupId;
        this.stageId = stageId;
        this.teamId = teamId;
        this.score = score;
    }
    public ResultTable(int driverId, int cupId, int stageId, int teamId, int score) {
        this.driverId = driverId;
        this.cupId = cupId;
        this.stageId = stageId;
        this.teamId = teamId;
        this.score = score;
    }

    public int getIdresultTable() {
        return idresultTable;
    }
    public void setIdresultTable(int idresultTable) {
        this.idresultTable = idresultTable;
    }

    public int getDriverId() {
        return driverId;
    }
    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public int getCupId() {
        return cupId;
    }
    public void setCupId(int cupId) {
        this.cupId = cupId;
    }

    public int getStageId() {
        return stageId;
    }
    public void setStageId(int stageId) {
        this.stageId = stageId;
    }

    public int getTeamId() {
        return teamId;
    }
    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public int getScore() {
        return score;
    }
    public void setScore(int score) {
        this.score = score;
    }
}
