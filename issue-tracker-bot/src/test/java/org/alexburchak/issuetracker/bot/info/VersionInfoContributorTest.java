package org.alexburchak.issuetracker.bot.info;

import org.springframework.boot.actuate.info.Info;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;

/**
 * @author alexburchak
 */
public class VersionInfoContributorTest {
    private VersionInfoContributor contributor = new VersionInfoContributor();

    @BeforeClass
    public void setUp() throws IOException {
        contributor.postConstruct();
    }

    @Test
    public void testGetVersion() {
        assertNotNull(contributor.getVersion());
    }

    @Test
    public void testContribute() {
        Info.Builder builder = new Info.Builder();
        contributor.contribute(builder);
        Info info = builder.build();
        assertFalse(info.getDetails().isEmpty());
    }
}
