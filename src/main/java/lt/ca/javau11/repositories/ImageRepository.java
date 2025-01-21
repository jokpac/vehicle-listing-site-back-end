package lt.ca.javau11.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import lt.ca.javau11.entities.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {

}
