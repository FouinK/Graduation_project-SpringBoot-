
function usernameCheck() {
    const username = $("#username").val();
    if (username == "") {
        alert("아이디를 입력해주세요!. 필수항목입니다.");
        $("#username").focus();
        return false;
    }
    $.ajax({
        type: "get",
        url: "/api/overlap/usernameRegister",
        data: {"username": username},
        dataType: "JSON",
        success: function (result) {
            if (result.result == "0") {
                if (confirm("이 아이디는 사용 가능합니다. \n사용하시겠습니까?")) {
                    usernameOverlapCheck = 1;
                    $("#username").attr("readonly", true);  //아이디 중복 아닐 시 아이디 입력값 수정 불가
                    $("#password").attr("readonly", false); //아이디 중복 아닐 시 비밀번호 인풋 활성화
                    $("#password2").attr("readonly", false);    //아이디 중복 아닐 시 비밀번호2 인풋 활성화
                    // $("#join_button").attr("disabled",false); //아이디 중복 아닐 시 버튼 활성화
                    $("#usernameOverlay").attr("disabled", true); //중복체크 버튼 없애기
                    $("#usernameOverlay").css("display", "none");
                    $("#resetUsername").attr("disabled", false);
                    $("#resetUsername").css("display", "inline-block");
                }
                return false;

            } else if (result.result == "1") {
                alert("이미 사용중인 아이디입니다.");
                $("#password").attr("readonly", true);
                $("#password2").attr("readonly", true);

                $("#username").focus();
            } else {
                alert("success이지만 result 값이 undefined 잘못됨");

            }
            },
        error: function (request, status,error) {
            alert("ajax 실행 실패");
            alert("code:" + request.status + "\n" + "error :" + error);
        }
    });
}

function check_pw(){  //비밀번호 확인
    var p = document.getElementById('password').value;
    var p_cf = document.getElementById('password2').value;

    if (p!=p_cf) {
        alert("비밀번호가 다릅니다." +
            "다시입력해주세요!");
        // document.getElementById('pw_check_msg').innerHTML = "비밀번호가 다릅니다. 다시 확인해 주세요.";
        // $("#join_button").attr("disabled",true); //비밀번호 다를 시 버튼 비활성화
    }else if (p == p_cf) {
        alert("비밀번호가 일치합니다.")
        // document.getElementById('pw_check_msg').innerHTML = "";
        $("#join_button").attr("disabled",false); //비밀번호 같을 시 버튼 활성화
    }
    if (p_cf=="") {
        document.getElementById('pw_check_msg').innerHTML = "";
    }
}