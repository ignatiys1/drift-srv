package COMMONS.services;

import java.sql.ResultSet;

public class CupTableByPilots extends forDB {

    protected int idcup;
    protected String cupName;
    protected int iddriver;
    protected String FIO;
    protected String nickname;
    protected String country;
    protected int score;

    public CupTableByPilots(ResultSet resSet){
        setFieldsByResSet(resSet);
    }


}
