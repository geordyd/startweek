package nl.quintor.solitaire.models.card;

/**<p>
 * Basic Card class. Each card has a {@link Suit} and a {@link Rank}, which are translated to an ordinal
 * value much like an {@link Enum}'s. The ordinal is final and unique, and forms the basis of the
 * {@link #hashCode()} and {@link #equals(Object)} methods. The ordinal also forms the basis for the natural
 * ordering of the Card instances. This order can be overridden in a {@link java.util.Collection} using a
 * {@link java.util.Comparator}{@literal <}{@link Card}{@literal >}.
 */
public final class Card implements Comparable<Card> {
    private final Suit suit;
    private final Rank rank;
    private final int ordinal;
    private String description;
    private String shortDescription;

    /**
     * Create a new Card object with the provided {@link Suit} and {@link Rank}. The Card's ordinal value is
     * calculated from the Suit and Rank.
     *
     * @param suit the {@link Suit} of the new card
     * @param rank the {@link Rank} of the new card
     */
    public Card(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
        this.ordinal = suit.ordinal() * 13 + rank.ordinal();
        this.setDescriptions();
    }

    /**
     * Create a new Card object with the provided ordinal. The Card's {@link Suit} and {@link Rank} are calculated
     * from the ordinal.
     *
     * @param ordinal 0-53 value corresponding to a single Card object
     */
    public Card(int ordinal) {
        if (ordinal < 0 || ordinal > 53) throw new IllegalArgumentException("0 <= ordinal <= 53");
        this.ordinal = ordinal;
        this.suit = Suit.values()[ordinal / 13];
        if (this.suit != Suit.JOKER) this.rank = Rank.values()[ordinal % 13];
        else this.rank = Rank.values()[ordinal % 13 + 13];
        this.setDescriptions();
    }

    /**
     * Suit getter
     *
     * @return suit
     */
    public Suit getSuit() {
        return suit;
    }

    /**
     * Rank getter
     *
     * @return rank
     */
    public Rank getRank() {
        return rank;
    }

    /**
     * Ordinal getter
     *
     * @return ordinal
     */
    public int getOrdinal() {
        return ordinal;
    }

    private void setDescriptions() {
        this.description = suit + " " + rank;
        this.shortDescription = suit.getSymbol() + " " + rank.getSymbol();
    }

    /**
     * Compares this Card to another Card, provided to the method, by ordinal.
     * If this Card has a lower ordinal than the argument Card, compareTo() returns a negative integer.
     * If this Card has a higher ordinal than the argument Card, compareTo() returns a positive integer.
     * If the Cards' ordinals are equal, compareTo() returns 0.
     *
     * @param that the Card object that this Card is compared with
     * @return positive int if this Card follows that Card, negative int if this Card precedes that Card, 0 if they
     * are equal
     */
    @Override
    public int compareTo(Card that) {
        return this.ordinal - that.ordinal;
    }

    /**
     * Returns true if the method argument o is a Card and has the same ordinal value as this Card.
     *
     * @param o object to be compared to this card
     * @return true if equal, false if unequal
     */
    @Override
    public boolean equals(Object o) {
        return o instanceof Card && ((Card) o).ordinal == this.ordinal;
    }

    /**
     * Returns the Card's hashCode (the unique ordinal is used as a hash).
     *
     * @return Card hash (ordinal)
     */
    @Override
    public int hashCode() {
        return ordinal;
    }

    /**
     * Returns the Card's long description, for example "SPADES KING".
     *
     * @return full String description of the Card
     */
    @Override
    public String toString() {
        return description;
    }

    /**
     * Returns the Card's short description, for example "♤ K".
     *
     * @return short String description of the Card
     */
    public String toShortString() {
        return shortDescription;
    }
}
