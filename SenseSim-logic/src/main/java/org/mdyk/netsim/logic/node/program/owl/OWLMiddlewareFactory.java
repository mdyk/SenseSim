package org.mdyk.netsim.logic.node.program.owl;

import org.mdyk.netsim.logic.node.MiddlewareFactory;
import org.mdyk.netsim.logic.node.program.Middleware;

/**
 * Created by michal on 22.07.2016.
 */
public class OWLMiddlewareFactory implements MiddlewareFactory {
    @Override
    public Middleware buildMiddleware() {
        return new OWLMiddleware();
    }
}
