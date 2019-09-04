package nl.quintor.solitaire.game.moves;

import nl.quintor.solitaire.game.CardMoveChecks;
import nl.quintor.solitaire.game.moves.ex.MoveException;
import nl.quintor.solitaire.models.deck.Deck;
import nl.quintor.solitaire.models.deck.DeckType;
import nl.quintor.solitaire.models.state.GameState;

/**
 * Class that represents a player action to move a card or multiple cards. This is an action that influences the
 * {@link GameState}, is revertible and influences the {@link GameState#baseScore}. It contains several helper methods
 * to get the job done, and uses the {@link CardMoveChecks} library class to determine if the requested card move is legal.
 *
 * <p>To make reverting the move possible, instances of this class store the player input, the source deck, the cards moved,
 * the destination deck, whether or not a previously invisible card was exposed on the source deck, whether or not
 * the stock was cycled because the first card was removed from it, and finally the previous score.
 */
public class MoveCard implements RevertibleMove {
    private final static String name = System.getProperty("os.name").contains("Windows") ? "Move" : "M̲ove";
    private String playerInput;
    private Deck sourceDeck;
    private Deck movedCards;
    private Deck destinationDeck;
    private boolean showedInvisible = false;
    private boolean cycledStock = false;
    private long previousScore = 0;

    public MoveCard(){}

    public MoveCard(String playerInput){
        this.playerInput = playerInput;
    }

    @Override
    public Move createInstance(String playerInput) {
        return new MoveCard(playerInput);
    }

    /**
     * Moves a card. Applying this move requires additional player input in the form of two arguments: the source and
     * the destination, separated from the move command by spaces, case-insensitive.
     *
     * The move is checked by the {@link CardMoveChecks} class. If everything checks out, the move is executed.
     * If a previously invisible column card is exposed, it is turned face-upwards. The stock is cycled when appropriate.
     * Finally, the new score is calculated and applied, and this move is stored in {@link GameState#moves}.
     *
     * @param gameState GameState object to which this move will be applied if it is legal
     * @return result of this move if it is successfully applied
     * @throws MoveException on illegal move request
     */
    @Override
    public String apply(GameState gameState) throws MoveException {
        // TODO: Write implementation
        return null;
    }

    @Override
    public String revert(GameState gameState){
        // TODO: Write implementation
        return null;
    }

    @Override
    public String toString() {
        return name;
    }

    /**
     * Returns the index of the source card based on the first player input parameter, the locationToken. If the requested
     * card is in the stock or a stack pile, the last card of that deck is returned. If the requested card is located in
     * a column, the specified row number is returned. Assumes the locationToken is syntactically correct and cast to
     * uppercase.
     *
     * @param sourceDeck Deck in which the card is looked up
     * @param locationToken syntactically correct String representation of the source card location
     * @return index in Deck
     * @throws MoveException if no index could be determined
     */
    private int getCardIndex(Deck sourceDeck, String locationToken) throws MoveException {
        if (sourceDeck.getDeckType() == DeckType.STOCK || sourceDeck.getDeckType() == DeckType.STACK) return sourceDeck.size() - 1;
        if (sourceDeck.getDeckType() == DeckType.COLUMN){
            int row = Integer.parseInt(locationToken.substring(1));
            if (row >= sourceDeck.size()) throw new MoveException("Column " + locationToken.substring(0, 1) + " has no card " + row);
            return row;
        }
        throw new MoveException("Card index for token \n" + locationToken + "\n could not be determined.");
    }

    /**
     * Determines which deck of the {@link GameState} is requested by the provided locationToken.
     * Assumes the locationToken is syntactically correct and cast to uppercase.
     *
     * @param gameState GameState object from which the deck is returned
     * @param locationToken syntactically correct String representation of a card location
     * @return the deck represented by the locationToken
     * @throws MoveException if the deck could not be determined
     */
    private Deck getDeck(GameState gameState, String locationToken) throws MoveException {
        String deckHeader = locationToken.substring(0 ,1);
        if ("O".equals(deckHeader)) return gameState.getStock();
        if (gameState.getStackPiles().containsKey(locationToken)) return gameState.getStackPiles().get(locationToken);
        if (gameState.getColumns().containsKey(deckHeader)) return gameState.getColumns().get(deckHeader);
        throw new MoveException("Deck for token \n" + locationToken + "\n could not be determined.");
    }

    /**
     * Calculates a new {@link GameState#baseScore} based on the type of card move this instance represents and stores
     * the previous value.
     *
     * @param gameState GameState object that the method is applied to
     */
    private void addScore(GameState gameState){
        previousScore = gameState.getBaseScore();
        long newScore = previousScore;

        if (sourceDeck.getDeckType() == DeckType.STOCK && destinationDeck.getDeckType() == DeckType.COLUMN) newScore += 5;
        if (sourceDeck.getDeckType() == DeckType.STOCK && destinationDeck.getDeckType() == DeckType.STACK) newScore += 10;
        if (sourceDeck.getDeckType() == DeckType.COLUMN && destinationDeck.getDeckType() == DeckType.STACK) newScore += 10;
        if (showedInvisible) newScore += 5;
        if (sourceDeck.getDeckType() == DeckType.STACK) newScore -= 15;

        gameState.setBaseScore(newScore);
    }
}
