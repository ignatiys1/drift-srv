package COMMONS.services;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Participation extends  forDB{

    public int idStageParticipiants;
    public int stageId;
    public int driverId;
    public int serialNumber;
//    public DriverWithUser driver;
//    public StageWithCup stage;
    public boolean confirmed;

    public Participation(ResultSet resultSet) {
        try {
            idStageParticipiants = resultSet.getInt("idStageParticipiants");
            serialNumber = resultSet.getInt("serialNumber");
            stageId = resultSet.getInt("stageId");
            driverId = resultSet.getInt("driverId");
            confirmed = resultSet.getBoolean("confirmed");
//            driver = new DriverWithUser(resultSet);
//            stage = new StageWithCup(resultSet);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }


    public int getIdStageParticipiants() {
        return idStageParticipiants;
    }

    public void setIdStageParticipiants(int idStageParticipiants) {
        this.idStageParticipiants = idStageParticipiants;
    }
}
