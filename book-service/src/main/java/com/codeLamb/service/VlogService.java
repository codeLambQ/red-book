package com.codeLamb.service;

import com.codeLamb.VO.IndexVlogVO;
import com.codeLamb.form.UpdateUserInfoForm;
import com.codeLamb.form.VlogBO;
import com.codeLamb.pojo.Users;
import com.codeLamb.utils.PagedGridResult;

import java.util.List;

/**
 * @author codeLamb
 */
public interface VlogService {



    /**
     * 发布 Vlog
     */
    void createVlog(VlogBO vlogBO);

    /**
     * 查询短视频
     */
    PagedGridResult getIndexVlog(String search, Integer page,
                                 Integer pageSize);
    IndexVlogVO getVlogDetailById(String vlogId);

    void updateVlogToPrivateOrrPublic(String userId, String vlogId, Integer yesOrrNo);

    PagedGridResult getMyVlogByUserId(String userId, Integer page,
                                      Integer pageSize, Integer yesOrrNo);
}
