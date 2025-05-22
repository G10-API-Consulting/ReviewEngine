package com.example.ReviewEngine.repository;

import com.example.ReviewEngine.model.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.Optional;
import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

class TagRepositoryTest {

    @Autowired private TagRepository tagRepository;

    @Test
    void findByName_andUniqueConstraint() {
        Tag t = new Tag("alpha");
        tagRepository.save(t);

        Optional<Tag> fetched = tagRepository.findByName("alpha");
        assertThat(fetched).isPresent()
                .get()
                .extracting(Tag::getName)
                .isEqualTo("alpha");
    }
}
