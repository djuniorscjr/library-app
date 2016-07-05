package com.library.app.category.repository;

import static com.library.app.commontest.category.CategoryForTestsRepository.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.library.app.category.model.Category;
import com.library.app.commontest.utils.DBCommand;
import com.library.app.commontest.utils.DBCommandTransactionalExecutor;

public class CategoryRepositoryUTest {
	private EntityManagerFactory emf;
	private EntityManager em;
	private CategoryRepository categoryRepository;
	private DBCommandTransactionalExecutor dbCommandTransactionalExecutor;

	@Before
	public void initTestCase() {
		emf = Persistence.createEntityManagerFactory("libraryPU");
		em = emf.createEntityManager();
		
		categoryRepository = new CategoryRepository();
		categoryRepository.em = em;
		dbCommandTransactionalExecutor = new DBCommandTransactionalExecutor(em);
	}

	@After
	public void closeEntityManager() {
		em.close();
		emf.close();
	}

	@Test
	public void addCategoryAndFindIt() {
		Long categoryAddedId = null;
		categoryAddedId = dbCommandTransactionalExecutor.executeCommand(() -> {
				return categoryRepository.add(java()).getId();
		});
		assertThat(categoryAddedId, is(notNullValue()));
		Category category = categoryRepository.findById(categoryAddedId);
		assertThat(category, is(notNullValue()));
		assertThat(category.getName(), is(equalTo(java().getName())));
	}
}
