package codesquad.user.controller;

import codesquad.support.dto.ResponseModel;
import codesquad.support.util.SessionUtil;
import codesquad.user.domain.User;
import codesquad.user.dto.LoginDto;
import codesquad.user.dto.SignupDto;
import codesquad.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseModel signup(@Valid @RequestBody SignupDto dto) {
        return ResponseModel.ofSuccess(userService.signup(dto), "회원가입에 성공했습니다.");
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public ResponseModel login(HttpSession session, @Valid @RequestBody LoginDto dto) {
        User user = userService.login(dto);
        SessionUtil.setUserInSession(session, user);
        return ResponseModel.ofSuccess(user, "성공적으로 접속되었습니다.");
    }
}
