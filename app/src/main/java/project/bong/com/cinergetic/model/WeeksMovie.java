package project.bong.com.cinergetic.model;

import java.util.ArrayList;

public class WeeksMovie {

    // 일단은 시간표게시때 사용할 데이터
    String dayofweeks;
    ArrayList<String> nameList, timeList;

    public WeeksMovie() {
        this.dayofweeks = "";
        nameList = new ArrayList<>();
        timeList = new ArrayList<>();
    }

    public WeeksMovie(String dayofweeks) {
        this.dayofweeks = dayofweeks;
        nameList = new ArrayList<>();
        timeList = new ArrayList<>();
    }

    public void addData(String name, String time){
        nameList.add(name);
        timeList.add(time);
    }

    // getter & setter
    public String getDayofweeks() {
        return dayofweeks;
    }

    public void setDayofweeks(String dayofweeks) {
        this.dayofweeks = dayofweeks;
    }

    public ArrayList<String> getNameList() {
        return nameList;
    }

    public void setNameList(ArrayList<String> nameList) {
        this.nameList = nameList;
    }

    public ArrayList<String> getTimeList() {
        return timeList;
    }
}
