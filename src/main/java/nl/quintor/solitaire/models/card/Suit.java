package nl.quintor.solitaire.models.card;

/**
 * Enum of card suits, ordered according to Bridge rules plus Jokers. The ordering of the enum values
 * should NOT be changed.
 *
 * <p>
 * Every Suit has a symbol, which is used to generate {@link Card} descriptions. Under competent terminal implementations,
 * that is non-Windows terminals, this can be a Unicode symbol. In Windows 10 in 2018, it cannot.
 */
public enum Suit {
    CLUBS ("\u2667", "C"),
    DIAMONDS ("\u2666", "D"),
    HEARTS ("\u2665", "H"),
    SPADES ("\u2664", "S"),
    JOKER("*", "*");

    private final String symbol;

    Suit(String competentSymbol, String windowsSymbol){
        this.symbol = System.getProperty("os.name").contains("Windows") ? windowsSymbol : competentSymbol;
    }

    /**
     * Returns the symbol representation of a {@link Card} suit, for example ♥ for Hearts (Linux / MacOS) or "H"
     * (Windows).
     *
     * @return suit symbol
     */
    public String getSymbol() {
        return symbol;
    }
}
