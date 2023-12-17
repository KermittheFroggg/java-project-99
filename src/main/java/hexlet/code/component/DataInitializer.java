package hexlet.code.component;


import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
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
    private final LabelRepository labelRepository;

    @Autowired
    private final TaskRepository taskRepository;

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
        generateTaskStatus("Draft", "draft");
        generateTaskStatus("ToReview", "to_review");
        generateTaskStatus("ToBeFixed", "to_be_fixed");
        generateTaskStatus("ToPublish", "to_publish");
        generateTaskStatus("Published", "published");

        generateLabel("bug");
        generateLabel("feature");

        generateTask("Task_1", taskStatusRepository.findBySlug("draft").get());
        generateTask("Task_2", taskStatusRepository.findBySlug("to_review").get());
    }

    private void generateTaskStatus(String name, String slug) {
        var taskStatus = new TaskStatus();
        taskStatus.setName(name);
        taskStatus.setSlug(slug);
        taskStatusRepository.save(taskStatus);
    }

    private void generateLabel(String name) {
        var label = new Label();
        label.setName(name);
        labelRepository.save(label);
    }

    private void generateTask(String name, TaskStatus taskStatus) {
        var task = new Task();
        task.setName(name);
        task.setTaskStatus(taskStatus);
        taskRepository.save(task);
    }
}
