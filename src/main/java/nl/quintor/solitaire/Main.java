package nl.quintor.solitaire;

import nl.quintor.solitaire.game.GameStateController;
import nl.quintor.solitaire.game.moves.*;
import nl.quintor.solitaire.game.moves.ex.MoveException;
import nl.quintor.solitaire.models.state.GameState;
import nl.quintor.solitaire.ui.UI;
import nl.quintor.solitaire.ui.cli.CommandLineUI;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


/**
 * Application entry point
 */
public class Main {
    /**
     * Application entry point. Consists of three phases: initialization, game loop and game shutdown. During the
     * initialization phase, the UI, game state and possible moves are created. The game loop is entered, which runs as
     * long as the game is not over. The game loop essentially consists of:
     *
     * <ul>
     *     <li>visualize GameState object
     *     <li>request input
     *     <li>translate input into a Move
     *     <li>apply the Move to the GameState object
     *     <li>communicate the result to the player
     * </ul>
     *
     * When the game loop exits, the result of the game is communicated to the player and the UI is refreshed one final
     * time.
     *
     * @param args the command-line arguments
     */
    public static void main(String... args){
        // initialize the GameState, UI and all possible moves
        UI ui = new CommandLineUI();
        GameState gameState = GameStateController.init();
        List<String> keys = Arrays.asList("C", "M", "R", "H", "Q");
        List<Move> moves = Arrays.asList(new CycleStock(), new MoveCard(), new Revert(), new Help(), new Quit());
        HashMap<String, Move> possibleMoves = new HashMap<>();
        for (int i = 0; i<keys.size(); i++) possibleMoves.put(keys.get(i), moves.get(i));

        // start the game by displaying the help info
        ui.setMessage(new Help().apply(gameState));
        String previousInput = "H";

        // game loop
        while (!gameState.isGameOver()) {
            // show gamestate to the player and ask for next move
            String playerInput = ui.refreshAndRequestMove(gameState, moves).toUpperCase();

            // default to the previous input if this input is empty (if the player hits <Return>)
            playerInput = playerInput.length() == 0 ? previousInput : playerInput;
            Move move = possibleMoves
                .getOrDefault(playerInput.substring(0,1), new Dummy()) // default to dummy if the player enters nonsense
                .createInstance(playerInput);
            previousInput = playerInput;

            try{
                ui.setMessage(move.apply(gameState));
                GameStateController.detectGameWin(gameState);
                GameStateController.applyTimePenalty(gameState);
            } catch (MoveException e){
                ui.setErrorMessage(e.getMessage());
            }
        }

        // if the game is over, add the time bonus and refresh one final time but don't show any possible moves
        if (gameState.isGameWon()){
            GameStateController.applyBonusScore(gameState);
            ui.setMessage("Congratulations, you beat the game!!! " + gameState.toString());
        }
        ui.refresh(gameState);
    }
}
