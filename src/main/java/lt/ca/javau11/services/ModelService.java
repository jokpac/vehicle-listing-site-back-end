package lt.ca.javau11.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lt.ca.javau11.entities.Make;
import lt.ca.javau11.entities.Model;
import lt.ca.javau11.exceptions.MakeNotFoundException;
import lt.ca.javau11.repositories.MakeRepository;
import lt.ca.javau11.repositories.ModelRepository;

@Service
public class ModelService {

    private final ModelRepository repo;
    private final MakeRepository makeRepo;

    public ModelService(ModelRepository repo, MakeRepository makeRepo) {
        this.repo = repo;
        this.makeRepo = makeRepo;
    }

    public List<Model> getAll() {
        return repo.findAll();
    }

    public Model addModel(Long makeId, Model model) {
        Optional<Make> makeBox = makeRepo.findById(makeId);
        if (makeBox.isPresent()) {
            Make make = makeBox.get();
            model.setMake(make);
            return repo.save(model);
        }
        throw new MakeNotFoundException("Make with ID " + makeId + " not found");
    }

    public Optional<Model> getById(Long id) {
        return repo.findById(id);
    }

    public Optional<Model> updateModel(Long id, Model modelDetails) {
        Optional<Model> box = repo.findById(id);
        if (box.isPresent()) {
            Model existingModel = box.get();
            existingModel.setName(modelDetails.getName());
            return Optional.of(repo.save(existingModel));
        }
        return Optional.empty();
    }

    public boolean deleteModel(Long id) {
        Optional<Model> box = repo.findById(id);
        if (box.isEmpty()) {
            return false;
        }
        repo.delete(box.get());
        return true;
    }
}