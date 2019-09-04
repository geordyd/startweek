package nl.quintor.solitaire.game

import nl.quintor.solitaire.TestUtil
import nl.quintor.solitaire.models.card.Card
import nl.quintor.solitaire.models.card.Rank
import nl.quintor.solitaire.models.card.Suit
import nl.quintor.solitaire.models.deck.DeckType
import nl.quintor.solitaire.models.state.GameState
import org.assertj.core.api.SoftAssertions
import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalDateTime


class GameStateControllerSpec extends Specification {

    def "init should create a valid game state" () {
        given:
            def softAssert = new SoftAssertions()

        when:
            def ResultingGameState = GameStateController.init()
        then:
            notThrown(Exception)
            with(ResultingGameState) {
                stock.size() == 1
                stock.deckType == DeckType.STOCK
                waste.size() == 23
                waste.deckType == DeckType.WASTE
                stackPiles.size() == 4
                stackPiles.values().forEach({deck-> deck.deckType == DeckType.STACK})
                columns.size() == 7
                columns.forEach { key, cards ->
                    switch (key){
                        case "A":
                            softAssert.assertThat(cards).hasSize(1)
                            softAssert.assertThat(cards.getInvisibleCards()).isEqualTo(0)
                            break;
                        case "B":
                            softAssert.assertThat(cards).hasSize(2)
                            softAssert.assertThat(cards.getInvisibleCards()).isEqualTo(1)
                            break;
                        case "C":
                            softAssert.assertThat(cards).hasSize(3)
                            softAssert.assertThat(cards.getInvisibleCards()).isEqualTo(2)
                            break;
                        case "D":
                            softAssert.assertThat(cards).hasSize(4)
                            softAssert.assertThat(cards.getInvisibleCards()).isEqualTo(3)
                            break
                        case "E":
                            softAssert.assertThat(cards).hasSize(5)
                            softAssert.assertThat(cards.getInvisibleCards()).isEqualTo(4)
                            break
                        case "F":
                            softAssert.assertThat(cards).hasSize(6)
                            softAssert.assertThat(cards.getInvisibleCards()).isEqualTo(5)
                            break
                        case "G":
                            softAssert.assertThat(cards).hasSize(7)
                            softAssert.assertThat(cards.getInvisibleCards()).isEqualTo(6)
                            break
                    }
                    cards.deckType == DeckType.COLUMN
                }
                startTime < LocalDateTime.now()
                score == 0
                stockCycles == 0
            }
            softAssert.assertThat(ResultingGameState.stackPiles).containsOnlyKeys("SA", "SB", "SC", "SD")
            softAssert.assertThat(ResultingGameState.columns).containsOnlyKeys("A", "B", "C", "D", "E", "F", "G")
            softAssert.assertAll()
    }

    @Unroll
    def "Win detection should return #expectedResult when testing: #testCase" () {
        expect:
            GameStateController.detectGameWin(gameState)
            gameState.isGameWon() == expectedResult
        where:
        gameState                          | expectedResult || testCase
        GameStateController.init()         | false          || "New game"
        TestUtil.createGameWithEmptyStockAndWaste() | false          || "Empty stock and waste but invisible cards left in columns"
        TestUtil.createGameWithOnlyVisibleCards()   | true           || "Empty stock and waste but visible cards left in columns"
    }

    @Unroll
    def "A bonus should be given of #expectedTimeScore when testing: #testCase" () {
        expect:
            GameStateController.applyBonusScore(gameState)
            gameState.timeScore == expectedTimeScore
        where:
        gameState| expectedTimeScore | testCase
        TestUtil.createGame(29L) | 0 | "Game less than 30 seconds"
        TestUtil.createGame(30L) | 0 | "Game exactly 30 seconds"
        TestUtil.createGame(31L) | 22580 | "Game exactly 31 seconds"
        TestUtil.createGame(500L) | 1400 | "Game of 500 seconds"
    }

    @Unroll
    def "A penalty should be applied of #expectedPenalty based on a game duration of #duration" () {
        expect:
            GameStateController.applyTimePenalty(gameState)
            gameState.timeScore == expectedPenalty
        where:
            gameState | duration || expectedPenalty
            TestUtil.createGame(29L) | 29 || -4
            TestUtil.createGame(30L) | 30 || -6
            TestUtil.createGame(500L) | 500 || -100

    }
}