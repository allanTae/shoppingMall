package com.allan.shoppingMall.domains.member.presentation;

import com.allan.shoppingMall.domains.member.domain.Gender;
import com.allan.shoppingMall.domains.member.domain.model.MemberForm;
import com.allan.shoppingMall.domains.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/member/signupForm")
    public String signUpForm(Model model){
        model.addAttribute("memberForm", new MemberForm());
        return "/member/signupForm";
    }

    @PostMapping("/member")
    public String join(@ModelAttribute("memberForm") @Valid MemberForm form, BindingResult bindingResult, HttpServletResponse response){

        // 유효성 검사를 위한 정규표현식.
        String ID_PATTERN = "^[a-zA-Z0-9]{10,16}$";
        String NAME_PATTERN = "^[가-힣]{2,16}$";
        String PASSWORD_PATTERN = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*()\\-_=+~₩|\\\\:;\"',.<>/?]{10,16}$";
        String DATE_OF_BIRTH_PATTERN ="^[0-9]{4}-[0-9]{2}-[0-9]{2}$";
        String EMAIL_PATTER = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$";

        String dateOfBirth = form.getYear() + "-" + form.getMonth() + "-" + form.getDay();
        form.setDateOfBirth(dateOfBirth);

        if(!Pattern.compile(ID_PATTERN).matcher(form.getAuthId()).find()){
            log.error("memberController's join() fail authId validation check!!");
            bindingResult.rejectValue("authId", "id.invalidatedVal", "아이디는 영대소문자, 숫자로 10자~16자까지만 입력이 가능합니다.");
        }
        if(!Pattern.compile(NAME_PATTERN).matcher(form.getName()).find()){
            log.error("memberController's join() fail name validation check!!");
            bindingResult.rejectValue("name", "name.invalidatedVal", "이름은 한글 2자~16자까지만 입력이 가능합니다.");
        }
        if(!Pattern.compile(PASSWORD_PATTERN).matcher(form.getPwd()).find()){
            log.error("memberController's join() fail pwd validation check!!");
            bindingResult.rejectValue("pwd", "pwd.invalidatedVal", "비밀번호는 영대소문자, 숫자, 특수문자를 반드시 한글자 포함하고, 10~16자까지만 입력이 가능합니다.");
        }
        if(!Pattern.compile(DATE_OF_BIRTH_PATTERN).matcher(dateOfBirth).find()){
            log.error("memberController's join() fail dateOfBirth validation check!!");
            bindingResult.rejectValue("dateOfBirth", "dateOfBirth.invalidatedVal", "생년월일을 제대로 입력해 주세요.");
        }
        if(!bindingResult.hasErrors() && !form.getPwd().equals(form.getRePwd())){
            log.error("memberController's join() fail pwd, rePwd validation check!!");
            bindingResult.rejectValue("pwd", "pwd.invalidatedVal", "비밀번호와 비밀번호 확인이 일치하지 않습니다.");
            bindingResult.rejectValue("re_pwd", "re_pwd.invalidatedVal", "비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        }
        if(!Pattern.compile(EMAIL_PATTER).matcher(form.getEmail()).find()){
            log.error("memberController's join() fail email validation check!!");
            bindingResult.rejectValue("email", "email.invalidatedVal", "올바른 이메일 형식이 아닙니다.");
        }

        if(bindingResult.hasErrors()){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            log.error("========biningFilerError List========");
            for(FieldError error: bindingResult.getFieldErrors()){
                log.error(error.getDefaultMessage());
            }
            log.error("======================================");
            return "member/signupForm";
        }
        memberService.join(form);
        return "redirect:/index";
    }

    @ModelAttribute("genders")
    public List<Gender> genders(){
        List<Gender> list = new ArrayList<Gender>();
        list.add(Gender.WOMAN);
        list.add(Gender.MAN);

        return list;
    }
}
