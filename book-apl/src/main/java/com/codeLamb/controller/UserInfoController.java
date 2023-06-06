package com.codeLamb.controller;

import com.codeLamb.VO.UserVO;
import com.codeLamb.base.BaseController;
import com.codeLamb.config.MinIOConfig;
import com.codeLamb.enums.FileTypeEnum;
import com.codeLamb.enums.UserInfoModifyType;
import com.codeLamb.form.UpdateUserInfoForm;
import com.codeLamb.grace.result.GraceJSONResult;
import com.codeLamb.grace.result.ResponseStatusEnum;
import com.codeLamb.pojo.Users;
import com.codeLamb.service.UserService;
import com.codeLamb.utils.MinIOUtils;
import com.codeLamb.utils.RedisOperator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * @author codeLamb
 */
@RestController
@Slf4j
@Api(tags = "UserInfoController 用户信息")
@RequestMapping("/userInfo")
public class UserInfoController extends BaseController {

    @Resource
    private UserService userService;

    @Resource
    private MinIOConfig minIOConfig;

    @ApiOperation(value = "查询用户信息")
    @GetMapping("/query")
    public GraceJSONResult query(@RequestParam String userId) {
        Users user = userService.queryUserInfo(userId);

        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);

        // 我关注的博主总数量
        String myFollowsCountsStr = redisOperator.get(REDIS_MY_FOLLOWS_COUNTS + ":" + userId);
        // 我的粉丝总数量
        String myFansCountsStr = redisOperator.get(REDIS_MY_FANS_COUNTS + ":" + userId);
        // 用户获赞总数，视频+评论（点赞/喜欢）总和
        String likedVlogCountsStr = redisOperator.get(REDIS_VLOG_BE_LIKED_COUNTS + ":" + userId);
        String likedVlogerCountsStr = redisOperator.get(REDIS_VLOGER_BE_LIKED_COUNTS + ":" + userId);


        Integer myFollowsCounts = 0;
        Integer myFansCounts = 0;
        Integer totalLikeMeCounts = 0;
        Integer likedVlogCounts = 0;
        Integer likedVlogerCounts = 0;

        if (StringUtils.isNotBlank(myFollowsCountsStr)) {
            myFollowsCounts = Integer.valueOf(myFollowsCountsStr);
        }
        if (StringUtils.isNotBlank(myFansCountsStr)) {
            myFansCounts = Integer.valueOf(myFansCountsStr);
        }
        if (StringUtils.isNotBlank(likedVlogCountsStr)) {
            likedVlogCounts = Integer.valueOf(likedVlogCountsStr);
        }
        if (StringUtils.isNotBlank(likedVlogCountsStr)) {
            likedVlogerCounts = Integer.valueOf(likedVlogerCountsStr);
        }

        totalLikeMeCounts = likedVlogCounts + likedVlogerCounts;

        userVO.setMyFollowsCounts(myFollowsCounts);
        userVO.setMyFansCounts(myFansCounts);
        userVO.setTotalLikeMeCounts(totalLikeMeCounts);

        return GraceJSONResult.ok(userVO);
    }

    @ApiOperation(value = "修改用户信息")
    @PostMapping("/modifyUserInfo")
    public GraceJSONResult modifyUserInfo(@RequestBody UpdateUserInfoForm form, @RequestParam Integer type) {

        UserInfoModifyType.checkUserInfoTypeIsRight(type);

        Users users = userService.updateUserInfo(form, type);
        return GraceJSONResult.ok(users);
    }

    @ApiOperation(value = "修改用户头像和背景")
    @PostMapping("/modifyImage")
    public GraceJSONResult modifyImage(MultipartFile file,
                                  @RequestParam String userId,
                                  @RequestParam Integer type) throws Exception {

        if (!type.equals(FileTypeEnum.BGIMG.type) && !type.equals(FileTypeEnum.FACE.type)) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.FILE_UPLOAD_FAILD);
        }

        // 获取上传文件的名字
        String fileName = file.getOriginalFilename();

        MinIOUtils.uploadFile(minIOConfig.getBucketName(),
                fileName, file.getInputStream());

        String uri = minIOConfig.getEndpoint() + "/" + minIOConfig.getBucketName() + "/" + fileName;

        UpdateUserInfoForm form = new UpdateUserInfoForm();
        form.setId(userId);
        if (type.equals(FileTypeEnum.BGIMG.type))
            form.setBgImg(uri);

        if (type.equals(FileTypeEnum.FACE.type))
            form.setFace(uri);

        Users users = userService.updateUserInfo(form);

        return GraceJSONResult.ok(users);
    }
}
