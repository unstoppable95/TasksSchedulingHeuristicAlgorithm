
public class ProjectMain {

    public static void main(String[] args){
        String dirForOutputFiles="Results";

        ProblemManager problems = new ProblemManager();
        problems.readFromFile("sch10.txt",0.4);
        problems.generateSchedulesForFile(dirForOutputFiles);


        SolutionChecker sol = new SolutionChecker();
        sol.checkSolutions(sol.getFiles(dirForOutputFiles));

    }
}
