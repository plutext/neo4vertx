package org.openpcf.neo4vertx;

/**
 * The FakeHandler object.
 *
 * @author Philipp Brüll <b.phifty@gmail.com>
 */
public class FakeHandler<T> implements Handler<T> {

    private T value;

    @Override
    public void handle(T value) {
        this.value = value;
    }

    public void reset() {
        value = null;
    }

    public T getValue() {
        return value;
    }

}
