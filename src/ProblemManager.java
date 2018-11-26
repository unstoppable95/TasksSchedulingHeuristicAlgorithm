import java.util.ArrayList;

public class ProblemManager {

    private ArrayList<Problem> problemList = new ArrayList<>();
    private FileManager manager = new FileManager();


    public void readFromFile(String fileName,double h){
        problemList= manager.loadProblemData(fileName,h);
    }

    public void generateSchedulesForFile (String directory , Integer...K){
        AcoAdministrator aco = new AcoAdministrator();
        Integer k = K.length > 0 ? K[0] : -1;
        if ( k==-1){
            //all k in files
        for (Problem p : problemList) {

            //p.generateSchedule();
            aco.metaheuristic(p);

            manager.saveInstance(String.valueOf(p.getNumberOfJobs()),String.valueOf(problemList.indexOf(p)+1),String.valueOf(Math.round(p.getH()*10)),(int)p.getGoalFunction(),p,String.valueOf(p.getR()), directory);

            }
        }
        else {
            //specify instance
            Problem p =problemList.get(k);
            aco.metaheuristic(p);
            manager.saveInstance(String.valueOf(p.getNumberOfJobs()),String.valueOf(problemList.indexOf(p)+1),String.valueOf(Math.round(p.getH()*10)),(int)p.getGoalFunction(),p,String.valueOf(p.getR()), directory);
        }
    }
}
