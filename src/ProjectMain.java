
public class ProjectMain {

    public static void main(String[] args){
        String outputDir="Results";
        String inputDir="ProblemFiles";

        ProblemManager problems = new ProblemManager(inputDir,outputDir);
        problems.readFromFile();

        SolutionChecker sol = new SolutionChecker();
        sol.checkSolutions(sol.getFiles(outputDir));

    }
}
