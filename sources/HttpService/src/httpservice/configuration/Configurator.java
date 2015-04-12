package httpservice.configuration;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import overclocking.jrobocontainer.classpathscanning.IClassPathScannerConfiguration;
import overclocking.jrobocontainer.container.Container;
import overclocking.jrobocontainer.container.IContainer;

import commons.settings.ISettings;
import commons.settings.KnownKeys;
import commons.settings.Settings;

public class Configurator
{
    private static Logger logger = Logger.getLogger(Configurator.class);

    public IContainer configure(String settingsPath) {
        logger.info(String.format("Loading settings by path %s", settingsPath));
        ISettings settings;
        try {
            settings = Settings.Load(settingsPath);
        } catch (Throwable e) {
            logger.error("Fail to load settings due to unexpected error.", e);
            throw e;
        }

        IClassPathScannerConfiguration configuration = new ClassPathLoaderConfiguration(settings.getString(KnownKeys.ServerRunProfile));
        IContainer result = new Container(configuration);
        result.bindInstance(ISettings.class, settings);
        result.bindInstance(IContainer.class, result);
        return result;
    }

    private class ClassPathLoaderConfiguration implements IClassPathScannerConfiguration
    {
        private String pathSeparator = System.getProperty("path.separator");
        private String userDir = System.getProperty("user.dir");
        private String runProfile;

        public ClassPathLoaderConfiguration(String runProfile)
        {
            this.runProfile = runProfile;
        }

        @Override
        public boolean acceptsJar(String arg0)
        {
            boolean result = "dev".equals(runProfile) ? arg0.startsWith("ov.") : true;
            if (result)
                logger.info(String.format("jar %s accepted.", arg0));
            else
                logger.info(String.format("jar %s rejected.", arg0));

            return result;
        }

        @Override
        public String getClassPaths() {
            if ("dev".equals(runProfile)) {
                Path currentPath = Paths.get(System.getProperty("user.dir"));
                HashSet<String> projectNames = getProjectReferences(Paths.get(currentPath.toString(), ".classpath"));
                projectNames.add(currentPath.getFileName().toString());
                String[] projectsNames = projectNames.toArray(new String[0]);
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < projectsNames.length; i++) {
                    builder.append(Paths.get(userDir, "..", projectsNames[i]).toAbsolutePath().toString() + pathSeparator);
                }
                return builder.substring(0, builder.length() - 1);
            }
            return System.getProperty("user.dir");
        }

        private HashSet<String> getProjectReferences(Path projectClassPath) {
            HashSet<String> result = new HashSet<String>();
            List<String> lines;
            try {
                lines = java.nio.file.Files.readAllLines(projectClassPath, Charset.defaultCharset());
            } catch (IOException e) {
                throw new RuntimeException(String.format("Fail to read classpath file %s.", projectClassPath.toString()), e);
            }
            for (Iterator<String> iterator = lines.iterator(); iterator.hasNext();) {
                String line = (String) iterator.next();
                if (line.contains("src") && line.contains("path=\"/")) {
                    String suffix = line.split("path=\"/")[1];
                    String projectName = suffix.split("\"/>")[0];
                    if (!result.contains(projectName))
                        result.add(projectName);
                }
            }
            return result;
        }
    }
}
