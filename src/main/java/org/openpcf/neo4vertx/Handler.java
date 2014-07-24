package org.openpcf.neo4vertx;

/**
 * The Handler interface.
 *
 * @author mailto:b.phifty@gmail.com[Philipp Brüll]
 */
public interface Handler<T> {

    public void handle(T value);

}
