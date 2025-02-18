package kr.co.shorten_url_service.controller;

import kr.co.shorten_url_service.service.UrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UrlController {

    private final UrlService urlService;

    @Value("${server.base-url}") //설정 파일(application.yml)에서 값 가져오기
    private String baseUrl;

//    @CrossOrigin(origins = "nginx's ip address")
    @PostMapping("/shorten")
    public ResponseEntity<Map<String, String>> shortenUrl(@RequestBody Map<String, String> request) {
        String originalUrl = request.get("url");
        String shortKey = urlService.shortenUrl(originalUrl);
        return ResponseEntity.ok(Map.of("shortUrl", baseUrl + "/api/u/" + shortKey));
    }

    @GetMapping("/u/{shortKey}")
    public ResponseEntity<Void> redirectToOriginal(@PathVariable String shortKey) {
        Optional<String> originalUrl = urlService.getOriginalUrl(shortKey);

        if (originalUrl.isPresent()) {
            // 302 Found 상태 코드
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create(originalUrl.get()));
            return new ResponseEntity<>(headers, HttpStatus.FOUND);
        } else {
            return ResponseEntity.notFound().build(); //404 응답
        }
    }
}
