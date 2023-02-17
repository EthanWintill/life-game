import java.util.concurrent.*;


public class Life {
    public static void main(String[] args) {
        FileHandler rickTest = new FileHandler("start.txt");
        BoardSim b = new BoardSim(rickTest.parse(),2);
        System.out.println(b);
        b.simulate(false);

        System.out.println(b);
    }
}
class BoardSim{
    boolean[][] boardState;
    int generationsLeft;
    int numThreads;
    
    public BoardSim(inputInformation start, int nt){
        boardState = start.board;
        generationsLeft = start.generations;
        numThreads = nt;
    }
    void simulate(boolean print) {
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        while(generationsLeft>0){
            for (int i = 0; i < numThreads; i++) {
                int startRow = i * boardState.length / numThreads;
                int endRow = (i + 1) * boardState.length / numThreads;
                
                executor.execute(new BoardUpdater(startRow, endRow, this));
                if(print){
                    System.out.println(this);
                }
            }
            generationsLeft--;
        }
        
        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
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
            boolean[][] nextBoard = new boolean[30][100];
            
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
            for(double i=0; i<70; i++){
                double j = i/3.2;
            }
            int numNeighbors = 0;
            boolean isLeft = (c==0);
            boolean isRight = (c==99);
            boolean isTop = (r==0);
            boolean isBottom = (r==29);
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
    boolean[][] board = new boolean[30][100];
    int generations;
}


