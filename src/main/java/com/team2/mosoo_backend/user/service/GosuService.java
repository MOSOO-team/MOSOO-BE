package com.team2.mosoo_backend.user.service;

import com.team2.mosoo_backend.user.entity.Gosu;
import com.team2.mosoo_backend.user.repository.GosuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class GosuService {
    @Autowired
    private GosuRepository gosuRepository;

    // 모든 고수 정보 호출
    public List<Gosu> getAllGosu() {
        return gosuRepository.findAll();
    }

    // 특정 고수를 ID로 가져오는 메서드
    public Optional<Gosu> getGosuById(Long id) {
        return gosuRepository.findById(id);
    }

    // 고수 정보 생성
    public Gosu createGosu(Gosu gosu) {
        if (gosu.getUserInfo() != null) {
            gosu.getUserInfo().setIsGosu(true);
        }

        gosu.setCreatedAt(LocalDateTime.now());
        return gosuRepository.save(gosu);
    }

    // 고수 정보 업데이트
    public Optional<Gosu> updateGosu(Long id, Gosu gosuDetails) {
        return gosuRepository.findById(id).map(gosu ->{
            gosu.setUserInfo(gosuDetails.getUserInfo());
            gosu.setGender(gosuDetails.getGender());
            gosu.setBusinessName(gosuDetails.getBusinessName());
            gosu.setBusinessNumber(gosuDetails.getBusinessNumber());
            gosu.setGosuInfoPhone(gosuDetails.getGosuInfoPhone());
            gosu.setGosuInfoAddress(gosuDetails.getGosuInfoAddress());
            gosu.setCategory(gosuDetails.getCategory());
            return gosuRepository.save(gosu);
        });
    }

    // 고수 정보 삭제
    public void deleteGosu(Long id) {
        gosuRepository.deleteById(id);
    }
}
