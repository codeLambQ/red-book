package com.codeLamb.form;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @author codeLamb
 */
@Data
public class UserLoginRegistForm {

    @NotBlank(message = "用户名不能为空")
    @Length(min = 11, max = 11, message = "手机号长度为11位")
    private String mobile;

    @NotBlank(message = "验证码不能为空")
    @Length(min = 6, max = 6, message = "验证码必须是6位")
    private String smsCode;
}
