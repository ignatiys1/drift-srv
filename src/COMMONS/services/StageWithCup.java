package COMMONS.services;

import java.lang.reflect.Field;
import java.sql.Date;
import java.sql.ResultSet;

public class StageWithCup extends Stage {

    protected Cup cup;

    public StageWithCup(ResultSet resSet) {
        setFieldsByResSet(resSet);
    }

    public Cup getCup() {
        return cup;
    }
    public void setCup(Cup cup) {
        this.cup = cup;
    }

    private Field[] getFields() {
        return this.getClass().getDeclaredFields();
    }
}
