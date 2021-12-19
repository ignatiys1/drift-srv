package COMMONS.services;

import java.lang.reflect.Field;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class forDB {

    public forDB() {
    }

    public forDB(ResultSet resSet) {
        setFieldsByResSet(resSet);
    }

    public forDB setFieldsByResSet(ResultSet resSet) {
        Field[] fields = this.getClass().getDeclaredFields();

        SetFields(fields, resSet);
        if (this.getClass().getSuperclass() != null) {
            Field[] superFields = this.getClass().getSuperclass().getDeclaredFields();
            SetFields(superFields, resSet);
        }

        return this;
    }

    public String getInsertQuery() {
        String query = "INSERT INTO " + this.getClass().toString().split("\\.")[this.getClass().toString().split("\\.").length-1].toLowerCase() + " (";
        String values = "VALUES(";

        Field[] fields = this.getClass().getDeclaredFields();

        for (Field field : fields) {

            if (field.getName().substring(0, 2).equalsIgnoreCase("id")) {
                continue;
            }

            Class fieldType = field.getType();
            //query += field.getName() + ",";

            try {
                if (fieldType == String.class) {
                    values += "\"" + field.get(this) + "\",";
                    query += field.getName() + ",";

                }
                if (fieldType.getName() == "int") {
                    values += field.getInt(this) + ",";
                    query += field.getName() + ",";
                }
                if (fieldType == Date.class) {
                    values += "\"" + field.get(this) + "\",";
                    query += field.getName() + ",";
                }
                if (fieldType.getName() == "boolean") {
                    if (field.getBoolean(this)) {
                        values += "TRUE,";
                    } else {
                        values += "FALSE,";
                    }
                    query += field.getName() + ",";
                }
            } catch (IllegalAccessException e) {
                values+=",";
                e.printStackTrace();
            }

        }
        query = query.substring(0, query.length() - 1) + ") ";
        values = values.substring(0, values.length() - 1) + ")";

        return query + values;
    }

    public String getUpdateQuery() {
        String query = "UPDATE " + this.getClass().toString().split("\\.")[this.getClass().toString().split("\\.").length-1].toLowerCase() + " SET ";
        String where = " WHERE ";
        Field[] fields = this.getClass().getDeclaredFields();

        for (Field field : fields) {

            if (field.getName().substring(0, 2).equalsIgnoreCase("id")) {
                try {
                    where += field.getName() + " = " +field.getInt(this);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                continue;
            }

            Class fieldType = field.getType();

            try {
                if (fieldType == String.class) {
                    query += field.getName() + " = ";
                    query += "\"" + field.get(this) + "\",";
                }
                if (fieldType == Date.class) {
                    query += field.getName() + " = ";
                    query += "\"" + field.get(this) + "\",";
                }
                if (fieldType.getName() == "int") {
                    query += field.getName() + " = ";
                    query += field.getInt(this) + ",";
                }
                if (fieldType.getName() == "boolean") {
                    query += field.getName() + " = ";
                    if (field.getBoolean(this)) {
                        query += "TRUE,";
                    } else {
                        query += "FALSE,";
                    }
                }
            } catch (IllegalAccessException e) {
                query+=",";
                e.printStackTrace();
            }

        }
        query = query.substring(0, query.length() - 1) + where;

        return query;
    }

    public String getFullSelectQuery(){
        String query = "SELECT * From " + this.getClass().toString().split("\\.")[this.getClass().toString().split("\\.").length-1].toLowerCase();

        return query;
    }

    public String getSelectByIdQuery() {
        String query = "SELECT * From " + this.getClass().toString().split("\\.")[this.getClass().toString().split("\\.").length - 1].toLowerCase() + " WHERE ";
        Field[] fields = this.getClass().getDeclaredFields();

        for (Field field : fields) {

            if (field.getName().substring(0, 2).equalsIgnoreCase("id")) {
                try {
                    query += field.getName() + " = " + field.getInt(this);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return query;
    }

    public String getTableName(){
        return this.getClass().toString().split("\\.")[this.getClass().toString().split("\\.").length - 1].toLowerCase();
    }

    private void SetFields(Field[] fields, ResultSet resSet) {
        for (Field field : fields) {
            Class fieldType = field.getType();
            try {
                if (fieldType == String.class) {
                    field.set(this, resSet.getString(field.getName()));
                }
                if (fieldType.getName() == "int") {
                    field.set(this, resSet.getInt(field.getName()));
                }
                if (fieldType.getName() == "boolean") {
                    field.set(this, resSet.getBoolean(field.getName()));
                }
                if (fieldType == Date.class) {
                    field.set(this, resSet.getDate(field.getName()));
                }
                if (fieldType == Cup.class) {
                    field.set(this, new Cup(resSet));
                }
                if (fieldType == User.class) {
                    field.set(this, new User(resSet));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        }
    }
}
