package com.wap.cano_be.repository;

import com.wap.cano_be.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long>, MenuCustomRepository, JpaSpecificationExecutor<Menu> {
    Menu findById (long id);
}