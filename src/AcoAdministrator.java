import javafx.util.Pair;
import java.util.*;

public class AcoAdministrator {
    //metaheuristic parametres
    private int numOfRandSchedules=1;
    private int numOfGenerateSchedules=1;
    private long executionTime =  5000L;
    private double evaporationCoefficient=0.09;
    private double smoothCoefficient=9.0;
    private int numberOfWinnersTournament=1;
    private int numberOfIterationWithoutImprovement=1000;
    private int totalTimeAlgorithm=0;

    private List<Schedule> schedules = new ArrayList<>();

    public void metaheuristic(Problem p){

        long startTime = System.currentTimeMillis();
        int currentIteration=0;
        int probabilityOfRandom=100;
        int decreaseProbability=2;

        //generate random schedules
        for (int i =0 ; i<numOfRandSchedules ; i++){
            Schedule tmpSchedule=new SchedulePunishment(p.getJobList(),p.getD(),p.getR());
            tmpSchedule.makeSchedule();
            schedules.add(tmpSchedule);
        }

        //set current best schedule
        Schedule best= Collections.max(schedules,new goalFunctionCompare());
        p.setJobList(best.jobList);
        p.setR(best.r);
        p.setGoalFunction(best.goalFunction);

        //fill pheromone matrix
        PheromoneMatrix myMatrix = new PheromoneMatrix(p.getNumberOfJobs());
        for (int x=0;x<schedules.size();x++){
            double value =  p.getGoalFunction()/schedules.get(x).goalFunction;
            myMatrix.fillMatrix(schedules.get(x).jobList,value);
        }

        //main loop of metaheuristic
        while(System.currentTimeMillis() - startTime < executionTime )
        {

            currentIteration+=1;
            probabilityOfRandom = (currentIteration %2 ==0) ? probabilityOfRandom-=decreaseProbability : probabilityOfRandom;
            probabilityOfRandom=(probabilityOfRandom <0 ) ? 0 : probabilityOfRandom;

            for (int i =0 ; i<numOfGenerateSchedules ; i++){
                if(Math.random() < probabilityOfRandom/100 ) {
                    Schedule tmpSchedule=new SchedulePunishment(p.getJobList(),p.getD(),p.getR());
                    tmpSchedule.makeSchedule();
                    schedules.add(tmpSchedule);
                }else {
                    int startJobNumber = new Random().nextInt(p.getNumberOfJobs());
                    List<Integer> jobsOrderPheromoneMatrix = new ArrayList<>(myMatrix.order(startJobNumber));
                    List<Job> jobsInOrder = new ArrayList<>(makeScheduleWithOrder(jobsOrderPheromoneMatrix,p.getJobList()));

                    Schedule tmpSchedule=new ScheduleBasic(jobsInOrder,p.getD(),0);
                    schedules.add(tmpSchedule);
                }
            }

            //mutation of schedules
            int scheduleSize=schedules.size();
            for (int k=0; k<scheduleSize; k++){
                int idxJobForSwap=new Random().nextInt(p.getNumberOfJobs());
                int idxJobForSwap2=new Random().nextInt(p.getNumberOfJobs());

                List<Job> swapList = new ArrayList<>(schedules.get(k).jobList);

                Collections.swap(swapList, idxJobForSwap, idxJobForSwap2);
                Schedule tmpSchedule=new ScheduleBasic(swapList,p.getD(),0);

                schedules.add(tmpSchedule);

            }

            //tournament - leave only number of winners
            Tournament tournament = new Tournament(schedules);
            tournament.makeCompetition(numberOfWinnersTournament);

            //set current best schedule
            Schedule bestMetaheuristic= Collections.max(schedules,new goalFunctionCompare());
            if( p.getGoalFunction()<=bestMetaheuristic.goalFunction){
                numberOfIterationWithoutImprovement-=1;
            }
            else{
                p.setJobList(bestMetaheuristic.jobList);
                p.setR(bestMetaheuristic.r);
                p.setGoalFunction(bestMetaheuristic.goalFunction);
                numberOfIterationWithoutImprovement=10;
            }

            //fill pheromone matrix after tournament and mutation
            for (int x=0;x<schedules.size();x++){
                double value =  p.getGoalFunction()/schedules.get(x).goalFunction;
                myMatrix.fillMatrix(schedules.get(x).jobList,value);
            }

            //smoothing pheromone matrix
            myMatrix.smoothMatrix(smoothCoefficient);

            //evaporation of pheromone matrix
            myMatrix.evaporateMatrix(evaporationCoefficient);

            //check number of iterations without improvement in a row
//            if (numberOfIterationWithoutImprovement<=0)   break;

        }

        long totalTime=System.currentTimeMillis()-startTime;
        this.totalTimeAlgorithm=(int)totalTime/1000;

    }

    private List<Job> makeScheduleWithOrder(List<Integer> order,List<Job> jobs){
        List<Job> jobsInOrder = new ArrayList<>();
        for (Integer i : order) {
            for (Job j : jobs) {
                if (j.getPosition() == i) {
                    jobsInOrder.add(j);
                }
            }
        }
        return jobsInOrder;
    }


    private class goalFunctionCompare implements Comparator<Schedule> {
        public int compare(Schedule a, Schedule b) {
            if (a.goalFunction < b.goalFunction) return 1;
            if (a.goalFunction > b.goalFunction) return -1;
            else return 0;
        }
    }

    public int getTotalTimeAlgorithm() {
        return totalTimeAlgorithm;
    }
}
