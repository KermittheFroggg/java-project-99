package hexlet.code.dto.task;

import com.fasterxml.jackson.annotation.JsonProperty;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
public class TaskCreateDTO {

    private Integer index;

    @JsonProperty("assignee_id")
    private Long assigneeId;

    @NotBlank
    @JsonProperty("title")
    private String name;

    @JsonProperty("content")
    private String description;

    @NotNull
    private String status;

    private Set<Long> taskLabelIds;

}