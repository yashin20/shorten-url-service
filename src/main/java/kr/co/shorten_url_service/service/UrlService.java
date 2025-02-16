package kr.co.shorten_url_service.service;

import kr.co.shorten_url_service.entity.ShortenedUrl;
import kr.co.shorten_url_service.repository.UrlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UrlService {

    private final UrlRepository urlRepository;
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int SHORT_KEY_LENGTH = 6;
    private final Random random = new Random();

    //단축 URL 생성 로직 - (ShortenedUrl 객체 생성 (originalUrl, shortKey))
    @Transactional
    public String shortenUrl(String originalUrl) {
        //originalUrl 을 이미 단축 URL 생성한 적이 있는지 확인
        Optional<ShortenedUrl> url = urlRepository.findByOriginalUrl(originalUrl);
        if (url.isPresent()) {
            return url.get().getShortKey();
        }

        String shortKey;
        do {
            shortKey = generateShortKey();
        } while (urlRepository.findByShortKey(shortKey).isPresent());

        ShortenedUrl shortenedUrl = new ShortenedUrl();
        shortenedUrl.setOriginalUrl(originalUrl);
        shortenedUrl.setShortKey(shortKey);
        urlRepository.save(shortenedUrl);

        return shortKey;
    }

    //원본 URL 조회
    public Optional<String> getOriginalUrl(String shortKey) {
        return urlRepository.findByShortKey(shortKey)
                .map(shortenedUrl -> shortenedUrl.getOriginalUrl());
    }

    //단축 키 생성
    private String generateShortKey() {
        StringBuilder sb = new StringBuilder(SHORT_KEY_LENGTH);
        for (int i = 0; i < SHORT_KEY_LENGTH; i++) {
            sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }
}
