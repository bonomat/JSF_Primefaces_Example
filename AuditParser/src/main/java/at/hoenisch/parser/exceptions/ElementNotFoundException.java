package at.hoenisch.parser.exceptions;

/**
 * Created by Philipp Hoenisch on 13/04/16.
 */
public class ElementNotFoundException extends Throwable {
    public ElementNotFoundException(String s) {
        super(s);
    }

    public ElementNotFoundException() {
        super();
    }
}
