package com.yuanxd.test.jbpm.jpa;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
@SuppressWarnings("unchecked")
public class SimpleCustomRepository<T, ID extends Serializable> extends
		SimpleJpaRepository<T, ID> implements BaseRepository<T, ID> {
	private final EntityManager entityManager;

	/**
	 * 重写查询时分页处理，如果当前页大于总页数，则返回最后一页，而不是空记录
	 */
	@Override
	public Page<T> findAll(Specification<T> spec, Pageable pageable) {
		TypedQuery<T> query = null;
		Method getCountQuery = null;
		try {
			Method queryMethod = SimpleJpaRepository.class.getDeclaredMethod(
					"getQuery", Specification.class, Pageable.class);
			getCountQuery = SimpleJpaRepository.class.getDeclaredMethod(
					"getCountQuery", Specification.class);
			queryMethod.setAccessible(true);
			getCountQuery.setAccessible(true);
			if (null != queryMethod) {
				query = (TypedQuery<T>) queryMethod
						.invoke(this, spec, pageable);
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		if (pageable == null) {
			return new PageImpl<T>(query.getResultList());
		}

		query.setFirstResult(pageable.getOffset());
		query.setMaxResults(pageable.getPageSize());
		TypedQuery<Long> countQuery = null;
		try {
			countQuery = (TypedQuery<Long>) getCountQuery.invoke(this, spec);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		Long total = QueryUtils.executeCountQuery(countQuery);
		List<T> content = null;
		if (total > pageable.getOffset()) {
			content = query.getResultList();
		} else {
			if (total == 0) {
				content = Collections.<T> emptyList();
			} else {
				// 计算最后一页
				long pages = total / pageable.getPageSize();
				if (total % pageable.getPageSize() == 0) {
					pages -= 1;
				}
				query.setFirstResult((int) pages * pageable.getPageSize());
				content = query.getResultList();
			}
		}
		return new PageImpl<T>(content, pageable, total);
	}

	public SimpleCustomRepository(Class<T> domainClass, EntityManager em) {
		super(domainClass, em);
		this.entityManager = em;
	}

	public SimpleCustomRepository(
			JpaEntityInformation<T, ID> entityInformation,
			EntityManager entityManager) {
		super(entityInformation, entityManager);
		this.entityManager = entityManager;
	}

}
