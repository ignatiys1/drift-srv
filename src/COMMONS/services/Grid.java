package COMMONS.services;

import java.sql.ResultSet;

public class Grid extends forDB {

    protected int idgrid;
    protected int stageId;
    protected int driverId;
    protected int position;
    protected int type;

    public Grid() {
    }
    public Grid(ResultSet resSet){
        setFieldsByResSet(resSet);
    }
    public Grid(int idgrid, int stageId, int driverId, int position, int type) {
        this.idgrid = idgrid;
        this.stageId = stageId;
        this.driverId = driverId;
        this.position = position;
        this.type = type;
    }
    public Grid(int stageId, int driverId, int position, int type) {
        this.stageId = stageId;
        this.driverId = driverId;
        this.position = position;
        this.type = type;
    }


    public int getIdgrid() {
        return idgrid;
    }
    public void setIdgrid(int idgrid) {
        this.idgrid = idgrid;
    }

    public int getStageId() {
        return stageId;
    }
    public void setStageId(int stageId) {
        this.stageId = stageId;
    }

    public int getDriverId() {
        return driverId;
    }
    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public int getPosition() {
        return position;
    }
    public void setPosition(int position) {
        this.position = position;
    }

    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
}
