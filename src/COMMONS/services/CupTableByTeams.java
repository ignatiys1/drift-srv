package COMMONS.services;

import java.sql.ResultSet;

public class CupTableByTeams extends forDB {

    protected int idcup;
    protected String cupName;
    protected int idteam;
    protected String teamName;
    protected String country;
    protected int score;


    public CupTableByTeams(ResultSet resSet){
        setFieldsByResSet(resSet);
    }


}
