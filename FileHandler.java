import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
class FileHandler{
    
    Scanner scan;
    File file;
    public FileHandler(String f)  {
        try {
            file = new File(f);
            scan = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void writeBoard(BoardSim board){
        try {
            FileWriter author = new FileWriter(file);
            author.write(board.toString());
            author.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public boolean[][] parse(){
        boolean[][] result;
        int numLines = 0;
        int lineLength = -1; // initialized to -1 so we can check if it gets updated
        
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                numLines++;
                if (lineLength == -1) {
                    lineLength = line.length();
                } else if (lineLength != line.length()) {
                    System.out.println("Error: not all lines have the same length");
                    return new boolean[0][0];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        result = new boolean[numLines][lineLength];

        int r = 0;
        while(scan.hasNextLine()){
            String line = scan.next();
            if(scan.hasNextLine()){
                for(int c=0; c<line.length(); c++){
                    if(line.charAt(c)=='.')
                        result[r][c] = false;
                    else if(line.charAt(c)=='X')
                        result[r][c] = true;
                    else
                        System.out.println("CAN'T READ CHAR: " + line.charAt(c));
                }            
            }else{
                //fileInfo.generations = Integer.parseInt(line);
            }
            r++;
        }
        return result;
    }
}