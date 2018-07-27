package codesquad.user.controller;

import codesquad.support.dto.response.ResponseModel;
import codesquad.support.test.AcceptanceTest;
import codesquad.user.domain.User;
import codesquad.user.dto.LoginDto;
import codesquad.user.dto.SignupDto;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.assertThat;

public class UserAcceptanceTest extends AcceptanceTest {
    private static final Logger logger = LoggerFactory.getLogger(UserAcceptanceTest.class);

    private SignupDto signupDto;
    private LoginDto loginDto;

    @Before
    public void setUp() {
        signupDto = new SignupDto("email@email.com", "password1234", "password1234", "윤찬명", "010-5114-6224");
        loginDto = new LoginDto("sanjigi@slipp.net", "password2");
    }

    @Test
    public void 회원가입_성공() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<ResponseModel<User>> response = jsonRequest("/api/users", HttpMethod.POST, signupDto, new ParameterizedTypeReference<ResponseModel<User>>() {
        });
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        userRepository.delete(response.getBody().getData());
    }

    @Test
    public void 회원가입_실패() {
        signupDto.setPassword(null);
        ResponseEntity<ResponseModel<Void>> response = jsonRequest("/api/users", HttpMethod.POST, signupDto, new ParameterizedTypeReference<ResponseModel<Void>>() {
        });
        assertThat(response.getBody().getErrors()).isNotEmpty();
    }

    @Test
    public void 회원가입때_이메일_중복() {
        signupDto.setEmail(defaultUser().getEmail());
        ResponseEntity<ResponseModel<Void>> response = jsonRequest("/api/users", HttpMethod.POST, signupDto, new ParameterizedTypeReference<ResponseModel<Void>>() {
        });
        logger.debug("{}", response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    public void 회원가입때_전화번호_중복() {
        signupDto.setPhoneNumber(defaultUser().getPhoneNumber());
        ResponseEntity<ResponseModel<Void>> response = jsonRequest("/api/users", HttpMethod.POST, signupDto, new ParameterizedTypeReference<ResponseModel<Void>>() {
        });
        logger.debug("{}", response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    public void 로그인_성공() {
        ResponseEntity<ResponseModel<Void>> response = jsonRequest("/api/users/login", HttpMethod.POST, loginDto, new ParameterizedTypeReference<ResponseModel<Void>>() {
        });

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void 로그인_계정_없음() {
        loginDto.setEmail("test@test.com");
        ResponseEntity<ResponseModel<Void>> response = jsonRequest("/api/users/login", HttpMethod.POST, loginDto, new ParameterizedTypeReference<ResponseModel<Void>>() {
        });

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void 로그인_비밀번호_불일치() {
        loginDto.setPassword("otherpassword123");
        ResponseEntity<ResponseModel<Void>> response = jsonRequest("/api/users/login", HttpMethod.POST, loginDto, new ParameterizedTypeReference<ResponseModel<Void>>() {
        });

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }
}
