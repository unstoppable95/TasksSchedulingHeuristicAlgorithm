import java.util.List;
import java.util.Map;

public class Problem {

    private Job[] jobList;
    private int numberOfJobs;
    private double h;
    private int sumP ;
    private double goalFunction;
    private int r;
    private int d;

    public int getD() {
        return d;
    }

    public Problem(int x, double h, Job[] jobList)
    {
        this.jobList=jobList;
        this.numberOfJobs=x;
        this.h=h;
        this.sumP = calculateSumP();
        this.d=(int) Math.floor(sumP * h);
        this.r=0;
    }

    public Problem(int x, double h, Job[] jobList , int r )
    {
        this.jobList=jobList;
        this.numberOfJobs=x;
        this.h=h;
        this.sumP = calculateSumP();
        this.d=(int) Math.floor(sumP * h);
        this.r=r;
    }
    public void setGoalFunction(double goalFunction) {
        this.goalFunction = goalFunction;
    }


    private int calculateSumP() {
        for (Job j : jobList) {
            sumP += j.getP();
        }
        return sumP;
    }

//    @Override
//    public String toString() {
//        return "Funkcja celu " + goalFunction + " h=" + h + " d="+d+ " sumP=" + sumP;
//    }

    @Override
    public String toString() {
        return "" + (int)goalFunction ;
    }

    public void setR(int r) {
        this.r = r;
    }

    public int getNumberOfJobs() {
        return numberOfJobs;
    }

    public double getH() {
        return h;
    }

    public int getR() {
        return r;
    }

    public void setJobList(Job[] jobList) {
        this.jobList = jobList;
    }

    public double getGoalFunction() {
        return goalFunction;
    }

    public Job[] getJobList() {
        return jobList;
    }

}
