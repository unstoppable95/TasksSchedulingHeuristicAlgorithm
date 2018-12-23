import java.util.ArrayList;
import java.util.List;

public class ProblemManager {

    private ArrayList<Problem> problemList;
    private FileManager manager = new FileManager();
    private String problemsDirectory;
    private String outputDirectory;

    public ProblemManager(String inputDir,String resultDir){
        this.problemsDirectory=inputDir;
        this.outputDirectory=resultDir;
    }

    public void readFromFile(){
        //all files, all h
            List<String> inputFiles=manager.getFilesNames(problemsDirectory);
            double[] tabH = {0.2,0.4,0.6,0.8};
            for (String file :inputFiles){
                System.out.println("Plik : " + file );
                for(double h :tabH){
                    System.out.println("H: " + h );
                    problemList= new ArrayList<>(manager.loadProblemData(file,h));
                    generateSchedulesForFile();
                }
            }
    }

    public void readFromFile(double h,int n){
        //one file, all k , specify h
        String file = problemsDirectory +"/" + "sch" + Integer.toString(n) + ".txt";
        problemList= new ArrayList<>(manager.loadProblemData(file,h));
        generateSchedulesForFile();
    }

    public void readFromFile(double h,int n,int k){
        //one file, all k , specify h
        String file = problemsDirectory +"/" + "sch" + Integer.toString(n) + ".txt";
        problemList= new ArrayList<>(manager.loadProblemData(file,h));
        generateSchedulesForFile(k);
    }


    public void generateSchedulesForFile ( Integer...K){

        Integer k = K.length > 0 ? K[0]-1 : -1;
        if ( k==-1){
        for (Problem p : problemList) {
            AcoAdministrator aco = new AcoAdministrator();
            aco.metaheuristic(p);
            System.out.println("Uszeregowanie dla k=" + (problemList.indexOf(p)+1));
            System.out.println(p);
            manager.saveInstance(String.valueOf(p.getNumberOfJobs()),String.valueOf(problemList.indexOf(p)+1),String.valueOf(Math.round(p.getH()*10)),(int)p.getGoalFunction(),p,String.valueOf(p.getR()), outputDirectory);
            }
        }
        else {
            //specify instance
            AcoAdministrator aco = new AcoAdministrator();
            Problem p =problemList.get(k);
            aco.metaheuristic(p);
            System.out.println("Uszeregowanie dla k=" + (problemList.indexOf(p)+1));
            System.out.println(p);
            manager.saveInstance(String.valueOf(p.getNumberOfJobs()),String.valueOf(problemList.indexOf(p)+1),String.valueOf(Math.round(p.getH()*10)),(int)p.getGoalFunction(),p,String.valueOf(p.getR()), outputDirectory);
        }
    }
}
