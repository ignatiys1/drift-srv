package COMMONS.phones;


import COMMONS.commands.DifObjectTypes;
import COMMONS.json.JsonMessage;
import COMMONS.services.*;
import Work_with_DB.Const;
import Work_with_DB.DataBaseHandler;
import com.google.gson.*;

import javax.xml.crypto.Data;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static java.util.Comparator.comparing;

public class Adapter {

    private static Adapter adapter;
    private static final Gson GSON = new GsonBuilder().setDateFormat("dd-MM-yyyy HH:mm:ss").create();

    public static synchronized Adapter getInstance() {
        if (adapter == null) {
                adapter = new Adapter();
        }
        return adapter;
    }

    private Adapter() {

    }

    public void processRequest(String jsonRequest, ClientHandler client, int COMMAND_ERROR) throws SQLException {
        JsonMessage jsonAnswer = new JsonMessage();
        jsonAnswer.user = client.getThisUser();
        jsonAnswer.COMMAND = ConstsForSend.COMMAND_NULL;


        if (COMMAND_ERROR != 0){

            switch (COMMAND_ERROR) {
                case 340:
                    jsonAnswer.COMMAND = ConstsForSend.COMMAND_STAGE_RUNS_STATUS_NOT_UPDATED;
            }

            client.sendMsg(jsonAnswer.getStringAnswer());
            return;
        }

        JsonMessage jsonMSG = GSON.fromJson(jsonRequest, JsonMessage.class);



        if (jsonMSG.COMMAND == ConstFromClient.COMMAND_NULL){
            client.sendMsg(jsonAnswer.getStringAnswer());
            return;
        }

        switch (jsonMSG.COMMAND) {
            case ConstFromClient.COMMAND_END_SESSION:
                client.close();
                return;
            case ConstFromClient.COMMAND_CHECK_USER:
                User userForCheck = GSON.fromJson(jsonMSG.getObject(), User.class);
                userForCheck = DataBaseHandler.getInstance().CheckUserInDB(userForCheck.getLogin(), userForCheck.getPassword());
                if (userForCheck != null) {
                    client.setThisUser(userForCheck);
                    jsonAnswer.COMMAND = ConstsForSend.COMMAND_USER_EXIST;
                    jsonAnswer.object = GSON.toJson(userForCheck);
                } else {
                    jsonAnswer.COMMAND = ConstsForSend.COMMAND_INCORRECT_USER;
                }
                break;
            case ConstFromClient.COMMAND_SIGN_USER:
                User userForSign = GSON.fromJson(jsonMSG.getObject(), User.class);
                userForSign = DataBaseHandler.getInstance().SignUpUser(userForSign);
                if (userForSign != null) {
                    client.setThisUser(userForSign);
                    jsonAnswer.COMMAND = ConstsForSend.COMMAND_USER_SIGNED;
                    jsonAnswer.object = GSON.toJson(userForSign);
                } else {
                    jsonAnswer.COMMAND = ConstsForSend.COMMAND_LOGIN_EXIST;
                }
                break;
            case ConstFromClient.COMMAND_GET_CUPS:
                if (client.getThisUser()==null){
                    client.setThisUser(jsonMSG.user);
                }
                ArrayList<Cup> cups = DataBaseHandler.getInstance().GetCups();
                if (cups.size()==0) {
                    jsonAnswer.COMMAND = ConstsForSend.COMMAND_CUPS_EMPTY;
                } else {
                    jsonAnswer.COMMAND = ConstsForSend.COMMAND_WITH_CUPS;
                    jsonAnswer.object = GSON.toJson(cups);
                }
                break;
            case ConstFromClient.COMMAND_GET_CUPS_FOR_USER:
                client.setThisUser(jsonMSG.user);
                ArrayList<Cup> cupsForUser = DataBaseHandler.getInstance().GetCups(client.getThisUser());
                if (cupsForUser.size()==0) {
                    jsonAnswer.COMMAND = ConstsForSend.COMMAND_CUPS_EMPTY;
                } else {
                    jsonAnswer.COMMAND = ConstsForSend.COMMAND_WITH_CUPS;
                    jsonAnswer.object = GSON.toJson(cupsForUser);
                }
                break;
            case ConstFromClient.COMMAND_GET_ONE_CUP_INFO:
                ArrayList<Stage> stages = DataBaseHandler.getInstance().GetStage(GSON.fromJson(jsonMSG.getObject(),Cup.class));
                if (stages.size() == 0) {
                    jsonAnswer.COMMAND = ConstsForSend.COMMAND_STAGES_EMPTY;
                } else {
                    jsonAnswer.COMMAND = ConstsForSend.COMMAND_WITH_STAGES;
                    jsonAnswer.object = GSON.toJson(stages);
                }
                break;
            case ConstFromClient.COMMAND_SAVE_CUP:
                Cup cupForSaving = GSON.fromJson(jsonMSG.getObject(), Cup.class);
                jsonAnswer.object ="";
                if (cupForSaving != null) {
                    if (DataBaseHandler.getInstance().SaveCup(cupForSaving)) {
                        jsonAnswer.COMMAND = ConstsForSend.COMMAND_CUP_SAVING_SUCCESS;
                    } else {
                        jsonAnswer.COMMAND = ConstsForSend.COMMAND_ERROR_CUP_SAVING;
                    }
                } else {
                    jsonAnswer.COMMAND = ConstsForSend.COMMAND_ERROR_CUP_SAVING;
                }
                break;
            case ConstFromClient.COMMAND_GET_STAGE_GRID:
                Stage stage = GSON.fromJson(jsonMSG.getObject(), Stage.class);
                jsonAnswer.object ="";
                if (stage != null) {
                    GridWithDrivers grid = DataBaseHandler.getInstance().GetGridForStage(stage);
                    if (grid != null) {
                        jsonAnswer.COMMAND = ConstsForSend.COMMAND_WITH_STAGE_GRID;
                        jsonAnswer.object = GSON.toJson(grid);
                    } else {
                        jsonAnswer.COMMAND = ConstsForSend.COMMAND_ERROR_GETTIN_GRID;
                    }
                } else {
                    jsonAnswer.COMMAND = ConstsForSend.COMMAND_ERROR_GETTIN_GRID;
                }
                break;
            case ConstFromClient.COMMAND_GET_ONLINE:
                StageWithCup onlineStage = DataBaseHandler.getInstance().GetOnlineStage();
                if (onlineStage == null) {
                    jsonAnswer.COMMAND = ConstsForSend.COMMAND_NOTHING_ONLINE;
                } else {
                    jsonAnswer.COMMAND = ConstsForSend.COMMAND_ONLINE_STAGE;
                    jsonAnswer.object = GSON.toJson(onlineStage);

                    StageTableTemp run = DataBaseHandler.getInstance().GetRunWithStatus(onlineStage.getIdstage(), DifObjectTypes.RUN_STATUS_NOW);
                    if (run == null){
                        run = DataBaseHandler.getInstance().GetRunWithStatus(onlineStage.getIdstage(), DifObjectTypes.RUN_STATUS_LATEST);
                    }
                    if (run != null) {
                        jsonAnswer.user = client.getThisUser();
                        client.sendMsg(jsonAnswer.getStringAnswer());

                        run.setFirstJudge(DataBaseHandler.getInstance().GetUserById(onlineStage.getFirstJudgeId(), false));
                        run.setSecondJudge(DataBaseHandler.getInstance().GetUserById(onlineStage.getSecondJudgeId(), false));
                        run.setThirdJudge(DataBaseHandler.getInstance().GetUserById(onlineStage.getThirdJudgeId(), false));

                        run.setFirstDriver(DataBaseHandler.getInstance().GetDriverById(run.getFirstDriverId()));
                        run.setSecondDriver(DataBaseHandler.getInstance().GetDriverById(run.getSecondDriverId()));

                        jsonAnswer.COMMAND = ConstsForSend.COMMAND_ONLINE_RUN;
                        jsonAnswer.object = GSON.toJson(run);

                    }
                }
                break;
            case ConstFromClient.COMMAND_ADD_JUDGE_DECISION:
                boolean added = false;
                JudgeDecision decision = GSON.fromJson(jsonMSG.getObject(), JudgeDecision.class);
                if (decision!=null){
                    if (client.getThisUser().getIdUser() == decision.iduser) {
                        added = DataBaseHandler.getInstance().AddJudgeDecision(decision);
                    }
                }
                if (added) {
                    jsonAnswer.object = jsonMSG.object;
                    jsonAnswer.COMMAND = ConstsForSend.COMMAND_ONLINE_JUDGE_DECISION;
                    jsonAnswer.user = client.getThisUser();
                    client.getServer().sendMessageToAllClients(jsonAnswer.getStringAnswer());
                    DataBaseHandler.getInstance().CalculateDecision(decision.idstagetable);
                }
                break;
            case ConstFromClient.COMMAND_GET_ALL_JUDGES:
                ArrayList<User> judges =DataBaseHandler.getInstance().GetJudges();
                if (judges.size()>0){
                    jsonAnswer.COMMAND = ConstsForSend.COMMAND_WITH_ALL_JUDGES;
                    jsonAnswer.object = GSON.toJson(judges);
                } else {
                    jsonAnswer.COMMAND = ConstsForSend.COMMAND_NO_JUDGES;
                }
                break;
            case ConstFromClient.COMMAND_SAVE_STAGE:
                StageWithCup stageWithCup = GSON.fromJson(jsonMSG.getObject(),StageWithCup.class);
                boolean saved = false;
                if (stageWithCup.getCup().getOrganizerId() == client.getThisUser().getIdUser()) {
                    saved = DataBaseHandler.getInstance().SaveStage(GSON.fromJson(jsonMSG.getObject(),Stage.class));
                }
                if (saved) {
                    jsonAnswer.COMMAND = ConstsForSend.COMMAND_STAGE_SAVED;
                    jsonAnswer.object = GSON.toJson(DataBaseHandler.getInstance().GetStage(stageWithCup.getIdstage()));
                } else {
                    jsonAnswer.COMMAND = ConstsForSend.COMMAND_STAGE_NOT_SAVED;
                }
                break;
            case ConstFromClient.COMMAND_GET_FULL_USER:
                client.setThisUser(DataBaseHandler.getInstance().GetUserById(GSON.fromJson(jsonMSG.getObject(),int.class), true));
                jsonAnswer.COMMAND = ConstsForSend.COMMAND_USER_OF_SESSION;
                break;
            case ConstFromClient.COMMAND_GET_ALL_DRIVERS:
                ArrayList<DriverWithUser> driversWithUser= DataBaseHandler.getInstance().GetDriversWithUser();
                if (driversWithUser.size() > 0) {
                    jsonAnswer.COMMAND = ConstsForSend.COMMAND_WITH_ALL_DRIVERS;
                    jsonAnswer.object = GSON.toJson(driversWithUser);
                }
                break;
            case ConstFromClient.COMMAND_SAVE_DRIVER:
                Driver driverForSaving = GSON.fromJson(jsonMSG.getObject(), Driver.class);
                if (driverForSaving != null) {
                    if (DataBaseHandler.getInstance().SaveDriver(driverForSaving)) {
                        jsonAnswer.COMMAND = ConstsForSend.COMMAND_DRIVER_SAVED;
                        jsonAnswer.object = GSON.toJson(DataBaseHandler.getInstance().GetDriverById(driverForSaving.getIddriver()));
                    } else {
                        jsonAnswer.COMMAND = ConstsForSend.COMMAND_DRIVER_SAVING_ERROR;
                    }
                } else {
                    jsonAnswer.COMMAND = ConstsForSend.COMMAND_DRIVER_SAVING_ERROR;
                }
                break;
            case ConstFromClient.COMMAND_GET_STAGE_PARICIPIANTS:
                Stage stage1 = GSON.fromJson(jsonMSG.getObject(), Stage.class);
                if (stage1 != null) {
                    ArrayList<Participation> participations = DataBaseHandler.getInstance().GetStageParticipiants(stage1.getIdstage());
                    if (participations.size()>0) {
                        jsonAnswer.COMMAND = ConstsForSend.COMMAND_WITH_STAGE_PARICIPIANTS;
                        jsonAnswer.object = GSON.toJson(participations);
                    } else {
                        jsonAnswer.COMMAND = ConstsForSend.COMMAND_NO_STAGE_PARICIPIANTS;
                    }
                 } else {
                    jsonAnswer.COMMAND = ConstsForSend.COMMAND_NO_STAGE_PARICIPIANTS;
                }
                break;
            case ConstFromClient.COMMAND_SAVE_PARICIPIANTS:
                Participation[] parts = GSON.fromJson(jsonMSG.getObject(), Participation[].class);
                ArrayList<Participation> partsArrayList = new ArrayList<>();
                Collections.addAll(partsArrayList, parts);
                boolean success = true;
                if (partsArrayList.size() > 0) {
                    for (Participation part : partsArrayList) {
                        if (!DataBaseHandler.getInstance().SaveParticipiant(part)){
                          success = false;
                        }
                    }
                    if (success) {
                        ArrayList<Participation> participations = DataBaseHandler.getInstance().GetStageParticipiants(parts[0].stageId);
                        if (participations.size()>0) {
                            jsonAnswer.COMMAND = ConstsForSend.COMMAND_PARICIPIANTS_SAVED;
                            jsonAnswer.object = GSON.toJson(participations);
                        } else {
                            jsonAnswer.COMMAND = ConstsForSend.COMMAND_PARICIPIANTS_NOT_SAVED;
                        }
                    } else  {
                        jsonAnswer.COMMAND = ConstsForSend.COMMAND_PARICIPIANTS_NOT_SAVED;
                    }
                }else  {
                    jsonAnswer.COMMAND = ConstsForSend.COMMAND_PARICIPIANTS_NOT_SAVED;
                }
                break;
            case ConstFromClient.COMMAND_SET_STAGE_STATUS:
                Stage stage2 = GSON.fromJson(jsonMSG.getObject(), Stage.class);
                if (DataBaseHandler.getInstance().SaveStageStatus(stage2)) {
                    Stage savedStage = DataBaseHandler.getInstance().GetStage(stage2.getIdstage());
                    if (savedStage.getStatus() == stage2.getStatus()) {
                        jsonAnswer.object = GSON.toJson(savedStage);
                        jsonAnswer.COMMAND = ConstsForSend.COMMAND_STAGE_NEW_STATUS;
                        client.sendMsg(jsonAnswer.getStringAnswer());
                    } else {
                        break;
                    }
                    if (savedStage.getStatus() == DifObjectTypes.STAGE_STATUS_QUAL) {
                        ArrayList<Participation> partsOfStage = DataBaseHandler.getInstance().GetStageParticipiants(savedStage.getIdstage());
                        partsOfStage.removeIf(e -> !e.confirmed);
                        DataBaseHandler.getInstance().SetQualStageTableTemp(savedStage, partsOfStage);


                        ArrayList<StageTableTemp> runs = DataBaseHandler.getInstance().GetRunsWithStatus(savedStage.getIdstage(), true);
                        if (runs.size() > 0) {
                            for (StageTableTemp run : runs) {
                                run.setFirstDriver(DataBaseHandler.getInstance().GetDriverById(run.getFirstDriverId()));
                            }
                            jsonAnswer.COMMAND = ConstsForSend.COMMAND_STAGE_RUNS;
                            jsonAnswer.object = GSON.toJson(runs);

                        } else {
                            jsonAnswer.COMMAND = ConstsForSend.COMMAND_NO_STAGE_RUNS;
                        }
                    } else if (savedStage.getStatus() == DifObjectTypes.STAGE_STATUS_PAIR_32) {
                        ArrayList<StageTableTemp> qualRuns = DataBaseHandler.getInstance().GetFinalRunsWithStatus(savedStage.getIdstage(), true);
                        if (qualRuns.size() > 0) {
                            qualRuns.sort(comparing(StageTableTemp::getScore, Comparator.reverseOrder()));

                            while (qualRuns.size() > savedStage.getFirstGrid()) {
                                qualRuns.remove(qualRuns.size()-1);
                            }
                            if (qualRuns.size()%2 > 0) {
                                qualRuns.remove(qualRuns.size() - 1);
                            }

                            GridWithDrivers stageGrid = new GridWithDrivers(savedStage.getIdstage());
                            int i;
                            int position = 1;
                            int[] addArr = {+8,+4,-8,+2,+8,-4,-8,+1,+8,+4,-8,-2,+8,-4,-8,+1};
                            added = true;
                            boolean reversed = false;
                            for (i = 0; i<(qualRuns.size());i+=2) {
                                int firstIndex = i;
                                int secondIndex = i+1;
                                if (reversed) {
                                    firstIndex = i+1;
                                    secondIndex = i;
                                }
                                if (!DataBaseHandler.getInstance().AddPositionIntoGrid(savedStage.getIdstage(),savedStage.getFirstGrid(), qualRuns.get(firstIndex).getFirstDriverId(), position)
                                 || !DataBaseHandler.getInstance().AddPositionIntoGrid(savedStage.getIdstage(),savedStage.getFirstGrid(), qualRuns.get(secondIndex).getFirstDriverId(), position+16)) {
                                    added = false;
                                }
                                position = position+addArr[i/2];
                                //added = success1;
                            }
                            if (added) {
                                jsonAnswer.object ="";
                                GridWithDrivers grid = DataBaseHandler.getInstance().GetGridForStage(savedStage);
                                    if (grid != null) {
                                        jsonAnswer.COMMAND = ConstsForSend.COMMAND_WITH_STAGE_GRID;
                                        jsonAnswer.object = GSON.toJson(grid);
                                    } else {
                                        jsonAnswer.COMMAND = ConstsForSend.COMMAND_ERROR_GETTIN_GRID;
                                    }
                            }


                        } else {
                            jsonAnswer.COMMAND = ConstsForSend.COMMAND_ERROR_GETTIN_GRID;
                        }

                    }
                }
                break;
            case ConstFromClient.COMMAND_GET_STAGE_RUNS:
                Stage stage3 = GSON.fromJson(jsonMSG.getObject(), Stage.class);
                if (stage3.getStatus() == 1) {
                    ArrayList<StageTableTemp> runs = DataBaseHandler.getInstance().GetRunsWithStatus(stage3.getIdstage(), true);
                    if (runs.size() > 0) {
                        for (StageTableTemp run : runs) {
                            run.setFirstDriver(DataBaseHandler.getInstance().GetDriverById(run.getFirstDriverId()));
                        }
                        jsonAnswer.COMMAND = ConstsForSend.COMMAND_STAGE_RUNS;
                        jsonAnswer.object = GSON.toJson(runs);

                    } else {
                        jsonAnswer.COMMAND = ConstsForSend.COMMAND_NO_STAGE_RUNS;
                    }
                } else if (stage3.getStatus() >= 2) {
                    GridWithDrivers stageGrid = DataBaseHandler.getInstance().GetGridForStage(stage3);
                     if (stageGrid!=null) {
                        jsonAnswer.COMMAND = ConstsForSend.COMMAND_WITH_STAGE_GRID;
                        jsonAnswer.object = GSON.toJson(stageGrid);

                    } else {
                        jsonAnswer.COMMAND = ConstsForSend.COMMAND_ERROR_GETTIN_GRID;
                    }

                }
                break;
            case ConstFromClient.COMMAND_SET_RUN_STATUS:
                StageTableTemp runNewStatus = GSON.fromJson(jsonMSG.getObject(), StageTableTemp.class);
                if (runNewStatus.getStatus() == DifObjectTypes.RUN_STATUS_NOW) {
                    DataBaseHandler.getInstance().ChageStatusesForRuns(DifObjectTypes.RUN_STATUS_LATEST, DifObjectTypes.RUN_STATUS_IN_PAST, runNewStatus.getStageId());
                    DataBaseHandler.getInstance().ChageStatusesForRuns(DifObjectTypes.RUN_STATUS_NOW, DifObjectTypes.RUN_STATUS_LATEST, runNewStatus.getStageId());
                }
                if (DataBaseHandler.getInstance().SaveRunStatus(runNewStatus)) {
                    ArrayList<StageTableTemp> runs = DataBaseHandler.getInstance().GetRunsWithStatus(runNewStatus.getStageId(), runNewStatus.isQual());
                    if (runs.size() > 0) {
                        for (StageTableTemp run : runs) {
                            run.setFirstDriver(DataBaseHandler.getInstance().GetDriverById(run.getFirstDriverId()));
                        }
                        jsonAnswer.COMMAND = ConstsForSend.COMMAND_STAGE_RUNS_STATUS_UPDATED;
                        jsonAnswer.object = GSON.toJson(runs);

                    } else {
                        jsonAnswer.COMMAND = ConstsForSend.COMMAND_STAGE_RUNS_STATUS_NOT_UPDATED;
                    }

                }

                break;
        }

        jsonAnswer.user = client.getThisUser();
        client.sendMsg(jsonAnswer.getStringAnswer());

    }


}
