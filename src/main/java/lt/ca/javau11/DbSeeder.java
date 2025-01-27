package lt.ca.javau11;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import lt.ca.javau11.entities.Role;
import lt.ca.javau11.enums.ERole;
import lt.ca.javau11.repositories.RoleRepository;

@Component
public class DbSeeder implements CommandLineRunner {
	private static final Logger logger = LoggerFactory.getLogger(DbSeeder.class);

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        if (roleRepository.findByName(ERole.ADMIN).isEmpty()) {
            roleRepository.save(new Role(ERole.ADMIN));
            logger.info("ADMIN role added.");
        }
        
        if (roleRepository.findByName(ERole.USER).isEmpty()) {
            roleRepository.save(new Role(ERole.USER));
            logger.info("USER role added.");
        }
        if (roleRepository.findByName(ERole.MODERATOR).isEmpty()) {
            roleRepository.save(new Role(ERole.MODERATOR));
            logger.info("MODERATOR role added.");
        }
    }
}
