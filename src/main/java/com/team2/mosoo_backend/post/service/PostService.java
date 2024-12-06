package com.team2.mosoo_backend.post.service;


import com.team2.mosoo_backend.category.entity.Category;
import com.team2.mosoo_backend.category.repository.CategoryRepository;
import com.team2.mosoo_backend.exception.CustomException;
import com.team2.mosoo_backend.exception.ErrorCode;
import com.team2.mosoo_backend.post.dto.*;
import com.team2.mosoo_backend.post.entity.Post;
import com.team2.mosoo_backend.post.mapper.PostMapper;
import com.team2.mosoo_backend.post.repository.PostRepository;
import com.team2.mosoo_backend.user.entity.UserInfo;
import com.team2.mosoo_backend.user.entity.Users;
import com.team2.mosoo_backend.user.repository.UserInfoRepository;
import com.team2.mosoo_backend.user.repository.UserRepository;
import com.team2.mosoo_backend.utils.s3bucket.service.S3BucketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final UserInfoRepository userInfoRepository;
    @Value("${default.image.url}")
    private String defaultImageUrl;

    private final S3BucketService s3BucketService;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final PostMapper postMapper;


    // 게시글 전체 조회
    public PostListResponseDto getAllPosts(int page) {

        Pageable pageable = PageRequest.of(page - 1, 10, Sort.by("id").descending());

        Page<Post> posts = postRepository.findAll(pageable);

        List<PostResponseDto> postResponseDtoList = new ArrayList<>();

        for (Post post : posts) {
            postResponseDtoList.add(postMapper.postToPostResponseDto(post));
        }

        int totalPages = (posts.getTotalPages() == 0 ? 1 : posts.getTotalPages());

        return new PostListResponseDto(postResponseDtoList, totalPages);

    }

    // 게시글 생성
    @Transactional
    public CreatePostResponseDto createPost(CreatePostRequestDto createPostRequestDto, boolean isOffer) throws IOException {

        Post post = postMapper.createPostRequestDtoToPost(createPostRequestDto);
        Users user = userRepository.findById(createPostRequestDto.getUserId()).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        UserInfo userInfo = userInfoRepository.findByUsers(user).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Category category = categoryRepository.findById(createPostRequestDto.getCategoryId()).orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

        // 연관관계 매핑
        post.setMapping(user, category);

        // 고수/ 일반 게시글 분류
        post.setIsOffer(isOffer);

        // 유저 정보로 주소 저장
        post.setAddress(userInfo.getAddress());

        // 게시글 상태 초기화
        post.setStatus(createPostRequestDto.getStatus());

        // 이미지 저장
        if(createPostRequestDto.getImageUrls() != null){
            List<MultipartFile> imageList = createPostRequestDto.getImageUrls();

            List<String> postImageUrls = s3BucketService.uploadFileList(imageList);

            post.setImgUrls(postImageUrls);
        }
        else{
            List<String> postImageUrls = new ArrayList<>();

            postImageUrls.add(defaultImageUrl);

            post.setImgUrls(postImageUrls);
        }


        return postMapper.postToCreatePostResponseDto(postRepository.save(post));
    }

    // 고수 / 일반 게시글 조회 + 페이지네이션
    public PostListResponseDto getPostsByIsOffer(int page, boolean isOffer) {

        Pageable pageable = PageRequest.of(page - 1, 9, Sort.by("id").descending());

        Page<Post> posts = postRepository.findByIsOffer(pageable, isOffer);

        List<PostResponseDto> postResponseDtoList = new ArrayList<>();

        for (Post post : posts) {
            postResponseDtoList.add(postMapper.postToPostResponseDto(post));
        }

        int totalPages = (posts.getTotalPages() == 0 ? 1 : posts.getTotalPages());

        return new PostListResponseDto(postResponseDtoList, totalPages);
    }

    // 게시글 수정
    @Transactional
    public PostResponseDto updatePost(PostUpdateRequestDto postUpdateRequestDto) {
        Post post = postRepository.findById(postUpdateRequestDto.getId()).orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        Post savedPost = update(post, postUpdateRequestDto);

        return postMapper.postToPostResponseDto(savedPost);
    }

    // 게시글 수정 로직
    private Post update(Post existPost, PostUpdateRequestDto postUpdateRequestDto) {
        existPost.setTitle(postUpdateRequestDto.getTitle());
        existPost.setDescription(postUpdateRequestDto.getDescription());
        existPost.setPrice(postUpdateRequestDto.getPrice());
        existPost.setDuration(postUpdateRequestDto.getDuration());
        return postRepository.save(existPost);
    }

    // 게시글 삭제
    @Transactional
    public PostResponseDto deletePost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        PostResponseDto postResponseDto = postMapper.postToPostResponseDto(post);

        postRepository.delete(post);

        return postResponseDto;
    }

    // 게시글 상태 수정
    @Transactional
    public PostResponseDto updatePostStatus(UpdatePostStatusRequestDto updatePostStatusRequestDto){

        Post post = postRepository.findById(updatePostStatusRequestDto.getId()).orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        post.setStatus(updatePostStatusRequestDto.getStatus());

        return postMapper.postToPostResponseDto(postRepository.save(post));
    }

    //단건 상세 조회
    public PostResponseDto getPostById(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        PostResponseDto postResponseDto = postMapper.postToPostResponseDto(post);

        postResponseDto.setFullName(post.getUser().getFullName());

        return postResponseDto;
    }

    public SearchedPostListResponseDto getSearchedPost(int page, Long categoryId, boolean isOffer, String keyword, String address) {
        Pageable pageable = PageRequest.of(page - 1, 9, Sort.by("id").descending());

        // Category 확인
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

        // 키워드를 기반으로 Post 검색
        Page<Post> posts;
        if ((keyword != null && !keyword.isEmpty()) && (address != null && !address.isEmpty())) {
            // 키워드와 주소가 둘 다 있을 경우
            posts = postRepository.findByTitleContainingAndAddressContainingAndIsOfferAndCategory(keyword, address, isOffer, category, pageable);

        } else if (keyword != null && !keyword.isEmpty()) {
            // 키워드만 있을 경우
            posts = postRepository.findByTitleContainingAndIsOfferAndCategory(keyword, isOffer, category, pageable);
        } else if (address != null && !address.isEmpty()) {
            // 주소만 있을 경우
            posts = postRepository.findByAddressContainingAndIsOfferAndCategory(address, isOffer, category, pageable);
        } else {
            // 키워드와 주소가 모두 없을 경우
            posts = postRepository.findByIsOfferAndCategory(pageable, category, isOffer);
        }


        // 검색된 Post를 PostResponseDto로 변환
        List<PostResponseDto> postResponseDtoList = posts.getContent().stream()
                .map(postMapper::postToPostResponseDto)
                .collect(Collectors.toList());

        int totalPages = (posts.getTotalPages() == 0 ? 1 : posts.getTotalPages());

        return new SearchedPostListResponseDto(postResponseDtoList, totalPages, keyword, address, category.getName());
    }

}
