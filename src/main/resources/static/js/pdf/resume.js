/**
 * resume.html
 */
$(function () {
    var educationNumber = 2;
    var practiceNumber = 2;
    var projectNumber = 2;
    var honorNumber = 2;
    var url = 'http://localhost:8081';
    var imgName = null;
    var formData = {};
    $("button.btn-outline-danger").click(function (event) {
        event.preventDefault();
        var object = $(this).parent().parent().parent().parent();
        object.remove();
    });
    $("button.btn-outline-primary").click(function (event) {
        event.preventDefault();
        var id = $(this).attr("id");
        var object = $(this).parent().parent();
        if (id == 1) {
            var text1 = "start" + educationNumber;
            var text2 = "end" + educationNumber;
            var text3 = "school" + educationNumber;
            var text4 = "major" + educationNumber
            var addElement = $('<div class="row"><div class="col-md-12 col-sm-12 col-12" style="margin-left: 10px"><div class="form-group row">' +
                '<label for="' + text1 + '"  class="col-md-2 col-sm-2 col-2">时间：</label><div class="col-md-3 col-sm-3 col-3">' +
                '<input class="form-control" type="month" id="' + text1 + '" name="' + text1 + '" /></div>到<div class="col-md-3 col-sm-3 col-3"> <input class="form-control" type="month" id="' +
                +text2 + '" name="' + text2 + '" /></div><div class="col-md-1 col-sm-1 col-1 offset-md-2 offset-sm-2 offset-md-2"> <button class="btn btn-outline-danger">删除此次经历</button> </div> </div> <div class="form-group row"> <label for="' + text3 + '" class="col-md-2 col-sm-2 col-2">学校：</label>' +
                '<div class="col-md-6 col-sm-6 col-6"><input class="form-control" id="' + text3 + '" name="' + text3 + '" placeholder="请输入学校"/></div></div><div class="form-group row"><label for="' + text4 + '" class="col-md-2 col-sm-2 col-2">专业：</label><div class="col-md-6 col-sm-6 col-6"><input class="form-control" id="' +
                text4 + '" name="' + text4 + '" placeholder="请输入专业"/></div></div></div></div>');
            object.before(addElement);
            educationNumber++;
        }
        else if (id == 2) {
            var text1 = "practiceStart" + practiceNumber;
            var text2 = "practiceEnd1" + practiceNumber;
            var text3 = "practiceCompany" + practiceNumber;
            var text4 = "practiceDepartment" + practiceNumber;
            var text5 = "praticeDescription" + practiceNumber;
            var addElement = $('<div class="row"><div class="col-md-12 col-sm-12 col-12" style="margin-left: 10px"><div class="form-group row"><label for="' + text1 + '" class="col-md-2 col-sm-2 col-2">时间：</label><div class="col-md-3 col-sm-3 col-3"> <input class="form-control" type="month" id="' + text1 + '" name="' + text1 + '" />' +
                '</div>到<div class="col-md-3 col-sm-3 col-3"><input class="form-control" type="month" id="' + text2 + '" name="' + text2 + '" /></div><div class="col-md-1 col-sm-1 col-1 offset-md-2 offset-sm-2 offset-md-2"><button class="btn btn-outline-danger">删除此次实习</button></div></div><div class="form-group row">' +
                '<label for="' + text3 + '" class="col-md-2 col-sm-2 col-2">公司：</label><div class="col-md-6 col-sm-6 col-6"><input class="form-control" id="' + text3 + '" name="' + text3 + '" placeholder="请输入公司"/></div></div><div class="form-group row"><label for="' + text4 + '" class="col-md-2 col-sm-2 col-2">部门：</label>' +
                '<div class="col-md-6 col-sm-6 col-6"><input class="form-control" id="' + text4 + '" name="' + text4 + '" placeholder="请输入部门"/></div></div><div class="form-group row"><label for="' + text5 + '" class="col-md-2 col-sm-2 col-2">实习介绍：</label><div class="col-md-6 col-sm-6 col-6"><textarea class="form-control" id="' +
                text5 + '" name="' + text5 + '" rows="5"placeholder="介绍实习自己做的事情，一段话，不需要换行和缩进"></textarea></div></div></div></div>');
            object.before(addElement);
            practiceNumber++;
        } else if (id == 3) {
            var text1 = "projectName" + projectNumber;
            var text2 = "projectStart" + projectNumber;
            var text3 = "projectEnd" + projectNumber;
            var text4 = "projectDuty" + projectNumber;
            var text5 = "projectIntroduction" + projectNumber;
            var text6 = "projectJobs" + projectNumber;
            var addElement = $('<div class="row"><div class="col-md-12 col-sm-12 col-12" style="margin-left: 10px"><div class="form-group row"><label for="' + text1 + '" class="col-md-2 col-sm-2 col-2">项目名称：</label><div class="col-md-6 col-sm-6 col-6"><input class="form-control" id="' + text1 + '" name="' + text1 + '" placeholder="请输入项目名称"/></div>' +
                '<div class="col-md-1 col-sm-1 col-1 offset-md-2 offset-sm-2 offset-md-2"><button class="btn btn-outline-danger">删除此项目</button></div></div><div class="form-group row"><label for="' + text2 + '" class="col-md-2 col-sm-2 col-2">时间：</label><div class="col-md-3 col-sm-3 col-3"><input class="form-control" type="month" id="' +
                text2 + '" name="' + text2 + '" /></div>到<div class="col-md-3 col-sm-3 col-3"><input class="form-control" type="month" id="' + text3 + '" name="' + text3 + '" /></div></div><div class="form-group row"><label for="' + text4 + '" class="col-md-2 col-sm-2 col-2">职责：</label><div class="col-md-6 col-sm-6 col-6"><select class="form-control" id="' +
                text4 + '" name="' + text4 + '"><option selected value="主研">主研</option><option value="参研">参研</option><option value="项目负责人员">项目负责人员</option><option value="项目核心人员">项目核心人员</option></select></div></div><div class="form-group row"><label for="' + text5 + '" class="col-md-2 col-sm-2 col-2">项目介绍：</label>' +
                '<div class="col-md-6 col-sm-6 col-6"><textarea class="form-control" id="' + text5 + '" name="' + text5 + '" rows="5" placeholder="一段话，不需要换行和缩进"></textarea></div></div><div class="form-group row"><label for="' + text6 + '" class="col-md-2 col-sm-2 col-2">具体工作：</label><div class="col-md-6 col-sm-6 col-6"><textarea class="form-control" id="' +
                text6 + '" name="' + text6 + '" rows="5" placeholder="工作职责1&#13;&#10;工作职责2&#13;&#10;工作职责2&#13;&#10;....不需要序号和缩进"></textarea></div></div></div></div>');
            object.before(addElement);
            projectNumber++;
        } else {
            var text1 = "honorYear" + honorNumber;
            var text2 = "honorName" + honorNumber;
            var addElement = $('<div class="form-group row"><div class="col-md-12 col-sm-12 col-12" style="margin-left: 10px"><div class="form-group row"><label for="' + text1 + '" class="col-md-2 col-sm-2 col-2">时间：</label><div class="col-md-6 col-sm-6 col-6"><input class="form-control" type="text" id="' + text1 + '" name="' + text1 + '" placeholder="输入年份即可，例如:2018年"/>' +
                '</div><div class="col-md-1 col-sm-1 col-1 offset-md-2 offset-sm-2 offset-md-2"><button class="btn btn-outline-danger">删除此次荣誉</button></div></div><div class="form-group row"><label for="' + text2 + '" class="col-md-2 col-sm-2 col-2">荣誉：</label><div class="col-md-6 col-sm-6 col-6"><input class="form-control" id="' + text2 + '" name="' + text2 + '" placeholder="请输入具体荣誉"/>' +
                '</div></div></div></div>');
            object.before(addElement);
            honorNumber++;
        }
        $("button.btn-outline-danger").click(function (event) {
            event.preventDefault();
            var object = $(this).parent().parent().parent().parent();
            object.remove();
        });
        addValidate();
    });
    //手机号码验证
    jQuery.validator.addMethod("isPhone", function (value, element) {
        var length = value.length;
        return this.optional(element) || (length == 11 && /^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\d{8})$/.test(value));
    },'请填写正确的手机号');
    //为个人信息和技能添加校验
    $('#name').addClass('required');
    $('#birth').addClass('required');
    $('#telephone').addClass('required isPhone');
    $('#politics').addClass('required');
    $('#email').addClass('required email');
    $('#target').addClass('required');
    $('#location').addClass('required');
    $('#skills').addClass('required');
    //为所有动态添加的元素添加校验属性
    function addValidate() {
        $('input[name^="start"]').each(function () {
            $(this).addClass('required');
        });
        $('input[name^="end"]').each(function () {
            $(this).addClass('required');
        });
        $('input[name^="school"]').each(function () {
            $(this).addClass('required');
        });
        $('input[name^="major"]').each(function () {
            $(this).addClass('required');
        });
        $('input[name^="practiceStart"]').each(function () {
            $(this).addClass('required');
        });
        $('input[name^="practiceEnd"]').each(function () {
            $(this).addClass('required');
        });
        $('input[name^="practiceCompany"]').each(function () {
            $(this).addClass('required');
        });
        $('input[name^="practiceDepartment"]').each(function () {
            $(this).addClass('required');
        });
        $('textarea[name^="praticeDescription"]').each(function () {
            $(this).addClass('required');
        });
        $('input[name^="projectName"]').each(function () {
            $(this).addClass('required');
        });
        $('input[name^="projectStart"]').each(function () {
            $(this).addClass('required');
        });
        $('input[name^="projectEnd"]').each(function () {
            $(this).addClass('required');
        });
        $('select[name^="projectDuty"]').each(function () {
            $(this).addClass('required');
        });
        $('textarea[name^="projectIntroduction"]').each(function () {
            $(this).addClass('required');
        });
        $('textarea[name^="projectJob"]').each(function () {
            $(this).addClass('required');
        });
        $('input[name^="honorYear"]').each(function () {
            $(this).addClass('required');
        });
        $('input[name^="honorName"]').each(function () {
            $(this).addClass('required');
        });
    }

    addValidate();
    $('#form').validate({
        //文本高亮
        highlight: function (element) {
            $(element).removeClass("form-control-success").addClass("form-control-danger");
            $(element).closest(".form-group").removeClass("has-success").addClass("has-danger");
        },
        //验证通过
        success: function (label) {
            label.before().removeClass("form-control-danger").addClass("form-control-success");
            label.closest(".form-group").removeClass("has-danger").addClass("has-success");
        },
        //表单提交
        submitHandler: function (form) {
            if (imgName == null) {
                toastr.info("请先上传头像！");
                return;
            }
            getFormData();
            sendFormData();
        }
    });

    $("#imgName").fileinput({
        uploadUrl: url + '/avator/upload',
        overwriteInitial: true,
        ajaxSettings: {type: 'post', processData: false, contentType: false},
        language: 'zh',
        theme: 'fa',
        maxFileSize: 1500,
        enctype: 'multipart/form-data',
        showClose: false,
        showCaption: false,
        showUpload: true,
        browseLabel: '',
        removeLabel: '',
        browseIcon: '<i class="fa fa-folder-open"></i>',
        uploadIcon: '<i class="fa fa-upload"></i>',
        removeIcon: '<i class="fa fa-trash"></i>',
        removeTitle: '取消或者重新上传',
        elErrorContainer: '#kv-avatar-errors-1',
        msgErrorClass: 'alert alert-block alert-danger',
        defaultPreviewContent: '<img src="/images/default_avatar_male.jpg" alt="Your Avatar">',
        layoutTemplates: {main2: '{preview}  {remove} {browse}'},
        allowedFileExtensions: ["jpg", "png", "gif"]
    }).on("fileuploaded", function (event, data, previewId, index) {    //一个文件上传成功
        if (data.response.success) {
            toastr.success(data.response.message);
            imgName = data.response.body;
        }
        else {
            toastr.error(data.response.message);
        }
    })
    function changeDate(date) {
        var data = date.split('-');
        return data[0] + "." + data[1];
    }

    function changeTextarea(data) {
        var text = data.split("\n");
        var length = text.length;
        if (text[length - 1] == "")
            text.pop();
        return text;
    }

    function educationsData() {
        var result = [];
        var start = [];
        var end = [];
        var school = [];
        var major = [];
        $('input[name^="start"]').each(function () {
            start.push($(this).val());
        });
        $('input[name^="end"]').each(function () {
            end.push($(this).val());
        });
        $('input[name^="school"]').each(function () {
            school.push($(this).val());
        });
        $('input[name^="major"]').each(function () {
            major.push($(this).val());
        });
        var length = start.length;
        for (var i = 0; i < length; i++) {
            var temp = {};
            temp["date"] = start[i] + "-" + end[i];
            temp["school"] = school[i];
            temp["major"] = major[i];
            result.push(temp);
        }
        return result;
    }

    function praticesData() {
        if ($('input[name^="practiceStart"]').length == 0) {
            return null;
        }
        var result = [];
        var praticeStart = [];
        var praticeEnd = [];
        var praticeCompany = [];
        var praticeDepartment = [];
        var praticeDescription = [];
        $('input[name^="practiceStart"]').each(function () {
            praticeStart.push($(this).val());
        });
        $('input[name^="practiceEnd"]').each(function () {
            praticeEnd.push($(this).val());
        });
        $('input[name^="practiceCompany"]').each(function () {
            praticeCompany.push($(this).val());
        });
        $('input[name^="practiceDepartment"]').each(function () {
            praticeDepartment.push($(this).val());
        });
        $('textarea[name^="praticeDescription"]').each(function () {
            praticeDescription.push($(this).val());
        });
        var length = praticeStart.length;
        for (var i = 0; i < length; i++) {
            var temp = {};
            temp["date"] = praticeStart[i] + "~" + praticeEnd[i];
            temp["company"] = praticeCompany[i];
            temp["department"] = praticeDepartment[i];
            temp["description"] = praticeDescription[i];
            result.push(temp);
        }
        return result;
    }

    function projectsData() {
        var result = [];
        var projectName = [];
        var projectStart = [];
        var projectEnd = [];
        var projectDuty = [];
        var projectIntroduction = [];
        var projectJob = [];
        $('input[name^="projectName"]').each(function () {
            projectName.push($(this).val());
        });
        $('input[name^="projectStart"]').each(function () {
            projectStart.push($(this).val());
        });
        $('input[name^="projectEnd"]').each(function () {
            projectEnd.push($(this).val());
        });
        $('select[name^="projectDuty"]').each(function () {
            projectDuty.push($(this).val());
        });
        $('textarea[name^="projectIntroduction"]').each(function () {
            projectIntroduction.push($(this).val());
        });
        $('textarea[name^="projectJob"]').each(function () {
            projectJob.push($(this).val());
        });
        var length = projectName.length;
        for (var i = 0; i < length; i++) {
            var temp = {};
            temp["name"] = projectName[i];
            temp["date"] = projectStart[i] + "~" + projectEnd[i];
            temp["duty"] = projectDuty[i];
            temp["introduction"] = projectIntroduction[i];
            temp["jobs"] = changeTextarea(projectJob[i]);
            result.push(temp);
        }
        return result;
    }

    function honorsData() {
        if ($('input[name^="honorYear"]').length == 0) {
            return null;
        }
        var result = [];
        var honorYear = [];
        var honorName = [];
        $('input[name^="honorYear"]').each(function () {
            honorYear.push($(this).val());
        });
        $('input[name^="honorName"]').each(function () {
            honorName.push($(this).val());
            ;
        });
        var length = honorYear.length;
        for (var i = 0; i < length; i++) {
            var temp = {};
            temp["date"] = honorYear[i];
            temp["info"] = honorName[i];
            result.push(temp);
        }
        return result;
    }

    function getFormData() {
        formData = {};
        var person = {};
        var educations = [];
        var skills = null;
        var practices = null;
        var projects = [];
        var honors = null;
        person["name"] = $('input[name="name"]').val();
        person["birth"] = changeDate($('input[name="birth"]').val());
        person["telephone"] = $('input[name="telephone"]').val();
        person["politics"] = $('select[name="politics"]').val();
        person["email"] = $('input[name="email"]').val();
        person["target"] = $('input[name="target"]').val();
        person["location"] = $('input[name="location"]').val();
        person["imgName"] = imgName;
        skills = changeTextarea($('textarea[name="skills"]').val());
        educations = educationsData();
        practices = praticesData();
        projects = projectsData();
        honors = honorsData();
        formData["person"] = person;
        formData["educations"] = educations;
        formData["skills"] = skills;
        formData["practices"] = practices;
        formData["projects"] = projects;
        formData["honors"] = honors;
    }

    function sendFormData() {
        $.ajax({
            url: url + "/pdf/resume",
            type: 'POST',
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(formData),
            success: function (data) {
                if (data.success) {
                    toastr.success(data.message);
                    $("#preview").removeClass("disabled");
                    $("#download").removeClass("disabled");
                    $("#preview").attr('href', url + "/view/" + data.body.id);
                    $("#download").attr('href', url + "/files/" + data.body.id);
                } else {
                    toastr.error(data.message);
                }
            },
            error: function () {
                toastr.error("error!");
            }
        });
    }
});