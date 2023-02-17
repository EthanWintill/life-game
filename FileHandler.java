import java.io.File;
import java.io.FileNotFoundException;
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
    public inputInformation parse(){
        inputInformation fileInfo = new inputInformation();
        int r = 0;
        while(scan.hasNextLine()){
            String line = scan.next();
            if(scan.hasNextLine()){
                for(int c=0; c<line.length(); c++){
                    if(line.charAt(c)=='.')
                        fileInfo.board[r][c] = false;
                    else if(line.charAt(c)=='X')
                        fileInfo.board[r][c] = true;
                    else
                        System.out.println("CAN'T READ CHAR: " + line.charAt(c));
                }            
            }else{
                fileInfo.generations = Integer.parseInt(line);
            }
            r++;
        }
        return fileInfo;
    }
}