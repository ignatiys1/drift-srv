package COMMONS.services;

import java.sql.ResultSet;

public class DriverWithUser extends Driver{

    protected User user;

    public DriverWithUser(ResultSet resSet) {
        setFieldsByResSet(resSet);
    }

}
