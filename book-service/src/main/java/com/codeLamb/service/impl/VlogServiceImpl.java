package com.codeLamb.service.impl;

import com.codeLamb.VO.IndexVlogVO;
import com.codeLamb.base.BaseController;
import com.codeLamb.enums.YesOrNo;
import com.codeLamb.exceptions.GraceException;
import com.codeLamb.form.VlogBO;
import com.codeLamb.grace.result.ResponseStatusEnum;
import com.codeLamb.mapper.VlogMapper;
import com.codeLamb.mapper.VlogMapperCustom;
import com.codeLamb.pojo.Vlog;
import com.codeLamb.service.VlogService;
import com.codeLamb.utils.PagedGridResult;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author codeLamb
 */
@Service
public class VlogServiceImpl extends BaseController implements VlogService {

    @Resource
    private VlogMapper vlogMapper;
    @Resource
    private VlogMapperCustom vlogMapperCustom;

    @Resource
    private Sid sid;


    @Override
    @Transactional
    public void createVlog(VlogBO vlogBO) {
        Vlog vlog = new Vlog();
        BeanUtils.copyProperties(vlogBO, vlog);

        // 生成 vlog id
        String vlogId = sid.nextShort();

        vlog.setId(vlogId);
        // 设置一些默认信息，也可以在数据库实现
        vlog.setLikeCounts(0);
        vlog.setCommentsCounts(0);
        vlog.setIsPrivate(YesOrNo.NO.type);
        vlog.setCreatedTime(new Date());
        vlog.setUpdatedTime(new Date());

        vlogMapper.insert(vlog);

    }

    @Override
    public PagedGridResult getIndexVlog(String search,
                                        Integer page,
                                        Integer pageSize) {
        PageHelper.startPage(page, pageSize);
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isNoneBlank(search)) {
            map.put("search", search);
        }
        List<IndexVlogVO> listVlog = vlogMapperCustom.getIndexVlog(map);

        return setterPagedGrid(listVlog,page);
    }

    @Override
    public IndexVlogVO getVlogDetailById(String vlogId) {
        Map<String, Object> map = new HashMap<>();
        map.put("vlogId", vlogId);

        List<IndexVlogVO> list = vlogMapperCustom.getVlogDetailById(map);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public void updateVlogToPrivateOrrPublic(String userId, String vlogId, Integer yesOrNo) {
        if (userId == null && vlogId == null) {
            GraceException.display(ResponseStatusEnum.SYSTEM_ERROR);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("vlogId", vlogId);
        map.put("yesOrNo", yesOrNo);

        vlogMapperCustom.updateVlogToPrivateOrPublic(map);
    }

    @Override
    public PagedGridResult getMyVlogByUserId(String userId, Integer page, Integer pageSize, Integer yesOrNo) {
        PageHelper.startPage(page, pageSize);
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("yesOrNo", yesOrNo);

        List<Vlog> list = vlogMapperCustom.getMyVlogByUserId(map);

        return setterPagedGrid(list, page);
    }
}
