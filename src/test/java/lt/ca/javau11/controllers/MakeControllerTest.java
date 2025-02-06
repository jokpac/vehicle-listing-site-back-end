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

import lt.ca.javau11.entities.Make;
import lt.ca.javau11.models.ModelDTO;
import lt.ca.javau11.services.MakeService;

@ExtendWith(MockitoExtension.class)
class MakeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private MakeService makeService;

    @InjectMocks
    private MakeController makeController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private Make make;
    private Long makeId = 1L;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(makeController).build();

        make = new Make();
        make.setId(makeId);
        make.setName("Test Make");
    }

    @Test
    void testGetAllMakes() throws Exception {
        when(makeService.getAll()).thenReturn(List.of(make));

        mockMvc.perform(get("/api/makes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test Make"));
    }

    @Test
    void testAddMake() throws Exception {
        when(makeService.addMake(any(Make.class))).thenReturn(make);

        mockMvc.perform(post("/api/makes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(make)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Make"));
    }

    @Test
    void testGetMakeById_Found() throws Exception {
        when(makeService.getById(makeId)).thenReturn(Optional.of(make));

        mockMvc.perform(get("/api/makes/{id}", makeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Make"));
    }

    @Test
    void testGetMakeById_NotFound() throws Exception {
        when(makeService.getById(makeId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/makes/{id}", makeId))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetModelsByMake_Found() throws Exception {
        ModelDTO modelDTO = new ModelDTO(1L, "Test Model");
        when(makeService.getModelsByMake(makeId)).thenReturn(Optional.of(List.of(modelDTO)));

        mockMvc.perform(get("/api/makes/{id}/models", makeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test Model"));
    }

    @Test
    void testGetModelsByMake_NotFound() throws Exception {
        when(makeService.getModelsByMake(makeId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/makes/{id}/models", makeId))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateMake_Success() throws Exception {
        when(makeService.updateMake(eq(makeId), any(Make.class))).thenReturn(Optional.of(make));

        mockMvc.perform(put("/api/makes/{id}", makeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(make)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Make"));
    }

    @Test
    void testUpdateMake_NotFound() throws Exception {
        when(makeService.updateMake(eq(makeId), any(Make.class))).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/makes/{id}", makeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(make)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteMake_Success() throws Exception {
        when(makeService.deleteMake(makeId)).thenReturn(true);

        mockMvc.perform(delete("/api/makes/{id}", makeId))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteMake_NotFound() throws Exception {
        when(makeService.deleteMake(makeId)).thenReturn(false);

        mockMvc.perform(delete("/api/makes/{id}", makeId))
                .andExpect(status().isNotFound());
    }
}
