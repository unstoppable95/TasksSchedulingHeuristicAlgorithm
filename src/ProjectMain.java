
public class ProjectMain {

    public static void main(String[] args){
        String dirForOutputFiles="Results";

        ProblemManager problems = new ProblemManager();
        problems.readFromFile("sch20.txt",0.2);
        problems.generateSchedulesForFile(dirForOutputFiles);


        SolutionChecker sol = new SolutionChecker();
        sol.checkSolutions(sol.getFiles(dirForOutputFiles));

    }
}
