package com.library.app.category.repository;

import static com.library.app.commontest.category.CategoryForTestsRepository.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.library.app.category.model.Category;
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
		Long categoryAddedId = dbCommandTransactionalExecutor.executeCommand(() -> {
				return categoryRepository.add(java()).getId();
		});
		assertThat(categoryAddedId, is(notNullValue()));
		Category category = categoryRepository.findById(categoryAddedId);
		assertThat(category, is(notNullValue()));
		assertThat(category.getName(), is(equalTo(java().getName())));
	}
	
	@Test
	public void findCategoryByIdNotFound(){
		Category category = categoryRepository.findById(999L);
		assertThat(category, is(nullValue()));
	}
	
	@Test
	public void findCategoryByIdWithNullId(){
		Category category = categoryRepository.findById(null);
		assertThat(category, is(nullValue()));
	}
	
	@Test
	public void updateCategory() {
		Long categoryAddedId = dbCommandTransactionalExecutor.executeCommand(() -> {
				return categoryRepository.add(java()).getId();
		});
		
		Category categoryAfterAdd = categoryRepository.findById(categoryAddedId);
		assertThat(categoryAfterAdd.getName(), is(equalTo(java().getName())));
		
		categoryAfterAdd.setName(cleanCode().getName());
		dbCommandTransactionalExecutor.executeCommand(() -> {
			categoryRepository.update(categoryAfterAdd);
			return null;
		});
		
		Category categoryAfterUpdate = categoryRepository.findById(categoryAddedId);
		assertThat(categoryAfterUpdate.getName(), is(equalTo(cleanCode().getName())));
	}
	
	@Test
	public void findAllCategories(){
		dbCommandTransactionalExecutor.executeCommand(() -> {
			allCategories().forEach(categoryRepository::add);
			return null;
		});
		
		List<Category> categories = categoryRepository.findAll("name");
		assertThat(categories.size(), is(equalTo(4)));
		assertThat(categories.get(0).getName(), is(equalTo(architecture().getName())));
		assertThat(categories.get(1).getName(), is(equalTo(cleanCode().getName())));
		assertThat(categories.get(2).getName(), is(equalTo(java().getName())));
		assertThat(categories.get(3).getName(), is(equalTo(networks().getName())));
	}
	
	@Test
	public void alreadyExistForAdd(){
		dbCommandTransactionalExecutor.executeCommand(() -> {
			categoryRepository.add(java());
			return null;
		});
		assertThat(categoryRepository.alreadyExist(java()), is(equalTo(true)));
		assertThat(categoryRepository.alreadyExist(cleanCode()), is(equalTo(false)));
	}
	
	@Test
	public void alreadyExistsCategoryWithId(){
		final Category java = dbCommandTransactionalExecutor.executeCommand(() -> {
			categoryRepository.add(cleanCode());
			return categoryRepository.add(java());
		});
		assertThat(categoryRepository.alreadyExist(java), is(equalTo(false)));
		
		java.setName(cleanCode().getName());
		assertThat(categoryRepository.alreadyExist(java), is(equalTo(true)));
		
		java.setName(networks().getName());
		assertThat(categoryRepository.alreadyExist(java), is(equalTo(false)));
	}
	
	public void existsById(){
		final Long categoryAddedId = dbCommandTransactionalExecutor.executeCommand(() -> {
			return categoryRepository.add(java()).getId();
		});
		
		assertThat(categoryRepository.existsById(categoryAddedId), is(equalTo(true)));
		assertThat(categoryRepository.existsById(999L), is(equalTo(false)));
	}
}
