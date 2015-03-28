package tests.production;

import java.awt.datatransfer.FlavorEvent;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.Locale;
import java.util.Random;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import overclocking.jrobocontainer.classpathscanning.IClassPathScannerConfiguration;
import overclocking.jrobocontainer.container.Container;
import overclocking.jrobocontainer.container.IContainer;
import storage.cassandraClient.ISchemeInitializer;
import commons.files.IFile;
import commons.files.IFileManager;
import commons.settings.ISettings;
import commons.settings.Settings;
import compressionservice.upload.IFileUploader;
import dataContracts.ContentType;
import dataContracts.files.FileType;
import tests.TestBase;

public abstract class ProductionTestBase extends TestBase {

    protected Container container;

    @Override
    @Before
    public void setUp() {
        super.setUp();

        container = new Container(new TestsClassPathLoaderConfiguration());
        ISettings applicationSettings = Settings.Load(Paths.get("conf", "productionApplication.settings").toAbsolutePath().toString());
        container.bindInstance(ISettings.class, applicationSettings);
        container.bindInstance(IContainer.class, container);
    }

//    @Test
//    public void InitCassandraSchemeTest() {
//        container.get(ISchemeInitializer.class).setUpCluster();
//    }

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

