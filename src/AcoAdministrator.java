import java.util.*;

public class AcoAdministrator {
    //metaheuristic parametres
    private int numOfRandSchedules=30;
    private int numOfGenerateSchedules=20;
    private long executionTime =  60000L;
    private double evaporationCoefficient=0.09;
    private double smoothCoefficient=9.0;
    private int numberOfWinnersTournament=10;
    private int numberOfIterationWithoutImprovement=50000;
    private int totalTimeAlgorithm=0;
    private LinkedList<Schedule> schedules = new LinkedList<>();



    public void metaheuristic(Problem p){

        long startTime = System.currentTimeMillis();
        int currentIteration=0;
        int probabilityOfRandom=100;
        int decreaseProbability;
        if(p.getNumberOfJobs()<500)
        {
        decreaseProbability=2;
        }
        else{
        decreaseProbability=10;
        }
        int d = p.getD();

        //add sort a b half schedule
        Schedule schedulePunishment=new SchedulePunishment(p.getJobList(),d,0);
        schedulePunishment.makeSchedule();
        schedulePunishment.calculateR();
        schedules.add(schedulePunishment);

        //generate random schedules
        for (int i=0 ; i<numOfRandSchedules ; i++){
            Schedule tmpSchedule;
            tmpSchedule=new ScheduleBasic(schedulePunishment.jobList,d,0);
            tmpSchedule.makeSchedule();
            tmpSchedule.calculateR();
            schedules.add(tmpSchedule);
        }

        //set current best schedule
        Schedule best= Collections.max(schedules,new goalFunctionCompare());
        best.calculateR();
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
            System.out.println("Iteracja metaheurystyki " + currentIteration);
            currentIteration+=1;
            probabilityOfRandom = (currentIteration %2 ==0) ? probabilityOfRandom-decreaseProbability : probabilityOfRandom;
            probabilityOfRandom=(probabilityOfRandom <0 ) ? 0 : probabilityOfRandom;
            Schedule tmpSchedule;


            double value;
            Tournament tournament;

            for (int i =0 ; i<numOfGenerateSchedules ; i++){
                if(Math.random() < probabilityOfRandom/100 ) {
                    tmpSchedule=new ScheduleBasic(schedulePunishment.jobList,d,0);
                    tmpSchedule.makeSchedule();
                    tmpSchedule.calculateR();
                    schedules.add(tmpSchedule);
                }else {
                    int startJobNumber = new Random().nextInt(p.getNumberOfJobs());
                    List<Integer> jobsOrderPheromoneMatrix = new ArrayList<>(myMatrix.order(startJobNumber));
                    Job [] jobsInOrder = makeScheduleWithOrder(jobsOrderPheromoneMatrix,p.getJobList(),p.getNumberOfJobs());

                    tmpSchedule=new ScheduleBasic(jobsInOrder,d,0);
                    tmpSchedule.calculateR();
                    schedules.add(tmpSchedule);
                }
            }

            //mutation of schedules
            int scheduleSize=schedules.size();

            for (int k=0; k<scheduleSize; k++){
                for(int i=0; i<5; i++){
                    tmpSchedule=new ScheduleBasic(schedules.get(k).jobList,d,0);
                    tmpSchedule.makeSchedule();
                    tmpSchedule.calculateR();
                    schedules.add(tmpSchedule);
                }
            }

            //tournament - leave only number of winners
            tournament = new Tournament(schedules);
            tournament.makeCompetition(numberOfWinnersTournament);

            //set current best schedule
            Schedule bestMetaheuristic= Collections.max(schedules,new goalFunctionCompare());
            bestMetaheuristic.calculateR();

            if( p.getGoalFunction()==bestMetaheuristic.goalFunction){
                numberOfIterationWithoutImprovement-=1;
            }
            else{
                if(p.getGoalFunction()>=bestMetaheuristic.goalFunction){
                    System.out.println("Iteracja " + currentIteration  + " Podmienilem rezultat z  " + p.getGoalFunction() + " na " + bestMetaheuristic.goalFunction);
                    p.setJobList(bestMetaheuristic.jobList);
                    p.setR(bestMetaheuristic.r);
                    p.setGoalFunction(bestMetaheuristic.goalFunction);
                }
                numberOfIterationWithoutImprovement=50000;
            }

            //fill pheromone matrix after tournament and mutation
            for (int x=0;x<schedules.size();x++){
                value =  p.getGoalFunction()/schedules.get(x).goalFunction;
                myMatrix.fillMatrix(schedules.get(x).jobList,value);
            }

            //smoothing pheromone matrix
            myMatrix.smoothMatrix(smoothCoefficient);

            //evaporation of pheromone matrix
            myMatrix.evaporateMatrix(evaporationCoefficient);

            //check number of iterations without improvement in a row
            if (numberOfIterationWithoutImprovement<=0)   break;

        }

        long totalTime=System.currentTimeMillis()-startTime;
        this.totalTimeAlgorithm=(int)totalTime/1000;

    }

    private Job [] makeScheduleWithOrder(List<Integer> order, Job[] jobs,int size){
        Job [] jobsInOrder = new Job[size];
        int tmp=0;
        for (Integer i : order) {
            for (Job j : jobs) {
                if (j.getPosition() == i) {
                    jobsInOrder[tmp]=j;
                    tmp++;
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


}