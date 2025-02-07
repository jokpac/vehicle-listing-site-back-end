package lt.ca.javau11.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import lt.ca.javau11.entities.Model;
import lt.ca.javau11.services.ModelService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/models")
public class ModelController {

    private final ModelService modelService;

    public ModelController(ModelService modelService) {
        this.modelService = modelService;
    }

    @GetMapping
    @Operation(summary = "Returns all models")
    public List<Model> getAll() {
        return modelService.getAll();
    }

    @PostMapping("/{makeId}")
    @Operation(summary = "Creates a new model for a make by id")
    public ResponseEntity<Model> addModel(@PathVariable Long makeId, @RequestBody Model model) {
        try {
            Model createdModel = modelService.addModel(makeId, model);
            return ResponseEntity.status(201).body(createdModel);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Returns a model by id")
    public ResponseEntity<Model> getById(@PathVariable Long id) {
        Optional<Model> box = modelService.getById(id);
        return ResponseEntity.of(box);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Updates a model by id")
    public ResponseEntity<Model> updateModel(@PathVariable Long id, @RequestBody Model model) {
        Optional<Model> box = modelService.updateModel(id, model);
        return ResponseEntity.of(box);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletes a model by id")
    public ResponseEntity<Void> deleteModel(@PathVariable Long id) {
        boolean isDeleted = modelService.deleteModel(id);
        return isDeleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
