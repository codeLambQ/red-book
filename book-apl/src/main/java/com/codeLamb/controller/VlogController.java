package com.codeLamb.controller;

import com.codeLamb.VO.IndexVlogVO;
import com.codeLamb.base.BaseController;
import com.codeLamb.config.MinIOConfig;
import com.codeLamb.enums.YesOrNo;
import com.codeLamb.form.VlogBO;
import com.codeLamb.grace.result.GraceJSONResult;
import com.codeLamb.service.VlogService;
import com.codeLamb.utils.MinIOUtils;
import com.codeLamb.utils.PagedGridResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author codeLamb
 */
@RestController
@RequestMapping("/vlog")
@Api(tags = "VlogController 短视频相关")
public class VlogController extends BaseController {

    @Resource
    private VlogService vlogService;



    @ApiOperation(value = "发布短视频")
    @PostMapping("/publish")
    public GraceJSONResult publish(@RequestBody VlogBO vlogBO) {

        vlogService.createVlog(vlogBO);

        return GraceJSONResult.ok();
    }

    @ApiOperation(value = "查询短视频")
    @GetMapping("/indexList")
    public GraceJSONResult indexList(@RequestParam(defaultValue = "", required = false) String search,
                                     @RequestParam Integer page,
                                     @RequestParam Integer pageSize) {

        if (page == null) {
            page = COMMON_START_PAGE;
        }

        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }

        PagedGridResult pagedGridResult = vlogService.getIndexVlog(search, page, pageSize);

        return GraceJSONResult.ok(pagedGridResult);
    }

    @ApiOperation(value = "点击查看短视频")
    @GetMapping("/detail")
    public GraceJSONResult detail(@RequestParam(defaultValue = "", required = false) String userId,
                                     @RequestParam String vlogId) {


        IndexVlogVO vlogDetailById = vlogService.getVlogDetailById(vlogId);

        return GraceJSONResult.ok(vlogDetailById);
    }

    @ApiOperation(value = "将短视频设为私有")
    @PostMapping("/changeToPrivate")
    public GraceJSONResult changeToPrivate(@RequestParam String userId,
                                           @RequestParam String vlogId) {
        vlogService.updateVlogToPrivateOrrPublic(userId,vlogId, YesOrNo.YES.type);

        return GraceJSONResult.ok();
    }

    @ApiOperation(value = "将短视频设为公开")
    @PostMapping("/changeToPublic")
    public GraceJSONResult changeToPublic(@RequestParam String userId,
                                           @RequestParam String vlogId) {

        vlogService.updateVlogToPrivateOrrPublic(userId,vlogId, YesOrNo.NO.type);

        return GraceJSONResult.ok();
    }

    @ApiOperation(value = "查询自己的公开视频")
    @GetMapping("/myPublicList")
    public GraceJSONResult myPublicList(@RequestParam String userId,
                                     @RequestParam Integer page,
                                     @RequestParam Integer pageSize) {

        if (page == null) {
            page = COMMON_START_PAGE;
        }
        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }

        PagedGridResult pagedGridResult = vlogService.getMyVlogByUserId(userId, page, pageSize, YesOrNo.NO.type);

        return GraceJSONResult.ok(pagedGridResult);
    }

    @ApiOperation(value = "查询自己的私有视频")
    @GetMapping("/myPrivateList")
    public GraceJSONResult myPrivateList(@RequestParam String userId,
                                        @RequestParam Integer page,
                                        @RequestParam Integer pageSize) {

        if (page == null) {
            page = COMMON_START_PAGE;
        }

        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }

        PagedGridResult pagedGridResult = vlogService.getMyVlogByUserId(userId, page, pageSize, YesOrNo.YES.type);

        return GraceJSONResult.ok(pagedGridResult);
    }

}
