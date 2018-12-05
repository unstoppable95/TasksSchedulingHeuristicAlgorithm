
public class ScheduleBasic extends Schedule {

    public ScheduleBasic(Job[] jobList, int d, int r){
        this.jobList=jobList.clone();

        this.d=d;
        this.r=r;
        this.goalFunction=calculateGoalFunction(this.r,this.jobList);
    }

    public void makeSchedule(){

    }

    public void run(){
        calculateR();
    }
}
