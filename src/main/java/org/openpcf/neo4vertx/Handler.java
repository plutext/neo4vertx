package org.openpcf.neo4vertx;

/**
 * The Handler interface.
 *
 * @author https://github.com/phifty[Philipp Brüll]
 */
public interface Handler<T> {

    public void handle(T value);

}
