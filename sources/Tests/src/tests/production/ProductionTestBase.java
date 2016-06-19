package tests.production;

import java.nio.file.Paths;
import org.junit.Before;
import commons.settings.ISettings;
import commons.settings.Settings;
import overclocking.jrobocontainer.classpathscanning.IClassPathScannerConfiguration;
import overclocking.jrobocontainer.container.Container;
import overclocking.jrobocontainer.container.IContainer;
import tests.TestBase;

public abstract class ProductionTestBase extends TestBase {

    protected Container container;

    @Override
    @Before
    public void setUp() {
        super.setUp();

        container = new Container(new TestsClassPathLoaderConfiguration());
        ISettings applicationSettings = Settings.Load(Paths.get("conf", "application.settings").toAbsolutePath().toString());
        container.bindInstance(ISettings.class, applicationSettings);
        container.bindInstance(IContainer.class, container);
    }

    private class TestsClassPathLoaderConfiguration implements IClassPathScannerConfiguration {
        @Override
        public boolean acceptsJar(String arg0) {
            return arg0.startsWith("ov.");
        }

        @Override
        public String getClassPaths() {
            return Paths.get(System.getProperty("user.dir")).getParent().toString();
        }
    }

}

