package com.example.ReviewEngine.repository;

import com.example.ReviewEngine.model.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class TagRepositoryTest {

    @Autowired
    private TagRepository tagRepo;

    @Test
    void findByName_returnsSavedEntity() {
        Tag t = new Tag("UniqueTag");
        tagRepo.save(t);

        Optional<Tag> found = tagRepo.findByName("UniqueTag");
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("UniqueTag");
    }
}
