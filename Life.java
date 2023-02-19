import java.util.concurrent.*;


public class Life {
    public static void main(String[] args) {
        FileHandler rickTest = new FileHandler("start.txt");
        animator basicFrame = new GameOfLifeGUI();
        inputInformation fileInput = rickTest.parse();
        basicFrame.animateBoard(fileInput.board);
        basicFrame.getUserInput();
        fileInput.board = basicFrame.getBoard();
        BoardSim b = new BoardSim(fileInput,2, basicFrame);
        long start = System.currentTimeMillis();
        b.simulate(false);

        System.out.println(b);
        System.out.println("Time: " +(System.currentTimeMillis() - start));
    }
}
class BoardSim{
    boolean[][] boardState;
    int generationsLeft;
    int numThreads;
    animator gui;
    
    public BoardSim(inputInformation start, int numberOfThreads, animator igui){
        boardState = start.board;
        generationsLeft = start.generations;
        numThreads = numberOfThreads;
        gui = igui;
    }
    void simulate(boolean print) {
        while(generationsLeft>0){
            ExecutorService executor = Executors.newFixedThreadPool(numThreads);
            for (int i = 0; i < numThreads; i++) {
                int startRow = i * boardState.length / numThreads;
                int endRow = (i + 1) * boardState.length / numThreads;
                executor.execute(new BoardUpdater(startRow, endRow, this));
            }
            generationsLeft--;
            if(print){
                System.out.println(this);
            }
            gui.animateBoard(boardState);
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            executor.shutdown();
            try {
                executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        
        
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
    private static class BoardUpdater implements Runnable {
        private int startRow;
        private int endRow;
        private BoardSim bSim;
        
        public BoardUpdater(int startRow, int endRow, BoardSim bSim) {
            this.startRow = startRow;
            this.endRow = endRow;
            this.bSim = bSim;
        }
        
        
        public void run() {
            boolean[][] nextBoard = new boolean[20][100];
            
            for (int i = startRow; i < endRow; i++) {
                for (int j = 0; j < 100; j++) {
                    int count = numNeighbors(i, j);
                    
                    if (bSim.boardState[i][j] && count == 2 || count == 3) {
                        nextBoard[i][j] = true;
                    } else if (!bSim.boardState[i][j] && count == 3) {
                        nextBoard[i][j] = true;
                    }
                }
                
            }
            
            synchronized (bSim.boardState) {
                for (int i = startRow; i < endRow; i++) {
                    System.arraycopy(nextBoard[i], 0, bSim.boardState[i], 0, bSim.boardState[0].length);
                }
            }
        }
        
        public int numNeighbors(int r, int c){
            for(double i=0; i<70; i++){ //here so there's enough work for the threads to do something.
                double j = i/3.2;
            }
            int numNeighbors = 0;
            boolean isLeft = (c==0);
            boolean isRight = (c==99);
            boolean isTop = (r==0);
            boolean isBottom = (r==19);
            if(!isTop && bSim.boardState[r-1][c])
                numNeighbors++;
            if(!isTop && !isLeft && bSim.boardState[r-1][c-1])
                numNeighbors++;
            if(!isTop && !isRight && bSim.boardState[r-1][c+1])
                numNeighbors++;
            if(!isBottom && bSim.boardState[r+1][c])
                numNeighbors++;
            if(!isBottom && !isLeft && bSim.boardState[r+1][c-1])
                numNeighbors++;
            if(!isBottom && !isRight && bSim.boardState[r+1][c+1])
                numNeighbors++;
            if(!isLeft && bSim.boardState[r][c-1])
                numNeighbors++;
            if(!isRight && bSim.boardState[r][c+1])
                numNeighbors++;
    
            return numNeighbors;            
        }
    }
}
class inputInformation{
    boolean[][] board = new boolean[20][100];
    int generations;
}


