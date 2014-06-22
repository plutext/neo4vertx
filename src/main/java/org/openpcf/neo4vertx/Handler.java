package org.openpcf.neo4vertx;

/**
 * The Handler interface.
 *
 * @author Philipp Brüll <b.phifty@gmail.com>
 * @since 2012-12-13
 * @version 1.0.0
 */
public interface Handler<T> {

    public void handle(T value);

}
