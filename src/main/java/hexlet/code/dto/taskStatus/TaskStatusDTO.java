package hexlet.code.dto.taskStatus;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

import java.time.LocalDate;
import java.util.Date;

@Setter
@Getter
public class TaskStatusDTO {

    private Long id;
    private String name;
    private String slug;
    private Date createdAt;
}
