import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;

public  class FileManager {

    public ArrayList<Problem> loadProblemData(String fileName,double h){
        ArrayList<Problem> listProblems=new ArrayList<>();
        try{
            File file =new File(fileName);
            Scanner scanner = new Scanner(file);
            int numOfProblems = Integer.parseInt(scanner.nextLine().replace(" ", ""));
            for (int i = 0; i < numOfProblems; i++) {
                int numJobs = Integer.parseInt(scanner.nextLine().replace(" ", ""));
                Job [] listJobs=new Job[numJobs];
                for (int j = 0; j < numJobs; j++) {
                    String line = scanner.nextLine();
                    String[] parts = line.trim().split("\\s+");
                    Job job = new Job(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]),j);
                    listJobs[j]=job;
                }
                Problem problem = new Problem(numJobs,h,listJobs);
                listProblems.add(problem);
            }
            scanner.close();
        }catch (FileNotFoundException ex){
            System.out.println("Nie znaleziono pliku "+fileName);
        }
        return listProblems;
    }


    public  void saveInstance(String n,String k,String h,int F,Problem b,String r,String directory){
        try{
            File dir = new java.io.File(directory);
            dir.mkdir();
        }
        catch (SecurityException ex){
            System.out.println("Brak uprawnień do utworzenia folderu " + directory);
        }

        String nameInstance=directory+"/sch"+n+"_"+k+"_"+h+".txt";
        try {
            File file = new File(nameInstance);
            file.createNewFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(h);
            writer.newLine();
            writer.write(String.valueOf(F));
            writer.newLine();
            writer.write(String.valueOf(n));
            writer.newLine();
            writer.write(String.valueOf(r));
            writer.newLine();
            for (int i =0; i<b.getJobList().length ;i++){
                writer.write(b.getJobList()[i].toString());
                writer.newLine();
            }
            writer.flush();
            writer.close();
        }
        catch(IOException e){
            System.out.println("Wystąpił błąd przy zapisie pliku "+nameInstance);
        }
    }

    public Vector<String[]> readSolutionFromFile(String fileNameFull){
        Vector<String []> fileContent = new Vector<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileNameFull))) {
            String currentLine;
            while ((currentLine = br.readLine()) != null) {
                fileContent.add(currentLine.trim().split("\t"));
            }
        } catch (IOException e) {
            System.out.println("Wystapił błąd z odczytem rozwiązania "  +fileNameFull);
        }
        return fileContent;
    }

    public List<String> getFilesNames(String directoryName){
        List<String> fileNames = new ArrayList<>();
        File[] files = new File(directoryName).listFiles();
        for (File file : files) {
            if (file.isFile()) {
                fileNames.add(directoryName+"/"+file.getName());
            }
        }
        return fileNames;
    }

}
