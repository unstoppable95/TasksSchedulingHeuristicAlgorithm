import java.util.*;

public class AcoAdministrator {
    //metaheuristic parametres
    private static final int START_NUM_WITHOUT_IMPROVEMENT = 5000;
    private static final long EXECUTION_TIME =  60000L;
    private static final int NUMBER_OF_WINNERS_TOURNAMENT=30;
    private static final int NUMBER_OF_START_SCHEDULES=30;
    private static int NUMBER_OF_SCHEDULES_PER_THREAD=10;
    private static final double EVAPORATION_COEFFICIENT=0.09;
    private static final double SMOOTH_COEFFICIENT=0.09;
    private static final int MAX_THREAD = Runtime.getRuntime().availableProcessors();
    private int probabilityOfRandom=100;
    private int numberOfIterationWithoutImprovement=START_NUM_WITHOUT_IMPROVEMENT ;
    private LinkedList<Schedule> schedules = new LinkedList<>();
    private int totalTimeAlgorithm=0;
    private int decreaseProbability=0;


    public void metaheuristic(Problem p){

        long startTime = System.currentTimeMillis();
        int currentIteration=0;
        int d = p.getD();
        if(p.getNumberOfJobs()<500)
        {
        decreaseProbability=2;
        }
        else{
        decreaseProbability=10;
        }

        //add Schedule sort by b-a ; 1st half sort by a ; 2nd by b
        Schedule schedulePunishment=new SchedulePunishment(p.getJobList(),d,0);
        schedulePunishment.makeSchedule();
        schedulePunishment.calculateR();
        schedules.add(schedulePunishment);

        //generate random schedules
        for (int i=0 ; i<NUMBER_OF_START_SCHEDULES; i++){
            Schedule tmpSchedule;
            tmpSchedule=new ScheduleBasic(schedulePunishment.jobList,d,schedulePunishment.r);
            tmpSchedule.makeSchedule();
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
        while(System.currentTimeMillis() - startTime <EXECUTION_TIME )
        {
            System.out.println("Iteracja metaheurystyki " + currentIteration);

            currentIteration+=1;
            Schedule tmpSchedule;
            double value;
            Tournament tournament;

            //decrease probability of random scheduler
            probabilityOfRandom = (currentIteration %2 ==0) ? probabilityOfRandom-decreaseProbability : probabilityOfRandom;
            probabilityOfRandom=(probabilityOfRandom <0 ) ? 0 : probabilityOfRandom;

            //threads which generate NUMBER_OF_SCHEDULES_PER_THREAD each using random scheduler and pheromone matrix
            Thread[] threads = new Thread[MAX_THREAD];
            for (int i = 0; i < MAX_THREAD; i++) {
                threads[i] = new Thread(new scheduleForACO(schedulePunishment,d,p,myMatrix));
                threads[i].setDaemon(true);
                threads[i].start();
            }
            for (int i = 0; i < MAX_THREAD; i++) {
                try {
                    threads[i].join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            //mutation of schedules
//            int numberMutations=schedules.size();
//            for (int k=0; k<numberMutations; k++){
//                for(int i=0; i<5; i++){
//                    tmpSchedule=new ScheduleBasic(schedules.get(k).jobList,d,schedulePunishment.r);
//                    tmpSchedule.makeSchedule();
//                    schedules.add(tmpSchedule);
//                }
//            }

            //mutation of schedules threads
            int numberMutations=schedules.size();
            int [] idxStart = new int[MAX_THREAD];
            int [] idxStop = new int[MAX_THREAD];
            int start=0;
            int range =(int) Math.ceil(numberMutations/MAX_THREAD);
            for (int i=0;i<MAX_THREAD;i++){
                if(i!=MAX_THREAD-1) {
                    idxStart[i] = start;
                    idxStop[i] = start + range;
                    start += range;
                }
                if (i==MAX_THREAD-1){
                    idxStart[i] = start;
                    idxStop[i]=numberMutations-1;
                }
            }

            for (int i = 0; i < MAX_THREAD; i++) {
                threads[i] = new Thread(new mutationForACO(schedulePunishment,d,idxStart[i],idxStop[i]));
                threads[i].setDaemon(true);
                threads[i].start();
            }
            for (int i = 0; i < MAX_THREAD; i++) {
                try {
                    threads[i].join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


            //tournament - leave only number of winners
            tournament = new Tournament(schedules);
            tournament.makeCompetition(NUMBER_OF_WINNERS_TOURNAMENT);

            //calculate new R value for best schedules after tournament
            for (int i=0 ; i< schedules.size() ; i++){
                schedules.get(i).calculateR();
            }

            //set current best schedule
            Schedule bestMetaheuristic= Collections.max(schedules,new goalFunctionCompare());

            //compare current best schedule with problem schedule
            if( p.getGoalFunction()==bestMetaheuristic.goalFunction){
                numberOfIterationWithoutImprovement-=1;
            }
            else if (p.getGoalFunction()>=bestMetaheuristic.goalFunction){
                    p.setJobList(bestMetaheuristic.jobList);
                    p.setR(bestMetaheuristic.r);
                    p.setGoalFunction(bestMetaheuristic.goalFunction);
                    numberOfIterationWithoutImprovement=START_NUM_WITHOUT_IMPROVEMENT;
            }

            //fill pheromone matrix after tournament and mutation
            for (int x=0;x<schedules.size();x++){
                value =  p.getGoalFunction()/schedules.get(x).goalFunction;
                myMatrix.fillMatrix(schedules.get(x).jobList,value);
            }

            //smoothing pheromone matrix
            myMatrix.smoothMatrix(SMOOTH_COEFFICIENT);

            //evaporation of pheromone matrix
            myMatrix.evaporateMatrix(EVAPORATION_COEFFICIENT);

            //check number of iterations without improvement in a row
            if (numberOfIterationWithoutImprovement<=0)   break;

        }

        this.totalTimeAlgorithm=(int)(System.currentTimeMillis()-startTime)/1000;
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


    private void addSchedule(LinkedList<Schedule> schedules, Schedule schedule) {
        synchronized (this) {
            schedules.add(schedule);
        }
    }

    private void addListSchedule(LinkedList<Schedule> schedules, LinkedList<Schedule> schedule) {
        synchronized (this) {
            schedules.addAll(schedule);
        }
    }

    private class mutationForACO implements Runnable{

        private int fromIdx;
        private int toIdx;
        private Schedule schedulePunishment;
        private int d;

        public mutationForACO(Schedule x,int d,int start,int stop){
            this.schedulePunishment=x;
            this.d=d;
            this.fromIdx=start;
            this.toIdx=stop;

        }

        @Override
        public void run(){
            Schedule tmpSchedule;
            LinkedList<Schedule> tmpList = new LinkedList<>();
            for (int k =fromIdx;k<=toIdx; k++) {
                for (int i = 0; i < 5; i++) {
                    tmpSchedule = new ScheduleBasic(schedules.get(k).jobList, d, schedulePunishment.r);
                    tmpSchedule.makeSchedule();
                    tmpList.add(tmpSchedule);
                }
            }
            addListSchedule(schedules, tmpList);
        }

    }

    private class scheduleForACO implements Runnable {

        private Schedule schedulePunishment;
        private int d;
        private Problem p;
        private PheromoneMatrix myMatrix;


        public scheduleForACO(Schedule x,int d, Problem p, PheromoneMatrix matrix){
            this.schedulePunishment=x;
            this.d=d;
            this.p = p;
            this.myMatrix=matrix;

        }

        @Override
        public void run() {
            Schedule tmpSchedule;
            for (int i = 0; i < NUMBER_OF_SCHEDULES_PER_THREAD; i++) {
                if (Math.random() < probabilityOfRandom / 100) {
                    tmpSchedule = new ScheduleBasic(schedulePunishment.jobList, d, schedulePunishment.r);
                    tmpSchedule.makeSchedule();
                    addSchedule(schedules,tmpSchedule);
                } else {
                    int startJobNumber = new Random().nextInt(p.getNumberOfJobs());
                    List<Integer> jobsOrderPheromoneMatrix = new ArrayList<>(myMatrix.order(startJobNumber));
                    Job[] jobsInOrder = makeScheduleWithOrder(jobsOrderPheromoneMatrix, p.getJobList(), p.getNumberOfJobs());

                    tmpSchedule = new ScheduleBasic(jobsInOrder, d, schedulePunishment.r);
                    addSchedule(schedules,tmpSchedule);
                }
            }
        }

    }

}