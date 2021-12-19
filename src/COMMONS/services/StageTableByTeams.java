package COMMONS.services;

import java.sql.ResultSet;

public class StageTableByTeams extends forDB {

    protected int idstage;
    protected String stageName;
    protected int idteams;
    protected String teamName;
    protected String country;
    protected int score;


    public StageTableByTeams(ResultSet resSet){
        setFieldsByResSet(resSet);
    }


}
