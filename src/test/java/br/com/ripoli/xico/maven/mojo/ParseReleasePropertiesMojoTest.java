package br.com.ripoli.xico.maven.mojo;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.project.MavenProject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

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
    public void setUp() throws Exception {
        when(mavenProject.getProperties()).thenReturn(properties);
    }

    @Test
    public void shouldAddNoneIfNoPropertyOnLatest() throws Exception {
        Set<Object> propertiesSet = new HashSet<Object>();
        when(properties.keySet()).thenReturn(propertiesSet);

        parseReleasePropertiesMojo.execute();

        verify(properties).put(eq(PROPERTIES_ON_RELEASE), eq(NONE));
        verify(properties).put(eq(PROPERTIES_ON_LATEST), eq(NONE));
        verify(properties).put(eq(PROPERTIES_ON_RELEASE_AND_LATEST), eq(NONE));
    }

    @Test
    public void shouldAddOneReleasePropertyToMavenProject() throws Exception {
        Set<Object> propertiesSet = new HashSet<Object>();
        propertiesSet.add(MOCKITO_VERSION);
        when(properties.keySet()).thenReturn(propertiesSet);
        when(properties.getProperty(MOCKITO_VERSION)).thenReturn(Artifact.RELEASE_VERSION);

        parseReleasePropertiesMojo.execute();

        verify(properties).put(eq(PROPERTIES_ON_RELEASE), eq(MOCKITO_VERSION));
        verify(properties).put(eq(PROPERTIES_ON_LATEST), eq(NONE));
        verify(properties).put(eq(PROPERTIES_ON_RELEASE_AND_LATEST), eq(MOCKITO_VERSION));
    }

    @Test
    public void shouldAddOneLatestPropertyToMavenProject() throws Exception {
        Set<Object> propertiesSet = new HashSet<Object>();
        propertiesSet.add(MOCKITO_VERSION);
        when(properties.keySet()).thenReturn(propertiesSet);
        when(properties.getProperty(MOCKITO_VERSION)).thenReturn(Artifact.LATEST_VERSION);
        when(properties.getProperty(PROPERTIES_ON_RELEASE)).thenReturn(NONE);

        parseReleasePropertiesMojo.execute();

        verify(properties).put(eq(PROPERTIES_ON_LATEST), eq(MOCKITO_VERSION));
        verify(properties).put(eq(PROPERTIES_ON_RELEASE_AND_LATEST), eq(NONE + "," + MOCKITO_VERSION));
    }

    @Test
    public void shouldAddTwoReleasePropertyToMavenProject() throws Exception {
        Set<Object> propertiesSet = new LinkedHashSet<Object>();
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
    public void shouldAddOneReleaseAndOneLatestPropertyToMavenProject() throws Exception {
        Set<Object> propertiesSet = new HashSet<Object>();
        propertiesSet.add(MOCKITO_VERSION);
        propertiesSet.add(JUNIT_VERSION);
        when(properties.keySet()).thenReturn(propertiesSet);
        when(properties.getProperty(MOCKITO_VERSION)).thenReturn(Artifact.RELEASE_VERSION);
        when(properties.getProperty(JUNIT_VERSION)).thenReturn(Artifact.LATEST_VERSION);
        when(properties.getProperty(PROPERTIES_ON_RELEASE)).thenReturn(MOCKITO_VERSION);

        parseReleasePropertiesMojo.execute();

        verify(properties).put(eq(PROPERTIES_ON_RELEASE), eq(MOCKITO_VERSION));
        verify(properties).put(eq(PROPERTIES_ON_LATEST), eq(JUNIT_VERSION));
        verify(properties).put(eq(PROPERTIES_ON_RELEASE_AND_LATEST), eq(MOCKITO_VERSION));
        verify(properties).put(eq(PROPERTIES_ON_RELEASE_AND_LATEST), eq(MOCKITO_VERSION_JUNIT_VERSION));
    }

}