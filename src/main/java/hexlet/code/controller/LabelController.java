package hexlet.code.controller;

import hexlet.code.dto.label.LabelCreateDTO;
import hexlet.code.dto.label.LabelDTO;
import hexlet.code.dto.label.LabelUpdateDTO;
import hexlet.code.service.LabelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/labels")
@RequiredArgsConstructor
public final class LabelController {

    private final LabelService labelService;

    @GetMapping(path = "")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<LabelDTO>> getAllLabels() {
        var labels = labelService.getAllLabels();
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(labels.size()))
                .body(labels);
    }

    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public LabelDTO createLabel(@Valid @RequestBody LabelCreateDTO labelData) {
        return labelService.create(labelData);
    }

    @GetMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<LabelDTO> getLabel(@PathVariable long id) {
        try {
            LabelDTO label = labelService.findById(id);
            return ResponseEntity.ok(label);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<LabelDTO> updateLabel(@PathVariable long id,
                                                @RequestBody @Valid LabelUpdateDTO labelData) {
        try {
            return ResponseEntity.ok(labelService.update(labelData, id));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLabel(@PathVariable long id) {
        labelService.delete(id);
    }
}
