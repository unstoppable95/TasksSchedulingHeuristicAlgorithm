import java.util.*;

public class SchedulePunishment extends Schedule {

    private InstanceCompare sort;
    private InstanceCompareA sortA;
    private InstanceCompareB sortB;

    public SchedulePunishment(Job [] jobList, int d, int r){
        this.jobList=jobList.clone();
        this.d=d;
        this.r=r;
        this.goalFunction=calculateGoalFunction(this.r,this.jobList);
        this.sort=new InstanceCompare();
        this.sortA=new InstanceCompareA();
        this.sortB=new InstanceCompareB();
    }


    public void makeSchedule(){
        Arrays.sort(this.jobList,sort);
        Arrays.sort(this.jobList,0,this.jobList.length/2,sortA);
        Arrays.sort(this.jobList,this.jobList.length/2,this.jobList.length,sortB);

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
