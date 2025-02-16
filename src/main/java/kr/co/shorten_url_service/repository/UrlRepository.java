package kr.co.shorten_url_service.repository;

import kr.co.shorten_url_service.entity.ShortenedUrl;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;


public interface UrlRepository extends JpaRepository<ShortenedUrl, Long> {

    Optional<ShortenedUrl> findByShortKey(String shortKey);
    Optional<ShortenedUrl> findByOriginalUrl(String originalUrl);
}
