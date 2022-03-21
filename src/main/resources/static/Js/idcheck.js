

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
    $("#username").attr("readonly", true);
    $("#usernameOverlay").attr("disabled", true);
    $("#usernameOverlay").css("display", "none");
    $("#resetUsername").attr("disabled", false);
    $("#resetUsername").css("display", "inline-block");
}
    return false;
} else if (result.result == "1") {
    alert("이미 사용중인 아이디입니다.");
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
