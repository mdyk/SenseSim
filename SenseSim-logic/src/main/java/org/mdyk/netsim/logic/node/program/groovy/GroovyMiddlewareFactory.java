package org.mdyk.netsim.logic.node.program.groovy;

import org.mdyk.netsim.logic.node.MiddlewareFactory;
import org.mdyk.netsim.logic.node.program.Middleware;


public class GroovyMiddlewareFactory implements MiddlewareFactory {

    @Override
    public Middleware buildMiddleware() {
        return new GroovyMiddleware();
    }

}
