package nl.quintor.solitaire.ui.cli;

import nl.quintor.solitaire.game.moves.Move;
import nl.quintor.solitaire.models.state.GameState;
import nl.quintor.solitaire.ui.UI;

import java.io.IOException;
import java.util.Collection;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Command line implementation of {@link UI}.
 */
public class CommandLineUI implements UI{
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_RESET = "\u001B[0m";
    private String message = "";
    private Scanner reader = new Scanner(System.in);

    public void setMessage(String message){
        this.message = message;
    }

    public void setErrorMessage(String message){
        this.message = System.getProperty("os.name").contains("Windows") ? message : ANSI_RED + message + ANSI_RESET;
    }

    public void refresh(GameState gameState){
        clrscr();
        System.out.println(GameStateParser.parseGameState(gameState));
        if (message.length() != 0) System.out.println(message);
    }

    public String refreshAndRequestMove(GameState gameState, Collection<Move> moves){
        refresh(gameState);
        System.out.println(parseMoves(moves));
        return requestMove();
    }

    /**
     * Creates a string representation of the provided collection of moves.
     *
     * @param moves moves to be represented
     * @return String representation of the moves collection
     */
    private String parseMoves(Collection<Move> moves){
        return moves.stream().map(Move::toString).collect(Collectors.joining(", "));
    }

    /**
     * Politely requests player input and returns it as a String.
     *
     * @return player input
     */
    private String requestMove(){
        System.out.println("What would you like to do?");
        return reader.nextLine();
    }

    /**
     * Clears the screen. Differentiates between Windows and Linux/MacOS.
     */
    private static void clrscr(){
        //Clears Screen in java
        try {
            if (System.getProperty("os.name").contains("Windows"))
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            else
                System.out.print("\033\143");
        } catch (IOException | InterruptedException ex) {
            throw new RuntimeException("Screen clearing error");
        }
    }
}
