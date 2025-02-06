package lt.ca.javau11.services;

import lt.ca.javau11.entities.Make;
import lt.ca.javau11.entities.Model;
import lt.ca.javau11.models.ModelDTO;
import lt.ca.javau11.repositories.MakeRepository;
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
public class MakeServiceTest {

    @Mock
    private MakeRepository makeRepository;

    @InjectMocks
    private MakeService makeService;

    private Make make;
    private Long makeId = 1L;

    @BeforeEach
    public void setUp() {
        make = new Make();
        make.setId(makeId);
        make.setName("Test Make");

        // Create some models for testing getModelsByMake
        Model model1 = new Model();
        model1.setId(1L);
        model1.setName("Model 1");
        Model model2 = new Model();
        model2.setId(2L);
        model2.setName("Model 2");
        make.setModels(List.of(model1, model2));
    }

    @Test
    public void testGetAllMakes() {
        when(makeRepository.findAll()).thenReturn(List.of(make));

        List<Make> makes = makeService.getAll();

        assertNotNull(makes);
        assertEquals(1, makes.size());
        assertEquals("Test Make", makes.get(0).getName());
    }

    @Test
    public void testAddMake() {
        when(makeRepository.save(make)).thenReturn(make);

        Make createdMake = makeService.addMake(make);

        assertNotNull(createdMake);
        assertEquals("Test Make", createdMake.getName());
    }

    @Test
    public void testGetMakeById_Found() {
        when(makeRepository.findById(makeId)).thenReturn(Optional.of(make));

        Optional<Make> foundMake = makeService.getById(makeId);

        assertTrue(foundMake.isPresent());
        assertEquals("Test Make", foundMake.get().getName());
    }

    @Test
    public void testGetMakeById_NotFound() {
        when(makeRepository.findById(makeId)).thenReturn(Optional.empty());

        Optional<Make> foundMake = makeService.getById(makeId);

        assertFalse(foundMake.isPresent());
    }

    @Test
    public void testUpdateMake_Success() {
        Make updatedMakeDetails = new Make();
        updatedMakeDetails.setName("Updated Make");

        when(makeRepository.findById(makeId)).thenReturn(Optional.of(make));
        when(makeRepository.save(make)).thenReturn(make);

        Optional<Make> updatedMake = makeService.updateMake(makeId, updatedMakeDetails);

        assertTrue(updatedMake.isPresent());
        assertEquals("Updated Make", updatedMake.get().getName());
    }

    @Test
    public void testUpdateMake_NotFound() {
        when(makeRepository.findById(makeId)).thenReturn(Optional.empty());

        Optional<Make> updatedMake = makeService.updateMake(makeId, make);

        assertFalse(updatedMake.isPresent());
    }

    @Test
    public void testDeleteMake_Success() {
        when(makeRepository.findById(makeId)).thenReturn(Optional.of(make));

        boolean isDeleted = makeService.deleteMake(makeId);

        assertTrue(isDeleted);
        verify(makeRepository, times(1)).delete(make);
    }

    @Test
    public void testDeleteMake_NotFound() {
        when(makeRepository.findById(makeId)).thenReturn(Optional.empty());

        boolean isDeleted = makeService.deleteMake(makeId);

        assertFalse(isDeleted);
    }

    @Test
    public void testGetModelsByMake_Found() {
        when(makeRepository.findById(makeId)).thenReturn(Optional.of(make));

        Optional<List<ModelDTO>> models = makeService.getModelsByMake(makeId);

        assertTrue(models.isPresent());
        assertEquals(2, models.get().size());
        assertEquals("Model 1", models.get().get(0).getName());
        assertEquals("Model 2", models.get().get(1).getName());
    }

    @Test
    public void testGetModelsByMake_NotFound() {
        when(makeRepository.findById(makeId)).thenReturn(Optional.empty());

        Optional<List<ModelDTO>> models = makeService.getModelsByMake(makeId);

        assertFalse(models.isPresent());
    }
}
