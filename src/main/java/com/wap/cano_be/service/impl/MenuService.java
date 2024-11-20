package com.wap.cano_be.service.impl;

import com.wap.cano_be.domain.Menu;
import com.wap.cano_be.domain.enums.Degree;
import com.wap.cano_be.dto.menu.MenuAromasResponseDto;
import com.wap.cano_be.dto.menu.MenuAttributeResponseDto;
import com.wap.cano_be.dto.menu.MenuRequestDto;
import com.wap.cano_be.dto.menu.MenuResponseDto;
import com.wap.cano_be.repository.MenuRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@Service
public class MenuService {
    private final MenuRepository menuRepository;

    public MenuService(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    private Degree getDegree(String degree){
        for (Degree deg:Degree.values()) {
            if (degree.equalsIgnoreCase(deg.name())){
                return deg;
            }
        }
        return null;
    }

    // 메뉴 조회 (attribute 한 개로)
    @ReadOnlyProperty
    public List<MenuAttributeResponseDto> getMenuByAttribute(String attribute, String degree) {
        List<Menu> menus = menuRepository.findAllByAttribute(attribute, getDegree(degree));

        if(menus.isEmpty()){
            return null;
        }

        switch (attribute.toLowerCase()){
            case "acidity"-> menus.stream()
                    .map(menu -> MenuAttributeResponseDto
                            .builder()
                            .id(menu.getId())
                            .name(menu.getName())
                            .score(menu.getScore())
                            .attribute(attribute.toLowerCase())
                            .degree(menu.getAcidity())
                            .image_url(menu.getImageUrl())
                            .build())
                    .collect(Collectors.toList());
            case "body"->menus.stream()
                    .map(menu -> MenuAttributeResponseDto
                            .builder()
                            .id(menu.getId())
                            .name(menu.getName())
                            .score(menu.getScore())
                            .attribute(attribute.toLowerCase())
                            .degree(menu.getBody())
                            .image_url(menu.getImageUrl())
                            .build())
                    .collect(Collectors.toList());
            case "bitterness"->menus.stream()
                    .map(menu -> MenuAttributeResponseDto
                            .builder()
                            .id(menu.getId())
                            .name(menu.getName())
                            .score(menu.getScore())
                            .attribute(attribute.toLowerCase())
                            .degree(menu.getBitterness())
                            .image_url(menu.getImageUrl())
                            .build())
                    .collect(Collectors.toList());
            case "sweetness"->menus.stream()
                    .map(menu -> MenuAttributeResponseDto
                            .builder()
                            .id(menu.getId())
                            .name(menu.getName())
                            .score(menu.getScore())
                            .attribute(attribute.toLowerCase())
                            .degree(menu.getSweetness())
                            .image_url(menu.getImageUrl())
                            .build())
                    .collect(Collectors.toList());
            default -> {
            }
        }
        return null;
    }

    // 아로마로 메뉴 조회
//    @ReadOnlyProperty
//    public List<MenuAromasResponseDto> getMenuByAromas(List<String> aromas){
//        List<Menu> menus = menuRepository.findAllByAromas(aromas);
//        if(menus.isEmpty()){
//            return null;
//        }
//        return menus.stream()
//                .map(menu -> MenuAromasResponseDto.builder()
//                        .name(menu.getName())
//                        .score(menu.getScore())
//                        .aromas(menu.getAromas())
//                        .imageUrl(menu.getImageUrl())
//                        .build()
//                )
//                .collect(Collectors.toList());
//    }

    // 검색어로 메뉴 조회
    // 검색어는 메뉴명으로 가정
    @ReadOnlyProperty
    public List<MenuResponseDto> getMenuByKeyword(String keyword){
        List<Menu> menus = menuRepository.findAllByKeyword(keyword.toLowerCase());
        if(menus.isEmpty()){
            return null;
        }
        
        return menus.stream()
                .map(menu -> MenuResponseDto.builder()
                        .id(menu.getId())
                        .name(menu.getName())
                        .price(menu.getPrice())
                        .acidity(menu.getAcidity())
                        .body(menu.getBody())
                        .bitterness(menu.getBitterness())
                        .sweetness(menu.getSweetness())
                        .imageUrl(menu.getImageUrl())
//                        .aromas(menu.getAromas())
                        .score(menu.getScore())
                        .build()
                )
                .collect(Collectors.toList());
    }

    // 메뉴 데이터 조회
    @ReadOnlyProperty
    public MenuResponseDto getMenuInfo(long id){
        Menu menu = menuRepository.findById(id);
        if(menu == null) return null;

        return MenuResponseDto.builder()
                .id(menu.getId())
                .name(menu.getName())
                .price(menu.getPrice())
                .imageUrl(menu.getImageUrl())
                .acidity(menu.getAcidity())
                .body(menu.getBody())
                .bitterness(menu.getBitterness())
                .sweetness(menu.getSweetness())
//                .aromas(menu.getAromas())
                .score(menu.getScore())
                .build();
    }

    // 메뉴 등록
    public void saveMenu(MenuRequestDto menuRequestDto){
        menuRepository.save(Menu.builder()
                        .name(menuRequestDto.cafeName() + " " + menuRequestDto.name())
                        .imageUrl(menuRequestDto.imageUrl())
                        .price(menuRequestDto.price())
                .build());
    }

    // 메뉴 리포트

}
