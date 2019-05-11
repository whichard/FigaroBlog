package com.whichard.spring.boot.blog.repository;

import com.whichard.spring.boot.blog.domain.VisitorLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * VistorLog Repository接口.
 *
 * @author <a href="http://www.whichard.cn">Whichard</a>
 * @since 1.0.0 2018年6月6日
 */
@Repository
public interface VisitorLogRepository extends JpaRepository<VisitorLog, Long> {
}
