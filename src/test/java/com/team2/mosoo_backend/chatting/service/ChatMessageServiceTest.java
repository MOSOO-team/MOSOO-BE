package com.team2.mosoo_backend.chatting.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team2.mosoo_backend.bid.entity.Bid;
import com.team2.mosoo_backend.chatting.dto.ChatMessageRequestDto;
import com.team2.mosoo_backend.chatting.dto.ChatMessageResponseWrapperDto;
import com.team2.mosoo_backend.chatting.entity.ChatMessage;
import com.team2.mosoo_backend.chatting.entity.ChatMessageType;
import com.team2.mosoo_backend.chatting.entity.ChatRoom;
import com.team2.mosoo_backend.chatting.entity.ChatRoomConnection;
import com.team2.mosoo_backend.chatting.mapper.ChatMessageMapper;
import com.team2.mosoo_backend.chatting.repository.ChatMessageQueryRepository;
import com.team2.mosoo_backend.chatting.repository.ChatMessageRepository;
import com.team2.mosoo_backend.chatting.repository.ChatRoomConnectionRepository;
import com.team2.mosoo_backend.chatting.repository.ChatRoomRepository;
import com.team2.mosoo_backend.config.SecurityUtil;
import com.team2.mosoo_backend.post.entity.Post;
import com.team2.mosoo_backend.user.entity.Authority;
import com.team2.mosoo_backend.user.entity.Users;
import com.team2.mosoo_backend.user.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("채팅 메세지 서비스 단위 테스트")
class ChatMessageServiceTest {

    @Mock
    private ChatMessageRepository chatMessageRepository;

    @Mock
    private ChatRoomRepository chatRoomRepository;

    @Mock
    private ChatMessageMapper chatMessageMapper;

    @Mock
    private SecurityUtil securityUtil;

    @Mock
    private ChatRoomUtils chatRoomUtils;

    @Mock
    private ChatMessageQueryRepository chatMessageQueryRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private ChatRoomConnectionRepository chatRoomConnectionRepository;

    @Mock
    private ApplicationContext applicationContext;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private ChatMessageService chatMessageService;

    static final Long chatRoomId = 1L;

    private ChatRoomConnection chatRoomConnection;
    private ChatMessageRequestDto chatMessageRequestDto;
    private ListOperations<String, Object> listOperations;
    private Users user;
    private Users gosu;
    private Post post;
    private Bid bid;
    private ChatRoom chatRoom;
    private ChatMessage chatMessage;


    @BeforeEach
    public void setUp() {
        chatRoomConnection = new ChatRoomConnection("1", 2);

        chatMessageRequestDto = ChatMessageRequestDto.builder()
                .sourceUserId(1L)
                .type(ChatMessageType.FILE)
                .base64File("base64test")
                .fileName("testFile")
                .createdAt(LocalDateTime.now()).build();

        // Mockito 사용 -> ListOperations 클래스의 mock 객체 생성
        listOperations = mock(ListOperations.class);

        user = Users.builder().id(1L).fullName("일반 유저").authority(Authority.ROLE_USER).build();
        gosu = Users.builder().id(2L).fullName("고수 유저").authority(Authority.ROLE_GOSU).build();

        post = Post.builder().id(1L).build();
        bid = Bid.builder().id(1L).build();

        chatRoom = ChatRoom.builder().id(1L).userId(1L).gosuId(2L).post(post).bid(bid).build();
        chatMessage = ChatMessage.builder().id(1L).chatRoom(chatRoom).content("내용").type(ChatMessageType.MESSAGE).sourceUserId(1L).createdAt(LocalDateTime.now()).checked(false).build();
    }

    @Test
    @Order(1)
    @DisplayName("레디스에 저장된 채팅방 연결 수 반환 메서드 테스트")
    public void getConnectionCountTest() throws Exception {
        //given
        given(chatRoomConnectionRepository.findById(Long.toString(chatRoomId)))
                .willReturn(Optional.ofNullable(chatRoomConnection));

        //when
        Integer result = chatMessageService.getConnectionCount(chatRoomId);

        //then
        assertThat(result).isEqualTo(2);
    }

//    @Test
//    @Order(2)
//    @DisplayName("레디스에 채팅 저장 메서드 테스트")
//    public void saveChatMessageToRedisTest() throws Exception {
//        // given
//        given(chatRoomConnectionRepository.findById(Long.toString(chatRoomId))).willReturn(Optional.ofNullable(chatRoomConnection));
//        given(chatRoomUtils.convertToMultipartFile(any())).willReturn(chatMessageRequestDto);
//        given(redisTemplate.opsForList()).willReturn(listOperations);
//
//        // when
//        chatMessageService.saveChatMessageToRedis(user.getId(), chatRoomId, chatMessageRequestDto);
//
//        // then
//        // chatRoomUtils.convertToMultipartFile()이 1번 호출 되었는지 검증
//        verify(chatRoomUtils).convertToMultipartFile(chatMessageRequestDto);
//        // redisTemplate.opsForList().leftPush(redisKey, chatMessageRequestDto) 가 1번 호출 되었는지 검증
//        verify(redisTemplate.opsForList()).leftPush("chatRoom:" + chatRoomId + ":messages", chatMessageRequestDto);
//    }

    @Test
    @Order(3)
    @DisplayName("db에 채팅 저장 메서드 테스트")
    public void saveChatMessagesToDbTest() throws Exception {
        // given
        given(chatRoomRepository.findById(1L)).willReturn(Optional.ofNullable(chatRoom));
        given(redisTemplate.opsForList()).willReturn(listOperations);

        // when
        chatMessageService.saveChatMessagesToDb(chatRoomId);

        // then
        // chatMessageRepository.saveAll() 이 1번 호출 되었는지 검증
        verify(chatMessageRepository).saveAll(any());
    }

    @Test
    @Order(4)
    @DisplayName("채팅 내역 조회 메서드 테스트 - redis 에서 조회")
    public void findChatMessagesFromRedisTest() throws Exception {
        // given
        given(userRepository.findById(user.getId())).willReturn(Optional.ofNullable(user));
        given(redisTemplate.opsForList()).willReturn(listOperations);
        given(chatMessageQueryRepository.findChatMessagesByChatRoomIdUsingNoOffset(any(), eq(chatRoomId), any()))
                .willReturn(new PageImpl<>(List.of(chatMessage)));


        // when
        ChatMessageResponseWrapperDto result = chatMessageService.findChatMessages(user.getId(), chatRoomId, null, true);

        // then
        assertThat(result.getChatMessageResponseDtoList().size()).isEqualTo(1);
    }

    @Test
    @Order(5)
    @DisplayName("읽지 않음 메세지 -> 읽음으로 변환 메서드 테스트")
    public void setChatMessagesToReadTest() throws Exception {
        //given
        Long loginUserId = 2L;

        // Redis에서 가져올 메시지 목록 설정
        List<Object> messages = new ArrayList<>();
        Map<String, Object> message1 = new HashMap<>();
        message1.put("sourceUserId", 3L);
        message1.put("checked", false);
        messages.add(message1);

        Map<String, Object> message2 = new HashMap<>();
        message2.put("sourceUserId", 2L);
        message2.put("checked", false);
        messages.add(message2);
        given(redisTemplate.opsForList()).willReturn(listOperations);


        given(redisTemplate.opsForList().range("chatRoom:" + chatRoomId + ":messages", 0, -1)).willReturn(messages);
        given(chatMessageRepository.existsByChatRoomIdAndSourceUserIdNotAndCheckedIs(chatRoomId, loginUserId, false)).willReturn(true);
        when(applicationContext.getBean(SimpMessagingTemplate.class)).thenReturn(messagingTemplate); // SimpMessagingTemplate 주입

        // 읽지 않은 메시지 목록 설정
        ChatMessage unReadChatMessage = new ChatMessage();
        unReadChatMessage.setChecked(false);
        given(chatMessageRepository.findChatMessagesByChatRoomIdAndSourceUserIdNotAndCheckedIs(chatRoomId, loginUserId, false))
                .willReturn(Collections.singletonList(unReadChatMessage));

        //when
        chatMessageService.setChatMessagesToRead(chatRoomId, loginUserId);

        //then
        // 메시지가 업데이트 되었는지 확인
        assertTrue((Boolean) message1.get("checked"));
        assertFalse((Boolean) message2.get("checked")); // loginUserId와 동일하므로 checked 값은 변경되지 않아야 함

        // Redis에 업데이트된 메시지가 저장되었는지 확인
        verify(redisTemplate, times(1)).delete("chatRoom:" + chatRoomId + ":messages");
        verify(redisTemplate.opsForList(), times(1)).rightPush("chatRoom:" + chatRoomId + ":messages", message1);

        // DB에 읽지 않은 메시지가 모두 읽음 상태로 저장되었는지 확인
        verify(chatMessageRepository, times(1)).save(unReadChatMessage);

    }
}