package org.alexburchak.issuetracker.bot.handler.action;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author alexburchak
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class GetFile {
    @Getter
    @Setter
    public static class Result {
        @JsonProperty("file_id")
        private String fileId;
        @JsonProperty("file_size")
        private Long fileSize;
        @JsonProperty("file_path")
        private String filePath;
    }

    @JsonProperty
    private boolean ok;
    @JsonProperty
    private Result result;
}
