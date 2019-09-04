package nl.quintor.solitaire.game.moves

import nl.quintor.solitaire.TestUtil
import nl.quintor.solitaire.game.moves.ex.MoveException
import spock.lang.Specification
import spock.lang.Unroll


class DummyMoveSpec extends Specification {

    @Unroll
    def "Dummy move should throw #exception.getSimpleName() with message #exceptionMessage given a player input of #playerInput" () {
        when:
            new Dummy().createInstance(playerInput).apply(gameState)
        then:
            def ex = thrown(exception)
            ex.message == exceptionMessage
        where:
        gameState | playerInput | exceptionMessage | exception
        null |null | "Unknown move played: null" | MoveException
        TestUtil.createGame(1L)|"ABC"|"Unknown move played: ABC"|MoveException
    }

    def 'Dummy toString method should return "Dummy"' () {
        when:
            def move = new Dummy()
        then:
            move.toString() == "Dummy"
    }

}