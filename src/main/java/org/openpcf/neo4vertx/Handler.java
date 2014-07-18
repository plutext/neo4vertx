package org.openpcf.neo4vertx;

/**
 * The Handler interface.
 *
 * @author Philipp Brüll <b.phifty@gmail.com>
 */
public interface Handler<T> {

    public void handle(T value);

}
