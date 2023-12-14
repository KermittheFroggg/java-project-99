package hexlet.code.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "task_statuses")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class TaskStatus {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private long id;

    @NotBlank
    @Column(unique = true)
    private String name;

    @NotBlank
    @Column(unique = true)
    private String slug;

    @CreatedDate
    private LocalDate createdAt;

    @Override
    public String toString() {
        return "\n TaskStatus{" +
                "id= " + id +
                ", name= " + name +
                ", slug= " +  slug +
                ", createdAt= " + createdAt +
                '}';
    }
}
