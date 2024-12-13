package com.team2.mosoo_backend.user.controller;

import com.team2.mosoo_backend.user.dto.GosuRequestDto;
import com.team2.mosoo_backend.user.dto.GosuUpdateRequestDto;
import com.team2.mosoo_backend.user.entity.Gosu;
import com.team2.mosoo_backend.user.service.GosuService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import retrofit2.http.Path;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gosu")
public class GosuController {


    private final GosuService gosuService;

    // 모든 고수 정보 조회
    @GetMapping("/")
    public List<Gosu> getAllGosu() {
        return gosuService.getAllGosu();
    }

    // 특정 고수 정보 ID 조회
    @GetMapping("/{gosuId}")
    public ResponseEntity<Gosu> getGosuById(@PathVariable(value = "id") Long gosuId) {
        Optional <Gosu> gosu = gosuService.getGosuById(gosuId);
        return gosu.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 고수 정보 저장
    @PostMapping("")
    public ResponseEntity<Long> createGosu(@RequestBody GosuRequestDto gosuRequestDto) {
        Long gosuId = gosuService.createGosu(gosuRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(gosuId);
    }

    // 고수 정보 업데이트
    @PutMapping("/{id}")
    public ResponseEntity<Long> updateGosu(@PathVariable(value = "id") Long id, @RequestBody GosuUpdateRequestDto gosuDetails) {
        Long updatedGosu = gosuService.updateGosu(id, gosuDetails);
        return ResponseEntity.status(HttpStatus.OK).body(updatedGosu);
    }

    // 고수 정보 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGosu(@PathVariable(value = "id") Long id) {
        gosuService.deleteGosu(id);
        return ResponseEntity.noContent().build();
    }
}
