package nl.quintor.solitaire.game.moves;

import nl.quintor.solitaire.game.moves.ex.MoveException;
import nl.quintor.solitaire.models.state.GameState;

/**
 * Class that represents a dummy move in case the player input does not match another move. Mainly to prevent
 * NullPointerExceptions in {@link nl.quintor.solitaire.Main}.
 */
public class Dummy implements Move{
    private String playerInput;

    public Dummy(){}

    public Dummy(String playerInput){
        this.playerInput = playerInput;
    }

    /**
     * Returns the player input as the message of a {@link MoveException}.
     *
     * @param gameState GameState object, which is ignored
     * @return never returns
     * @throws MoveException always, with player input as message
     */
    @Override
    public String apply(GameState gameState) throws MoveException {
        // TODO: Write implementation
        return "";
    }

    @Override
    public Move createInstance(String playerInput) {
        return new Dummy(playerInput);
    }

    @Override
    public String toString() {
        // TODO: Write implementation
        return null;
    }
}
