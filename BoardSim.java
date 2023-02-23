import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class BoardSim{
    boolean[][] boardState;
    int generationsLeft;
    int numThreads;
    animator gui;
    boolean addExtraWork;
    /**
     * This function initializes the BoardSim object with starting board, generations, threads, animator, and additional work flag
     * 
     * @param startingBoard The starting board
     * @param startingGenerations The number of generations to simulate
     * @param numberOfThreads The number of threads to use for simulation
     * @param igui The animator object to use for simulation
     * @param threadDemo a boolean flag indicating if extra work should be added to simulate longer run time
     * @pre startingBoard is not null, startingGenerations is greater than 0, numberOfThreads is greater than 0, igui is not null
     * @post a new BoardSim object is created with the given parameters
     */

    public BoardSim(boolean[][] startingBoard, int startingGenerations, int numberOfThreads, animator igui, boolean threadDemo){
        boardState = startingBoard;
        generationsLeft = startingGenerations;
        numThreads = numberOfThreads;
        gui = igui;
        addExtraWork = threadDemo;

    }

    /**
     * Simulates the board for the specified number of generations
     * @param animate a boolean flag indicating if the GUI should be animated
     * @pre none
     * @post the board is simulated for the specified number of generations with the given number of threads
     */

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
    /**
     * Returns a String representation of the board
     * @pre none
     * @post a String representation of the board is returned
     * @return a String representation of the board
     */

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
    /**
     * An inner class that updates a portion of the board in a separate thread
     */

    private static class BoardUpdater implements Runnable {
        private int startRow;
        private int endRow;
        private BoardSim bSim;
        
        public BoardUpdater(int startRow, int endRow, BoardSim bSim) {
            this.startRow = startRow;
            this.endRow = endRow;
            this.bSim = bSim;
        }
        
        // Pre-condition: None
        // Post-condition: updates the state of the board in the given row range
        public void run() {
            boolean[][] nextBoard = new boolean[bSim.boardState.length][bSim.boardState[0].length];
            for (int i = startRow; i < endRow; i++) {
                for (int j = 0; j < bSim.boardState[0].length; j++) {
                    int count = numLiveNeighbors(i, j);
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
        
        // Pre-condition: i and j are valid indices in boardState
        // Post-condition: returns the number of live neighbors for the given cell
        public int numLiveNeighbors(int r, int c){
            if(bSim.addExtraWork){
                long startTime = System.nanoTime();
                while (System.nanoTime() - startTime < 250000) {
                }
            }
            int numLiveNeighbors = 0;
            boolean isLeft = (c==0);
            boolean isRight = (c==bSim.boardState[0].length-1);
            boolean isTop = (r==0);
            boolean isBottom = (r==bSim.boardState.length-1);
            if(!isTop && bSim.boardState[r-1][c])
                numLiveNeighbors++;
            if(!isTop && !isLeft && bSim.boardState[r-1][c-1])
                numLiveNeighbors++;
            if(!isTop && !isRight && bSim.boardState[r-1][c+1])
                numLiveNeighbors++;
            if(!isBottom && bSim.boardState[r+1][c])
                numLiveNeighbors++;
            if(!isBottom && !isLeft && bSim.boardState[r+1][c-1])
                numLiveNeighbors++;
            if(!isBottom && !isRight && bSim.boardState[r+1][c+1])
                numLiveNeighbors++;
            if(!isLeft && bSim.boardState[r][c-1])
                numLiveNeighbors++;
            if(!isRight && bSim.boardState[r][c+1])
                numLiveNeighbors++;
    
            return numLiveNeighbors;            
        }
    }
}



