package cryptogame.common.interfaces;

/**
 * Interface for classes that require initialization.
 */
public interface Initializable {
    /**
     * The default implementation of the initialize method.
     */
    default void initialize() {
        // empty
    }
}
