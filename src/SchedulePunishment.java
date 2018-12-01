import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SchedulePunishment extends Schedule {


    public SchedulePunishment(List<Job> jobList, int d, int r){
        this.jobList=new ArrayList<>(jobList);
        this.d=d;
        this.r=r;
        this.goalFunction=calculateGoalFunction(this.r,this.jobList);
    }


    public void makeSchedule(){

        InstanceCompare sort = new InstanceCompare();
        Collections.sort(this.jobList, sort);
        InstanceCompareA sortA = new InstanceCompareA();
        Collections.sort(this.jobList.subList(0,this.jobList.size()/2), sortA);

        int firstHalfLength=0;
        for (int i=0;i<this.jobList.size()/2;i++){
            firstHalfLength+=this.jobList.get(i).getP();
        }

        InstanceCompareB sortB = new InstanceCompareB();
        Collections.sort(this.jobList.subList((this.jobList.size()/2),this.jobList.size()), sortB);

        this.goalFunction=calculateGoalFunction(this.r,this.jobList);

    }



    private class InstanceCompare implements Comparator<Job> {
        public int compare(Job a, Job b) {
            if (a.getDifferencePunishment() < b.getDifferencePunishment()) return 1;
            if (a.getDifferencePunishment() > b.getDifferencePunishment()) return -1;
            else return 0;
        }
    }

    private class InstanceCompareB implements Comparator<Job> {
        public int compare(Job a, Job b) {
            if (a.getB() < b.getB()) return 1;
            if (a.getB() > b.getB()) return -1;
            else return 0;
        }
    }

    private class InstanceCompareA implements Comparator<Job> {
        public int compare(Job a, Job b) {
            if (a.getA() < b.getA()) return -1;
            if (a.getA() > b.getA()) return 1;
            else return 0;
        }
    }
}
