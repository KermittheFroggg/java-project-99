package hexlet.code.dto.task;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
public class TaskDTO {
    private Long id;

    private int index;

    @JsonProperty("title")
    private String name;

    @JsonProperty("content")
    private String description;

    private String status;

    private Date createdAt;

    @JsonProperty("assignee_id")
    private Long assigneeId;

    private Set<Long> taskLabelIds;
}
