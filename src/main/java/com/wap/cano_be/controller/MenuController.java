package com.wap.cano_be.controller;

import com.wap.cano_be.controller.enums.MenuType;
import com.wap.cano_be.domain.enums.Degree;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/menus")
public class MenuController {
    // Params 유효성 검사
    // Enum 타입이라서 추후 Custom Annotation 으로 유효성 검사 가능
    private boolean isVaildType(String type) {
        for (MenuType menuType : MenuType.values()) {
            if (menuType.name().equalsIgnoreCase(type)) {
                return false;
            }
        }
        return true;
    }

    // Degree 유효성 검사
    private boolean isVaildDegree(String degree){
        for(Degree deg : Degree.values()){
            if(deg.name().equalsIgnoreCase(degree)){
                return true;
            }
        }
        return false;
    }

    // 메뉴 조회
    @GetMapping("/attribute")
    public ResponseEntity<?> getMenu(
            @RequestParam String type,
            @RequestParam String degree
            ) {
        if(!isVaildType(type)){
            Map<String, String> response = new HashMap<>();
            response.put("error", type + " is not a valid menu type");
            return ResponseEntity.badRequest().body(response);
        }

        if(!isVaildDegree(degree)){
            Map<String, String> response = new HashMap<>();
            response.put("error", degree + " is not a valid value");
            return ResponseEntity.badRequest().body(response);
        }

        if(isVaildType(type) && isVaildDegree(degree)) {
            Map<String, String> response = new HashMap<>();
            response.put("data", "data입니다.");
            return ResponseEntity.ok().body(response);
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("error", "bad request params");
            return ResponseEntity.badRequest().body(response);
        }
    }

    // 메뉴 조회 - 아로마
    @GetMapping("/aroma")
    public ResponseEntity<?> getMenuByAroma(
            @RequestParam String aroma
    ){
        return ResponseEntity.ok().body(aroma);
    }

    // 검색어로 메뉴 조회
    @GetMapping
    public ResponseEntity<?> getMenuByKeyword(@RequestParam("query") String keyword){
        return ResponseEntity.ok().body(keyword);
    }

    // 메뉴 데이터 조회
    @GetMapping("/{menu_id}")
    public ResponseEntity<?> getMenuByMenuId(@PathVariable("menu_id") String menuId){
        // id로 메뉴 찾기
        return ResponseEntity.ok().body(menuId);
    }

    // 메뉴로부터 리뷰 조회
    @GetMapping("/{menu_id}/reviews")
    public ResponseEntity<?> getMenuByMenuIdReview(@PathVariable("menu_id") String menuId){
        // id로 메뉴 찾기
        // 있다면 작성된 리뷰 모두 찾기
        return ResponseEntity.ok().body(menuId);
    }
    
    // 메뉴 등록
    @PostMapping
    public ResponseEntity<?> createMenu(@RequestBody MenuRequestDto menuRequestDto){
        // DTO 를 서비스로 전달
        // 정상 처리 시 200 응답
        // 기타 에러 4XX 응답
        Map<String, String> response = new HashMap<>();
        response.put("success", "정상 처리 되었습니다");
        return ResponseEntity.ok().body(response);
    }

    // 메뉴 리포트
    @PostMapping("/report")
    public ResponseEntity<?> reportMenu(@RequestBody MenuReportDto menuReportDto){
        // DTO 를 서비스로 전달
        // 정상 처리 시 200 응답
        // 기타 에러 4XX 응답
        Map<String, String> response = new HashMap<>();
        response.put("success", "정상 처리 되었습니다");
        return ResponseEntity.ok().body(response);
    }
}
