package lt.ca.javau11.controllers;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import lt.ca.javau11.entities.Model;
import lt.ca.javau11.services.ModelService;

@ExtendWith(MockitoExtension.class)
class ModelControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ModelService modelService;

    @InjectMocks
    private ModelController modelController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private Model model;
    private Long makeId = 1L;
    private Long modelId = 1L;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(modelController).build();

        model = new Model();
        model.setId(modelId);
        model.setName("Test Model");
    }

    @Test
    void testGetAllModels() throws Exception {
        when(modelService.getAll()).thenReturn(List.of(model));

        mockMvc.perform(get("/api/models"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test Model"));
    }

    @Test
    void testAddModel_Success() throws Exception {
        when(modelService.addModel(eq(makeId), any(Model.class))).thenReturn(model);

        mockMvc.perform(post("/api/models/{makeId}", makeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(model)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test Model"));
    }

    @Test
    void testAddModel_Failure() throws Exception {
        when(modelService.addModel(eq(makeId), any(Model.class))).thenThrow(new IllegalArgumentException());

        mockMvc.perform(post("/api/models/{makeId}", makeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(model)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetModelById_Found() throws Exception {
        when(modelService.getById(modelId)).thenReturn(Optional.of(model));

        mockMvc.perform(get("/api/models/{id}", modelId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Model"));
    }

    @Test
    void testGetModelById_NotFound() throws Exception {
        when(modelService.getById(modelId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/models/{id}", modelId))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateModel_Success() throws Exception {
        when(modelService.updateModel(eq(modelId), any(Model.class))).thenReturn(Optional.of(model));

        mockMvc.perform(put("/api/models/{id}", modelId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(model)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Model"));
    }

    @Test
    void testUpdateModel_NotFound() throws Exception {
        when(modelService.updateModel(eq(modelId), any(Model.class))).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/models/{id}", modelId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(model)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteModel_Success() throws Exception {
        when(modelService.deleteModel(modelId)).thenReturn(true);

        mockMvc.perform(delete("/api/models/{id}", modelId))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteModel_NotFound() throws Exception {
        when(modelService.deleteModel(modelId)).thenReturn(false);

        mockMvc.perform(delete("/api/models/{id}", modelId))
                .andExpect(status().isNotFound());
    }
}
