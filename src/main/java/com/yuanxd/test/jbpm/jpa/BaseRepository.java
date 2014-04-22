package com.yuanxd.test.jbpm.jpa;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Dao层共通基类<br>
 * <p>
 * 所有数据访问层（@Entity注解）继承此共通基类，实现数据访问层的共通处理。
 * <p>
 * 继承了{@link JpaRepository}接口，提供了大部分的数据CRUD处理，如果子类有特殊处理方法需要提供额外的接口
 * <p>
 * 在此处添加的接口方法所有Dao都可以使用，需要在MyCustomRepository实现方法
 * 
 * @param <E>
 *            Dao所处理的Entity对象
 * @param <PK>
 *            Entity对象的主键类型，一般是{@link Long}
 * @author 袁晓冬
 */
public interface BaseRepository<T, ID extends Serializable> extends
		JpaRepository<T, ID>, JpaSpecificationExecutor<T> {

}
