/*
  _____      _ _                                 _        ___   ___  __  ___  
 / ____|    | | |                               | |      |__ \ / _ \/_ |/ _ \ 
| |     __ _| | |_   _ _ __ ___     ___ ___   __| | ___     ) | | | || | (_) |
| |    / _` | | | | | | '_ ` _ \   / __/ _ \ / _` |/ _ \   / /| | | || |> _ < 
| |___| (_| | | | |_| | | | | | | | (_| (_) | (_| |  __/  / /_| |_| || | (_) |
 \_____\__,_|_|_|\__,_|_| |_| |_|  \___\___/ \__,_|\___| |____|\___/ |_|\___/ 
 
 */
package cs448_hexapawn;

import java.util.ArrayList;

/**
 *
 * @author callumijohnston
 */
public class Game {

    Square[][] squares;
    Pawn[] blackPawns;
    Pawn[] whitePawns;
    boolean whiteTurn;
    int winner;

    public Game(int boardSize) {
        squares = new Square[boardSize][boardSize];
        blackPawns = new Pawn[squares.length];
        whitePawns = new Pawn[squares.length];
        whiteTurn = true;
        winner = 0;
        initPawns();
    }
    

    public void playTurn(Move m) {

        if (findPawn(m.x2, m.y2, false) != null) {
            findPawn(m.x2, m.y2, false).alive = false;
        }
        Pawn p = findPawn(m.x1, m.y1, true);
        p.x = m.x2;
        p.y = m.y2;
        switchTurn();
        winner = checkWinner();

    }

    public void randomMove() {
        ArrayList<Move> moves = findAllLegalMoves();
        if (moves.isEmpty()) {
            winner = checkWinner();
            return;
        }
        int randomInt = (int) (moves.size() * Math.random());
        playTurn(moves.get(randomInt));
    }

    public void switchTurn() {
        if (whiteTurn) {
            whiteTurn = false;
        } else {
            whiteTurn = true;
        }
    }

    public boolean isLegalMove(Move m) {
        if ((!m.p1 && m.y2 >= squares.length) || (m.p1 && m.y2 < 0)) {
            return false;
        }
        if (m.x2 < 0 || m.x2 >= squares.length) {
            return false;
        }
        if (m.x1 == m.x2 && m.y1 + (m.p1 ? -1 : 1) == m.y2 && findPawn(m.x2, m.y2, false) == null
                && findPawn(m.x2, m.y2, true) == null) {
            return true;
        }
        if (Math.abs(m.x2 - m.x1) == 1 && m.y1 + (m.p1 ? -1 : 1) == m.y2
                && findPawn(m.x2, m.y2, false) != null && findPawn(m.x2, m.y2, false).alive) {
            return true;
        }
        return false;
    }

    public Pawn findPawn(int x, int y, boolean ofTurn) {
        Pawn pawn = null;
        for (int i = 0; i < squares.length; i++) {
            if (whiteTurn == ofTurn) {
                if (whitePawns[i].x == x && whitePawns[i].y == y && whitePawns[i].alive) {
                    pawn = whitePawns[i];
                }
            } else {
                if (blackPawns[i].x == x && blackPawns[i].y == y && blackPawns[i].alive) {
                    pawn = blackPawns[i];
                }
            }
        }

        return pawn;
    }

    public ArrayList<Move> findAllLegalMoves() {
        ArrayList<Move> legalMoves = new ArrayList<>();
        Pawn[] pieces;
        if (whiteTurn) {
            pieces = whitePawns;
        } else {
            pieces = blackPawns;
        }
        for (Pawn p : pieces) {
            if (p.alive) {
                for (int i = Math.max(0, p.x - 1); i <= Math.min(p.x + 1, squares.length - 1); i++) {
                    Move m = new Move(p.x, p.y, i, p.y + (whiteTurn ? -1 : 1), whiteTurn);
                    if (isLegalMove(m)) {
                        legalMoves.add(m);
                    }
                }
            }
        }
        return legalMoves;
    }

    public int checkWinner() {
        if (findAllLegalMoves().isEmpty()) {
            return whiteTurn ? -1 : 1;
        }
        for (Pawn p : whitePawns) {
            if (p.y == 0) {
                return 1;
            }
        }
        for (Pawn p : blackPawns) {
            if (p.y == squares.length - 1) {
                return -1;
            }
        }
        return 0;
    }

    public void initPawns() {
        for (int i = 0; i < squares.length; i++) {
            blackPawns[i] = new Pawn(i, 0, true);
            whitePawns[i] = new Pawn(i, squares.length - 1, false);
        }
    }

    public Game getGame() {
        Game newGame = new Game(squares.length);
        for (int i = 0; i < squares.length; i++) {
            newGame.blackPawns[i] = new Pawn(blackPawns[i].x, blackPawns[i].y, true);
            newGame.whitePawns[i] = new Pawn(whitePawns[i].x, whitePawns[i].y, false);
        }
        newGame.whiteTurn = whiteTurn;
        newGame.winner = winner;
        return newGame;
    }

    public String toString() {
        String returnMe = "Game: \n";
        for (int i = 0; i < squares.length+1; i++) {
            returnMe+="*";
        }
        for (int i = 0; i < squares.length; i++) {
            returnMe+="*\n*";
            for (int j = 0; j < squares[i].length; j++) {
                Pawn p1 = findPawn(j, i, whiteTurn);
                Pawn p2 = findPawn(j, i, !whiteTurn);
                if (p1 == null && p2 == null) {
                    returnMe += "-";
                } else {
                    if (whiteTurn) {
                        if (p1 != null) {
                            returnMe += "O";
                        }
                        if (p2 != null) {
                            returnMe += "X";
                        }
                    } else {
                        if (p1 != null) {
                            returnMe += "X";
                        }
                        if (p2 != null) {
                            returnMe += "O";
                        }
                    }
                }
            }
        }
        returnMe+="*\n*";
        for (int i = 0; i < squares.length+1; i++) {
            returnMe+="*";
        }
        return returnMe;
    }

}
