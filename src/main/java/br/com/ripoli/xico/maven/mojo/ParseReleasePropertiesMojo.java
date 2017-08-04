package br.com.ripoli.xico.maven.mojo;

import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.StringUtils;

/**
 * @author Francisco Ripoli
 */
@Mojo(name = "parse", defaultPhase = LifecyclePhase.VALIDATE)
public class ParseReleasePropertiesMojo extends AbstractMojo {

    private static final String PROPERTIES_ON_RELEASE = "propertiesOnRelease";
    private static final String PROPERTIES_ON_LATEST = "propertiesOnLatest";
    private static final String PROPERTIES_ON_RELEASE_AND_LATEST = "propertiesOnReleaseAndLatest";
    private static final String NONE = "none";

    @Parameter(readonly = true, defaultValue = "${project}")
    private MavenProject mavenProject;

    public void execute() throws MojoExecutionException, MojoFailureException {
        Set<String> propertiesOnRelease = new LinkedHashSet<String>();
        Set<String> propertiesOnLatest = new LinkedHashSet<String>();

        for (Object key : mavenProject.getProperties().keySet()) {
            String propertyName = (String) key;
            String propertyValue = mavenProject.getProperties().getProperty(propertyName);

            if (Artifact.RELEASE_VERSION.equals(propertyValue)) {
                propertiesOnRelease.add(propertyName);
            } else if (Artifact.LATEST_VERSION.equals(propertyValue)) {
                propertiesOnLatest.add(propertyName);
            }
        }

        if (!propertiesOnRelease.isEmpty()) {
            String commaSeparatedProperties = StringUtils.join(propertiesOnRelease.iterator(), ",");

            mavenProject.getProperties().put(PROPERTIES_ON_RELEASE, commaSeparatedProperties);
            mavenProject.getProperties().put(PROPERTIES_ON_RELEASE_AND_LATEST, commaSeparatedProperties);

            getLog().info("Properties on RELEASE: " + commaSeparatedProperties);
        } else {
            mavenProject.getProperties().put(PROPERTIES_ON_RELEASE, NONE);
            mavenProject.getProperties().put(PROPERTIES_ON_RELEASE_AND_LATEST, NONE);
        }

        if (!propertiesOnLatest.isEmpty()) {
            String commaSeparatedProperties = StringUtils.join(propertiesOnLatest.iterator(), ",");

            mavenProject.getProperties().put(PROPERTIES_ON_LATEST, commaSeparatedProperties);

            getLog().info("Properties on LATEST: " + commaSeparatedProperties);

            joinReleaseAndLatestProperties(commaSeparatedProperties);
        } else {
            mavenProject.getProperties().put(PROPERTIES_ON_LATEST, NONE);
        }
    }

    private void joinReleaseAndLatestProperties(String propertiesOnLatest) {
        String allProperties;
        String propertiesOnRelease = mavenProject.getProperties().getProperty(PROPERTIES_ON_RELEASE);

        if (propertiesOnRelease != null) {
            allProperties = propertiesOnRelease + "," + propertiesOnLatest;
        } else {
            allProperties = propertiesOnLatest;
        }

        mavenProject.getProperties().put(PROPERTIES_ON_RELEASE_AND_LATEST, allProperties);
    }
}
