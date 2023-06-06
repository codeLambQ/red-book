package com.codeLamb.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class VlogBO {

    @NotBlank(message = "id 不能为空")
    private String id;
    @NotBlank(message = "vlogerId 不能为空")
    private String vlogerId;
    @NotBlank(message = "url 不能为空")
    private String url;
    @NotBlank(message = "cover")
    private String cover;
    @NotBlank(message = "title 不能为空")
    private String title;
    private Integer width;
    private Integer height;
    private Integer likeCounts;
    private Integer commentsCounts;
    @NotBlank(message = "isPrivate 不能为空")
    private Integer isPrivate;

}