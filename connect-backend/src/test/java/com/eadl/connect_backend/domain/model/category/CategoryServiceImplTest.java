package com.eadl.connect_backend.domain.model.category;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class CategoryServiceImplTest {

    @Test
    void shouldCreateCategoryWithDefaultValues() {
        Category category = Category.create(
                "Plomberie",
                "Services de plomberie"
        );

        assertThat(category.getName()).isEqualTo("Plomberie");
        assertThat(category.getDescription()).isEqualTo("Services de plomberie");
        assertThat(category.isActive()).isTrue();
        assertThat(category.getDisplayOrder()).isEqualTo(0);
        assertThat(category.getIconUrl()).isNull();
        assertThat(category.getIdCategory()).isNull();
    }

    @Test
    void shouldUpdateCategoryNameAndDescription() {
        Category category = Category.create(
                "Plomberie",
                "Services de plomberie"
        );

        category.updateCategory("Électricité", "Services électriques");

        assertThat(category.getName()).isEqualTo("Électricité");
        assertThat(category.getDescription()).isEqualTo("Services électriques");
    }

    @Test
    void shouldDeactivateAndActivateCategory() {
        Category category = Category.create(
                "Plomberie",
                "Services de plomberie"
        );

        category.deactivate();
        assertThat(category.isActive()).isFalse();

        category.activate();
        assertThat(category.isActive()).isTrue();
    }

    @Test
    void shouldUpdateIconUrl() {
        Category category = Category.create(
                "Plomberie",
                "Services de plomberie"
        );

        category.updateIcon("https://cdn.app/icons/plomberie.png");

        assertThat(category.getIconUrl()).contains("plomberie");
    }

    @Test
    void shouldUpdateDisplayOrder() {
        Category category = Category.create(
                "Plomberie",
                "Services de plomberie"
        );

        category.updateDisplayOrder(3);

        assertThat(category.getDisplayOrder()).isEqualTo(3);
    }

    @Test
    void shouldReturnTrueWhenActive() {
        Category category = Category.create(
                "Plomberie",
                "Services de plomberie"
        );

        assertThat(category.isActive()).isTrue();
    }

    @Test
    void shouldRespectEqualsAndHashCode() {
        Category category1 = Category.create("Plomberie", "Services");
        Category category2 = Category.create("Plomberie", "Services");

        category1.setIdCategory(1L);
        category2.setIdCategory(1L);

        assertThat(category1).isEqualTo(category2);
        assertThat(category1.hashCode()).isEqualTo(category2.hashCode());
    }

    @Test
    void shouldNotBeEqualIfIdDifferent() {
        Category category1 = Category.create("Plomberie", "Services");
        Category category2 = Category.create("Plomberie", "Services");

        category1.setIdCategory(1L);
        category2.setIdCategory(2L);

        assertThat(category1).isNotEqualTo(category2);
    }

    @Test
    void shouldNotBeEqualIfIdIsNull() {
        Category category1 = Category.create("Plomberie", "Services");
        Category category2 = Category.create("Plomberie", "Services");

        assertThat(category1).isNotEqualTo(category2);
    }
}
