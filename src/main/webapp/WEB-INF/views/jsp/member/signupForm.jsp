<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<style>
	body{padding : 0px}
	#tile_header { width:100%; }
	#tile_body { width:100%; float:left; }
</style>

<!--우편번호 검색 API-->
<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script>
    // form 날자 설정 이벤트.
    $(document).ready(function () {
        setDateBox();
    });

    // 회원가입 이벤트.
	$(document).on('click', '#btnSignup', function(e){
	    // 나이 계산
	    var dt = new Date();
        var today_year = dt.getFullYear();
		e.preventDefault();
		if(!checkValidation(e)){
		    return;
		}
		var age = today_year - parseInt($('#year').val());
		$('#age').val(age);
		$("#form").submit();
	});

    // 취소 버튼 이벤트.
	$(document).on('click', '#btnCancle', function(e){
		e.preventDefault();
		location.href="${pageContext.request.contextPath}/board/getBoardList";
	});

	// 중복 아이디 검사 버튼 이벤트.
	$(document).on('click', '#btnIdCheck', function(e){
		e.preventDefault();

		// 아이디 유효성 검사가 통과한 경우,
		// 아이디 중복검사를 수행.
        var reg = /^[a-zA-Z0-9]{10,16}$/g;
        var authId = $.trim($('#authId').val());
        var validationCheck = RegTest(authId, reg);

		if(validationCheck !== false && $('#checkId').val() === "" ){
		    var authId = $('#authId').val();
            var paramData = JSON.stringify({"authId": authId});
            var headers = {"Content-Type" : "application/json; charset=UTF-8;"
                    , "X-HTTP-Method-Override" : "POST"};
            $.ajax({
                url: "${pageContext.request.contextPath}/member/checkId"
                , headers : headers
                , data : paramData
                , type : 'POST'
                , dataType : 'text'
                , success: function(result){

                }
                , error:function(request,status,error){
                    if(request.status === 400 && request.responseText.indexOf("inputInvalidException")){
                        alert("아이디는 영대소문자, 숫자로 10자~16자까지만 입력이 가능합니다.");
                    }
                }
            });
		}else{
		    var htmls = '<div style="color: red; font-size: 10pt">아이디는 영대소문자, 숫자 10~16자리만 허용됩니다.</div>';
		    $("#checkId").html(htmls);
		}
	});

    // 유효성 체크 함수.
    // return true => 유효성 검사 통과
    // return false => 유효성 검사 실패
    function RegTest(text, reg) {
        return reg.test(text);
    }

    // 아이디 유효성 체크 이벤트
    $(document).on('input', '#authId', function(e){
        // 영소문자, 숫자만 허용하는 10~16자리 문자열
        var reg = /^[a-zA-Z0-9]{10,16}$/g;
        var id = e.target.value;
        var result = RegTest($.trim(e.target.value), reg);
        var html = '';
        if(result === false){
            html += '<div style="color: red; font-size: 10pt">아이디는 영대소문자, 숫자 10~16자리만 허용됩니다.</div>';
        }else{
            html += "";
        }
        $("#checkId").html(html);
    });

    // 폼내 사용자 입력 값 공백 제거 함수.
    function textTrim(){
        var inputTexts = $("#form input[type=text]");
        for(let i=0; i<inputTexts.length; i++){
            if(inputTexts[i].value !== null && inputTexts[i].value !== ''){
                inputTexts[i].value = $.trim(inputTexts[i].value);
            }
        }
    }
    /**
     * 폼 데이터 유효성 검사 수행.
     * @param event object
     * @return boolean
     */
    function checkValidation(e){
        // 공백 제거
        textTrim();

        // 아이디 유효성 검사.
        var regAuthId = /^[a-zA-Z0-9]{10,16}$/g;
        if(!RegTest($('#authId').val(), regAuthId)){
            alert("아이디 중복체크를 확인 해 주세요.");
            return false;
        }

        // 이름 유효성 검사.
        var regName = /^[가-힣]{2,16}$/;
        if(!RegTest($("#name").val(), regName)){
            alert("이름은 한글로 2자~16자이 사이로 입력하셔야 합니다.");
            e.preventDefault();
            return false;
        }
        // 비밀번호 유효성 검사.
        // 조건1) 영문자, 숫자, 특수문자가 모두 들어가야 한다.
        // 조건2) 공백문자 들어가서는 안된다.
        var regPwd = /^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*()\-_=+~₩|\\:;"',.<>/?]{10,16}$/;
        if(!RegTest($("#pwd").val(), regPwd)){
            alert("비밀번호는 영문자, 숫자, 특수문자가 모두 들어가 10자~16자로 입력하셔야 합니다.");
            e.preventDefault();
            return false;
        }
        return true;
    }
    // 날자 출력하는 메소드.
    function setDateBox() {
        var dt = new Date();
        var com_year = dt.getFullYear();
        // 발행 뿌려주기
        $("#year").append("<option value=''>년도</option>");
        // 올해 기준으로 -50년부터 +1년을 보여준다.
        for (var y = (com_year - 50); y <= (com_year + 1); y++) {
        $("#year").append("<option value='" + y + "'>" + y + " 년도" +"</option>");
        }
        // 월 뿌려주기(1월부터 12월)
        $("#month").append("<option value=''>월</option>");
        for (var i = 1; i <= 12; i++) {
        $("#month").append("<option value='" + zeroFill(i,2) + "'>" + i + " 월" + "</option>");
        }
        // 일 뿌려주기(1일부터 31일)
        $("#day").append("<option value=''>일</option>");
        for (var i = 1; i <= 31; i++) {
        $("#day").append("<option value='" + zeroFill(i,2) + "'>" + i + " 일" + "</option>");
        }
    }
    //숫자가 한자리인 경우 0 붙이기
    function zeroFill(n, width) {
      var n = n + '';
      return n.length >= width ? n : new Array(width - n.length + 1).join('0') + n;
    }
    // 우편번호 검색창 메소드
    function execDaumPostcode() {
            new daum.Postcode({
                oncomplete: function(data) {
                    // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.
                    // 도로명 주소의 노출 규칙에 따라 주소를 표시한다.
                    // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
                    var roadAddr = data.roadAddress; // 도로명 주소 변수
                    var extraRoadAddr = ''; // 참고 항목 변수
                    // 법정동명이 있을 경우 추가한다. (법정리는 제외)
                    // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
                    if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
                        extraRoadAddr += data.bname;
                    }
                    // 건물명이 있고, 공동주택일 경우 추가한다.
                    if(data.buildingName !== '' && data.apartment === 'Y'){
                       extraRoadAddr += (extraRoadAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                    }
                    // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
                    if(extraRoadAddr !== ''){
                        extraRoadAddr = ' (' + extraRoadAddr + ')';
                    }
                    // 우편번호와 주소 정보를 해당 필드에 넣는다.
                    document.getElementById('postCode').value = data.zonecode;
                    document.getElementById('roadAddress').value = roadAddr;
                    document.getElementById('jibunAddress').value = data.jibunAddress;
                    // 참고항목 문자열이 있을 경우 해당 필드에 넣는다.
                    if(roadAddr !== ''){
                        document.getElementById('extraAddress').value = extraRoadAddr;
                    } else {
                        document.getElementById('extraAddress').value = '';
                    }
                    var guideTextBox = document.getElementById("guide");
                    // 사용자가 '선택 안함'을 클릭한 경우, 예상 주소라는 표시를 해준다.
                    if(data.autoRoadAddress) {
                        var expRoadAddr = data.autoRoadAddress + extraRoadAddr;
                        guideTextBox.innerHTML = '(예상 도로명 주소 : ' + expRoadAddr + ')';
                        guideTextBox.style.display = 'block';
                    } else if(data.autoJibunAddress) {
                        var expJibunAddr = data.autoJibunAddress;
                        guideTextBox.innerHTML = '(예상 지번 주소 : ' + expJibunAddr + ')';
                        guideTextBox.style.display = 'block';
                    } else {
                        guideTextBox.innerHTML = '';
                        guideTextBox.style.display = 'none';
                    }
                }
            }).open();
        }
</script>
<article>
	<div class="container col-md-6" role="main">
		<div class="card">
			<div class="card-header">Register</div>
			<div class="card-body">
				<form:form name="form" id="form" class="form-signup" role="form" modelAttribute="memberForm" method="post" action="${pageContext.request.contextPath}/member">

					<div class="form-group row">
						<label for="authId" class="col-md-3 col-form-label text-md-right">아이디</label>
						<div class="col-md-5">
							<form:input path="authId" id="authId" class="form-control" placeholder="아이디을 입력해 주세요" />
							<div id="checkId"></div>
						</div>
						<div class="col-md-3">
							<button type="button" name="btnIdCheck" class="btn btn-sm btn-primary" id="btnIdCheck"
									style="width=50px;">아이디 중복체크</button>
						</div>
					</div>
					<div class="row">
						<label for="authIdError" class="col-md-3" text-md-right></label>
						<form:errors path="authId" id="authIdError" class="col-md-6 form-group text-left" style="font-size:15px; color:red; width=100px;"/>
					</div>

					<div class="form-group row">
						<label for="name" class="col-md-3 col-form-label text-md-right">이름</label>
						<div class="col-md-5">
							<form:input path="name" id="name" class="form-control" placeholder="이름을 입력해 주세요" />
						</div>
					</div>
					<div class="row">
						<label for="nameError" class="col-md-3" text-md-right></label>
						<form:errors path="name" id="nameError" class="col-md-6 form-group text-left" style="font-size:15px; color:red; width=100px;"/>
					</div>

					<div class="form-group row">
						<label for="pwd" class="col-md-3 col-form-label text-md-right">비밀번호</label>
						<div class="col-md-5">
							<form:password path="pwd" id="pwd" class="form-control" placeholder="비밀번호를 입력해 주세요" />
						</div>
					</div>
					<div class="row">
						<label for="pwdError" class="col-md-3" text-md-right></label>
						<form:errors path="pwd" id="pwdError" class="col-md-6 form-group text-left" style="font-size:15px; color:red; width=100px;"/>
					</div>

					<div class="form-group row">
						<label for="rePwd" class="col-md-3 col-form-label text-md-right">비밀번호 확인</label>
						<div class="col-md-5">
							<form:password path="rePwd" id="rePwd" class="form-control" placeholder="입력하신 비밀번호와 동일하게 입력해 주세요" />
						</div>
					</div>
					<div class="row">
						<label for="rePwdError" class="col-md-3" text-md-right></label>
						<form:errors path="rePwd" id="rePwdError" class="col-md-6 form-group text-left" style="font-size:15px; color:red; width=100px;"/>
					</div>

					<div class="form-group row">
                        <label for="email" class="col-md-3 col-form-label text-md-right">이메일</label>
                        <div class="col-md-5">
                            <form:input path="email" id="email" class="form-control" placeholder="이메일을 입력해 주세요" />
                        </div>
                    </div>
                    <div class="row">
                        <label for="emailError" class="col-md-3" text-md-right></label>
                        <form:errors path="email" id="emailError" class="col-md-6 form-group text-left" style="font-size:15px; color:red; width=100px;"/>
                    </div>

					<div class="form-group row">
                        <label for="dateOfBirth" class="col-md-3 col-form-label text-md-right">생년월일</label>
                        <div class="input-group col-md-3">
                            <select name="year" id="year" title="년도" class="custom-select"></select>
                        </div>
                        <div class="input-group col-md-3">
                            <select name="month" id="month" title="월" class="custom-select"></select>
                        </div>
                        <div class="input-group col-md-3">
                            <select name="day" id="day" title="일" class="custom-select"></select>
                        </div>
                        <input type="hidden" id="age" name="age" value="default" />
                    </div>
                    <div class="row">
                        <label for="dateOfBirthError" class="col-md-3" text-md-right></label>
                        <form:errors path="dateOfBirth" id="dateOfBirthError" class="col-md-6 form-group text-left" style="font-size:15px; color:red; width=100px;"/>
                    </div>

                    <div class="form-group row">
                        <label for="postCode" class="col-md-3 col-form-label text-md-right">우편번호</label>
                        <div class="input-group col-md-4">
                            <div class="input-group-prepend">
                                <span class="input-group-text">@</span>
                            </div>
                            <form:input path="postCode" id="postCode" class="form-control" placeholder="우편번호" />
                        </div>
                        <div class="input-group col-md-3">
                            <input type="button" onclick="execDaumPostcode()" value="우편번호 찾기">
                        </div>
                    </div>
                    <div class="row">
                        <label for="postCodeError" class="col-md-3" text-md-right></label>
                        <form:errors path="postCode" id="postCodeError" class="col-md-6 form-group text-left" style="font-size:15px; color:red; width=100px;"/>
                    </div>

					<div class="form-group row">
						<label for="roadAddress" class="col-md-3 col-form-label text-md-right">도로명 주소</label>
						<div class="input-group col-md-7">
							<div class="input-group-prepend">
								<span class="input-group-text">@</span>
							</div>
							<form:input path="roadAddress" id="roadAddress" class="form-control" placeholder="도로명 주소" />
						</div>
					</div>
					<div class="row">
						<label for="roadAddressError" class="col-md-3" text-md-right></label>
						<form:errors path="roadAddress" id="roadAddressError" class="col-md-6 form-group text-left" style="font-size:15px; color:red; width=100px;"/>
					</div>

					<div class="form-group row">
						<label for="jibunAddress" class="col-md-3 col-form-label text-md-right">지번 주소</label>
						<div class="input-group col-md-7">
							<div class="input-group-prepend">
								<span class="input-group-text">@</span>
							</div>
							<form:input path="jibunAddress" id="jibunAddress" class="form-control" placeholder="지번 주소" />
						</div>
					</div>
					<div class="row">
						<label for="jibunAddressError" class="col-md-3" text-md-right></label>
						<form:errors path="jibunAddress" id="jibunAddressError" class="col-md-6 form-group text-left" style="font-size:15px; color:red; width=100px;"/>
					</div>

					<div class="row">
                        <span id="guide" style="color:#999;display:none"></span>
                    </div>

					<div class="form-group row">
                        <label for="detailAddress" class="col-md-3 col-form-label text-md-right">상세 주소</label>
                        <div class="input-group col-md-7">
                            <div class="input-group-prepend">
                                <span class="input-group-text">@</span>
                            </div>
                            <form:input path="detailAddress" id="detailAddress" class="form-control" placeholder="상세 주소" />
                        </div>
                    </div>
                    <div class="row">
                        <label for="detailAddressError" class="col-md-3" text-md-right></label>
                        <form:errors path="detailAddress" id="detailAddressError" class="col-md-6 form-group text-left" style="font-size:15px; color:red; width=100px;"/>
                    </div>

                    <div class="form-group row">
                        <label for="extraAddress" class="col-md-3 col-form-label text-md-right">참고 항목</label>
                        <div class="input-group col-md-7">
                            <div class="input-group-prepend">
                                <span class="input-group-text">@</span>
                            </div>
                            <form:input path="extraAddress" id="extraAddress" class="form-control" placeholder="참고 항목" />
                        </div>
                    </div>
                    <div class="row">
                        <label for="extraAddressError" class="col-md-3" text-md-right></label>
                        <form:errors path="extraAddress" id="extraAddressError" class="col-md-6 form-group text-left" style="font-size:15px; color:red; width=100px;"/>
                    </div>

					<div class="form-group row">
						<label for="phone" class="col-md-3 col-form-label text-md-right">전화번호</label>
						<div class="input-group col-md-7">
							<div class="input-group-prepend">
								<span class="input-group-text">@</span>
							</div>
							<form:input path="phone" id="phone" class="form-control" placeholder="전화번호를 입력해 주세요" />
						</div>
					</div>
					<div class="row">
						<label for="phoneError" class="col-md-3" text-md-right></label>
						<form:errors path="phone" id="phoneError" class="col-md-6 form-group text-left" style="font-size:15px; color:red; width=100px;"/>
					</div>

					<div class="form-group row">
					    <label for="gender" class="col-md-3 col-form-label text-md-right">성별</label>
						<div class="input-group col-md-7">
							<div class="input-group-prepend">
								<span class="input-group-text">@</span>
							</div>
							<form:radiobuttons path="gender" id="gender" items="${genders}" itemLabel="desc" itemValue="id" class="form-control" />
						</div>
					</div>
					<div class="row">
						<label for="genderError" class="col-md-3" text-md-right></label>
						<form:errors path="gender" id="genderError" class="col-md-6 form-group text-left" style="font-size:15px; color:red; width=100px;"/>
					</div>
				</form:form>
			</div>
		</div>
		<div style="margin-top:10px">
			<button type="button" class="btn btn-sm btn-primary" id="btnSignup">회원가입</button>
			<button type="button" class="btn btn-sm btn-primary" id="btnCancle">취소</button>
		</div>
	</div>
</article>