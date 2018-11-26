import javafx.util.Pair;

import java.util.*;

public class AcoAdministrator {
    private int numOfRandSchedules=100;
    private int numOfGenerateSchedules=50;
    private List<Pair> schedules = new ArrayList<>();
    private long executionTime =  3000L;

    public void metaheuristic(Problem p){

        long startTime = System.currentTimeMillis();
        int currentIteration=0;
        int probabilityOfRandom=100;
        int decreaseProbability=2;

        for (int i =0 ; i<numOfRandSchedules ; i++){
            List<Job> newJobList = new ArrayList<>(p.getJobList());
            Collections.shuffle(newJobList);
            schedules.add(new Pair(newJobList,p.calculateGoalFunction(p.getR(),newJobList)));
        }

        Pair best= Collections.max(schedules,new goalFunctionCompare());
        //set current best schedule
        p.setJobList((List<Job>)best.getKey());
        p.setGoalFunction(p.calculateGoalFunction(p.getR(),p.getJobList()));


        //fill pheromone matrix
        PheromoneMatrix myMatrix = new PheromoneMatrix(p.getNumberOfJobs());
        for (int x=0;x<schedules.size();x++){
            double value =  p.getGoalFunction()/Double.parseDouble(schedules.get(x).getValue().toString());
            myMatrix.fillMatrix((List<Job>)schedules.get(x).getKey(),value);
        }

        myMatrix.displayContent();

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

                    int startJobNumber = new Random().nextInt(p.getNumberOfJobs()) + 1;

                }
            }

            //mutacja


            //turniej (ile_ma_zostac_najlepszych_z_schedules)

            //uzupelnienie macierzy feromonowej


            //wygladzanie macierzy feromowej

            //parowanie macierzy feromonowej

        }

    }



    private class goalFunctionCompare implements Comparator<Pair> {
        public int compare(Pair a, Pair b) {
            if (Integer.parseInt(a.getValue().toString()) < Integer.parseInt(b.getValue().toString())) return 1;
            if (Integer.parseInt(a.getValue().toString()) > Integer.parseInt(b.getValue().toString())) return -1;
            else return 0;
        }
    }
}
