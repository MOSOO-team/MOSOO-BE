package com.team2.mosoo_backend.user.controller;




import com.team2.mosoo_backend.user.dto.UserListResponse;
import com.team2.mosoo_backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin/member")
public class UserAdminController {

    private final UserService userService;

    @GetMapping("")
    public ResponseEntity<UserListResponse> getMembers(
            @RequestParam(required = false, value = "page", defaultValue = "1") int page) {
        UserListResponse memberList = userService.getMemberList(page);

        return ResponseEntity.ok(memberList);
    }

    @GetMapping("/delete")
    public ResponseEntity<UserListResponse> getDeleteMember(
            @RequestParam(required = false, value = "page", defaultValue = "1") int page) {
        UserListResponse memberList = userService.getIsDeleteMembers(page);

        return ResponseEntity.ok(memberList);
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<?> deleteMember(@PathVariable("memberId") Long memberId) {
        userService.deleteMemberByMemberId(memberId);
        return ResponseEntity.status(HttpStatus.OK).body("회원 정지 완료 : " + memberId);
    }
}
