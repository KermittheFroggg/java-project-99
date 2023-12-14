package hexlet.code.controller;

import hexlet.code.dto.taskStatus.TaskStatusCreateDTO;
import hexlet.code.dto.taskStatus.TaskStatusDTO;
import hexlet.code.dto.taskStatus.TaskStatusUpdateDTO;

import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.service.TaskStatusService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/task_statuses")
public class TaskStatusController {

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private TaskStatusService taskStatusService;

    @GetMapping(path = "")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<List<TaskStatusDTO>> index() {
        var taskStatuses = taskStatusService.getAllTaskStatus();
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(taskStatuses.size()))
                .body(taskStatuses);
    }

    @GetMapping(path = "/{id}")
    public TaskStatusDTO show(@PathVariable long id) {
        return taskStatusService.getTaskStatusById(id);
    }

    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public TaskStatusDTO create(@Valid @RequestBody TaskStatusCreateDTO taskStatusData) {
        return taskStatusService.create(taskStatusData);
    }

    @PutMapping(path = "/{id}")
    public TaskStatusDTO update(@PathVariable long id, @Valid @RequestBody TaskStatusUpdateDTO taskStatusData) {
        return taskStatusService.updateTaskStatus(id, taskStatusData);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        taskStatusService.deleteTaskStatus(id);
    }

}
