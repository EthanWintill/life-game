import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class GameOfLifeGUI extends JFrame implements animator {
    private JPanel gamePanel;
    private boolean[][] board;
    private boolean start = true;
    int cellSize;


    public GameOfLifeGUI(boolean[][] startingBoard) {
        board = startingBoard;
        int width = board[0].length * 10;
        int height = board.length * 10+20;
        setTitle("Game of Life");
        setSize(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        gamePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                cellSize = 10;
                int width = getWidth();
                int height = getHeight();
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, width, height);

                if (board != null) {
                    for (int i = 0; i < board.length; i++) {
                        for (int j = 0; j < board[i].length; j++) {
                            if (board[i][j]) {
                                g.setColor(Color.WHITE);
                                g.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);
                            }
                        }
                    }
                }
            }
        };
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    // Enter key was pressed
                    // Do something here
                    start = !start;
                }
            }
        });
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = e.getY() / cellSize;
                int col = e.getX() / cellSize;
                if (row >= 0 && row < board.length && col >= 0 && col < board[0].length) {
                    // Toggle cell state
                    board[row-2][col] = !board[row-2][col];
                    repaint();
                }
            }
        });
        add(gamePanel);
        setVisible(true);
    }

    public void animateBoard(boolean[][] board) {
        this.board = board;
        gamePanel.repaint();
    }

    public void getUserInput() {
        while(start){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }
        
    }
    public boolean[][] getBoard(){
        return board;
    }

    

}
