package com.yuanxd.test.jbpm.jpa;

import static org.springframework.data.querydsl.QueryDslUtils.QUERY_DSL_PRESENT;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.LockModeRepositoryPostProcessor;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.core.RepositoryMetadata;

public class CustomRepositoryFactory extends JpaRepositoryFactory {

	public CustomRepositoryFactory(EntityManager entityManager) {
		super(entityManager);
	}

	@Override
	protected Object getTargetRepository(RepositoryMetadata metadata) {
		return super.getTargetRepository(metadata);
	}

	/**
	 * Callback to create a {@link JpaRepository} instance with the given
	 * {@link EntityManager}
	 * 
	 * @param <T>
	 * @param <ID>
	 * @param entityManager
	 * @see #getTargetRepository(RepositoryMetadata)
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected <T, ID extends Serializable> JpaRepository<?, ?> getTargetRepository(
	        RepositoryMetadata metadata, EntityManager entityManager) {
		Class<?> repositoryInterface = metadata.getRepositoryInterface();
		JpaEntityInformation<?, Serializable> entityInformation = getEntityInformation(metadata
		        .getDomainType());
		if (isQueryDslExecutor(repositoryInterface)) {
			throw new RuntimeException("not support");
		}
		SimpleJpaRepository<?, ?> repo = new SimpleCustomRepository(entityInformation,
		        entityManager);
		repo.setLockMetadataProvider(LockModeRepositoryPostProcessor.INSTANCE
		        .getLockMetadataProvider());
		return repo;
	}

	/**
	 * Returns whether the given repository interface requires a QueryDsl
	 * specific implementation to be chosen.
	 * 
	 * @param repositoryInterface
	 * @return
	 */
	private boolean isQueryDslExecutor(Class<?> repositoryInterface) {

		return QUERY_DSL_PRESENT
		        && QueryDslPredicateExecutor.class.isAssignableFrom(repositoryInterface);
	}

	@Override
	protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
		return SimpleCustomRepository.class;
	}
}
