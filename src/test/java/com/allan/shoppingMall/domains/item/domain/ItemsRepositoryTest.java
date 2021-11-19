package com.allan.shoppingMall.domains.item.domain;

import com.allan.shoppingMall.common.config.jpa.auditing.JpaAuditingConfig;
import com.allan.shoppingMall.common.config.jpa.auditing.LoginIdAuditorAware;
import com.allan.shoppingMall.domains.item.domain.clothes.Clothes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.dao.DataAccessException;
import org.springframework.security.test.context.support.WithMockUser;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@DataJpaTest(
        includeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {JpaAuditingConfig.class, LoginIdAuditorAware.class}
        )
)
@WithMockUser
public class ItemsRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    TestEntityManager testEntityManager;

    @Test
    public void 의류엔티티_생성_테스트() throws Exception {
        //given

        Clothes TEST_CLOTHES = createClothes();

        //when
        itemRepository.save(TEST_CLOTHES);
        Clothes findClothes = (Clothes) itemRepository.findById(TEST_CLOTHES.getItemId()).orElseThrow(() -> new DataAccessException("Clothes Entity is not exist") {
        });

        //then
        assertThat(findClothes.getName(), is(TEST_CLOTHES.getName()));
        assertThat(findClothes.getPrice(), is(TEST_CLOTHES.getPrice()));
        assertThat(findClothes.getStockQuantity(), is(TEST_CLOTHES.getStockQuantity()));
        assertThat(findClothes.getEngName(), is(TEST_CLOTHES.getEngName()));

    }

    private Clothes createClothes() {
        return Clothes.builder()
                .name("testName")
                .price(1000l)
                .stockQuantity(200l)
                .engName("testEngName")
                .build();
    }
}
