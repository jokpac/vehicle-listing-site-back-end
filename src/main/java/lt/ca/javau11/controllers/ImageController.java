package lt.ca.javau11.controllers;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lt.ca.javau11.entities.Image;
import lt.ca.javau11.repositories.ImageRepository;

@RestController
@RequestMapping("/images")
public class ImageController {

	private ImageRepository imageRepository;

	public ImageController(ImageRepository imageRepository) {
		this.imageRepository = imageRepository;
	}

	@PostMapping("/upload")
	public ResponseEntity<String> uploadImage(@RequestParam  MultipartFile file) throws IOException{
		if(file.isEmpty())
			return ResponseEntity.badRequest().body("File is empty");
		else if(file.getSize() > 15_000_000) // 15 MB
			return ResponseEntity.badRequest().body("File is too large (max. 15 MB)");
		
		Image image = new Image();
		image.setFileName( file.getOriginalFilename());
		image.setData( file.getBytes() );
		imageRepository.save(image);
		
		return ResponseEntity.ok("File uploaded succesfully. ID: " + image.getId());
		
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<byte[]> getImage(@PathVariable Long id){
		return imageRepository.findById(id)
				.map(image -> ResponseEntity.ok()
		                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + image.getFileName() + "\"")
		                .contentType(MediaType.APPLICATION_OCTET_STREAM)
		                .body(image.getData())
		        )
		        .orElseGet(() -> ResponseEntity.notFound().build());
	}

}