package br.com.ripoli.xico.maven.mojo;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.project.MavenProject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ParseReleasePropertiesMojoTest {

    private static final String MOCKITO_VERSION = "mockito.version";
    private static final String JUNIT_VERSION = "junit.version";
    private static final String PROPERTIES_ON_RELEASE = "propertiesOnRelease";
    private static final String PROPERTIES_ON_LATEST = "propertiesOnLatest";
    private static final String PROPERTIES_ON_RELEASE_AND_LATEST = "propertiesOnReleaseAndLatest";
    private static final String MOCKITO_VERSION_JUNIT_VERSION = MOCKITO_VERSION + "," + JUNIT_VERSION;
    private static final String NONE = "none";

    @Mock
    private MavenProject mavenProject;

    @Mock
    private Properties properties;

    @InjectMocks
    private ParseReleasePropertiesMojo parseReleasePropertiesMojo;

    @Before
    public void setUp() {
        when(mavenProject.getProperties()).thenReturn(properties);
    }

    @Test
    public void shouldAddNoneIfNoPropertyOnLatest() {
        Set<Object> propertiesSet = new HashSet<>();
        when(properties.keySet()).thenReturn(propertiesSet);

        parseReleasePropertiesMojo.execute();

        verify(properties).put(eq(PROPERTIES_ON_RELEASE), eq(NONE));
        verify(properties).put(eq(PROPERTIES_ON_LATEST), eq(NONE));
        verify(properties).put(eq(PROPERTIES_ON_RELEASE_AND_LATEST), eq(NONE));
    }

    @Test
    public void shouldAddOneReleasePropertyToMavenProject() {
        Set<Object> propertiesSet = new HashSet<>();
        propertiesSet.add(MOCKITO_VERSION);
        when(properties.keySet()).thenReturn(propertiesSet);
        when(properties.getProperty(MOCKITO_VERSION)).thenReturn(Artifact.RELEASE_VERSION);

        parseReleasePropertiesMojo.execute();

        verify(properties).put(eq(PROPERTIES_ON_RELEASE), eq(MOCKITO_VERSION));
        verify(properties).put(eq(PROPERTIES_ON_LATEST), eq(NONE));
        verify(properties).put(eq(PROPERTIES_ON_RELEASE_AND_LATEST), eq(MOCKITO_VERSION));
    }

    @Test
    public void shouldAddOneLatestPropertyToMavenProject() {
        Set<Object> propertiesSet = new HashSet<>();
        propertiesSet.add(MOCKITO_VERSION);
        when(properties.keySet()).thenReturn(propertiesSet);
        when(properties.getProperty(MOCKITO_VERSION)).thenReturn(Artifact.LATEST_VERSION);

        parseReleasePropertiesMojo.execute();

        verify(properties).put(eq(PROPERTIES_ON_LATEST), eq(MOCKITO_VERSION));
        verify(properties).put(eq(PROPERTIES_ON_RELEASE_AND_LATEST), eq(MOCKITO_VERSION));
    }

    @Test
    public void shouldAddTwoReleasePropertyToMavenProject() {
        Set<Object> propertiesSet = new HashSet<>();
        propertiesSet.add(MOCKITO_VERSION);
        propertiesSet.add(JUNIT_VERSION);
        when(properties.keySet()).thenReturn(propertiesSet);
        when(properties.getProperty(MOCKITO_VERSION)).thenReturn(Artifact.RELEASE_VERSION);
        when(properties.getProperty(JUNIT_VERSION)).thenReturn(Artifact.RELEASE_VERSION);

        parseReleasePropertiesMojo.execute();

        verify(properties).put(eq(PROPERTIES_ON_RELEASE), eq(MOCKITO_VERSION_JUNIT_VERSION));
        verify(properties).put(eq(PROPERTIES_ON_LATEST), eq(NONE));
        verify(properties).put(eq(PROPERTIES_ON_RELEASE_AND_LATEST), eq(MOCKITO_VERSION_JUNIT_VERSION));
    }

    @Test
    public void shouldAddOneReleaseAndOneLatestPropertyToMavenProject() {
        Set<Object> propertiesSet = new HashSet<>();
        propertiesSet.add(MOCKITO_VERSION);
        propertiesSet.add(JUNIT_VERSION);
        when(properties.keySet()).thenReturn(propertiesSet);
        when(properties.getProperty(MOCKITO_VERSION)).thenReturn(Artifact.RELEASE_VERSION);
        when(properties.getProperty(JUNIT_VERSION)).thenReturn(Artifact.LATEST_VERSION);

        parseReleasePropertiesMojo.execute();

        verify(properties).put(eq(PROPERTIES_ON_RELEASE), eq(MOCKITO_VERSION));
        verify(properties).put(eq(PROPERTIES_ON_LATEST), eq(JUNIT_VERSION));
        verify(properties).put(eq(PROPERTIES_ON_RELEASE_AND_LATEST), eq(MOCKITO_VERSION_JUNIT_VERSION));
    }

}