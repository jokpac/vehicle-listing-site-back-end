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
import lt.ca.javau11.entities.Make;
import lt.ca.javau11.models.ModelDTO;
import lt.ca.javau11.services.MakeService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/makes")
public class MakeController {

    private final MakeService makeService;

    public MakeController(MakeService makeService) {
        this.makeService = makeService;
    }

    @GetMapping
    @Operation(summary = "Returns all makes with models")
    public List<Make> getAll() {
        return makeService.getAll();
    }

    @PostMapping
    @Operation(summary = "Creates a new make")
    public Make addMake(@RequestBody Make make) {
        return makeService.addMake(make);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Returns a make by id with its models")
    public ResponseEntity<Make> getById(@PathVariable Long id) {
        Optional<Make> box = makeService.getById(id);
        return ResponseEntity.of(box);
    }
    
    @GetMapping("/{id}/models")
    @Operation(summary = "Returns models by make id")
    public ResponseEntity<List<ModelDTO>> getModelsByMake(@PathVariable Long id) {
        Optional<List<ModelDTO>> models = makeService.getModelsByMake(id);
        return models.map(ResponseEntity::ok)
                     .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Updates a make by id")
    public ResponseEntity<Make> updateMake(@PathVariable Long id, @RequestBody Make make) {
        Optional<Make> box = makeService.updateMake(id, make);
        return ResponseEntity.of(box);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletes a make by id")
    public ResponseEntity<Void> deleteMake(@PathVariable Long id) {
        boolean isDeleted = makeService.deleteMake(id);
        return isDeleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}