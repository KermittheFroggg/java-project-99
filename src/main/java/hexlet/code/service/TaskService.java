package hexlet.code.service;

import hexlet.code.dto.task.TaskCreateDTO;
import hexlet.code.dto.task.TaskDTO;
import hexlet.code.dto.task.TaskUpdateDTO;
import hexlet.code.mapper.TaskMapper;
import hexlet.code.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskMapper taskMapper;


    public List<TaskDTO> getAllTasks() {
        var taskes = taskRepository.findAll();
        return taskes.stream()
                .map(taskMapper::map)
                .toList();
    }

    public TaskDTO create(TaskCreateDTO taskData) {
        var task = taskMapper.map(taskData);
        taskRepository.save(task);
        return taskMapper.map(task);
    }

    public TaskDTO getTaskById(Long id) {
        var task = taskRepository.findById(id)
                .orElseThrow();
        return taskMapper.map(task);
    }

    public TaskDTO updateTask(Long id, TaskUpdateDTO taskData) {
        var task = taskRepository.findById(id)
                .orElseThrow();

        taskMapper.update(taskData, task);
        taskRepository.save(task);
        return taskMapper.map(task);
    }

    public void deleteTask(Long id) {
        taskRepository.findById(id)
                .orElseThrow();
        taskRepository.deleteById(id);
    }

    public List<TaskDTO> getTasksByParameters(String titleCont, Long assigneeId, String status, Long labelId) {
        var tasks = taskRepository.findAll();
        return tasks.stream()
                .filter(task -> titleCont == null || task.getName().contains(titleCont))
                .filter(task -> assigneeId == null || task.getAssignee().getId() == (assigneeId))
                .filter(task -> status == null || task.getTaskStatus().getSlug().equals(status))
                .filter(task -> labelId == null || task.getLabels().stream().anyMatch(label -> label.getId().equals(labelId)))
                .map(taskMapper::map)
                .toList();
    }
}
