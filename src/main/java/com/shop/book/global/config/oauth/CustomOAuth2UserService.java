package com.shop.book.global.config.oauth;


import com.shop.book.api.repository.MemberRepository;
import com.shop.book.domain.member.constant.MemberStatus;
import com.shop.book.domain.member.constant.OAuthType;
import com.shop.book.domain.member.constant.Role;
import com.shop.book.domain.member.dto.MemberDto;
import com.shop.book.domain.member.entity.Member;
import com.shop.book.global.config.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        System.out.println("oAuth2User ==>> " + oAuth2User);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response;
        if(registrationId.equals("naver")){
            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        }else if( registrationId.equals("kakao")){
            oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
        }else{
            return null;
        }

        //추후 작성
        String email = oAuth2Response.getEmail();
        String name = oAuth2Response.getName();
        OAuthType type = oAuth2Response.getProvider();
        Member member = memberRepository.findByEmail(email)
                .map(m -> updateSocialMember(m, oAuth2Response))
                .orElseGet(() -> createSocialMember(email, name, type));

        return new CustomUserDetails(member);
    }

    //기존에 이미 소셜로 회원가입이 되어있으면 UPDATE
    private Member updateSocialMember(Member m, OAuth2Response oAuth2Response) {
        if(!m.getEmail().equals(oAuth2Response.getEmail()) || !m.getMemberName().equals(oAuth2Response.getName())){
            m = m.toBuilder()
                    .email(oAuth2Response.getEmail())
                    .memberName(oAuth2Response.getName())
                    .build();
        }
        return memberRepository.save(m);
    }

    //소셜로 회원가입이 안되어있으면 SAVE
    private Member createSocialMember(String email, String name, OAuthType type) {
        Member member =  Member.builder()
                .email(email)
                .memberName(name)
                .password(passwordEncoder.encode(String.valueOf(System.currentTimeMillis())))
                .oAuthType(type)
                .gender("미입력")
                .phoneNumber("미입력")
                .birthDate("미입력")
                .postcode("미입력")
                .address("미입력")
                .extraAddress("미입력")
                .detailAddress("미입력")
                .status(MemberStatus.ACTIVE.typeName())
                .build()
                .addRole(Role.USER);

        memberRepository.save(member);
    }
}
