package cryptogame.utils.interfaces;

/**
 * Interface for classes that may need to be explicitly refreshed to acquire the current state.
 */
public interface Refreshable {
    /**
     * The method that needs to be called to acquire the current state in the object
     * that implements the interface.
     */
    void refresh();
}
