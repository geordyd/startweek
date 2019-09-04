package nl.quintor.solitaire

import nl.quintor.solitaire.game.GameStateController
import nl.quintor.solitaire.models.card.Card
import nl.quintor.solitaire.models.card.Rank
import nl.quintor.solitaire.models.card.Suit
import nl.quintor.solitaire.models.deck.Deck
import nl.quintor.solitaire.models.deck.DeckType
import nl.quintor.solitaire.models.state.GameState

import java.time.LocalDateTime
import java.util.stream.IntStream

class TestUtil {
    static  Deck createTestDeck(deckType, nrOfCards = 0, nrOfInvisible = 0){
        def result = new Deck(deckType)
        result.invisibleCards = nrOfInvisible
        def fullCards = Deck.createDefaultDeck()
        if (nrOfCards > 0){
            (0..nrOfCards).each {
                result.add(fullCards.remove(it))
            }
        }
        return result
    }

    static  Deck createTestDeckWithCards(deckType = DeckType.STOCK, specificCards){
        def result = new Deck(deckType)
        result.addAll(specificCards)
        return result
    }

    static  Collection<Deck> createTestColumnsCollections(int nrOfRows) {

        def result = new ArrayList<>(7)
        IntStream.range(0,7).forEach({result.add(new Deck())})
        Deck cards = Deck.createDefaultDeck()

        IntStream.range(0,nrOfRows)
            .forEach({result.stream().forEach({deck->deck.add(cards.remove(cards.size()-1))})})
        return result
    }

    static  Collection<Deck> setInvisiblecards(decks, nrOfInvisible) {
        decks.forEach({it.setInvisibleCards(nrOfInvisible)})
        return decks
    }

    static GameState createGame(duration) {
        def result = GameStateController.init()
        result.startTime = LocalDateTime.of(2000, 1, 1, 1, 1, 0)
        result.endTime = result.startTime.plusSeconds(duration)
        return result
    }


    static GameState createGameWithOnlyVisibleCards() {
        def result = createGameWithEmptyStockAndWaste()
        result.columns.values().forEach({deck->
            deck.setInvisibleCards(0)
        })
        return result
    }

    static GameState createGameWithEmptyStockAndWaste() {
        def result = GameStateController.init()
        result.stock.clear()
        result.waste.clear()
        return result
    }

    static GameState createFixedNewGame() {
        def result = GameStateController.init()
        result.stock.clear()
        result.stock.add(new Card(Suit.CLUBS, Rank.EIGHT))
        result.waste.clear()
        result.waste.add(new Card(Suit.DIAMONDS, Rank.FIVE))
        result.columns.values().forEach{
            deck -> deck.remove(deck.size()-1)
        }
        result.columns.get("A").add(new Card(Suit.HEARTS, Rank.KING))
        result.columns.get("B").add(new Card(Suit.CLUBS, Rank.QUEEN))
        result.columns.get("C").add(new Card(Suit.CLUBS, Rank.TWO))
        result.columns.get("D").add(new Card(Suit.SPADES, Rank.ACE))
        result.columns.get("E").add(new Card(Suit.CLUBS, Rank.TEN))
        result.columns.get("F").add(new Card(Suit.HEARTS, Rank.NINE))
        result.columns.get("G").add(new Card(Suit.CLUBS, Rank.JACK))
        result.getStackPiles().get("SA").add(new Card(Suit.CLUBS, Rank.ACE))
        return result
    }
}
