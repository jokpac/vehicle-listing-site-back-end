package lt.ca.javau11.services;

import lt.ca.javau11.entities.Make;
import lt.ca.javau11.entities.Model;
import lt.ca.javau11.exceptions.MakeNotFoundException;
import lt.ca.javau11.repositories.MakeRepository;
import lt.ca.javau11.repositories.ModelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ModelServiceTest {

    @Mock
    private ModelRepository modelRepository;

    @Mock
    private MakeRepository makeRepository;

    @InjectMocks
    private ModelService modelService;

    private Model model;
    private Make make;
    private Long modelId = 1L;
    private Long makeId = 1L;

    @BeforeEach
    public void setUp() {
        make = new Make();
        make.setId(makeId);
        make.setName("Test Make");

        model = new Model();
        model.setId(modelId);
        model.setName("Test Model");
        model.setMake(make);
    }

    @Test
    public void testGetAllModels() {
        when(modelRepository.findAll()).thenReturn(List.of(model));

        List<Model> models = modelService.getAll();

        assertNotNull(models);
        assertEquals(1, models.size());
        assertEquals("Test Model", models.get(0).getName());
    }

    @Test
    public void testAddModel_Success() {
        when(makeRepository.findById(makeId)).thenReturn(Optional.of(make));
        when(modelRepository.save(model)).thenReturn(model);

        Model createdModel = modelService.addModel(makeId, model);

        assertNotNull(createdModel);
        assertEquals("Test Model", createdModel.getName());
        assertEquals(make, createdModel.getMake());
    }

    @Test
    public void testAddModel_MakeNotFound() {
        when(makeRepository.findById(makeId)).thenReturn(Optional.empty());

        assertThrows(MakeNotFoundException.class, () -> {
            modelService.addModel(makeId, model);
        });
    }

    @Test
    public void testGetModelById_Found() {
        when(modelRepository.findById(modelId)).thenReturn(Optional.of(model));

        Optional<Model> foundModel = modelService.getById(modelId);

        assertTrue(foundModel.isPresent());
        assertEquals("Test Model", foundModel.get().getName());
    }

    @Test
    public void testGetModelById_NotFound() {
        when(modelRepository.findById(modelId)).thenReturn(Optional.empty());

        Optional<Model> foundModel = modelService.getById(modelId);

        assertFalse(foundModel.isPresent());
    }

    @Test
    public void testUpdateModel_Success() {
        Model updatedModelDetails = new Model();
        updatedModelDetails.setName("Updated Model");

        when(modelRepository.findById(modelId)).thenReturn(Optional.of(model));
        when(modelRepository.save(model)).thenReturn(model);

        Optional<Model> updatedModel = modelService.updateModel(modelId, updatedModelDetails);

        assertTrue(updatedModel.isPresent());
        assertEquals("Updated Model", updatedModel.get().getName());
    }

    @Test
    public void testUpdateModel_NotFound() {
        when(modelRepository.findById(modelId)).thenReturn(Optional.empty());

        Optional<Model> updatedModel = modelService.updateModel(modelId, model);

        assertFalse(updatedModel.isPresent());
    }

    @Test
    public void testDeleteModel_Success() {
        when(modelRepository.findById(modelId)).thenReturn(Optional.of(model));

        boolean isDeleted = modelService.deleteModel(modelId);

        assertTrue(isDeleted);
        verify(modelRepository, times(1)).delete(model);
    }

    @Test
    public void testDeleteModel_NotFound() {
        when(modelRepository.findById(modelId)).thenReturn(Optional.empty());

        boolean isDeleted = modelService.deleteModel(modelId);

        assertFalse(isDeleted);
    }
}
