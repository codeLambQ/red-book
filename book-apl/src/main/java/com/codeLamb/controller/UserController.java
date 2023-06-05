package com.codeLamb.controller;

import com.codeLamb.grace.result.GraceJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author codeLamb
 */
@RestController
@Slf4j
@Api(tags = "这是一个测试")
public class UserController {

    @ApiOperation(value = "hello - 这是一个测试")
    @GetMapping("/hello")
    public Object hello() {
        return GraceJSONResult.ok("返回成功");
    }
}
