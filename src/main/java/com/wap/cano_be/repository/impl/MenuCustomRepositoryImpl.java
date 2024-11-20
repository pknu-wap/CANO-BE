package com.wap.cano_be.repository.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wap.cano_be.domain.Menu;
import com.wap.cano_be.domain.enums.Degree;
import com.wap.cano_be.repository.MenuCustomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.wap.cano_be.domain.QMenu.menu;

@RequiredArgsConstructor
public class MenuCustomRepositoryImpl implements MenuCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    /**
     * <h2>속성의 수치 범위내 Menu를 찾는 동적 쿼리문</h2>
     * NumberPath + PathBuilder 를 활용해 attribute 에 맞는 필드를 찾습니다.
     * BooleanBuilder 사용해 degree 에 해당하는 범위를 찾습니다.
     * 해당 범위 내 Menu 를 전부 찾아 List 형식으로 반환합니다.
     * @param attribute 메뉴의 속성 명
     * @param degree 속성의 정도
     */
    @Override
    public List<Menu> findAllByAttribute(String attribute, Degree degree){
        NumberPath<Double> attributePath = new PathBuilder<>(Menu.class, "menu").getNumber(attribute, Double.class);
        BooleanBuilder predicate = new BooleanBuilder();

        switch (degree){
            case NONE:
                predicate.and(attributePath.goe(Degree.NONE.getPercentage())
                        .and(attributePath.loe(Degree.LOW.getPercentage())));
                break;
            case LOW:
                predicate.and(attributePath.gt(Degree.LOW.getPercentage())
                        .and(attributePath.loe(Degree.MEDIUM.getPercentage())));
                break;
            case MEDIUM:
                predicate.and(attributePath.gt(Degree.MEDIUM.getPercentage())
                        .and(attributePath.loe(Degree.HIGH.getPercentage())));
                break;
            case HIGH:
                predicate.and(attributePath.gt(Degree.HIGH.getPercentage())
                        .and(attributePath.loe(Degree.VERY_HIGH.getPercentage())));
                break;
            case VERY_HIGH:
                predicate.and(attributePath.gt(Degree.VERY_HIGH.getPercentage())
                        .and(attributePath.loe(1.0)));
                break;
            default:
                break;
        }

        return jpaQueryFactory.selectFrom(menu)
                .where(predicate)
                .fetch();
    }

    @Override
    public List<Menu> findAllByAromas(List<String> aromas){
        return null;
//        BooleanBuilder predicate = new BooleanBuilder();
//
//        for (String aroma: aromas) {
//            predicate.or(menu.aromas.containsKey(aroma));
//        }
//
//        return jpaQueryFactory.selectFrom(menu)
//                .where(predicate)
//                .fetch();
    }

    @Override
    public List<Menu> findAllByKeyword(String keyword){
        BooleanExpression predicate = !StringUtils.hasText(keyword)?null:menu.name.contains(keyword);
        return jpaQueryFactory.selectFrom(menu)
                .where(predicate)
                .fetch();
    }
}
