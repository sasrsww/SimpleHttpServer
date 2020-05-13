package com.sww.controller;

import com.google.common.collect.Lists;
import com.sww.bean.Result;
import com.sww.bean.UserDTO;
import com.sww.mvc.annotation.RequestBody;
import com.sww.mvc.annotation.RequestMapping;
import com.sww.mvc.annotation.RequestParam;

import java.util.List;

/**
 * HelloWorldController
 *
 * @author shenwenwen
 * @date 2020/4/28 21:43
 */
@RequestMapping("/api")
public class HelloWorldController {

    public List<UserDTO> users = Lists.newArrayList();

    @RequestMapping("/createUser")
    public Result<UserDTO> getUserInfo(@RequestParam String name, @RequestParam Integer age) {
        UserDTO user = new UserDTO();
        user.setName(name);
        user.setAge(age);
        users.add(user);
        return Result.success();
    }

    @RequestMapping("/combineUser")
    public Result<List<UserDTO>> getUserInfo(@RequestParam String name, @RequestParam Integer age, @RequestBody UserDTO user1) {
        UserDTO user = new UserDTO();
        user.setName(name + user1.getName());
        user.setAge(age + user1.getAge());
        users.add(user);
        return Result.success(users);
    }

    @RequestMapping("/getUsers")
    public Result<List<UserDTO>> getUserInfo() {
        return Result.success(users);
    }


}
