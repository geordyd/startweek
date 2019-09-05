package nl.quintor.solitaire.game;

import nl.quintor.solitaire.game.moves.Help;
import nl.quintor.solitaire.game.moves.ex.MoveException;
import nl.quintor.solitaire.models.card.Card;
import nl.quintor.solitaire.models.card.Rank;
import nl.quintor.solitaire.models.card.Suit;
import nl.quintor.solitaire.models.deck.Deck;
import nl.quintor.solitaire.models.deck.DeckType;

import java.util.List;

/**
 * Library class for card move legality checks. The class is not instantiable, all constructors are private and all methods are
 * static. The class contains several private helper methods. All methods throw {@link MoveException}s, which can
 * contain a message that is fed to the {@link nl.quintor.solitaire.ui.UI}-implementation as error messages to be
 * shown to the user.
 */
public class CardMoveChecks {
    private CardMoveChecks(){}
    private final static String helpInstructions = new Help().toString();

    /**
     * Verifies that the player input for a CardMove is syntactically legal. Legal input consists of three parts:
     * the move command "M", the source location and the destination location.
     * The source location has to be the stock header, a stack header or a column coordinate.
     * The destination location has to be the stock header, a stack header or a column header (the column row is not
     * relevant because cards can only be added at the end of a column). The method verifies the syntax using regular
     * expressions.
     *
     * @param input the user input, split on the space character, cast to uppercase
     * @throws MoveException on syntax error
     */
    public static void checkPlayerInput(String[] input) throws MoveException{
        List<String> sourceCoordStart = List.of(
            "A0", "A1", "A2", "A3", "A4", "A5", "A6", "A7",
            "B0", "B1", "B2", "B3", "B4", "B5", "B6", "B7",
            "C0", "C1", "C2", "C3", "C4", "C5", "C6", "C7",
            "D0", "D1", "D2", "D3", "D4", "D5", "D6", "D7",
            "E0", "E1", "E2", "E3", "E4", "E5", "E6", "E7",
            "F0", "F1", "F2", "F3", "F4", "F5", "F6", "F7",
            "G0", "G1", "G2", "G3", "G4", "G5", "G6", "G7",
            "O",
            "SA", "SB", "SC", "SD");
        List<String> destCoordStart = List.of(
            "A", "B", "C", "D", "E", "F", "G", "O", "SA", "SB", "SC", "SD");

        if (!sourceCoordStart.contains(input[1])){
            throw new MoveException("Invalid Move syntax. " + "\"" + input[1] + "\"" + " is not a valid source location.\nSee H̲elp for instructions.");
        }
        if (!destCoordStart.contains(input[2])){
            throw new MoveException("Invalid Move syntax. " + "\"" + input[2] + "\"" + " is not a valid destination location.\nSee H̲elp for instructions.");
        }

    }

    /**
     * Verifies that a card move is possible given the source deck, the source card index and the destination deck.
     * Assumes that the {@link #checkPlayerInput(String[])} checks have passed.
     * {@link Deck} objects have a {@link DeckType} that is used in this method. The rank and suit of the actual cards
     * are not taken into consideration here.
     *
     * @param sourceDeck deck that the card(s) originate from
     * @param sourceCardIndex index of the (first) card
     * @param destinationDeck deck that the card(s) will be transferred to
     * @throws MoveException on illegal move
     */
    public static void deckLevelChecks(Deck sourceDeck, int sourceCardIndex, Deck destinationDeck) throws MoveException {
        int invisibleCards = sourceDeck.getInvisibleCards();

        if (sourceDeck == destinationDeck){
            throw new MoveException("Move source and destination can't be the same");
        }
        if (sourceDeck.isEmpty()){
            throw new MoveException("You can\'t move a card from an empty deck");
        }
        if (destinationDeck.getDeckType() == DeckType.STOCK){
            throw new MoveException("You can\'t move cards to the stock");
        }
        if (sourceCardIndex < invisibleCards){
            throw new MoveException("You can\'t move an invisible card");
        }
        if (sourceCardIndex != (sourceDeck.size() - 1) && destinationDeck.getDeckType() == DeckType.STACK){
            throw new MoveException("You can\'t move more than 1 card at a time to a Stack Pile");
        }
    }

    /**
     * Verifies that a card move is possible given the rank and suit of the card or first card to be moved. Assumes the
     * {@link #checkPlayerInput(String[])} and {@link #deckLevelChecks(Deck, int, Deck)} checks have passed. The checks
     * for moves to a stack pile or to a column are quite different, so the method calls one of two helper methods,
     * {@link #checkStackMove(Card, Card)} and {@link #checkColumnMove(Card, Card)}.
     *
     * @param targetDeck deck that the card(s) will be transferred to
     * @param cardToAdd (first) card
     * @throws MoveException on illegal move
     */
    public static void cardLevelChecks(Deck targetDeck, Card cardToAdd) throws MoveException {
        if (targetDeck.getDeckType() != DeckType.STACK && targetDeck.getDeckType() != DeckType.COLUMN){
            throw new MoveException("Target deck is neither Stack nor Column.");
        }
        if (targetDeck.getDeckType() == DeckType.STACK && targetDeck.isEmpty()){
            if (cardToAdd.getRank() != Rank.ACE){
                throw new MoveException("An Ace has to be the first card of a Stack Pile");
            }
        }
        if (targetDeck.getDeckType() == DeckType.COLUMN && targetDeck.isEmpty()){
            if (cardToAdd.getRank() != Rank.KING){
                throw new MoveException("A King has to be the first card of a Column");
            }
        }

        Card lastCard = targetDeck.get(targetDeck.size() - 1);
        if (targetDeck.getDeckType() == DeckType.STACK){
            if (cardToAdd.getSuit() != lastCard.getSuit()){
                throw new MoveException("Stack Piles can only contain same-suit cards");
            }
        }
        if (targetDeck.getDeckType() == DeckType.STACK){
            if (cardToAdd.getOrdinal() != lastCard.getOrdinal() - 1){
                throw new MoveException("Stack Piles hold same-suit cards of increasing Rank from Ace to King");
            }
        }
        if (targetDeck.getDeckType() == DeckType.COLUMN){
            if (redSuit(cardToAdd) == redSuit(lastCard)){
                throw new MoveException("Column cards have te alternate colors (red and black)");
            }
        }
        if (targetDeck.getDeckType() == DeckType.COLUMN){
            if (cardToAdd.getOrdinal() != lastCard.getOrdinal() + 1){
                throw new MoveException("Columns hold alternating-color cards of decreasing rank from King to Two");
            }
        }
    }

    // Helper methods

    /**
     * Verifies that the proposed move is legal given that the targetCard is the top of a stack pile.
     *
     * @param targetCard top card of a stack or null if the stack is empty
     * @param cardToAdd card to add to the stack
     * @throws MoveException on illegal move
     */
    static void checkStackMove(Card targetCard, Card cardToAdd) throws MoveException {
        // TODO: Write implementation
    }

    /**
     * Verifies that the proposed move is legal given that the targetCard is the last card of a column.
     *
     * @param targetCard last card of a column or null if the column is empty
     * @param cardToAdd card to add to the column
     * @throws MoveException on illegal move
     */
    static void checkColumnMove(Card targetCard, Card cardToAdd) throws MoveException {
        /// TODO: Write implementation
    }

    /**
     * Helper method to determine if the provided cards are of opposing color (red versus black).
     *
     * @param card1 first card
     * @param card2 second card
     * @return true if the cards are of different colors
     */
    static boolean opposingColor(Card card1, Card card2){
        return redSuit(card1) != redSuit(card2);
    }

    /**
     * Helper method to determine if the card's suit is colored red (Diamonds or Hearts).
     *
     *
     * @param card card to be tested for red color
     * @return true if card is either of suit Diamonds or Hearts
     * @throws RuntimeException exception when Joker card is checked with message 'Method redSuit() should not be used with Jokers'
     */
    static boolean redSuit(Card card){
        if (card.getSuit() == Suit.JOKER) throw new RuntimeException("Method redSuit() should not be used with Jokers");
        return card.getSuit() == Suit.DIAMONDS || card.getSuit() == Suit.HEARTS;
    }
}
