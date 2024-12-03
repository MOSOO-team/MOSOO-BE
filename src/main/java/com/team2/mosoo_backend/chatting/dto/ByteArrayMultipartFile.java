package com.team2.mosoo_backend.chatting.dto;

import lombok.AllArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

// MultipartFile 을 구현한 구현체
// 바이트 배열 -> MultipartFile 객체 생성을 위해 필요
@AllArgsConstructor
public class ByteArrayMultipartFile implements MultipartFile {

    private final String name;
    private final byte[] content;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getOriginalFilename() {
        return name;
    }

    @Override
    public String getContentType() { return ""; }

    @Override
    public boolean isEmpty() {
        return content.length == 0;
    }

    @Override
    public long getSize() {
        return content.length;
    }

    @Override
    public byte[] getBytes() {
        return content;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(content);
    }

    // 사용 x
    @Override
    public void transferTo(java.io.File dest) throws IOException, IllegalStateException {
        throw new UnsupportedOperationException("transferTo is not supported in this implementation.");
    }
}
