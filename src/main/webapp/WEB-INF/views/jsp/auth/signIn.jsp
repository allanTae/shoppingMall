<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!-- jQuery -->
<script src="https://code.jquery.com/jquery-1.12.4.min.js"></script>
</script>

<!-- Bootstrap CSS -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>

<!-- Custom styles for this template -->
<link rel="stylesheet" type="text/css"
	  href="${pageContext.request.contextPath}/resources/common/css/login.css" >

<!-- Ajax jquery -->
<script src="http://code.jquery.com/jquery-latest.min.js"></script>

<!-- SWAL -->
<script src="https://unpkg.com/sweetalert/dist/sweetalert.min.js"></script>

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
    .sign-in_more-action .more-action_text_sign-in:after {
        position: relative;
        right: 60px;
        display: inline-block;
        width: 1px;
        height: 10px;
        background-color: #858a8d;
        content: " ";
    }
    .sign-in_more-action .more-action_text_sign-in {
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
    $(document).on('click', '.sign-in', function(e){
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
        <form:form class="form-signin" name="form" id="form" role="form"
                   modelAttribute="loginForm" method="post" action="${pageContext.request.contextPath}/auth/signIn">
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
            <span class="more-action_text_sign-in sign-in more-action_text" style="font-size:11pt;">
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

    <!-- Modal Section start-->
    <script>
        // 스프링 시큐리티, ajax 처리를 위한 csrf 토큰 정보와 토큰을 서버에 전달하기 위한 헤더 이름.
        var token = '${_csrf.token}';
        var headerName = '${_csrf.headerName}';
        $('.modal').on('hidden.bs.modal', function (e) {
            $(this).find('form')[0].reset()
        });
        $(document).on('click', '#checkEmail', function(e){
            let authId1 = $('#authId').val();
            let memberName1 = $("#memberName").val();
            $.ajax({
                type: "GET",
                url: "${pageContext.request.contextPath}/member/check/findPwd",
                dataType: 'json',
                data: {
                    "memberName": memberName1,
                    "authId": authId1
                },
                success: function (res) {
                    if (res['status'] === "in use") {
                        swal("발송 완료!", "입력하신 이메일로 임시비밀번호가 발송되었습니다.", "success").then((OK) => {
                            if(OK) {
                                $.ajax({
                                    type: "POST",
                                    url: "${pageContext.request.contextPath}/member/check/findPwd/sendEmail",
                                    data: {
                                        "authId": authId1,
                                        "memberName": memberName1
                                    },beforeSend: function(xhr){
                                        xhr.setRequestHeader(headerName, token);
                                    },
                                    success: function(res){
                                    },
                                    error: function(request, status, error){
                                         swal("발송 실패!", "이메일 발송에 실패하였습니다. 서버관리자에게 문의 해주세요.", "fail")
                                    }
                                })
                                window.location = "${pageContext.request.contextPath}/serviceLogin/loginForm";
                            }
                        })
                    } else if(res['status'] === "in not use") {
                        $('#checkMsg').html('<p style="color:red">존재하지 않는 정보입니다.</p>');
                    }else{
                        $('#checkMsg').html('<p style="color:red">이름과 아이디를 다시 확인 해 주세요.</p>');
                    }
                },
                error:function(request, status, error){
                    alert("code: " + request.status+ "\n" + "message: " + request.responseText + "\n" + "error: " + error);
                }
            });
            e.preventDefault();
        });
    </script>

    <div class="container">
        <!-- modal 구동 버튼 (trigger) -->
        <!-- <span data-toggle="modal" data-target="#myModal" style="font-size:11pt;">비밀번호 찾기</span> -->

       <!-- Modal -->
       <div class="modal fade" id="myModal" role="dialog">
           <div class="modal-dialog">

               <!-- Modal content-->
               <div class="modal-content">
                   <div class="modal-header" style="padding:35px 50px;">
                       <h4><span class="glyphicon glyphicon-lock"></span>비밀번호 찾기</h4>
                   </div>
                   <div class="modal-body" style="padding:40px 50px;">
                       <div style="color: #ac2925">
                           <center>입력한 계정으로 임시 비밀번호를 발송합니다.</center>
                       </div>
                       <hr>
                       <form role="form">
                           <div class="form-group">
                               <label for="memberName"다><span class="glyphicon glyphicon-eye-open"></span>이름</label>
                               <input type="text" class="form-control" id="memberName" placeholder="가입시 등록한 이름을 입력하세요.">
                           </div>
                           <div class="form-group">
                               <label for="authId"><span class="glyphicon glyphicon-user"></span>아이</label>디
                               <input type="text" class="form-control" id="authId" placeholder="가입시 등록한 아이디를 입력하세요.">
                           </div>
                           <button type="button" class="btn btn-success btn-block" id="checkEmail">OK</button>
                       </form>
                       <hr>
                       <div class="text-center small mt-2" id="checkMsg" style="color: red"></div>
                   </div>
                   <div class="modal-footer">
                       <button type="submit" class="btn btn-danger btn-default pull-left" data-dismiss="modal"><span
                               class="glyphicon glyphicon-remove"></span> Cancel
                       </button>
                   </div>
               </div>
           </div>
       </div>
    </div>
    <!-- Modal Section end-->
</body>
</html>
