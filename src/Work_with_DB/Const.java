package Work_with_DB;

public class Const {
    public static final String USERS_TABEL = "user";
    public static final String USERS_ID = "iduser";
    public static final String USERS_LOGIN = "login";
    public static final String USERS_PASSWORD = "password";
    public static final String USERS_FIRSTNAME = "firstName";
    public static final String USERS_LASTNAME = "lastName";
    public static final String USERS_TYPE = "type";
    public static final String USERS_DRIVER = "driverId";

    public static final String CUPS_TABEL = "cup";
    public static final String CUPS_ID = "idcup";
    public static final String CUPS_NAME = "name";
    public static final String CUPS_YEAR = "year";
    public static final String CUPS_ORGANIZER = "organizerId";


    public static final String DRIVERS_TABEL = "driver";
    public static final String DRIVERS_ID = "iddriver";
    public static final String DRIVERS_FIO = "FIO";
    public static final String DRIVERS_NICKNAME = "nickname";
    public static final String DRIVERS_CAR = "car";
    public static final String DRIVERS_COUNTRY = "country";
    public static final String DRIVERS_CITY = "city";

    public static final String GRIDS_TABEL = "grid";
    public static final String GRIDS_ID = "idgrid";
    public static final String GRIDS_STAGE = "stageId";
    public static final String GRIDS_DRIVER = "driverId";
    public static final String GRIDS_POSITION = "position";
    public static final String GRIDS_TYPE = "type";

    public static final String RESULTTABLE_TABEL = "resulttable";
    public static final String RESULTTABLE_ID = "idresultTable";
    public static final String RESULTTABLE_DRIVER = "driverId";
    public static final String RESULTTABLE_CUP = "cupId";
    public static final String RESULTTABLE_STAGE = "stageId";
    public static final String RESULTTABLE_TEAM = "teamId";
    public static final String RESULTTABLE_SCORE = "score";

    public static final String STAGE_TABEL = "stage";
    public static final String STAGE_ID = "idstage";
    public static final String STAGE_NAME = "name";
    public static final String STAGE_PLACE = "place";
    public static final String STAGE_CITY = "city";
    public static final String STAGE_CUP = "cupId";
    public static final String STAGE_STATUS = "status";
    public static final String STAGE_FIRSTGRID = "firstGrid";
    public static final String STAGE_FIRSTJUDGE = "firstJudgeId";
    public static final String STAGE_SECONDJUDGE = "secondJudgeId";
    public static final String STAGE_THIRDJUDGE = "thirdJudgeId";

    public static final String STAGETABLETEMP_TABEL = "stagetabletemp";
    public static final String STAGETABLETEMP_ID = "idstagetable";
    public static final String STAGETABLETEMP_STAGE = "stageId";
    public static final String STAGETABLETEMP_FIRSTDRIVER = "firstDriverId";
    public static final String STAGETABLETEMP_QUAL = "qual";
    public static final String STAGETABLETEMP_SECONDDRIVER = "secondDriverId";
    public static final String STAGETABLETEMP_SCORE = "score";
    public static final String STAGETABLETEMP_WINNER = "winnerId";
    public static final String STAGETABLETEMP_FIRSTJUDGESOLUTION = "firstJudgeSolution";
    public static final String STAGETABLETEMP_SECONDJUDGESOLUTION = "secondJudgeSolution";
    public static final String STAGETABLETEMP_THIRDJUDGESOLUTION = "thirdJudgeSolution";
    public static final String STAGETABLETEMP_ATTEMPT = "attempt";
    public static final String STAGETABLETEMP_STATUS = "status";
    public static final String STAGETABLETEMP_NUM = "num";

    public static final String TEAMS_TABEL = "team";
    public static final String TEAMS_ID = "idteam";
    public static final String TEAMS_NAME = "name";
    public static final String TEAMS_COUNTRY = "country";
    public static final String TEAMS_MANAGER = "manager";

    public static final String PARTICIOIANTS_TABLE = "participation";
    public static final String PARTICIOIANTS_ID = "idStageParticipiants";
    public static final String PARTICIOIANTS_STAGE = "stageId";
    public static final String PARTICIOIANTS_DRIVER = "driverId";
    public static final String PARTICIOIANTS_CONFIRMED = "confirmed";
    public static final String PARTICIOIANTS_SERIALNUMBER = "serialNumber";
}
