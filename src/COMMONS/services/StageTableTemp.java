package COMMONS.services;

import java.sql.ResultSet;

public class StageTableTemp extends forDB {

    protected int idstagetable;
    protected int stageId;
    protected int firstDriverId;
    protected boolean qual;
    protected int secondDriverId;
    protected int score;
    protected int winnerId;
    protected int firstJudgeSolution;
    protected int secondJudgeSolution;
    protected int thirdJudgeSolution;
    protected int attempt;
    protected int status;
    protected int num;

    protected User firstJudge;
    protected User secondJudge;
    protected User thirdJudge;

    protected Driver firstDriver;
    protected Driver secondDriver;

    public StageTableTemp() {
    }
    public StageTableTemp(ResultSet resSet){
        setFieldsByResSet(resSet);
    }
    public StageTableTemp(int idstagetable, int stageId, int firstDriverId, boolean qual, int secondDriverId, int score, int winnerId, int firstJudgeSolution, int secondJudgeSolution, int thirdJudgeSolution, int attempt) {
        this.idstagetable = idstagetable;
        this.stageId = stageId;
        this.firstDriverId = firstDriverId;
        this.qual = qual;
        this.secondDriverId = secondDriverId;
        this.score = score;
        this.winnerId = winnerId;
        this.firstJudgeSolution = firstJudgeSolution;
        this.secondJudgeSolution = secondJudgeSolution;
        this.thirdJudgeSolution = thirdJudgeSolution;
        this.attempt = attempt;
    }
    public StageTableTemp(int stageId, int firstDriverId, boolean qual, int secondDriverId, int score, int winnerId, int firstJudgeSolution, int secondJudgeSolution, int thirdJudgeSolution, int attempt) {
        this.stageId = stageId;
        this.firstDriverId = firstDriverId;
        this.qual = qual;
        this.secondDriverId = secondDriverId;
        this.score = score;
        this.winnerId = winnerId;
        this.firstJudgeSolution = firstJudgeSolution;
        this.secondJudgeSolution = secondJudgeSolution;
        this.thirdJudgeSolution = thirdJudgeSolution;
        this.attempt = attempt;
    }
    public StageTableTemp(int stageId, int firstDriverId, boolean qual) {
        this.stageId = stageId;
        this.firstDriverId = firstDriverId;
        this.qual = qual;
        status = 0;
        firstJudgeSolution = -1;
        secondJudgeSolution = -1;
        thirdJudgeSolution = -1;
    }


    public int getIdstagetable() {
        return idstagetable;
    }
    public void setIdstagetable(int idstagetable) {
        this.idstagetable = idstagetable;
    }

    public int getStageId() {
        return stageId;
    }
    public void setStageId(int stageId) {
        this.stageId = stageId;
    }

    public int getFirstDriverId() {
        return firstDriverId;
    }
    public void setFirstDriverId(int firstDriverId) {
        this.firstDriverId = firstDriverId;
    }

    public boolean isQual() {
        return qual;
    }
    public void setQual(boolean qual) {
        this.qual = qual;
    }

    public int getSecondDriverId() {
        return secondDriverId;
    }
    public void setSecondDriverId(int secondDriverId) {
        this.secondDriverId = secondDriverId;
    }

    public int getScore() {
        return score;
    }
    public void setScore(int score) {
        this.score = score;
    }

    public int getWinnerId() {
        return winnerId;
    }
    public void setWinnerId(int winnerId) {
        this.winnerId = winnerId;
    }

    public int getFirstJudgeSolution() {
        return firstJudgeSolution;
    }
    public void setFirstJudgeSolution(int firstJudgeSolution) {
        this.firstJudgeSolution = firstJudgeSolution;
    }

    public int getSecondJudgeSolution() {
        return secondJudgeSolution;
    }
    public void setSecondJudgeSolution(int secondJudgeSolution) {
        this.secondJudgeSolution = secondJudgeSolution;
    }

    public int getThirdJudgeSolution() {
        return thirdJudgeSolution;
    }
    public void setThirdJudgeSolution(int thirdJudgeSolution) {
        this.thirdJudgeSolution = thirdJudgeSolution;
    }

    public int getAttempt() {
        return attempt;
    }
    public void setAttempt(int attempt) {
        this.attempt = attempt;
    }

    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }

    public User getFirstJudge() {
        return firstJudge;
    }
    public void setFirstJudge(User firstJudge) {
        this.firstJudge = firstJudge;
    }

    public User getSecondJudge() {
        return secondJudge;
    }
    public void setSecondJudge(User secondJudge) {
        this.secondJudge = secondJudge;
    }

    public User getThirdJudge() {
        return thirdJudge;
    }
    public void setThirdJudge(User thirdJudge) {
        this.thirdJudge = thirdJudge;
    }

    public Driver getFirstDriver() {
        return firstDriver;
    }
    public void setFirstDriver(Driver firstDriver) {
        this.firstDriver = firstDriver;
    }

    public Driver getSecondDriver() {
        return secondDriver;
    }
    public void setSecondDriver(Driver secondDriver) {
        this.secondDriver = secondDriver;
    }



}
