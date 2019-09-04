package nl.quintor.solitaire.game.moves

import nl.quintor.solitaire.TestUtil
import spock.lang.Specification


class QuitMoveSpec extends Specification {

    def 'When quit move is called move return "Game over" and gamestate.gameLost is true'() {
        given:
            def gameState = TestUtil.createGame(0L)
        when:
            new Quit().apply(gameState)
        then:
            gameState.isGameLost() == true
    }

    def 'Quit toString method should return "Q̲uit"' () {
        when:
        def move = new Quit().createInstance("test")
        then:
        move.toString() == "Q̲uit"
    }

}