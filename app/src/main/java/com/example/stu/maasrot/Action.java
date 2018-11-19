package com.example.stu.maasrot;



public class Action {
    public Action(String actionName, int actionSum, Boolean plus, String date) {
        this.actionName = actionName;
        this.actionSum = actionSum;
        this.plus = plus;
        this.date = date;
    }


    String actionName;
    int actionSum, totalSum;
    Boolean plus;
    String date;

    public void setTotalSum(){
        int i = DBManager.getDbManager(null).getLastAction();
        if (plus){
            totalSum = i + actionSum;
        }else {
            totalSum = i - actionSum;
        }
    }

}
