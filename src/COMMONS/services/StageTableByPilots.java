package COMMONS.services;

import java.sql.ResultSet;

public class StageTableByPilots extends forDB {

    protected int idstage;
    protected String stageName;
    protected int iddrivers;
    protected String FIO;
    protected String nickname;
    protected String country;
    protected int score;

    public StageTableByPilots(ResultSet resSet){
        setFieldsByResSet(resSet);
    }


}
