import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AcoAdministrator {
    private int numOfRandSchedules=100;
    private List<Pair> schedules = new ArrayList<>();
    public void metaheuristic(Problem p){

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

    }



    private class goalFunctionCompare implements Comparator<Pair> {
        public int compare(Pair a, Pair b) {
            if (Integer.parseInt(a.getValue().toString()) < Integer.parseInt(b.getValue().toString())) return 1;
            if (Integer.parseInt(a.getValue().toString()) > Integer.parseInt(b.getValue().toString())) return -1;
            else return 0;
        }
    }
}
