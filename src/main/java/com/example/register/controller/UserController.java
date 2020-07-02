package com.example.register.controller;

import com.example.register.Utils.CreateImageCode;
import com.example.register.entity.Result;
import com.example.register.entity.user.User;
import com.example.register.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("user")
@CrossOrigin
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("getImage")
    @ResponseBody
    public Map getImage(HttpServletRequest request) throws IOException {
        Map<String, String> result = new HashMap<>();
        CreateImageCode createImageCode = new CreateImageCode();
        String sessionCode = createImageCode.getCode();
        System.out.println(sessionCode);
        String key = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        request.getServletContext().setAttribute(key, sessionCode);
        BufferedImage image = createImageCode.getBuffImg();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "png", byteArrayOutputStream);
        String s = Base64Utils.encodeToString(byteArrayOutputStream.toByteArray());
        result.put("image", s);
        result.put("key", key);
        return result;
    }

    @PostMapping("register")
    @ResponseBody
    public Result register(String code, String key, @RequestBody User user, HttpServletRequest request) {
        Result result = new Result();
        System.out.println(key);
        String keyCode = (String) request.getServletContext().getAttribute(key);
        System.out.println(keyCode);
        try {
            System.out.println(code.equalsIgnoreCase(keyCode));
            if (code.equalsIgnoreCase(keyCode)) {
                userService.register(user);
                result.setMsg("注册成功");
            } else {
                throw new RuntimeException("验证码错误");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setMsg(e.getMessage()).setState(false);
        }
        return result;
    }

    @PostMapping("login")
    @ResponseBody
    public Result login(@RequestBody User user, HttpServletRequest request) {
        System.out.println(user);
        Result result = new Result();
        try {
            User userDB = userService.login(user);
            request.getServletContext().setAttribute(userDB.getId(), userDB.getUsername());
            result.setState(true).setMsg("登录成功").setUserID(userDB.getId());

        } catch (Exception e) {
            result.setMsg(e.getMessage()).setState(false);
        }
        return result;
    }

    @GetMapping("findByPage")
    @ResponseBody
    public Map<String, Object> findByPage(Integer page, Integer rows) {
        page = page == null ? 1 : page;
        rows = rows == null ? 5 : rows;
        HashMap<String, Object> map = new HashMap<>();
        List<User> users = userService.findByPage(page, rows);
        Integer totals = userService.findTotals();
        Integer totalPage = totals % rows == 0 ? totals / rows : totals / rows + 1;
        map.put("users", users);
        map.put("totals", totals);
        map.put("totalPage", totalPage);
        map.put("page", page);
        return map;
    }

    @GetMapping("delete")
    @ResponseBody
    public Result delete(String id) {
        Result result = new Result();
        try {
            userService.delete(id);
            result.setState(true).setMsg("删除成功");
        } catch (Exception e) {
            result.setState(false).setMsg(e.getMessage());
        }
        return result;
    }

    @PostMapping("update")
    @ResponseBody
    public Result update(@RequestBody User user) {
        Result result = new Result();
        try {
            userService.update(user);
            result.setState(true).setMsg("修改成功");
        } catch (Exception e) {
            result.setState(false).setMsg(e.getMessage());
        }
        return result;
    }

    @GetMapping("findById")
    @ResponseBody
    public User findById(String id) {
        return userService.findById(id);
    }

    @PostMapping("addUser")
    @ResponseBody
    public Result addUser(@RequestBody User user) {
        Result result = new Result();
        try {
            userService.register(user);
            result.setState(true).setMsg("添加成功");
        } catch (Exception e) {
            result.setState(false).setMsg(e.getMessage());
        }
        return result;
    }
}
