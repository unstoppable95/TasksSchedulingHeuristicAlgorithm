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

    public void run(){
        makeSchedule();
        calculateR();
    }
    public void makeSchedule(){


        Arrays.sort(this.jobList,sort);


        Arrays.sort(this.jobList,0,this.jobList.length/2,sortA);

        int firstHalfLength=0;
        for (int i=0;i<this.jobList.length/2;i++){
            firstHalfLength+=this.jobList[i].getP();
        }

        Arrays.sort(this.jobList,this.jobList.length/2,this.jobList.length,sortA);

        int idxJobForSwap;
        int idxJobForSwap2;
        for (int i=0;i<5;i++){
        do{
            idxJobForSwap=new Random().nextInt(this.jobList.length/2);
            idxJobForSwap2=new Random().nextInt(this.jobList.length/2);
        }
        while(idxJobForSwap==idxJobForSwap2);
        Mutation.swap(this.jobList, idxJobForSwap, idxJobForSwap2);
            do{
                idxJobForSwap=new Random().nextInt(this.jobList.length/2)+this.jobList.length/2;
                idxJobForSwap2=new Random().nextInt(this.jobList.length/2)+this.jobList.length/2;
            }
            while(idxJobForSwap==idxJobForSwap2);
            Mutation.swap(this.jobList, idxJobForSwap, idxJobForSwap2);
        }
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

//    private class goalFunctionCompare implements Comparator<Schedule> {
//        public int compare(Schedule a, Schedule b) {
//            if (a.goalFunction < b.goalFunction) return 1;
//            if (a.goalFunction > b.goalFunction) return -1;
//            else return 0;
//        }
//    }

}
