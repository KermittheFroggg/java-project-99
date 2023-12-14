package hexlet.code.component;


import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import hexlet.code.repository.TaskStatusRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;


@Component
@AllArgsConstructor
public class DataInitializer implements ApplicationRunner {
    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final TaskStatusRepository taskStatusRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        var email = "hexlet@example.com";
        var password = "qwerty";
        var encodedPassword = encoder.encode(password);
        var userData = new User();
        userData.setEmail(email);
        userData.setPasswordDigest(encodedPassword);
        userRepository.save(userData);
        generatedTaskStatus("Draft", "draft");
        generatedTaskStatus("ToReview", "to_review");
        generatedTaskStatus("ToBeFixed", "to_be_fixed");
        generatedTaskStatus("ToPublish", "to_publish");
        generatedTaskStatus("Published", "published");

    }

    private void generatedTaskStatus(String name, String slug) {
        var taskStatus = new TaskStatus();
        taskStatus.setName(name);
        taskStatus.setSlug(slug);
        taskStatusRepository.save(taskStatus);
    }
}
