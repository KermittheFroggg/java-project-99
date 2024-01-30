package hexlet.code.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.util.ModelGenerator;
import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class LabelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ModelGenerator modelGenerator;

    @Autowired
    private TaskRepository taskRepository;

    private Label testLabel;


    @BeforeEach
    public void setUp() {
        testLabel = Instancio.of(modelGenerator.getLabelModel())
                .create();
    }

    @AfterEach
    public void tearDown() {
        labelRepository.delete(testLabel);
    }

    @Test
    public void testIndex() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/labels").with(jwt()))
                .andExpect(status().isOk())
                .andReturn();

        String body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray().isNotEmpty();
    }

    @Test
    public void testCreate() throws Exception {
        Map<String, String> data = new HashMap<>(Map.of("name", "buggy"));

        MockHttpServletRequestBuilder request = post("/api/labels").with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request)
                .andExpect(status().isCreated());

        Label addedLabel = labelRepository.findByName(data.get("name")).orElse(null);

        assertThat(addedLabel).isNotNull();
        assertThat(addedLabel.getName()).isEqualTo("buggy");
    }

    @Test
    public void testUpdate() throws Exception {
        labelRepository.save(testLabel);

        Map<String, String> data = new HashMap<>(Map.of("name", "bugs"));

        MockHttpServletRequestBuilder request = put("/api/labels/" + testLabel.getId()).with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));
        MvcResult result = mockMvc.perform(request)
                .andReturn();

        int status = result.getResponse().getStatus();
        System.out.println("Update status code: " + status);

        Label updatedLabel = labelRepository.findById(testLabel.getId()).orElse(null);
        assertThat(updatedLabel).isNotNull();
        assertThat(updatedLabel.getName()).isEqualTo("bugs");

        mockMvc.perform(delete("/api/labels/" + testLabel.getId()).with(jwt()));
        mockMvc.perform(put("/api/labels/" + testLabel.getId()).with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(data)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDelete() throws Exception {
        labelRepository.save(testLabel);

        Optional<Label> existingLabel = labelRepository.findById(testLabel.getId());
        if (existingLabel.isEmpty()) {
            fail("Label does not exist, cannot delete");
        }

        MvcResult result = mockMvc.perform(delete("/api/labels/" + testLabel.getId()).with(jwt()))
                .andReturn();

        int status = result.getResponse().getStatus();
        System.out.println("Delete status code: " + status);

        Optional<Label> destroyedLabel = labelRepository.findById(testLabel.getId());
        assertThat(destroyedLabel.isPresent()).isFalse();
    }
}
