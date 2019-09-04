package nl.quintor.solitaire.game.moves

import nl.quintor.solitaire.TestUtil
import nl.quintor.solitaire.models.card.Card
import nl.quintor.solitaire.models.card.Rank
import nl.quintor.solitaire.models.card.Suit
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll


class MoveCardSpec extends Specification {

    @Shared
    def initialGameState = TestUtil.createFixedNewGame()

    def setupSpec() {
        initialGameState.stock << new Card(Suit.CLUBS, Rank.ACE)
    }

    @Unroll
    def "Given a invalid move: #move game state is not changed when testing: #testCase" (){
        when:
            new MoveCard().createInstance(move).apply(initialGameState)
        then:
            thrown(Exception)
        where:
          move    | testCase
          "M Z A" | "Illegal source move syntax"
          "M O X" | "Illegal destination move syntax"
          "M O A" | "Illegal move dont pass decklevel checks"
          "M SA O"| "Illegal move dont pass cardlevel checks"
    }

    @Unroll
    def "Given a valid move #move gamestate is changed testing: #testCase" (){
        when:
            def actualResult = new MoveCard().createInstance(move).apply(initialGameState)
        then:
            notThrown(Exception)
            actualResult == expectedResult
        where:
            move     | expectedResult | testCase
            "M O SD" |"Moved [♧ A] from O to SD"|"Legal move from stock to stack"
            "M O F"  |"Moved [♧ 8] from O to F"|"Legal move from stock to column"
            "M B1 A" |"Moved [♧ Q] from B1 to A"|"legal move from column to column"
            "M C2 SA"|"Moved [♧ 2] from C2 to SA"|"legal move from column to stack"
    }

}