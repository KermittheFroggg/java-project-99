package hexlet.code.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.Set;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "tasks")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Task {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private long id;

    @NotBlank
    private String name;

    private int index;

    private String description;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "task_status_id")
    private TaskStatus taskStatus;

    @ManyToOne
    @JoinColumn(name = "assignee_id")
    private User assignee;

    private LocalDate createdAt;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private Set<Label> labels;

}