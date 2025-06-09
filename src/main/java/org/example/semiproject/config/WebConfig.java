package org.example.semiproject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

@Configuration
// WebConfig 클래스는 RestTemplate 빈을 설정하는 역할 담당
public class WebConfig {

    // @Bean 어노테이션을 사용하여 RestTemplate 객체를 스프링 빈으로 등록
    @Bean
    public RestTemplate restTemplate() {
        // RestTemplate 인스턴스 생성
        RestTemplate restTemplate = new RestTemplate();

        // 메시지 컨버터(MessageConverter) 설정
        // StringHttpMessageConverter를 UTF-8 인코딩으로 생성하여
        // 메시지 컨버터 리스트의 0번째(가장 우선순위)에 추가
        // 이렇게 하면 RestTemplate가 문자열(String) 데이터를 송수신할 때
        // 기본 인코딩을 UTF-8로 사용
        restTemplate.getMessageConverters().add(
                0, new StringHttpMessageConverter(StandardCharsets.UTF_8)
        );

        // 설정이 완료된 RestTemplate 객체를 반환
        return restTemplate;
    }

}

