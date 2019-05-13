// DOM 加载完再执行
//DOM 加载完再执行
$(function () {
    var _pageSize; // 存储用于搜索
    var _deleteCommentid;

    // 根据用户名、页面索引、页面大小获取评论列表
    function getCommentsByContent(pageIndex, pageSize) {
        $.ajax({
            url: "/comments/all",
            contentType: 'application/json',
            data: {
                "async": true,
                "pageIndex": pageIndex,
                "pageSize": pageSize,
                "content": $("#searchName").val()
            },
            success: function (data) {
                $("#mainContainer").html(data);
            },
            error: function () {
                toastr.error("刷新数据失败");
            }
        });
    }

    // 分页
    $.tbpage("#mainContainer", function (pageIndex, pageSize) {
        getCommentsByContent(pageIndex, pageSize);
        _pageSize = pageSize;
    });

    // 搜索
    $("#searchNameBtn").click(function () {
        getCommentsByContent(0, _pageSize);
    });

    $("#rightContainer").on("click", ".blog-delete-comment", function () {
        _deleteCommentid = $(this).attr("commentId");
        $('#confirmComments').modal();
    })

    // 删除评论
    $("#submitDeleteComment").click(function () {

        // 获取 CSRF Token 
        var csrfToken = $("meta[name='_csrf']").attr("content");
        var csrfHeader = $("meta[name='_csrf_header']").attr("content");

        $.ajax({
            url: "/comments/" + _deleteCommentid,
            type: 'DELETE',
            beforeSend: function (request) {
                request.setRequestHeader(csrfHeader, csrfToken); // 添加  CSRF Token 
            },
            success: function (data) {
                if (data.success) {
                    // 从新刷新主界面
                    toastr.success("删除评论成功！");
                    getCommentsByContent(0, _pageSize);
                } else {
                    toastr.error(data.message);
                }
            },
            error: function () {
                toastr.error("error!");
            }
        });
    });
});