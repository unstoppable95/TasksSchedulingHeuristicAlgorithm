import java.util.ArrayList;
import java.util.List;

public class Problem {

    private List<Job> jobList;
    private int numberOfJobs;
    private double h;
    private int sumP ;
    private double goalFunction;
    private int r;
    private int d;

    public int getD() {
        return d;
    }

    public Problem(int x, double h, List<Job> jobList)
    {
        this.jobList=jobList;
        this.numberOfJobs=x;
        this.h=h;
        this.sumP = calculateSumP();
        this.d=(int) Math.floor(sumP * h);
        this.r=0;
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

    @Override
    public String toString() {
        return "Funkcja celu " + goalFunction + " h=" + h + " d="+d+ " sumP=" + sumP;
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

    public void setJobList(List<Job> jobList) {
        this.jobList = jobList;
    }

    public double getGoalFunction() {
        return goalFunction;
    }

    public List<Job> getJobList() {
        return jobList;
    }

}
