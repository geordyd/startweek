package nl.quintor.solitaire.game.moves;

import nl.quintor.solitaire.models.state.GameState;

/**
 * Class that represents a player action to quit the game.
 */
public class Quit implements Move{
    private final static String name = System.getProperty("os.name").contains("Windows") ? "Quit" : "Q̲uit";

    /**
     * Sets the {@link GameState#gameLost} boolean to true.
     *
     * @param gameState GameState object to which this move will be applied
     * @return "Game Over"
     */
    @Override
    public String apply(GameState gameState){
        // TODO: Write implementation
        return "";
    }

    @Override
    public Move createInstance(String playerInput) {
        return new Quit();
    }

    @Override
    public String toString(){
        // TODO: Write implementation
        return null;
    }
}
