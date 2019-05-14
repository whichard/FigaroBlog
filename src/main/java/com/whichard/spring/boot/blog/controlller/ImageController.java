package com.whichard.spring.boot.blog.controlller;

import com.whichard.spring.boot.blog.service.QiniuService;
import com.whichard.spring.boot.blog.util.ToutiaoUtil;
import com.whichard.spring.boot.blog.vo.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author wq
 * @date 2019/5/14
 */
public class ImageController {
    private static final Logger logger = LoggerFactory.getLogger(ImageController.class);
    @Autowired
    QiniuService qiniuService;

    @PostMapping("/uploadImage")
    @ResponseBody
    public String uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            //String fileUrl = ImageService.saveImage(file);
            String fileUrl = qiniuService.saveImage(file);
            if (fileUrl == null) {
                return ToutiaoUtil.getJSONString(1, "上传图片失败");
            }
            return fileUrl;//ToutiaoUtil.getJSONString(0, fileUrl);
            //return ResponseEntity.ok().body(new Response(true, "保存头像成功", fileUrl)).toString();
            //return ResponseEntity.ok().body(new Response(true, "保存头像成功", avatarUrl));
        } catch (Exception e) {
            logger.error("上传图片失败" + e.getMessage());
            return ToutiaoUtil.getJSONString(1, "上传失败");
        }
    }
}
