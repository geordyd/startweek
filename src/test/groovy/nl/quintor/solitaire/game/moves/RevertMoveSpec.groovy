package nl.quintor.solitaire.game.moves

import nl.quintor.solitaire.TestUtil
import nl.quintor.solitaire.game.moves.ex.MoveException
import nl.quintor.solitaire.models.card.Card
import nl.quintor.solitaire.models.card.Rank
import nl.quintor.solitaire.models.card.Suit
import spock.lang.Specification

class RevertMoveSpec extends Specification {

    def "Revert move should throw MoveException when no previous moves have been played" () {
        given:
            def gameState = TestUtil.createGame(0L)
        when:
            new Revert().createInstance("R").apply(gameState)
        then:
            def exception = thrown(MoveException)
            exception.message == 'Cannot revert; no moves have been played.'
    }

    def "Given a game with one move played move can be reverted" () {
        given:
            def gameState = TestUtil.createFixedNewGame()
            new MoveCard().createInstance("M O F").apply(gameState)
            assert gameState.moves.size() == 1
        when:
            new Revert().createInstance("R").apply(gameState)
        then:
            with(gameState){
                moves.size() == 0
                stock[0] == new Card(Suit.CLUBS, Rank.EIGHT)
                columns["F"].get(5) == new Card(Suit.HEARTS, Rank.NINE)
            }
    }

    def "Given a game with zero move played cycle one time, cycle can be reverted" () {
        given:
        def gameState = TestUtil.createFixedNewGame()
        new  CycleStock().createInstance("C").apply(gameState)
        assert gameState.moves.size() == 1
        assert gameState.stock[1] == new Card(Suit.DIAMONDS, Rank.FIVE)
        when:
        new Revert().createInstance("R").apply(gameState)
        then:
        with(gameState){
            moves.size() == 0
            stock[0] == new Card(Suit.CLUBS, Rank.EIGHT)
            waste[0] == new Card(Suit.DIAMONDS, Rank.FIVE)
        }
    }

    def "Given a waste with one card cycle count is resetted and cards are back to waste" () {
        given:
        def gameState = TestUtil.createFixedNewGame()
        gameState.stock.add(new Card(Suit.CLUBS, Rank.EIGHT))
        new  CycleStock().createInstance("C").apply(gameState)
        new  CycleStock().createInstance("C").apply(gameState)
        assert gameState.moves.size() == 2
        assert gameState.stockCycles == 1
        assert gameState.stock[0] == new Card(Suit.CLUBS, Rank.EIGHT)
        when:
        new Revert().createInstance("R").apply(gameState)
        then:
        with(gameState){
            moves.size() == 1
            stock[0] == new Card(Suit.CLUBS, Rank.EIGHT)
            waste.size() == 0
            stockCycles == 0
        }
    }


    def "Given a game with multiple moves played last move can be reverted" () {
        given:
        def gameState = TestUtil.createFixedNewGame()
        new MoveCard().createInstance("M O F").apply(gameState)
        new MoveCard().createInstance("M B1 A").apply(gameState)
        assert gameState.moves.size() == 2

        when:
        new Revert().createInstance("R").apply(gameState)

        then:
            with(gameState){
                moves.size() == 1
                stock[0] == new Card(Suit.DIAMONDS, Rank.FIVE)
                columns["F"].get(6) == new Card(Suit.CLUBS, Rank.EIGHT)
                columns["A"].get(0) == new Card(Suit.HEARTS, Rank.KING)
                columns["B"].get(1) == new Card(Suit.CLUBS, Rank.QUEEN)
            }
    }

}