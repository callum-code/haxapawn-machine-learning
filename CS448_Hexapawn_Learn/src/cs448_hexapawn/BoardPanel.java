/*
   _____      _ _                                 _        ___   ___  __  ___  
  / ____|    | | |                               | |      |__ \ / _ \/_ |/ _ \ 
 | |     __ _| | |_   _ _ __ ___     ___ ___   __| | ___     ) | | | || | (_) |
 | |    / _` | | | | | | '_ ` _ \   / __/ _ \ / _` |/ _ \   / /| | | || |> _ < 
 | |___| (_| | | | |_| | | | | | | | (_| (_) | (_| |  __/  / /_| |_| || | (_) |
  \_____\__,_|_|_|\__,_|_| |_| |_|  \___\___/ \__,_|\___| |____|\___/ |_|\___/ 
 
*/

package cs448_hexapawn;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author callumijohnston
 */
public class BoardPanel extends JPanel {

    int sqLength;
    Pawn selected;
    Game game;
    Learner p2;
    Learner p1;

    public BoardPanel() {
        int boardSize  = Integer.parseInt((String)JOptionPane.showInputDialog(
                    null,
                    "How large would you like your board",
                    "Board Size",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    "3"));
        game = new Game(boardSize);
        int panelSize = Math.min(100 * boardSize,700);
        setSize(panelSize, panelSize);
        sqLength = this.getWidth() / game.squares.length;
        selected = null;
        initSquares();
        p2 = new Learner();
        p1 = new Learner();
        p2.newGame();
        p1.newGame();
    }
    
    public void newGame(){
        p2.endGame(game.winner == -1);
        p1.endGame(game.winner == 1);
        game = new Game(game.squares.length);
        game.blackPawns = new Pawn[game.squares.length];
        game.whitePawns = new Pawn[game.squares.length];
        selected = null;
        game.whiteTurn = true;
        game.winner = 0;
        p2.newGame();
        p1.newGame();
        initSquares();
        game.initPawns();
        repaint();
    }
    
    public void learn(){
        LearningSimulation simulation = new LearningSimulation(this);
        simulation.run();
    }

    public void paint(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
        initBoard(g2D);
        placePawns(g2D);
    }

    public void squareSelected(int x, int y) {
        if (game.winner!=0) return;
        for (int i = 0; i < game.squares.length; i++) {
            for (int j = 0; j < game.squares.length; j++) {
                if (game.squares[i][j].containsPt(x, y)) {
                    if (game.findPawn(i, j, true) != null) {
                        selected = game.findPawn(i, j, true);
                    } else {
                        game.squares[i][j].setColor(new Color(100, 200, 150));
                        game.squares[i][j].setSelected(true);
                    }
                }
            }
        }
        repaint();
    }

    public void resetSquares() {
        for (int i = 0; i < game.squares.length; i++) {
            for (int j = 0; j < game.squares.length; j++) {
                if (game.squares[i][j].isSelected() && selected != null) {
                    Move m = new Move(selected.x, selected.y, i, j, true);
                    if(game.isLegalMove(m)){
                        game.playTurn(m);
                        selected = null;
                        repaint();
                        if (game.checkWinner()==0)game.playTurn(p2.playMove(game.findAllLegalMoves(), game));
                        repaint();
                    }
                }
                game.squares[i][j].setColor(game.squares[i][j].getMainColor());
                game.squares[i][j].setSelected(false);
            }
        }
        repaint();
    }
    
    public String checkWin(){
        if (game.checkWinner()==1) return "Player won";
        if (game.checkWinner()==-1) return "Computer won";
        return "Playing Game";
    }

    /**
     * Creates new Image from file at a location
     *
     * @param file file link (ex: "src/mypackage/myimage.png")
     * @param g Graphics 2D
     * @param x x position (left)
     * @param y y position (top)
     * @param w width
     * @param h height
     */
    private void drawImage(String file, Graphics g, int x, int y, int w, int h) {
        Graphics2D g1 = (Graphics2D) g;
        BufferedImage preImg = null;
        try {
            preImg = ImageIO.read(new File(file));
        } catch (IOException e) {
            System.out.println("image not read");
        }
        Image img = preImg.getScaledInstance(w, h, 1);
        g1.drawImage(img, x, y, this);
    }

    private void drawPawn(Graphics2D g, Pawn p) {
        if (!p.alive) {
            return;
        }
        String[] paths = {"src/images/whitePawn.png", "src/images/blackPawn.png"};
        if (selected == p) {
            paths[0] = "src/images/whitePawnSelected.png";
            paths[1] = "src/images/blackPawnSelected.png";
        }
        Square sq = game.squares[p.x][p.y];
        drawImage(paths[p.black ? 1 : 0], g, sq.x, sq.y, sqLength, sqLength);
    }

    private void placePawns(Graphics2D g) {
        for (Pawn p : game.blackPawns) {
            drawPawn(g, p);
        }
        for (Pawn p : game.whitePawns) {
            drawPawn(g, p);
        }
    }

    private void initBoard(Graphics2D g) {
        for (Square[] row : game.squares) {
            for (Square square : row) {
                g.setColor(square.getColor());
                g.fill(square);
            }
        }
    }

    private void initSquares() {
        for (int i = 0; i < game.squares.length; i++) {
            for (int j = 0; j < game.squares.length; j++) {
                Color c = new Color((((j + game.squares.length * i) + ((game.squares.length % 2 == 0 && i % 2 == 1) ? 1 : 0)) % 2) * 50 + 105,
                        (((j + game.squares.length * i) + ((game.squares.length % 2 == 0 && i % 2 == 1) ? 1 : 0)) % 2) * 40 + 60,
                        (((j + game.squares.length * i) + ((game.squares.length % 2 == 0 && i % 2 == 1) ? 1 : 0)) % 2) * 60);
                game.squares[i][j] = new Square(c, i * sqLength, j * sqLength, sqLength, sqLength);
            }
        }
    }

    

}
