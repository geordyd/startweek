package nl.quintor.solitaire.game.moves;

import nl.quintor.solitaire.game.moves.ex.MoveException;
import nl.quintor.solitaire.models.state.GameState;

/**
 * Interface representing a game move. A game move is defined as an action that can be taken by the player, which may or
 * may not influence the {@link GameState}.
 */
public interface Move {
    /**
     * Apply this move (to the GameState). A {@link MoveException} is thrown if the move cannot be applied.
     * The reason will be supplied in the message. The return value String can be shown to the player by the UI as a
     * message. Likewise, the MoveException message can be shown as an error message.
     *
     * @param gameState GameState object to which this move will be applied
     * @return message with the result of applying this move
     * @throws MoveException on illegal move or error during move application
     */
    String apply(GameState gameState) throws MoveException;

    /**
     * Factory method to create a new instance of the implementing class. The new Move object can use the player input
     * as it wishes.
     *
     * @param playerInput the input of the player when requesting the action that this Move represents
     * @return new instance of the class that implements the Move interface
     */
    Move createInstance(String playerInput);

    /**
     * The name of the implementing class to show to the player. Override of {@link Object} method to force implementing
     * classes to implement the method.
     *
     * @return name of the implementing class, to identify the class to the player
     */
    @Override
    String toString();
}
