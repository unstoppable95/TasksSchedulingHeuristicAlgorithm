
public class ProjectMain {

    public static void main(String[] args){
        String dirForOutputFiles="Results";

        ProblemManager problems = new ProblemManager();
        problems.readFromFile("sch10.txt",0.2);
        problems.generateSchedulesForFile(dirForOutputFiles,0);


        SolutionChecker sol = new SolutionChecker();
        sol.checkSolutions(sol.getFiles(dirForOutputFiles));

    }
}