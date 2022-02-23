package com.allan.shoppingMall.domains.category.domain;

import com.allan.shoppingMall.common.config.jpa.auditing.JpaAuditingConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest(
        includeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = JpaAuditingConfig.class
        )
)
public class CategoryRepositoryTest {

    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    CategoryRepository categoryRepository;

    @Test
    public void 카테고리_브랜치_이름으로_조회테스트() throws Exception {
        //given
        Category TEST_CATEGORY = createCategory("testBranch", "testName", 1);
        Category TEST_CATEGORY_2 = createCategory("testBranch2", "testName2", 2);

        testEntityManager.persist(TEST_CATEGORY);
        testEntityManager.persist(TEST_CATEGORY_2);

        List<Category> categories = categoryRepository.findAll();
        assertThat(categories.size(), is(2));

        //when
        Category category = categoryRepository.findByBranchAndName(TEST_CATEGORY.getBranch(), TEST_CATEGORY.getName()).get();

        //then
        assertThat(category.getBranch(), is(TEST_CATEGORY.getBranch()));
        assertThat(category.getName(), is(TEST_CATEGORY.getName()));
        assertThat(category.getDepth(), is(TEST_CATEGORY.getDepth()));
    }

    @Test
    public void 카테고리_브랜치_이름_유무_확인테스트() throws Exception {
        //given
        Category TEST_CATEGORY = createCategory("testBranch", "testName", 1);
        testEntityManager.persist(TEST_CATEGORY);

        //when
        Boolean exists = categoryRepository.existsByBranchAndName(TEST_CATEGORY.getBranch(), TEST_CATEGORY.getName());

        //then
        assertTrue(exists);
    }

    private Category createCategory(String branch, String name, int depth){
        return Category.builder()
                .name(name)
                .branch(branch)
                .depth(depth)
                .build();
    }
}
