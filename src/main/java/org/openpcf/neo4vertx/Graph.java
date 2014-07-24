package org.openpcf.neo4vertx;

/**
 * The Graph interface.
 *
 * @author mailto:b.phifty@gmail.com[Philipp Brüll]
 */
public interface Graph {

    public Nodes nodes();

    public Relationships relationships();

    public Complex complex();

    public void clear(Handler<Boolean> handler) throws Exception;

    public void shutdown();

}
