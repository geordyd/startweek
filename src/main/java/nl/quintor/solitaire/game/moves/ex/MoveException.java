package nl.quintor.solitaire.game.moves.ex;

/**
 * Class that represents an error in applying a {@link nl.quintor.solitaire.game.moves.Move}.
 */
public class MoveException extends Exception{
    public MoveException(String message){
        super(message);
    }
}
