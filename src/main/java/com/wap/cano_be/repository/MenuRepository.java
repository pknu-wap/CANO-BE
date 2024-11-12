package com.wap.cano_be.repository;

import com.wap.cano_be.domain.Menu;
import com.wap.cano_be.domain.enums.Degree;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long>, MenuCustomRepository {
    Menu findById (long id);
}