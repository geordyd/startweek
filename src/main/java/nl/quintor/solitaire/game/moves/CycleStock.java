package nl.quintor.solitaire.game.moves;

import nl.quintor.solitaire.game.moves.ex.MoveException;
import nl.quintor.solitaire.models.deck.Deck;
import nl.quintor.solitaire.models.state.GameState;

/**
 * Class that represents a player action to cycle the stock. This is an action that influences the {@link GameState}, is
 * revertible and influences the {@link GameState#baseScore}. It stores the previous score in case this move is reverted.
 */
public class CycleStock implements RevertibleMove {
    private final static String name = System.getProperty("os.name").contains("Windows") ? "Cycle stock" : "C̲ycle stock";
    private long previousScore = 0;

    @Override
    public Move createInstance(String playerInput) {
        return new CycleStock();
    }

    /**
     * Cycles the stock. This implementation uses two {@link Deck} objects, the {@link GameState#waste}
     * and the {@link GameState#stock}, to represent a deck
     * of down-faced and up-faced cards respectively. This greatly simplifies counting the number of stock cycles, which
     * influence the score. It is also possible to implement this method using only the stock deck, but if score
     * calculation is implemented this requires maintaining a pointer to the first or last card in order to count the
     * cycles. Since the first or last card can be removed, this gets complicated really quickly. The move is stored in
     * {@link GameState#moves}.
     *
     * @param gameState GameState object to which this move will be applied
     * @return result of cycling the stock, i.e. "Stock card 3 out of 14, cycle 1"
     * @throws MoveException on empty stock
     */
    @Override
    public String apply(GameState gameState) throws MoveException{
        // TODO: Write implementation
        return null;
    }

    @Override
    public String revert(GameState gameState){
        // TODO: Write implementation
        return "";
    }

    @Override
    public String toString() {
        return name;
    }

    /**
     * Subtracts a hundred points from {@link GameState#baseScore} and stores the previous value.
     *
     * @param gameState GameState object that the method is applied to
     */
    private void addScore(GameState gameState){
        previousScore = gameState.getBaseScore();
        gameState.setBaseScore(previousScore - 100);
    }
}
