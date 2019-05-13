// DOM 加载完再执行
//DOM 加载完再执行
$(function () {

    $("#submit").click(function () {
        $.ajax({
            url: "/register",
            type: 'POST',
            data: $('#registerForm').serialize(),
            success: function (data) {
                if (data.success) {
                    toastr.success("注册成功，请登录");
                    window.location.href = "/login"
                } else {
                    toastr.error("注册失败:" + data.message);
                }
            },
            error: function () {
                toastr.error("error!");
            }
        });
    });
});