package com.codeLamb.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserVO {
    private String id;


    private String mobile;


    private String nickname;


    private String imoocNum;


    private String face;


    private Integer sex;

    private Date birthday;

    private String country;

    private String province;

    private String city;


    private String district;

    private String description;


    private String bgImg;


    private Integer canImoocNumBeUpdated;


    private Date createdTime;


    private Date updatedTime;


    private String userToken;

    private Integer myFollowsCounts;
    private Integer myFansCounts;
//    private Integer myLikedVlogCounts;
    private Integer totalLikeMeCounts;
}