package extension;

import java.io.File;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.jar.Attributes;

public class PluginManifestReader {

    /**
     * Reads "Plugin-Class" from the JAR manifest.
     * @return fully-qualified class name or null
     */
    public static String getMainClassFromJar(File jarFile) {
        try (JarFile jar = new JarFile(jarFile)) {
            Manifest manifest = jar.getManifest();
            if (manifest == null) return null;

            Attributes attrs = manifest.getMainAttributes();
            if (attrs == null) return null;

            return attrs.getValue("Plugin-Class");
        } catch (Exception e) {
            System.err.println("[PluginManifestReader] Error reading " + jarFile.getName() + ": " + e.getMessage());
            return null;
        }
    }
}
