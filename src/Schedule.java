import java.util.ArrayList;
import java.util.List;

public abstract class Schedule {

    protected List<Job> jobList;
    protected int d;
    protected int r;
    protected double goalFunction;

    public abstract void makeSchedule();


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

}
