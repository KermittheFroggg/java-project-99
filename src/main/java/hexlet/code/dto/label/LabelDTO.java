package hexlet.code.dto.label;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class LabelDTO {
    private long id;
    private String name;
    private Date createdAt;
}
