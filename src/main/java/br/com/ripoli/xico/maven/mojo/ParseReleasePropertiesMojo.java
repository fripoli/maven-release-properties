package br.com.ripoli.xico.maven.mojo;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.util.*;
import java.util.stream.Collectors;

import static org.apache.maven.artifact.Artifact.LATEST_VERSION;
import static org.apache.maven.artifact.Artifact.RELEASE_VERSION;

/**
 * @author Francisco Ripoli
 */
@Mojo(name = "parse", defaultPhase = LifecyclePhase.VALIDATE)
public class ParseReleasePropertiesMojo extends AbstractMojo {

    private static final String PROPERTIES_ON_RELEASE = "propertiesOnRelease";
    private static final String PROPERTIES_ON_LATEST = "propertiesOnLatest";
    private static final String PROPERTIES_ON_RELEASE_AND_LATEST = "propertiesOnReleaseAndLatest";
    private static final String NONE = "none";
    private static final Map<String, List<String>> propertyTypeMapping = new HashMap<>();

    static {
        propertyTypeMapping.put(PROPERTIES_ON_RELEASE, Collections.singletonList(RELEASE_VERSION));
        propertyTypeMapping.put(PROPERTIES_ON_LATEST, Collections.singletonList(LATEST_VERSION));
        propertyTypeMapping.put(PROPERTIES_ON_RELEASE_AND_LATEST, Arrays.asList(RELEASE_VERSION, LATEST_VERSION));
    }

    @Parameter(readonly = true, defaultValue = "${project}")
    private MavenProject mavenProject;

    public void execute() {
        Map<String, List<String>> propertiesMap = mavenProject
                .getProperties().keySet().stream()
                .map(o -> (String) o)
                .filter(property -> RELEASE_VERSION.equals(getPropertyValue(property)) || LATEST_VERSION.equals(getPropertyValue(property)))
                .collect(Collectors.groupingBy(this::getPropertyValue));

        propertyTypeMapping.keySet().forEach(key ->
                mavenProject.getProperties().put(key,
                        propertyTypeMapping
                                .get(key)
                                .stream()
                                .map(propertiesMap::get)
                                .filter(this::isNotNullOrEmpty)
                                .flatMap(List::stream)
                                .collect(Collectors.collectingAndThen(Collectors.joining(","), l -> l.isEmpty() ? NONE : l))
                )
        );
    }

    private boolean isNotNullOrEmpty(List<String> strings) {
        return strings != null && !strings.isEmpty();
    }

    private String getPropertyValue(String propertyName) {
        return mavenProject.getProperties().getProperty(propertyName);
    }
}
