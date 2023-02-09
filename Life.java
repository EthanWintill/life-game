import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;


public class Life {
    public static void main(String[] args) {
        FileHandler rickTest = new FileHandler("start.txt");
        BoardSim b = new BoardSim(rickTest.parse());
        System.out.println(b);
        b.run(false);
        System.out.println(b);
        //rickTest.writeBoard(b);
    }
}
class BoardSim{
    boolean[][] boardState;
    int generationsLeft;
    boolean[][] temp;
    int numThreads;
    
    public BoardSim(inputInformation start){
        boardState = start.board;
        generationsLeft = start.generations;
        numThreads = 2; //declare num threads
    }
    void run(boolean print) {
        while(generationsLeft>0){
            step();
            if(print){
                System.out.println(this);
                try {
                    Thread.sleep(150);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    void step()  {
        temp = new boolean[30][100];

        cellComputer[] threadArray = new cellComputer[numThreads];
        for (int i = 0; i < numThreads; i++) {
            threadArray[i] = new cellComputer(i,numThreads);
            threadArray[i].start();
        }
        // comp = new cellComputer(1,2);
        // comp.start();
        try {
            for (int i = 0; i < numThreads; i++) {
                threadArray[i].t.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        generationsLeft--;
        boardState = temp;
    }
    public int numNeighbors(int r, int c){
        int numNeighbors = 0;
        boolean isLeft = (c==0);
        boolean isRight = (c==99);
        boolean isTop = (r==0);
        boolean isBottom = (r==29);
        if(!isTop && boardState[r-1][c])
            numNeighbors++;
        if(!isTop && !isLeft && boardState[r-1][c-1])
            numNeighbors++;
        if(!isTop && !isRight && boardState[r-1][c+1])
            numNeighbors++;
        if(!isBottom && boardState[r+1][c])
            numNeighbors++;
        if(!isBottom && !isLeft && boardState[r+1][c-1])
            numNeighbors++;
        if(!isBottom && !isRight && boardState[r+1][c+1])
            numNeighbors++;
        if(!isLeft && boardState[r][c-1])
            numNeighbors++;
        if(!isRight && boardState[r][c+1])
            numNeighbors++;

        return numNeighbors;            
    }
    public String toString(){
        String result = "";
        for(boolean[] r: boardState){
            for(boolean c: r){
                if(c)
                    result += "X";
                else
                    result += ".";
            }
            result += "\n";
        }
        result += generationsLeft;
        return result;
    }
    class cellComputer implements Runnable{
        private int threadIndex;
        public Thread t;
        private int threadTotal;

        public cellComputer( int tn, int tt){
            threadIndex = tn;
            threadTotal = tt;
        }
        public void start(){
            if(t == null){
                t = new Thread(this);
                t.start();
            }else{
                System.out.println("already started!");
            }
        }

        public void run(){
            int step = 30/threadTotal;            
            for(int r = step*threadIndex; r< Math.min(30, (step)*(1+threadIndex)); r++){
                for(int c=0; c<100; c++){
                    int numNeighbors = numNeighbors(r, c);
                    if(numNeighbors==2 && boardState[r][c] || numNeighbors==3)
                        temp[r][c] = true;
                    else 
                        temp[r][c] = false;
                }
            }
        }
    }
}
class inputInformation{
    boolean[][] board = new boolean[30][100];
    int generations;
}
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