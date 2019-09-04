package nl.quintor.solitaire.game.moves;

import nl.quintor.solitaire.models.state.GameState;

/**
 * Interface representing a move that is revertible. All revertible moves are moves that effect the {@link GameState},
 * otherwise there would be no point in reverting them.
 */
public interface RevertibleMove extends Move{
    /**
     * Reverts the effects of {@link #apply(GameState)} on the GameState object. The return value String can be shown
     * to the player by the UI as a message.
     *
     * @param gameState GameState object to which this move has been applied
     * @return message with the result of reverting this move
     */
    String revert(GameState gameState);
}
