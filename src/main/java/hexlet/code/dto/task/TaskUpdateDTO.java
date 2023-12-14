package hexlet.code.dto.task;

import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import jakarta.validation.constraints.Size;
import org.openapitools.jackson.nullable.JsonNullable;

import java.util.Set;

public class TaskUpdateDTO {
    @Size(min = 1)
    private JsonNullable<String> name;

    private JsonNullable<Integer> index;

    private JsonNullable<String> description;

    private JsonNullable<String> status;

    private JsonNullable<Long> assigneeId;

    private JsonNullable<Set<Long>> taskLabelIds;
}
