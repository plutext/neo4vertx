package org.openpcf.neo4vertx;

/**
 * The FakeHandler object.
 *
 * @author https://github.com/phifty[Philipp Brüll]
 * @author https://github.com/fraik[Freek Alleman]
 *
 */
public class FakeHandler<T> implements Handler<T> {

    private T value;

    @Override
    public void handle(T value) {
        this.value = value;
    }

    public T getValue() {
        return this.value;
    }
}
