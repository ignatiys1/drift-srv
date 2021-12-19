package COMMONS.phones;

public class ConstsForSend {

    //system commands 0-99
    public static final int COMMAND_NULL = 0;

    //work with USER - 100-199
    public static final int COMMAND_USER_EXIST = 101;
    public static final int COMMAND_USER_SIGNED = 102;
    public static final int COMMAND_USER_NOT_EXIST = 103;
    public static final int COMMAND_LOGIN_EXIST = 104;
    public static final int COMMAND_INCORRECT_USER = 105;
    public static final int COMMAND_USER_OF_SESSION = 106;

    //work with CUP - 200-299
    public static final int COMMAND_CUPS_EMPTY =201;
    public static final int COMMAND_WITH_CUPS = 202;
    public static final int COMMAND_ERROR_CUP_SAVING = 203;
    public static final int COMMAND_CUP_SAVING_SUCCESS = 204;
    public static final int COMMAND_WITH_STAGES = 205;
    public static final int COMMAND_STAGES_EMPTY = 206;

    //work with STAGE - 300-399
    public static final int COMMAND_ERROR_GETTIN_GRID = 300;
    public static final int COMMAND_WITH_STAGE_GRID = 301;
    public static final int COMMAND_STAGE_SAVED = 302;
    public static final int COMMAND_STAGE_NOT_SAVED = 303;
    public static final int COMMAND_NO_STAGE_PARICIPIANTS = 304;
    public static final int COMMAND_WITH_STAGE_PARICIPIANTS = 305;
    public static final int COMMAND_PARICIPIANTS_SAVED = 306;
    public static final int COMMAND_PARICIPIANTS_NOT_SAVED = 307;
    public static final int COMMAND_STAGE_RUNS = 308;
    public static final int COMMAND_NO_STAGE_RUNS = 309;

    public static final int COMMAND_STAGE_NEW_STATUS = 321;

    public static final int COMMAND_STAGE_RUNS_STATUS_UPDATED = 341;
    public static final int COMMAND_STAGE_RUNS_STATUS_NOT_UPDATED = 342;

    //work with ONLINE - 400-499
    public static final int COMMAND_NOTHING_ONLINE = 401;
    public static final int COMMAND_ONLINE_STAGE = 402;
    public static final int COMMAND_ONLINE_RUN = 403;
    public static final int COMMAND_ONLINE_JUDGE_DECISION = 404;

    //work with JUDGES - 500-599
    public static final int COMMAND_WITH_ALL_JUDGES = 501;
    public static final int COMMAND_NO_JUDGES = 502;

    //work with DRIVERS - 600-699
    public static final int COMMAND_WITH_ALL_DRIVERS = 601;
    public static final int COMMAND_DRIVER_SAVED = 602;
    public static final int COMMAND_DRIVER_SAVING_ERROR = 603;


}
