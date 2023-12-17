package hexlet.code.dto.user;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDTO {
    private long id;

    private String username;

    private String firstName;

    private String lastName;

    @Size(min = 3)
    private String password;
}
