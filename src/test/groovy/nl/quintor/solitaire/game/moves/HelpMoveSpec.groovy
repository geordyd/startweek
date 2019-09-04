package nl.quintor.solitaire.game.moves

import nl.quintor.solitaire.TestUtil
import spock.lang.Specification

class HelpMoveSpec extends Specification {

    def "Help move should return help text" () {
        given:
            def expectedMessage =
            """
You can give a command with the Capital letter followed by <Return>,
so in order to display these instructions, simply type "H + <Return>"!
You can simply hit <Return> to repeat the last command you entered.
The H̲elp command requires arguments. The syntax is: 

M Source Destination (case insensitive)

For example:
"M O SA" moves the top card from the Stock to the top of Stack Pile A
"M SB F" moves the top card from Stack Pile B to the end of Column F

Dutch Patience rules: http://www.patiencespel.nl/patiencespelregels.php
English Patience rules: http://digsolitaire.com/solitaire-rules.php
"""
        when:
            def actualMessage = new Help().createInstance("test").apply(TestUtil.createGame(30L))
        then:
        expectedMessage == actualMessage
    }

    def "Help move toString should return 'H̲elp'" () {
        expect:
            new Help().toString() == "H̲elp"
    }

}