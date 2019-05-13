// DOM 加载完再执行
//DOM 加载完再执行
"use strict";

$(function () {
    var _pageSize; // 存储用于搜索
    var _deleteBlogid;

    // 根据用户名、页面索引、页面大小获取博客列表
    function getBlogsByContent(pageIndex, pageSize) {
        $.ajax({
            url: "/blogs/all",
            contentType: 'application/json',
            data: {
                "async": true,
                "pageIndex": pageIndex,
                "pageSize": pageSize,
                "title": $("#searchName").val()
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
        getBlogsByContent(pageIndex, pageSize);
        _pageSize = pageSize;
    });

    $("#refreshBlogES").click(function () {
        $.get("/blogs/refresh", function (data, status) {
            toastr.success(data.message);
        });
    });
    // 搜索
    $("#searchNameBtn").click(function () {
        getBlogsByContent(0, _pageSize);
    });

    $("#rightContainer").on("click", ".blog-delete-blog", function () {
        _deleteBlogid = $(this).attr("blogId");
        $('#confirmBlogs').modal();
    })

    // 删除博客
    $("#submitDeleteBlog").click(function () {

        // 获取 CSRF Token 
        var csrfToken = $("meta[name='_csrf']").attr("content");
        var csrfHeader = $("meta[name='_csrf_header']").attr("content");

        $.ajax({
            url: "/blogs/" + _deleteBlogid,
            type: 'DELETE',
            beforeSend: function (request) {
                request.setRequestHeader(csrfHeader, csrfToken); // 添加  CSRF Token 
            },
            success: function (data) {
                if (data.success) {
                    // 从新刷新主界面
                    toastr.success("删除博文成功！");
                    getBlogsByContent(0, _pageSize);
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