package sensesim.integration.mcop;


public class NullMCopPluginFactory implements MCopPluginFactory {
    @Override
    public MCopPlugin getMCopPlugin() {
        return new NullMCopPlugin();
    }
}
