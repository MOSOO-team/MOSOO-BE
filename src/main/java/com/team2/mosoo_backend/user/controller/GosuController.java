package com.team2.mosoo_backend.user.controller;

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

    @Autowired
    private GosuService gosuService;

    // 모든 고수 정보 조회
    @GetMapping("/all")
    public List<Gosu> getAllGosu() {
        return gosuService.getAllGosu();
    }

    // 특정 고수 정보 ID 조회
    @GetMapping("/{id}")
    public ResponseEntity<Gosu> getGosuById(@PathVariable Long id) {
        Optional <Gosu> gosu = gosuService.getGosuById(id);
        return gosu.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 고수 정보 저장
    @PostMapping("")
    public ResponseEntity<Gosu> createGosu(@RequestBody Gosu gosu) {
        Gosu createdGosu = gosuService.createGosu(gosu);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdGosu);
    }

    // 고수 정보 업데이트
    @PutMapping("/{id}")
    public ResponseEntity<Gosu> updateGosu(@PathVariable Long id, @RequestBody Gosu gosuDetails) {
        Optional<Gosu> updatedGosu = gosuService.updateGosu(id, gosuDetails);
        return updatedGosu.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 고수 정보 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGosu(@PathVariable Long id) {
        gosuService.deleteGosu(id);
        return ResponseEntity.noContent().build();
    }
}
