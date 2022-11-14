package one.digitalinnovation.gof;
/**
 * Singleton "apressado".
 *
 * @autor silvanors
 */
public class SingletonEager {

    private static SingletonEager instancia = new SingletonEager();

    private SingletonEager() {
        super();
    }

    public static SingletonEager getInstancia() {
        return instancia;
    }
}
