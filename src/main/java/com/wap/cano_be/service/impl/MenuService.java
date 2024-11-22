package com.wap.cano_be.service.impl;

import com.wap.cano_be.domain.Menu;
import com.wap.cano_be.domain.enums.Degree;
import com.wap.cano_be.dto.menu.MenuRequestDto;
import com.wap.cano_be.dto.menu.MenuResponseDto;
import com.wap.cano_be.repository.MenuRepository;
import com.wap.cano_be.repository.MenuSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class MenuService {
    private final MenuRepository menuRepository;
    private final ImageService imageService;

    private Degree getDegree(String degree) {
        for (Degree deg : Degree.values()) {
            if (degree.equalsIgnoreCase(deg.name())) {
                return deg;
            }
        }
        return null;
    }

    // 메뉴 조회 (attribute 한 개로)
//    @ReadOnlyProperty
//    public List<MenuAttributeResponseDto> getMenuByAttribute(String attribute, String degree) {
//        List<Menu> menus = menuRepository.findAllByAttribute(attribute, getDegree(degree));
//
//        if(menus.isEmpty()){
//            return null;
//        }
//
//        switch (attribute.toLowerCase()){
//            case "acidity"-> menus.stream()
//                    .map(menu -> MenuAttributeResponseDto
//                            .builder()
//                            .id(menu.getId())
//                            .name(menu.getName())
//                            .score(menu.getScore())
//                            .attribute(attribute.toLowerCase())
//                            .degree(menu.getAcidity())
//                            .image_url(menu.getImageUrl())
//                            .build())
//                    .collect(Collectors.toList());
//            case "body"->menus.stream()
//                    .map(menu -> MenuAttributeResponseDto
//                            .builder()
//                            .id(menu.getId())
//                            .name(menu.getName())
//                            .score(menu.getScore())
//                            .attribute(attribute.toLowerCase())
//                            .degree(menu.getBody())
//                            .image_url(menu.getImageUrl())
//                            .build())
//                    .collect(Collectors.toList());
//            case "bitterness"->menus.stream()
//                    .map(menu -> MenuAttributeResponseDto
//                            .builder()
//                            .id(menu.getId())
//                            .name(menu.getName())
//                            .score(menu.getScore())
//                            .attribute(attribute.toLowerCase())
//                            .degree(menu.getBitterness())
//                            .image_url(menu.getImageUrl())
//                            .build())
//                    .collect(Collectors.toList());
//            case "sweetness"->menus.stream()
//                    .map(menu -> MenuAttributeResponseDto
//                            .builder()
//                            .id(menu.getId())
//                            .name(menu.getName())
//                            .score(menu.getScore())
//                            .attribute(attribute.toLowerCase())
//                            .degree(menu.getSweetness())
//                            .image_url(menu.getImageUrl())
//                            .build())
//                    .collect(Collectors.toList());
//            default -> {
//            }
//        }
//        return null;
//    }

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
//    @ReadOnlyProperty
//    public List<MenuResponseDto> getMenuByKeyword(String keyword){
//        List<Menu> menus = menuRepository.findAllByKeyword(keyword.toLowerCase());
//        if(menus.isEmpty()){
//            return null;
//        }
//
//        return menus.stream()
//                .map(menu -> MenuResponseDto.builder()
//                        .id(menu.getId())
//                        .name(menu.getName())
//                        .price(menu.getPrice())
//                        .acidity(menu.getAcidity())
//                        .body(menu.getBody())
//                        .bitterness(menu.getBitterness())
//                        .sweetness(menu.getSweetness())
//                        .imageUrl(menu.getImageUrl())
////                        .aromas(menu.getAromas())
//                        .score(menu.getScore())
//                        .build()
//                )
//                .collect(Collectors.toList());
//    }

    // 메뉴 데이터 조회
//    @ReadOnlyProperty
//    public MenuResponseDto getMenuInfo(long id){
//        Menu menu = menuRepository.findById(id);
//        if(menu == null) return null;
//
//        return MenuResponseDto.builder()
//                .id(menu.getId())
//                .name(menu.getName())
//                .price(menu.getPrice())
//                .imageUrl(menu.getImageUrl())
//                .acidity(menu.getAcidity())
//                .body(menu.getBody())
//                .bitterness(menu.getBitterness())
//                .sweetness(menu.getSweetness())
////                .aromas(menu.getAromas())
//                .score(menu.getScore())
//                .build();
//    }

    // 메뉴 등록
    public ResponseEntity<MenuResponseDto> createMenu(MenuRequestDto menuRequestDto, MultipartFile image) {
        Menu menu = Menu.builder()
                .name(menuRequestDto.cafeName() + " " + menuRequestDto.name())
                .price(menuRequestDto.price())
                .build();

        if (image != null && !image.isEmpty()) {
            String imageUrl = imageService.uploadImage(image);
            menu.setImageUrl(imageUrl);
        }

        menu = menuRepository.save(menu);

        return ResponseEntity.ok().body(MenuResponseDto.builder()
                .id(menu.getId())
                .name(menu.getName())
                .price(menu.getPrice())
                .imageUrl(menu.getImageUrl())
                .build());
    }

    // 메뉴 조회
    public ResponseEntity<MenuResponseDto> getMenuById(Long menuId) {
        Menu menu = menuRepository.findById(menuId).orElseThrow(() -> new IllegalArgumentException("Menu not found"));

        return ResponseEntity.ok().body(mapToMenuResponseDto(menu));
    }

    private MenuResponseDto mapToMenuResponseDto(Menu menu) {
        return MenuResponseDto.builder()
                .id(menu.getId())
                .name(menu.getName())
                .price(menu.getPrice())
                .score(menu.getAverageScore())
                .acidity(menu.getAverageAcidity())
                .body(menu.getAverageBody())
                .bitterness(menu.getAverageBitterness())
                .sweetness(menu.getAverageSweetness())
                .imageUrl(menu.getImageUrl())
                .build();
    }

    // 메뉴 검색
    public ResponseEntity<List<MenuResponseDto>> searchMenus(String query, String attribute, String degree) {
        Specification<Menu> spec = Specification.where(MenuSpecification.nameContains(query));

        if (attribute != null && degree != null) {
            Double[] range = getRangeForDegree(degree);
            if (range != null) {
                spec = spec.and(MenuSpecification.attributeInRange(attribute, range[0], range[1]));
            }
        }

        List<Menu> menus = menuRepository.findAll(spec);
        return ResponseEntity.ok().body(menus.stream()
                .map(this::mapToMenuResponseDto)
                .collect(Collectors.toList()));
    }

    private Double[] getRangeForDegree(String degree) {
        return switch (degree.toUpperCase()) {
            case "NONE" -> new Double[]{0.0, 0.0};
            case "LOW" -> new Double[]{0.0, 0.25};
            case "MEDIUM" -> new Double[]{0.25, 0.5};
            case "HIGH" -> new Double[]{0.5, 0.75};
            case "VERY_HIGH" -> new Double[]{0.75, 1.0};
            default -> null;
        };
    }

    // 메뉴 리포트

}
