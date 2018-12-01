import java.util.ArrayList;
import java.util.List;

public class ScheduleBasic extends Schedule {

    public ScheduleBasic(List<Job> jobList, int d, int r){
        this.jobList=new ArrayList<>(jobList);
        this.d=d;
        this.r=r;
        this.goalFunction=calculateGoalFunction(this.r,this.jobList);
    }

    public void makeSchedule(){

    }
}
