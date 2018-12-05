import java.util.*;

public class AcoAdministrator {
    //metaheuristic parametres
    private int numOfRandSchedules=10;
    private int numOfGenerateSchedules=20;
    private long executionTime =  30000L;
    private double evaporationCoefficient=0.09;
    private double smoothCoefficient=9.0;
    private int numberOfWinnersTournament=10;
    private int numberOfIterationWithoutImprovement=50000;
    private int totalTimeAlgorithm=0;
    private final Object lock = new Object();
    private LinkedList<Schedule> schedules = new LinkedList<>();

    public void metaheuristic(Problem p){

        long startTime = System.currentTimeMillis();
        int currentIteration=0;
        int probabilityOfRandom=100;
        int decreaseProbability=10;
        int d = p.getD();

        //generate random schedules
        for (int i =0 ; i<numOfRandSchedules ; i++){
            Schedule tmpSchedule=new SchedulePunishment(p.getJobList(),d,0);
            Thread tobj =new Thread(tmpSchedule);
            tobj.start();
            synchronized (lock) {
                schedules.add(tmpSchedule);
            }
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
            System.out.println(currentIteration );

            currentIteration+=1;
            probabilityOfRandom = (currentIteration %2 ==0) ? probabilityOfRandom-decreaseProbability : probabilityOfRandom;
            probabilityOfRandom=(probabilityOfRandom <0 ) ? 0 : probabilityOfRandom;
            Schedule tmpSchedule;
            Schedule tmpSchedule2;
            Schedule tmpSchedule3;
            Mutation mut;
            double value;
            Tournament tournament;

            for (int i =0 ; i<numOfGenerateSchedules ; i++){
                if(Math.random() < probabilityOfRandom/100 ) {
                    tmpSchedule=new SchedulePunishment(p.getJobList(),d,0);
                    Thread tobj =new Thread(tmpSchedule);
                    tobj.start();

                    synchronized (lock) {
                        schedules.add(tmpSchedule);
                    }

                }else {
                    int startJobNumber = new Random().nextInt(p.getNumberOfJobs());
                    List<Integer> jobsOrderPheromoneMatrix = new ArrayList<>(myMatrix.order(startJobNumber));
                    Job [] jobsInOrder = makeScheduleWithOrder(jobsOrderPheromoneMatrix,p.getJobList(),p.getNumberOfJobs());

                    tmpSchedule=new ScheduleBasic(jobsInOrder,d,0);
                    Thread tobj =new Thread(tmpSchedule);
                    tobj.start();
                    synchronized (lock) {
                        schedules.add(tmpSchedule);
                    }

                }
            }

            //mutation of schedules
            int scheduleSize=schedules.size();
            for (int k=0; k<scheduleSize; k++){
                mut = new Mutation(schedules.get(k));
                for(int i=0; i<3; i++){

                    tmpSchedule=new ScheduleBasic(mut.simpleSwap(),d,0);
                    tmpSchedule2=new ScheduleBasic(mut.maxABSwap(),d,0);
                    tmpSchedule3=new ScheduleBasic(mut.minABSwap(),d,0);

                    Thread tobj =new Thread(tmpSchedule);
                    tobj.start();

                    Thread tobj2 =new Thread(tmpSchedule2);
                    tobj2.start();
                    Thread tobj3 =new Thread(tmpSchedule3);
                    tobj3.start();

                    synchronized (lock) {
                        schedules.add(tmpSchedule);
                    }

                    synchronized (lock) {
                        schedules.add(tmpSchedule2);
                    }

                    synchronized (lock) {
                        schedules.add(tmpSchedule3);
                    }

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

    public int getTotalTimeAlgorithm() {
        return totalTimeAlgorithm;
    }
}