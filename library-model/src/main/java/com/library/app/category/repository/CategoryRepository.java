package com.library.app.category.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.library.app.category.model.Category;

public class CategoryRepository {
	
	EntityManager em;
	
	public Category add(Category category){
		em.persist(category);
		return category;
	}

	public Category findById(Long id) {
		if(id == null){
			return null;
		}
		return em.find(Category.class, id);
	}

	public void update(Category category) {
		em.merge(category);
	}

	@SuppressWarnings("unchecked")
	public List<Category> findAll(String orderField) {
		return em.createQuery("Select e From Category e Order by e." + orderField).getResultList();
	}

	public boolean alreadyExist(Category category) {
		StringBuilder sb = new StringBuilder();
		sb.append("Select 1 from Category e where e.name = :name");
		if(category.getId() != null){
			sb.append(" and e.id != :id");
		}
		Query query = em.createQuery(sb.toString());
		query.setParameter("name", category.getName());
		if(category.getId() != null){
			query.setParameter("id", category.getId());
		}
		return query.setMaxResults(1).getResultList().size() > 0;
	}

	public boolean existsById(Long id) {
		StringBuilder sb = new StringBuilder();
		sb.append("Select 1 from Category e where e.id = :id");
		
		Query query = em.createQuery(sb.toString());
		query.setParameter("id", id);
		
		return query.setMaxResults(1).getResultList().size() > 0;
	}
}
