import java.util.ArrayList;

public class ProblemManager {

    private ArrayList<Problem> problemList = new ArrayList<>();
    private FileManager manager = new FileManager();


    public void readFromFile(String fileName,double h){
        problemList= manager.loadProblemData(fileName,h);
    }

    public void generateSchedulesForFile (String directory , Integer...K){

        Integer k = K.length > 0 ? K[0] : -1;
        if ( k==-1){
            //all k in files
        for (Problem p : problemList) {
            AcoAdministrator aco = new AcoAdministrator();
            aco.metaheuristic(p);
            System.out.println("Uszeregowanie dla k=" + (problemList.indexOf(p)+1));
            System.out.println(p);
            manager.saveInstance(String.valueOf(p.getNumberOfJobs()),String.valueOf(problemList.indexOf(p)+1),String.valueOf(Math.round(p.getH()*10)),(int)p.getGoalFunction(),p,String.valueOf(p.getR()), directory);

            }
        }
        else {
            //specify instance
            AcoAdministrator aco = new AcoAdministrator();
            Problem p =problemList.get(k);
            aco.metaheuristic(p);
            System.out.println("Uszeregowanie dla k=" + (problemList.indexOf(p)+1));
            System.out.println(p);
            manager.saveInstance(String.valueOf(p.getNumberOfJobs()),String.valueOf(problemList.indexOf(p)+1),String.valueOf(Math.round(p.getH()*10)),(int)p.getGoalFunction(),p,String.valueOf(p.getR()), directory);
        }
    }
}
