package lt.ca.javau11.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lt.ca.javau11.entities.Make;
import lt.ca.javau11.services.MakeService;

@RestController
@RequestMapping("/api/makes")
public class MakeController {

    private final MakeService makeService;

    public MakeController(MakeService makeService) {
        this.makeService = makeService;
    }

    @GetMapping
    public List<Make> getAll() {
        return makeService.getAll();
    }

    @PostMapping
    public Make addMake(@RequestBody Make make) {
        return makeService.addMake(make);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Make> getById(@PathVariable Long id) {
        Optional<Make> box = makeService.getById(id);
        return ResponseEntity.of(box);
    }
    
    @GetMapping("/{id}/models")
    public ResponseEntity<List<String>> getModelsByMake(@PathVariable Long id) {
        Optional<List<String>> models = makeService.getModelsByMake(id);
        return ResponseEntity.of(models);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Make> updateMake(@PathVariable Long id, @RequestBody Make make) {
        Optional<Make> box = makeService.updateMake(id, make);
        return ResponseEntity.of(box);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMake(@PathVariable Long id) {
        boolean isDeleted = makeService.deleteMake(id);
        return isDeleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}