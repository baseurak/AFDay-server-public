package com.baseurak.AFDay.user;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;
/**
 * 연동 로그인 사용자 정보입니다.
 * @Author: Uju, Ru
 */
@Getter
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String email;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String email){
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.email = email;
    }

    public static OAuthAttributes of(Provider provider, String userNameAttributeName, Map<String, Object> attributes){
        switch(provider){
            case GOOGLE: return ofGoogle(userNameAttributeName, attributes);
            default: throw new IllegalArgumentException("Invalid Provider.");
        }
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes){
        return OAuthAttributes.builder()
                .email((String) attributes.get("email"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes){
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return OAuthAttributes.builder()
                .email((String) response.get("email"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes){
        Map<String, Object> properties = (Map<String, Object>) attributes.get("kakao_account");

        return OAuthAttributes.builder()
                .email((String) properties.get("email"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    public User toEntity(){
        return User.builder()
                .email(email)
                .build();
    }
}