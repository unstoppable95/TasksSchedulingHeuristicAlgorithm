import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class Schedule {//implements Runnable{

    protected Job[] jobList;
    protected int d;
    protected int r;
    protected double goalFunction;

    public abstract void makeSchedule();

    private static final <T> void swap (T[] a, int i, int j) {
        T t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    public Job[] simpleSwap() {
        int idxJobForSwap;
        int idxJobForSwap2;
        Job[] swapList = jobList;

        idxJobForSwap = new Random().nextInt(this.jobList.length);
        idxJobForSwap2 = new Random().nextInt(this.jobList.length);

        if (idxJobForSwap == idxJobForSwap2 && idxJobForSwap != jobList.length - 1){
            idxJobForSwap2 = idxJobForSwap + 1;
        }
        if (idxJobForSwap == idxJobForSwap2 && idxJobForSwap == jobList.length - 1) {
            idxJobForSwap = jobList.length - 2;
            idxJobForSwap2 = jobList.length - 1;
        }

        swap(swapList, idxJobForSwap, idxJobForSwap2);

        return swapList;
    }

    public int calculateGoalFunction(int r,Job[] jobList) {
        List<Integer> endTimeJob = new ArrayList<>();
        int currentTime = r;
        int result = 0;
        for (Job j : jobList) {
            int jobEndTime = currentTime + j.getP();
            endTimeJob.add(jobEndTime);
            currentTime = currentTime + j.getP();
        }
        for (int i = 0; i < endTimeJob.size(); i++) {
            result += jobList[i].getA() * Math.max(d - endTimeJob.get(i), 0) + jobList[i].getB() * Math.max(endTimeJob.get(i) - d, 0);
        }
        return result;
    }

    protected  void calculateR(){
        int r = 0;
        int rBest = this.r;
        int fBest = calculateGoalFunction(rBest, jobList);
        int i=0;
        while (i < jobList.length && r <= d) {
           r+=jobList[i].getP();
            int fTmp = calculateGoalFunction(r, jobList);
            if (fTmp < fBest) {
                fBest = fTmp;
                rBest = r;
            }
            i++;
        }

        this.r = rBest;
        this.goalFunction = fBest;
    }


}
