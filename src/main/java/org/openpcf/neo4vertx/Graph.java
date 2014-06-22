package org.openpcf.neo4vertx;

/**
 * The Graph interface.
 *
 * @author Philipp Brüll <b.phifty@gmail.com>
 * @since 2012-12-13
 * @version 1.0.0
 */
public interface Graph {

    public Nodes nodes();

    public Relationships relationships();

    public Complex complex();

    public void clear(Handler<Boolean> handler) throws Exception;

    public void shutdown();

}
