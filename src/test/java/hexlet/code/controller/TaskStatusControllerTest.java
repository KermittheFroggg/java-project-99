package hexlet.code.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.util.ModelGenerator;
import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class TaskStatusControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ModelGenerator modelGenerator;

    private TaskStatus testTaskStatus;

    @BeforeEach
    public void setUp() {
        testTaskStatus = Instancio.of(modelGenerator.getTaskStatusModel()).create();
        taskStatusRepository.save(testTaskStatus);
    }

    @AfterEach
    public void tearDown() {
        taskStatusRepository.delete(testTaskStatus);
    }


    @Test
    public void testIndex() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/task_statuses").with(jwt()))
                .andExpect(status().isOk())
                .andReturn();

        String body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray().isNotEmpty();
    }

    @Test
    public void testCreate() throws Exception {
        Map<String, String> data = new HashMap<>(Map.of("name", "newStatus", "slug", "new-status"));

        MockHttpServletRequestBuilder request = post("/api/task_statuses").with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request)
                .andExpect(status().isCreated());

        TaskStatus addedStatus = taskStatusRepository.findByName(data.get("name")).orElse(null);

        assertThat(addedStatus).isNotNull();
        assertThat(addedStatus.getName()).isEqualTo("newStatus");
        assertThat(addedStatus.getSlug()).isEqualTo("new-status");
    }

    @Test
    public void testUpdate() throws Exception {
        Map<String, String> data = new HashMap<>(Map.of("name", "updatedStatus", "slug", "updated-status"));

        MockHttpServletRequestBuilder request = put("/api/task_statuses/" + testTaskStatus.getId()).with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        TaskStatus updatedStatus = taskStatusRepository.findById(testTaskStatus.getId()).orElse(null);
        assertThat(updatedStatus).isNotNull();
        assertThat(updatedStatus.getName()).isEqualTo("updatedStatus");
        assertThat(updatedStatus.getSlug()).isEqualTo("updated-status");
    }

    @Test
    public void testDelete() throws Exception {
        MvcResult result = mockMvc.perform(delete("/api/task_statuses/" + testTaskStatus.getId()).with(jwt()))
                .andExpect(status().isNoContent())
                .andReturn();

        Optional<TaskStatus> destroyedStatus = taskStatusRepository.findById(testTaskStatus.getId());
        assertThat(destroyedStatus.isPresent()).isFalse();
    }
}
