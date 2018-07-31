package codesquad.category.domain;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static junit.framework.TestCase.assertFalse;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoryRepositoryTest {
    @Autowired
    private CategoryRepository categoryRepository;


    @Test
    public void 카테고리_저장() {

        //Given
        Category child = new Category("child");
        Category parent = new Category("parent");

        //When
        categoryRepository.save(parent);
        child.setParentCategory(parent);
        Category saved = categoryRepository.save(child);

        //Then
        assertThat(categoryRepository.findById(saved.getId()).get().getParentCategory().getTitle()).isEqualTo(parent.getTitle());


    }

    @Test
    @Transactional
    public void deleteParentCategory() {
        //When
        Category parent = categoryRepository.findById(1L).get();
        categoryRepository.deleteByParentCategory(parent);
        categoryRepository.delete(parent);
        //Then

        assertFalse(categoryRepository.existsById(parent.getId()));
        assertFalse(categoryRepository.existsByParentCategory(parent));
    }
}

