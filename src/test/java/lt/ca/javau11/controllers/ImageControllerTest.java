package lt.ca.javau11.controllers;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import lt.ca.javau11.entities.Image;
import lt.ca.javau11.repositories.ImageRepository;

@ExtendWith(MockitoExtension.class)
class ImageControllerTest {

    @Mock
    private ImageRepository imageRepository;

    @InjectMocks
    private ImageController imageController;

    private MultipartFile mockFile;
    private Image image;

    @BeforeEach
    void setUp() {
        mockFile = new MockMultipartFile("file", "test.jpg", "image/jpeg", new byte[]{1, 2, 3});
        image = new Image();
        image.setId(1L);
        image.setFileName("test.jpg");
        image.setData(new byte[]{1, 2, 3});
    }

    @Test
    void uploadImage_ShouldReturnSuccess() throws IOException {
        when(imageRepository.save(any(Image.class))).thenReturn(image);

        ResponseEntity<String> response = imageController.uploadImage(mockFile);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        
        String responseBody = response.getBody();
        assertNotNull(responseBody);
        assertTrue(responseBody.contains("File uploaded succesfully"));
    }


    @Test
    void uploadImage_EmptyFile_ShouldReturnBadRequest() throws IOException {
        MultipartFile emptyFile = new MockMultipartFile("file", "empty.jpg", "image/jpeg", new byte[]{});

        ResponseEntity<String> response = imageController.uploadImage(emptyFile);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("File is empty", response.getBody());
    }

    @Test
    void uploadImage_LargeFile_ShouldReturnBadRequest() throws IOException {
        byte[] largeData = new byte[16_000_000];
        MultipartFile largeFile = new MockMultipartFile("file", "large.jpg", "image/jpeg", largeData);

        ResponseEntity<String> response = imageController.uploadImage(largeFile);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("File is too large (max. 15 MB)", response.getBody());
    }

    @Test
    void getImage_ExistingId_ShouldReturnImage() {
        when(imageRepository.findById(1L)).thenReturn(Optional.of(image));

        ResponseEntity<byte[]> response = imageController.getImage(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertArrayEquals(image.getData(), response.getBody());
        assertEquals("attachment; filename=\"test.jpg\"", response.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION));
    }

    @Test
    void getImage_NonExistingId_ShouldReturnNotFound() {
        when(imageRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<byte[]> response = imageController.getImage(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
