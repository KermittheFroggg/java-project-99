package hexlet.code.dto.taskStatus;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TaskStatusCreateDTO {

    @NotNull
    @Size(min = 3)
    private String name;

    @NotNull
    @Size(min = 3)
    private String slug;
}
