package COMMONS.phones;

import COMMONS.services.User;

public class ConstFromClient {

    //system commands 0-99
    public static final int COMMAND_END_SESSION = 999;
    public static final int COMMAND_NULL = 0;


    //work with USER - 100-199
    public static final int COMMAND_CHECK_USER = 101;
    public static final int COMMAND_SIGN_USER = 102;
    public static final int COMMAND_GET_FULL_USER = 103;

    //work with CUP - 200-299
    public static final int COMMAND_GET_CUPS = 201;
    public static final int COMMAND_GET_CUPS_FOR_USER = 202;
    public static final int COMMAND_GET_ONE_CUP_INFO = 203;
    public static final int COMMAND_SAVE_CUP = 204;

    //work with STAGE - 300-399
    public static final int COMMAND_GET_STAGE_GRID = 301;
    public static final int COMMAND_GET_ONE_STAGE_INFO = 302;
    public static final int COMMAND_SAVE_STAGE = 303;
    public static final int COMMAND_GET_STAGE_PARICIPIANTS = 304;
    public static final int COMMAND_SAVE_PARICIPIANTS = 305;
    public static final int COMMAND_GET_STAGE_RUNS = 306;


    //STAGE STATUSES
    public static final int COMMAND_SET_STAGE_STATUS = 320;

    public static final int COMMAND_SET_RUN_STATUS = 340;

    //work with ONLINE - 400-499
    public static final int COMMAND_GET_ONLINE = 401;
    public static final int COMMAND_ADD_JUDGE_DECISION = 402;


    //work with JUDGES - 500-599
    public static final int COMMAND_GET_ALL_JUDGES = 501;

    //work with DRIVERS - 600-699
    public static final int COMMAND_GET_ALL_DRIVERS = 601;
    public static final int COMMAND_SAVE_DRIVER = 602;

}
