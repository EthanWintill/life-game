import java.util.Arrays;
import java.util.Scanner;

/**
 * This function initializes a blank board and a board from a file and 
 * then simulates the Game of Life on the board for a specified number of generations
 * 
 * 
 * Pre-condition: None
 * Post-condition: The Game of Life is simulated on the board for the specified number of generations
 */

public class Life {
    public static void main(String[] args) {
        // Initialize blank board
        boolean[][] blankBoard = new boolean[50][100];
        boolean[] blankRow = new boolean[100];
        Arrays.fill(blankRow, false);
        for (boolean[] row : blankBoard) {
            row = Arrays.copyOf(blankRow, blankRow.length);
        }

        // Initialize board from file
        FileHandler rickTest = new FileHandler("start.txt");
        boolean[][] boardFromFile = rickTest.parse();

        //board to be used, can be either board from file or custom board like 'blankBoard'
        boolean[][] board = boardFromFile;

        // Get user input
        Scanner in = new Scanner(System.in);
        System.out.println("Enter number of generations: ");
        int generations = in.nextInt();
        System.out.println("Enter number of threads: ");
        int numThreads = in.nextInt();

        // Initialize GameOfLifeGUI and BoardSim
        animator basicFrame = new GameOfLifeGUI(board);

        // pauses execution until enter key so user can change cells from GUI and update board
        basicFrame.getUserInput();
        board = basicFrame.getBoard();

        BoardSim b = new BoardSim(board,generations,numThreads, basicFrame,true);

        // Simulate Game of Life
        long start = System.currentTimeMillis();
        b.simulate(true);


        System.out.println(b);
        System.out.println("Time: " +(System.currentTimeMillis() - start));
    }
}



