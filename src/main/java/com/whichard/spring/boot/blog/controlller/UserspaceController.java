package com.whichard.spring.boot.blog.controlller;

import java.io.File;
import java.io.FileInputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.whichard.spring.boot.blog.domain.Blog;
import com.whichard.spring.boot.blog.domain.Catalog;
import com.whichard.spring.boot.blog.domain.User;
import com.whichard.spring.boot.blog.repository.CatalogRepository;
import com.whichard.spring.boot.blog.repository.UserRepository;
import com.whichard.spring.boot.blog.service.*;
import com.whichard.spring.boot.blog.util.IpUtil;
import com.whichard.spring.boot.blog.util.ToutiaoUtil;
import org.hibernate.engine.jdbc.StreamUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.whichard.spring.boot.blog.domain.Vote;
import com.whichard.spring.boot.blog.util.ConstraintViolationExceptionHandler;
import com.whichard.spring.boot.blog.vo.Response;

/**
 * 用户主页控制器.
 *
 * @author <a href="http://www.whichard.cn">Whichard</a>
 * @since 1.0.0 2018年5月28日
 */
@Controller
@RequestMapping("/u")
public class UserspaceController {
    private static final Logger logger = LoggerFactory.getLogger(UserspaceController.class);

    @Autowired
    private UserService userService;

    @Autowired
    BlogService blogService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private CatalogService catalogService;

    @Autowired
    private CatalogRepository catalogRepository;

    @Autowired
    private SensitiveService sensitiveService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LikeService likeService;

    @Autowired
    private RedisCountService redisCountService;

    @Autowired
    QiniuService qiniuService;

    @Value("${file.server.url}")
    private String fileServerUrl;

    /**
     * 用户的主页
     *
     * @param username
     * @param model
     * @return
     */
    @GetMapping("/{username}")
    public String userSpace(@PathVariable("username") String username, Model model) {
        User user = (User) userDetailsService.loadUserByUsername(username);
        model.addAttribute("user", user);
        try {
            username = java.net.URLEncoder.encode(username, "UTF-8");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        String result = "redirect:/u/" + username + "/blogs";
        return result;
    }


    /**
     * 获取个人设置页面
     *
     * @param username
     * @param model
     * @return
     */
    @GetMapping("/{username}/profile")
    @PreAuthorize("authentication.name.equals(#username)")
    public ModelAndView profile(@PathVariable("username") String username, Model model) {
        User user = (User) userDetailsService.loadUserByUsername(username);
        model.addAttribute("user", user);
        model.addAttribute("fileServerUrl", fileServerUrl);// 文件服务器的地址返回给客户端
        return new ModelAndView("/userspace/profile", "userModel", model);
    }

    /**
     * 保存个人设置
     *
     * @param username
     * @param user
     * @return
     */
    @PostMapping("/{username}/profile")
    @PreAuthorize("authentication.name.equals(#username)")
    public ResponseEntity<Response> saveProfile(@PathVariable("username") String username, User user) {
        try {
            userService.saveOrUpdateUserWithProfile(user);
        } catch (ConstraintViolationException e) {
            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true, "修改成功", null));
    }

    /**
     * 获取编辑头像的界面
     *
     * @param username
     * @param model
     * @return
     */
    @GetMapping("/{username}/avatar")
    @PreAuthorize("authentication.name.equals(#username)")
    public ModelAndView avatar(@PathVariable("username") String username, Model model) {
        User user = (User) userDetailsService.loadUserByUsername(username);
        model.addAttribute("user", user);
        return new ModelAndView("/userspace/avatar", "userModel", model);
    }

    /**
     * 保存头像
     *
     * @param username
     * @param user
     * @return
     */
    @PostMapping("/{username}/avatar")
    @PreAuthorize("authentication.name.equals(#username)")
    public ResponseEntity<Response> saveAvatar(@PathVariable("username") String username, @RequestBody User user) {
        String avatarUrl = user.getAvatar();

        User originalUser = userService.getUserById(user.getId());
        originalUser.setAvatar(avatarUrl);
        userService.saveOrUpdateUserWithoutException(originalUser);

        return ResponseEntity.ok().body(new Response(true, "保存头像成功", avatarUrl));
    }

    /**
     * 获取用户的博客列表
     *
     * @param username
     * @param order
     * @param catalogId
     * @param keyword
     * @param async
     * @param pageIndex
     * @param pageSize
     * @param model
     * @return
     */
    @GetMapping("/{username}/blogs")
    public String listBlogsByOrder(@PathVariable("username") String username,
                                   @RequestParam(value = "order", required = false, defaultValue = "new") String order,
                                   @RequestParam(value = "catalog", required = false) Long catalogId,
                                   @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
                                   @RequestParam(value = "async", required = false) boolean async,
                                   @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex,
                                   @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                   Model model) {

        User user = (User) userDetailsService.loadUserByUsername(username);

        ;

        // 判断操作用户是否是访客
        boolean isOwner = false;

        if (SecurityContextHolder.getContext().getAuthentication() != null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
                && !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")) {
            User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal != null && user.getUsername().equals(principal.getUsername())) {
                isOwner = true;
            }
        }
        Page<Blog> page = null;

        if (catalogId != null && catalogId > 0) { // 分类查询
            Catalog catalog = catalogService.getCatalogById(catalogId);
            Pageable pageable = new PageRequest(pageIndex, pageSize);
            page = blogService.listBlogsByCatalog(catalog, pageable);
            order = "";
        } else if (order.equals("hot")) { // 最热查询
            Sort sort = new Sort(Direction.DESC, "priority", "readSize", "commentSize", "voteSize");
            Pageable pageable = new PageRequest(pageIndex, pageSize, sort);
            page = blogService.listBlogsByTitleVoteAndSort(user, keyword, pageable);
        } else if (order.equals("new")) { // 最新查询
            //	 Sort sort = new Sort(Direction.DESC,"priority","createTime");
            Pageable pageable = new PageRequest(pageIndex, pageSize);
            page = blogService.listBlogsByTitleVote(user, keyword, pageable);
        }


        List<Blog> list = page.getContent();    // 当前所在页面数据列表

        model.addAttribute("user", user);
        model.addAttribute("order", order);
        model.addAttribute("catalogId", catalogId);
        model.addAttribute("keyword", keyword);
        model.addAttribute("page", page);
        model.addAttribute("blogList", list);
        model.addAttribute("isOwner", isOwner);
        return (async == true ? "/userspace/u :: #mainContainerRepleace" : "/userspace/u");

    }

    /**
     * 获取博客展示界面
     *
     * @param username
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/{username}/blogs/{id}")
    public String getBlogById(@PathVariable("username") String username, @PathVariable("id") Long id, Model model) {
        User principal = null;
        Blog blog = blogService.getBlogById(id);

        // 3小时内只能增加一次阅读量
        if (readingIsMax(id)) {
            blogService.readingIncrease(id);
            redisCountService.increaseReadSize(id);
        }


        // 判断操作用户是否是博客的所有者
        boolean isBlogOwner = false;
        if (SecurityContextHolder.getContext().getAuthentication() != null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
                && !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")) {
            principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal != null && username.equals(principal.getUsername())) {
                isBlogOwner = true;
            }
            Iterator iterator = principal.getAuthorities().iterator();
            if (iterator.hasNext()) {
                String role = iterator.next().toString();
                if (role.equals("ROLE_ADMIN"))
                    isBlogOwner = true;
            }
        }

        // 判断操作用户的点赞情况
        List<Vote> votes = blog.getVotes();
        Vote currentVote = null; // 当前用户的点赞情况



        if (principal != null) {
            Iterator<Vote> voteIterator = votes.iterator();
            while (voteIterator.hasNext()) {
                Vote vote = voteIterator.next();
                if (vote.getUser().getName().equals(principal.getName()))
                    currentVote = vote;
            }
        }

        model.addAttribute("currentVote", currentVote);
        //currentVote.getId()
        model.addAttribute("isBlogOwner", isBlogOwner);
        model.addAttribute("blogModel", blog);
        //当前like数
        model.addAttribute("likeCount", likeService.getLikeCount(0,blog.getId() ));
        //当前用户like情况
        model.addAttribute("isLiked", likeService.getLikeStatus(principal.getId(),0, blog.getId()));
        model.addAttribute("userId", principal.getId());
        //redis - 喜欢数
        model.addAttribute("readSize", redisCountService.getReadSize(blog.getId()));

        return "/userspace/blog";
    }


    /**
     * 获取新增博客的界面
     *
     * @param model
     * @return
     */
    @GetMapping("/{username}/blogs/edit")
    @PreAuthorize("authentication.name.equals(#username)")
    public ModelAndView createBlog(@PathVariable("username") String username, Model model) {
        // 获取用户分类列表
        User user = (User) userDetailsService.loadUserByUsername(username);
        List<Catalog> catalogs = catalogService.listCatalogs(user);

        model.addAttribute("catalogs", catalogs);
        model.addAttribute("blog", new Blog(null, null, null));
        model.addAttribute("fileServerUrl", fileServerUrl);// 文件服务器的地址返回给客户端
        return new ModelAndView("/userspace/blogedit", "blogModel", model);
    }

    /**
     * 获取编辑博客的界面
     *
     * @param model
     * @return
     */
    @GetMapping("/{username}/blogs/edit/{id}")
    @PreAuthorize("authentication.name.equals(#username) or hasAnyAuthority('ROLE_ADMIN')")
    public ModelAndView editBlog(@PathVariable("username") String username, @PathVariable("id") Long id, Model model) {
        // 获取用户分类列表
        User user = (User) userDetailsService.loadUserByUsername(username);
        List<Catalog> catalogs = catalogService.listCatalogs(user);

        model.addAttribute("catalogs", catalogs);
        model.addAttribute("blog", blogService.getBlogById(id));
        model.addAttribute("fileServerUrl", fileServerUrl);// 文件服务器的地址返回给客户端
        return new ModelAndView("/userspace/blogedit", "blogModel", model);
    }

    /**
     * 保存博客
     *
     * @param username
     * @param blog
     * @return
     */
    @PostMapping("/{username}/blogs/edit")
    @PreAuthorize("authentication.name.equals(#username) or hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<Response> saveBlog(@PathVariable("username") String username, @RequestBody Blog blog) {
        // 对 Catalog 进行空处理
        if (blog.getCatalog().getId() == null) {
            return ResponseEntity.ok().body(new Response(false, "未选择分类"));
            /*List<Catalog> list = catalogRepository.findByUserAndName(userRepository.findByUsername(username),"default");
            if(list ==  null || list.size() == 0)
                catalogService.saveCatalog(new Catalog(userRepository.findByUsername(username),"default"));
            blog.setCatalog(new Catalog(userRepository.findByUsername(username),"default"));*/
        }

        try {
            //过滤敏感词
            blog.setTitle(sensitiveService.filter(blog.getTitle()));
            blog.setSummary(sensitiveService.filter(blog.getSummary()));
            blog.setContent(sensitiveService.filter(blog.getContent()));
            blog.setTags(sensitiveService.filter(blog.getTags()));

            // 判断是修改还是新增
            if (blog.getId() != null) {
                Blog orignalBlog = blogService.getBlogById(blog.getId());
                orignalBlog.setTitle(blog.getTitle());
                orignalBlog.setContent(blog.getContent());
                orignalBlog.setSummary(sensitiveService.filter(blog.getSummary()));
                orignalBlog.setCatalog(blog.getCatalog());
                orignalBlog.setTags(blog.getTags());
                blogService.saveBlog(orignalBlog);
            } else {
                User user = (User) userDetailsService.loadUserByUsername(username);
                blog.setUser(user);

                blogService.saveBlog(blog);
            }

        } catch (ConstraintViolationException e) {
            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }

        String redirectUrl = "/u/" + username + "/blogs/" + blog.getId();
        return ResponseEntity.ok().body(new Response(true, "保存博客", redirectUrl));
    }

    /**
     * 删除博客
     *
     * @param username
     * @param id
     * @return
     */
    @DeleteMapping("/{username}/blogs/{id}")
    @PreAuthorize("authentication.name.equals(#username) or hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<Response> deleteBlog(@PathVariable("username") String username, @PathVariable("id") Long id) {
        try {
            blogService.removeBlog(id);
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }

        String redirectUrl = "/u/" + username + "/blogs";
        return ResponseEntity.ok().body(new Response(true, "删除博客成功", redirectUrl));
    }

    @RequestMapping(path = {"/image"}, method = {RequestMethod.GET})
    @ResponseBody
    public void getImage(@RequestParam("name") String imageName,
                         HttpServletResponse response) {
        try {
            response.setContentType("image/jpeg");
            StreamUtils.copy(new FileInputStream(new
                    File(ToutiaoUtil.IMAGE_DIR + imageName)), response.getOutputStream());
        } catch (Exception e) {
            logger.error("读取图片错误" + imageName + e.getMessage());
        }
    }

    @PostMapping("/uploadImage")
    @ResponseBody
    public String uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            //String fileUrl = ImageService.saveImage(file);
            String fileUrl = qiniuService.saveImage(file);
            if (fileUrl == null) {
                return ToutiaoUtil.getJSONString(1, "上传图片失败");
            }
            //return fileUrl;//ToutiaoUtil.getJSONString(0, fileUrl);
            return ResponseEntity.ok().body(new Response(true, "保存头像成功", fileUrl)).toString();
            //return ResponseEntity.ok().body(new Response(true, "保存头像成功", avatarUrl));
        } catch (Exception e) {
            logger.error("上传图片失败" + e.getMessage());
            return ToutiaoUtil.getJSONString(1, "上传失败");
        }
    }

    //判定当前访问IP是否在一个小时内访问过
    public boolean readingIsMax(Long blogId) {
        //Date date = new Date();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String ip = IpUtil.getClinetIpByReq(request);
        if (redisCountService.setIpCount(blogId, ip)) {
           return true;
        } else {
            return false;
        }
    }
}

