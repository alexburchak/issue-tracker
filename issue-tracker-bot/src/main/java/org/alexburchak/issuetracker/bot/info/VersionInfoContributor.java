package org.alexburchak.issuetracker.bot.info;

import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

/**
 * @author alexburchak
 */
@Component
@SuppressWarnings("unused")
public class VersionInfoContributor implements InfoContributor {
    private Properties props = new Properties();

    @PostConstruct
    public void postConstruct() throws IOException {
        props.load(VersionInfoContributor.class.getResourceAsStream("/info.properties"));
    }

    @Override
    public void contribute(Info.Builder builder) {
        builder.withDetails((Map) props);
    }

    @SuppressWarnings("unused")
    public String getVersion() {
        return props.getProperty("version");
    }
}
