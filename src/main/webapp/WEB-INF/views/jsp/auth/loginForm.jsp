<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<style>
    html{
        max-width: 345px;
        margin: auto;
        padding: 0;
        min-width: 300px;
        min-height: 100%;
    }
    body{
        width
    }
    .sign-in_more-action .more-action_text_sign-up:after {
        position: relative;
        right: 60px;
        display: inline-block;
        width: 1px;
        height: 10px;
        background-color: #858a8d;
        content: " ";
    }
    .sign-in_more-action .more-action_text_sign-up {
        margin-left: 8px;
    }
    .social-sign-in_hr {
        display: flex;
        flex-basis: 100%;
        align-items: center;
        color: rgba(0, 0, 0, 0.35);
        font-size: 12px;
        margin: 8px 0px;
    }
    .social-sign-in_hr::before, .social-sign-in_hr::after {
        content: "";
        flex-grow: 1;
        background: rgba(0, 0, 0, 0.35);
        height: 1px;
        font-size: 0px;
        line-height: 0px;
        margin: 0px 16px;
    }
    .social-sign-in_btnWrap{
        text-align:center;
    }
    .social-sign-in_btnWrap .social-sign-in_btn{
        width: 44px;
        height: 44px;
        background-size: contain;
        border-radius: 50%;
        border: 0;
    }
    .social-sign-in_btnWrap .btnNaver{
        background-image: url("${pageContext.request.contextPath}/resources/common/img/oauth2/btnNaver.png");
    }
    .social-sign-in_btnWrap .btnGoogle{
        background-image: url("${pageContext.request.contextPath}/resources/common/img/oauth2/btnGoogle.jpeg");
    }
    .social-sign-in_btnWrap .btnKakao{
        background-image: url("${pageContext.request.contextPath}/resources/common/img/oauth2/btnKakao.png");
    }
</style>
<script>
    var errorMessage = "${errorMessage}";
    if(errorMessage){
        alert(errorMessage);
    }
    $(document).on('click', '.sign-up', function(e){
        location.href ="${pageContext.request.contextPath}/member/signupForm";
        e.preventDefault();
    });
    $(document).on('click', '#find-pwd', function(e){
        <!-- location.href ="${pageContext.request.contextPath}/serviceLogin/findPwdForm"; -->
        e.preventDefault();
    });
    // 소셜 로그인 버튼 이벤트들.
    $(document).on('click', '.btnNaver', function(e){
        location.href = "${pageContext.request.contextPath}/oauth2/authorization/naver";
        e.preventDefault();
    });
    $(document).on('click', '.btnGoogle', function(e){
        location.href = "${pageContext.request.contextPath}/oauth2/authorization/google";
        e.preventDefault();
    });
    $(document).on('click', '.btnKakao', function(e){
        location.href = "${pageContext.request.contextPath}/oauth2/authorization/kakao";
        e.preventDefault();
    });
</script>
    <div>
        <!-- login form {s} -->
        <form:form class="form-login" name="form" id="form" role="form"
                   modelAttribute="loginForm" method="post" action="${pageContext.request.contextPath}/auth/login">
            <div class="text-center mb-4">
                <h1 class="h3 mb-3 font-weight-normal">TaeTae.Official</h1>
            </div>
            <div class="form-label-group">
                <form:input path="userId" id="id" class="form-control" placeholder="User ID" required="" autofocus="" />
                <form:errors path="userId"/>
                <label for="id" class="sr-only">User ID</label>
            </div>
            <div class="form-label-group">
                <form:password path="userPwd" id="pwd" class="form-control" placeholder="User Password" required="" />
                <form:errors path="userPwd"/>
                <label for="pwd" class="sr-only">User Password</label>
            </div>
            <div class="checkbox">
                <label>
                    <form:checkbox path="useCookie"/>아이디 기억
                </label>
            </div>
            <button class="btn btn-lg btn-primary btn-block" type="submit">로그인</button>
        </form:form>
        <!-- login form {e} -->

        <p class="sign-in_more-action">
            <!-- modal 구동 버튼 (trigger) -->
            <span clsss="more-action_text_find-password more-action_text " id="find-pwd" data-toggle="modal" data-target="#myModal" style="font-size:11pt;">
               비밀번호 찾기
            </span>
            <span class="more-action_text_sign-up sign-up more-action_text" style="font-size:11pt;">
               회원가입
            </span>
        </p>
        <div class="social-sign-in">
            <div class="social-sign-in_hr">간편 로그인</div>
            <div class="social-sign-in_btnWrap">
                <button class="social-sign-in_btn btnNaver"></button>
                <button class="social-sign-in_btn btnGoogle"></button>
                <button class="social-sign-in_btn btnKakao"></button>
            </div>
        </div>
    </div>

</body>
</html>
