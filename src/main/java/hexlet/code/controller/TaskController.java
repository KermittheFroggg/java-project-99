package hexlet.code.controller;

import hexlet.code.dto.task.TaskCreateDTO;
import hexlet.code.dto.task.TaskDTO;
import hexlet.code.dto.task.TaskUpdateDTO;
import hexlet.code.repository.TaskRepository;
import hexlet.code.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskService taskService;

    @GetMapping(path = "")
    public ResponseEntity<List<TaskDTO>> index(
            @RequestParam(required = false) String titleCont,
            @RequestParam(required = false) Long assigneeId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long labelId) {

        List<TaskDTO> tasks = taskService.getTasksByParameters(titleCont, assigneeId, status, labelId);

        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(tasks.size()))
                .body(tasks);
    }

    @GetMapping(path = "/{id}")
    public TaskDTO show(@PathVariable long id) {
        return taskService.getTaskById(id);
    }

    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public TaskDTO create(@Valid @RequestBody TaskCreateDTO TaskData) {
        return taskService.create(TaskData);
    }

    @PutMapping(path = "/{id}")
    public TaskDTO update(@PathVariable long id, @Valid @RequestBody TaskUpdateDTO TaskData) {
        return taskService.updateTask(id, TaskData);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        taskService.deleteTask(id);
    }

}
