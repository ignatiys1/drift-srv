package COMMONS.services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class GridWithDrivers {

    private int stageId;
   // private ArrayList<Map<Integer,Driver>>[] grids = new ArrayList[6];

    public Map<Integer,Driver>[] grids = new Map[6];

    public GridWithDrivers(int stageId) {
        this.stageId = stageId;


    }

    public int getStageId() {
        return stageId;
    }

    public void setStageId(int stageId) {
        this.stageId = stageId;
    }

    public GridWithDrivers(ResultSet resultSet, int stageId) {
        this.stageId = stageId;

        Map<Integer,Driver> top32 = new HashMap<>();
        Map<Integer,Driver> top16 = new HashMap<>();
        Map<Integer,Driver> top8 = new HashMap<>();
        Map<Integer,Driver> top4 = new HashMap<>();
        Map<Integer,Driver> top2 = new HashMap<>();
        Map<Integer,Driver> on3 = new HashMap<>();

        while (true) {
            try {
                if (!resultSet.next()) break;

                switch (resultSet.getInt("type")) {
                    case 32:
                        top32.put(resultSet.getInt("position"),new Driver(resultSet));
                        break;
                    case 16:
                        top16.put(resultSet.getInt("position"),new Driver(resultSet));
                        break;
                    case 8:
                        top8.put(resultSet.getInt("position"),new Driver(resultSet));
                        break;
                    case 4:
                        top4.put(resultSet.getInt("position"),new Driver(resultSet));
                        break;
                    case 2:
                        top2.put(resultSet.getInt("position"),new Driver(resultSet));
                        break;
                    case 3:
                        on3.put(resultSet.getInt("position"),new Driver(resultSet));
                        break;
                }

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        grids[0] = top32;
        grids[1] = top16;
        grids[2] = top8;
        grids[3] = top4;
        grids[4] = top2;
        grids[5] = on3;
    }
}
