package hexlet.code.dto.task;

import jakarta.validation.constraints.Size;
import org.openapitools.jackson.nullable.JsonNullable;

import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskUpdateDTO {
    @Size(min = 1)
    private JsonNullable<String> name;

    private JsonNullable<Integer> index;

    private JsonNullable<String> description;

    private JsonNullable<String> status;

    private JsonNullable<Long> assigneeId;

    private JsonNullable<Set<Long>> taskLabelIds;
}
