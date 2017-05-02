package org.mdyk.netsim.logic.node.program.groovy;

import org.mdyk.netsim.logic.node.MiddlewareFactory;
import org.mdyk.netsim.logic.node.program.Middleware;
import sensesim.integration.mcop.MCopPluginFactory;

import javax.inject.Inject;
import javax.inject.Singleton;


@Singleton
public class GroovyMiddlewareFactory implements MiddlewareFactory {

    @Inject
    private MCopPluginFactory mCopPluginFactory;

    @Override
    public Middleware buildMiddleware() {

        mCopPluginFactory.getMCopPlugin();

        return new GroovyMiddleware();
    }

}
