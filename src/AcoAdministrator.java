import javafx.util.Pair;
import java.util.*;

public class AcoAdministrator {
    //metaheuristic parametres
    private int numOfRandSchedules=20;
    private int numOfGenerateSchedules=40;
    private long executionTime =  40000L;
    private double evaporationCoefficient=0.09;
    private double smoothCoefficient=9.0;
    private int numberOfWinnersTournament=30;

    private List<Pair> schedules = new ArrayList<>();

    public void metaheuristic(Problem p){

        long startTime = System.currentTimeMillis();
        int currentIteration=0;
        int probabilityOfRandom=100;
        int decreaseProbability=2;

        //generate random schedules
        for (int i =0 ; i<numOfRandSchedules ; i++){
            List<Job> newJobList = new ArrayList<>(p.getJobList());
            Collections.shuffle(newJobList);
            schedules.add(new Pair(newJobList,p.calculateGoalFunction(p.getR(),newJobList)));
        }

        //set current best schedule
        Pair best= Collections.max(schedules,new goalFunctionCompare());
        p.setJobList((List<Job>)best.getKey());
        p.setGoalFunction(p.calculateGoalFunction(p.getR(),p.getJobList()));

        //fill pheromone matrix
        PheromoneMatrix myMatrix = new PheromoneMatrix(p.getNumberOfJobs());
        for (int x=0;x<schedules.size();x++){
            double value =  p.getGoalFunction()/Double.parseDouble(schedules.get(x).getValue().toString());
            myMatrix.fillMatrix((List<Job>)schedules.get(x).getKey(),value);
        }

        //main loop of metaheuristic
        while(System.currentTimeMillis() - startTime < executionTime )
        {
            currentIteration+=1;
            probabilityOfRandom = (currentIteration %2 ==0) ? probabilityOfRandom-=decreaseProbability : probabilityOfRandom;
            probabilityOfRandom=(probabilityOfRandom <0 ) ? 0 : probabilityOfRandom;

            for (int i =0 ; i<numOfGenerateSchedules ; i++){
                if(Math.random() < probabilityOfRandom/100 ) {
                    List<Job> newJobList = new ArrayList<>(p.getJobList());
                    Collections.shuffle(newJobList);
                    schedules.add(new Pair(newJobList,p.calculateGoalFunction(p.getR(),newJobList)));
                }else {
                    int startJobNumber = new Random().nextInt(p.getNumberOfJobs());
                    List<Integer> jobsOrderPheromoneMatrix = new ArrayList<>(myMatrix.order(startJobNumber));
                    List<Job> jobsInOrder = new ArrayList<>(makeScheduleWithOrder(jobsOrderPheromoneMatrix,p.getJobList()));
                    schedules.add(new Pair(jobsInOrder,p.calculateGoalFunction(p.getR(),jobsInOrder)));
                }
            }

            //mutation of schedules
            int scheduleSize=schedules.size();
            for (int k=0; k<scheduleSize; k++){
                int idxJobForSwap=new Random().nextInt(p.getNumberOfJobs());
                int idxJobForSwap2=new Random().nextInt(p.getNumberOfJobs());
                List<Job> swapList = new ArrayList<>((List<Job>)schedules.get(k).getKey());
                Collections.swap(swapList, idxJobForSwap, idxJobForSwap2);
                schedules.add(new Pair(swapList,p.calculateGoalFunction(p.getR(),swapList)));
            }

            //tournament - leave only number of winners
            Tournament tournament = new Tournament(schedules);
            tournament.makeCompetition(numberOfWinnersTournament);

            //set current best schedule
            Pair bestFinish= Collections.max(schedules,new goalFunctionCompare());
            p.setJobList((List<Job>)bestFinish.getKey());
            p.setGoalFunction(p.calculateGoalFunction(p.getR(),p.getJobList()));

            //fill pheromone matrix after tournament and mutation
            for (int x=0;x<schedules.size();x++){
                double value =  p.getGoalFunction()/Double.parseDouble(schedules.get(x).getValue().toString());
                myMatrix.fillMatrix((List<Job>)schedules.get(x).getKey(),value);
            }

            //smoothing pheromone matrix
            myMatrix.smoothMatrix(smoothCoefficient);

            //evaporation of pheromone matrix
            myMatrix.evaporateMatrix(evaporationCoefficient);
        }

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


    private class goalFunctionCompare implements Comparator<Pair> {
        public int compare(Pair a, Pair b) {
            if (Integer.parseInt(a.getValue().toString()) < Integer.parseInt(b.getValue().toString())) return 1;
            if (Integer.parseInt(a.getValue().toString()) > Integer.parseInt(b.getValue().toString())) return -1;
            else return 0;
        }
    }

}
