package nl.quintor.solitaire.game.moves

import nl.quintor.solitaire.TestUtil
import nl.quintor.solitaire.game.moves.ex.MoveException
import nl.quintor.solitaire.models.card.Card
import nl.quintor.solitaire.models.card.Rank
import nl.quintor.solitaire.models.card.Suit
import nl.quintor.solitaire.models.deck.Deck
import spock.lang.Specification
import spock.lang.Unroll

import static org.assertj.core.api.Assertions.*


class CycleStockSpec extends Specification {

    /**
     * Cycles the stock. This implementation uses two {@link nl.quintor.solitaire.models.deck.Deck} objects, the {@link nl.quintor.solitaire.models.state.GameState#waste}
     * and the {@link nl.quintor.solitaire.models.state.GameState#stock}, to represent a deck
     * of down-faced and up-faced cards respectively. This greatly simplifies counting the number of stock cycles, which
     * influence the score. It is also possible to implement this method using only the stock deck, but if score
     * calculation is implemented this requires maintaining a pointer to the first or last card in order to count the
     * cycles. Since the first or last card can be removed, this gets complicated really quickly. The move is stored in
     * {@link nl.quintor.solitaire.models.state.GameState#moves}.
     *
     * @param gameState GameState object to which this move will be applied
     * @return result of cycling the stock, i.e. "Stock card 3 out of 14, cycle 1"
     * @throws nl.quintor.solitaire.game.moves.ex.MoveException on empty stock
     */

    def "Testing Cycle with empty stock and waste" () {
        given: "A empty stock and waste deck"
            def gameState = TestUtil.createGameWithEmptyStockAndWaste()
        when:"A Cycle move is payed"
            new CycleStock().createInstance("C").apply(gameState)
        then: "A exception is thrown with specific message"
            def exception = thrown(MoveException)
            exception.message == "Stock is empty"
    }

    def "Testing Cycle with empty waste" () {
        given: "A empty waste and one card in stock"
            def gameState = TestUtil.createGameWithEmptyStockAndWaste()
        gameState.stock << new Card(Suit.CLUBS, Rank.ACE)
        when:"A Cycle move is payed"
            new CycleStock().createInstance("C").apply(gameState)
        then:"the stock still has a card and cycle count is incremented"
            gameState.stockCycles == 1
            gameState.stock.size() == 1
            gameState.waste.size() == 0
    }

    def "Testing Cycle with empty waste and multiple cards in stock" () {
        given: "A empty waste and multiple cards in stock"
        def gameState = TestUtil.createGameWithEmptyStockAndWaste()
        gameState.stock << new Card(Suit.CLUBS, Rank.ACE)
        gameState.stock << new Card(Suit.CLUBS, Rank.TWO)
        when:"A Cycle move is payed"
        new CycleStock().createInstance("C").apply(gameState)
        then:"the stock still has a card and cycle count is incremented"
        gameState.stockCycles == 1
        gameState.stock.size() == 1
        gameState.waste.size() == 1
        println(gameState.stock)
    }

    @Unroll
    def """Given a stock having: #stock and a waste having: #waste cycling #cyclingTimes times should result in stock having: #resultingStock
         waste having: #resultingWaste and cycle count of #cycleCount"""() {
        given:
            def gameState = TestUtil.createGameWithEmptyStockAndWaste()
            assert gameState.stock.size() == 0
            gameState.stock.addAll(stock)
        assert gameState.stock.size() == stock.size()
            gameState.waste.addAll(waste)
        expect:
            for (i in 1..cyclingTimes) {
                new CycleStock().createInstance("C").apply(gameState)
            }
            assertThat(gameState.stock).containsExactly(resultingStock.toArray())
            assertThat(gameState.waste).containsSequence(resultingWaste.toArray())
            gameState.stockCycles == cycleCount
        where:
        stock | waste | cyclingTimes | cycleCount | resultingStock | resultingWaste
        [new Card(Suit.CLUBS, Rank.ACE)] as Deck | [] as Deck | 1 |1| [new Card(Suit.CLUBS, Rank.ACE)] as Deck | [] as Deck
        [new Card(Suit.CLUBS, Rank.ACE)] as Deck | [new Card(Suit.CLUBS, Rank.TWO)] as Deck | 1 |0| [new Card(Suit.CLUBS, Rank.ACE), new Card(Suit.CLUBS, Rank.TWO)] as Deck| [] as Deck
        [new Card(Suit.CLUBS, Rank.ACE), new Card(Suit.CLUBS, Rank.TWO)] as Deck | [] as Deck | 2 |1| [new Card(Suit.CLUBS, Rank.ACE), new Card(Suit.CLUBS, Rank.TWO)]as Deck | [] as Deck
        [] as Deck | [new Card(Suit.CLUBS, Rank.ACE), new Card(Suit.CLUBS, Rank.TWO)] as Deck| 1 |0| [new Card(Suit.CLUBS, Rank.ACE)] as Deck| [new Card(Suit.CLUBS, Rank.TWO)] as Deck

    }



}