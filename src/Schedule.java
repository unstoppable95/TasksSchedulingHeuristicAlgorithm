import java.util.ArrayList;
import java.util.List;

public abstract class Schedule {

    protected Job[] jobList;
    protected int d;
    protected int r;
    protected double goalFunction;

    public abstract void makeSchedule();


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
    public void calculateR(){
        int r = 0;
        int r_best = this.r;
        int f_best = calculateGoalFunction(r_best, jobList);

        while (r<=d) {
            r++;
            int f_tmp = calculateGoalFunction(r, jobList);
            if (f_tmp < f_best) {
                f_best = f_tmp;
                r_best = r;
            }
        }

        this.r = r_best;
        this.goalFunction = f_best;
    }
}
