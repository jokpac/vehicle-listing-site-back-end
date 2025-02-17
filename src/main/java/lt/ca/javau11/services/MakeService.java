package lt.ca.javau11.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lt.ca.javau11.entities.Make;
import lt.ca.javau11.models.ModelDTO;
import lt.ca.javau11.repositories.MakeRepository;

@Service
public class MakeService {


    private final MakeRepository repo;

    public MakeService(MakeRepository repo) {
        this.repo = repo;
    }

    public List<Make> getAll() {
        return repo.findAll();
    }

    public Make addMake(Make make) {
        return repo.save(make);
    }

    public Optional<Make> getById(Long id) {
        return repo.findById(id);
    }

    public Optional<Make> updateMake(Long id, Make makeDetails) {
        Optional<Make> box = repo.findById(id);
        if (box.isPresent()) {
            Make existingMake = box.get();
            existingMake.setName(makeDetails.getName());
            return Optional.of(repo.save(existingMake));
        }
        return Optional.empty();
    }

    public boolean deleteMake(Long id) {
        Optional<Make> box = repo.findById(id);
        if (box.isEmpty()) {
            return false;
        }
        repo.delete(box.get());
        return true;
    }

    public Optional<List<ModelDTO>> getModelsByMake(Long makeId) {
        Optional<Make> box = repo.findById(makeId);
        if (box.isPresent()) {
            List<ModelDTO> modelDTOs = box.get()
                .getModels()  // Assuming Make has a collection of Models
                .stream()
                .map(model -> new ModelDTO(model.getId(), model.getName())) // Mapping Model to ModelDTO
                .collect(Collectors.toList());
            return Optional.of(modelDTOs);
        }
        return Optional.empty();  // Return empty if Make is not found
    }
}
