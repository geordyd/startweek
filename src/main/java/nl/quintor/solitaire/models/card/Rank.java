package nl.quintor.solitaire.models.card;

/**
 * Enum of card ranks, from 2 to Ace plus black and red Jokers. The ordering of the enum values
 * should NOT be changed.
 *
 * <p>
 * Every Rank has a symbol, which is used to generate {@link Card} descriptions. Under competent terminal implementations,
 * that is non-Windows terminals, this can be a Unicode symbol. In Windows 10 in 2018, it cannot.
 */
public enum Rank{
    TWO ("2", "2"),
    THREE ("3", "3"),
    FOUR("4", "4"),
    FIVE("5", "5"),
    SIX("6", "6"),
    SEVEN ("7", "7"),
    EIGHT ("8", "8"),
    NINE ("9", "9"),
    TEN ("10", "10"),
    JACK ("J", "J"),
    QUEEN ("Q", "Q"),
    KING ("K", "K"),
    ACE ("A", "A"),
    JOKER_BLACK("\u2606", "JB"),
    JOKER_RED("\u2605", "JR");

    private final String symbol;

    Rank(String competentSymbol, String windowsSymbol){
        this.symbol = System.getProperty("os.name").contains("Windows") ? windowsSymbol : competentSymbol;
    }

    /**
     * Returns the symbol representation of a {@link Card} rank, for example A for Ace.
     *
     * @return rank symbol
     */
    public String getSymbol() {
        return symbol;
    }
}
