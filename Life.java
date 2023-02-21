import java.util.Arrays;
import java.util.concurrent.*;


public class Life {
    public static void main(String[] args) {
        boolean[][] blankBoard = new boolean[50][100];
        boolean[] blankRow = new boolean[100];
        Arrays.fill(blankRow, false);
        for (boolean[] row : blankBoard) {
            row = Arrays.copyOf(blankRow, blankRow.length);
        }



        FileHandler rickTest = new FileHandler("blank.txt");
        boolean[][] boardFromFile = rickTest.parse();
        animator basicFrame = new GameOfLifeGUI(blankBoard);
        basicFrame.getUserInput();
        blankBoard = basicFrame.getBoard();
        BoardSim b = new BoardSim(blankBoard,50000,2, basicFrame);
        long start = System.currentTimeMillis();
        b.simulate(true);

        System.out.println(b);
        System.out.println("Time: " +(System.currentTimeMillis() - start));
    }
}
class BoardSim{
    boolean[][] boardState;
    int generationsLeft;
    int numThreads;
    animator gui;
    
    public BoardSim(boolean[][] startingBoard, int startingGenerations, int numberOfThreads, animator igui){
        boardState = startingBoard;
        generationsLeft = startingGenerations;
        numThreads = numberOfThreads;
        gui = igui;
    }
    void simulate(boolean animate) {
        while(generationsLeft>0){
            ExecutorService executor = Executors.newFixedThreadPool(numThreads);
            for (int i = 0; i < numThreads; i++) {
                int startRow = i * boardState.length / numThreads;
                int endRow = (i + 1) * boardState.length / numThreads;
                executor.execute(new BoardUpdater(startRow, endRow, this));
            }
            generationsLeft--;
            if(animate){
                gui.animateBoard(boardState);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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
            boolean[][] nextBoard = new boolean[bSim.boardState.length][bSim.boardState[0].length];
            for (int i = startRow; i < endRow; i++) {
                for (int j = 0; j < bSim.boardState[0].length; j++) {
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
            boolean isRight = (c==bSim.boardState[0].length-1);
            boolean isTop = (r==0);
            boolean isBottom = (r==bSim.boardState.length-1);
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


