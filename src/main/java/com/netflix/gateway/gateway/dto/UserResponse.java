package com.netflix.gateway.gateway.dto;

import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

/**
 * @author flaoliveira
 * @version : $<br/>
 * : $
 * @since 31/10/2019 15:37
 */
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private Long id;
    private String name;
    private String email;
    private String phone;

}
