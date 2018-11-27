import java.util.ArrayList;
import java.util.List;

public class Problem {

    private List<Job> jobList;
    private int numberOfJobs;
    private double h;
    private int sumP ;
    private double goalFunction=0;
    private int r=0;
    private int d;

    public Problem(int x,double h,List<Job> jobList)
    {
        this.jobList=jobList;
        this.numberOfJobs=x;
        this.h=h;
        this.sumP = calculateSumP();
        this.d=(int) Math.floor(sumP * h);
        this.goalFunction=calculateGoalFunction(r,jobList);
    }

    public void setGoalFunction(double goalFunction) {
        this.goalFunction = goalFunction;
    }


    public int calculateGoalFunction(int r,List<Job> jobList) {
        List<Integer> endTimeJob = new ArrayList<>();
        int currentTime = r;
        int result = 0;
        for (Job j : jobList) {
            int jobEndTime = currentTime + j.getP();
            endTimeJob.add(jobEndTime);
            currentTime = currentTime + j.getP();
        }
        for (int i = 0; i < endTimeJob.size(); i++) {
            result += jobList.get(i).getA() * Math.max(d - endTimeJob.get(i), 0) + jobList.get(i).getB() * Math.max(endTimeJob.get(i) - d, 0);
        }
        return result;
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
