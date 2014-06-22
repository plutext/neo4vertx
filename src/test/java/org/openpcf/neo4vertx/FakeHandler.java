package org.openpcf.neo4vertx;

/**
 * The FakeHandler object.
 *
 * @author Philipp Brüll <b.phifty@gmail.com>
 * @since 2012-12-13
 * @version 1.0.0
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
