package com.wap.cano_be.repository;

import com.wap.cano_be.domain.Menu;
import com.wap.cano_be.domain.enums.Degree;

import java.util.List;

public interface MenuCustomRepository {
    List<Menu> findAllByAttribute(String attribute, Degree degree);
    List<Menu> findAllByKeyword(String keyword);
}
