package nl.quintor.solitaire.game.moves;

import nl.quintor.solitaire.models.state.GameState;

/**
 * Class that represents a player action to view the game instructions.
 */
public class Help implements Move{
    private final static String name = System.getProperty("os.name").contains("Windows") ? "Help" : "H̲elp";

    /**
     * Returns the help information for the UI to show the player. Does not influence the {@link GameState}.
     *
     * @param gameState GameState object, which is ignored
     * @return help information
     */
    @Override
    public String apply(GameState gameState) {
        // TODO: Write implementation
        return null;
    }

    @Override
    public Move createInstance(String playerInput) {
        return new Help();
    }

    @Override
    public String toString() {
        // TODO: Write implementation
        return null;
    }
}
