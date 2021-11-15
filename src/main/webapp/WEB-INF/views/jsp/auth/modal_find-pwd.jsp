<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>

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