package hexlet.code.dto.task;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
public class TaskDTO {
    private Long id;

    private int index;

    private String name;

    private String description;

    private String status;

    private Date createdAt;

    private Long assigneeId;

    private Set<Long> taskLabelIds;
}
