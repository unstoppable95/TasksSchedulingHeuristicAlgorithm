import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class SolutionChecker {

    private FileManager manager = new FileManager();

    public List<String> getFiles (String directoryName){
        return manager.getFilesNames(directoryName);
    }

    public void checkSolutions(List<String> files){
        for (String file : files){
            checkSolution(file);
        }
    }

    private void checkSolution(String solutionName){
        Vector<String[]> content=manager.readSolutionFromFile(solutionName);
        double h=Double.parseDouble(content.get(0)[0])/10;
        double goalFunction = Double.parseDouble(content.get(1)[0]);
        int n = Integer.parseInt(content.get(2)[0]);
        int r = Integer.parseInt(content.get(3)[0]);
        List<Job> solutionJobs = new ArrayList<>();
        for (int i=4;i<content.size();i++){
            solutionJobs.add(new Job(Integer.parseInt(content.get(i)[0]), Integer.parseInt(content.get(i)[1]), Integer.parseInt(content.get(i)[2])));
        }

        Problem p= new Problem(n,h,solutionJobs);
        p.setGoalFunction(new ScheduleBasic(p.getJobList(),p.getD(),p.getR()).calculateGoalFunction(p.getR(),p.getJobList()));
        double goalFuntionCheck=p.getGoalFunction();

        if(goalFunction!=goalFuntionCheck) System.out.println("Wyniki w pliku " + solutionName.split("/")[1] + " się nie zgadzają");
    }

}
