package tests.integration;

import java.nio.file.Paths;

import org.junit.After;
import org.junit.Before;

import commons.settings.ISettings;
import commons.settings.Settings;
import overclocking.jrobocontainer.classpathscanning.IClassPathScannerConfiguration;
import overclocking.jrobocontainer.container.Container;
import tests.TestBase;

public class IntegrationTestBase extends TestBase {
    protected Container container;

    @Override
    @Before
    public void setUp() {
        super.setUp();

        container = new Container(new TestsClassPathLoaderConfiguration());
        container.bindInstance(ISettings.class, Settings.Load(Paths.get("conf", "application.settings").toAbsolutePath().toString()));
    }
    
    @Override
    @After
    public void tearDown() {
        super.tearDown();
    }

    private class TestsClassPathLoaderConfiguration implements IClassPathScannerConfiguration {
        @Override
        public boolean acceptsJar(String arg0) {
            return arg0.startsWith("ov.");
        }

        @Override
        public String getClassPaths() {
            //return Paths.get(System.getProperty("user.dir")).getParent().toString();
            return Paths.get(System.getProperty("user.dir")).toString();
        }
    }
}
