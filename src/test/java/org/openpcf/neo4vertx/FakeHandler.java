package org.openpcf.neo4vertx;

/**
 * The FakeHandler object.
 *
 * @author mailto:b.phifty@gmail.com[Philipp Brüll]
 */
public class FakeHandler<T> implements Handler<T> {

    private T value;

    @Override
    public void handle(T value) {
        this.value = value;
    }
}
