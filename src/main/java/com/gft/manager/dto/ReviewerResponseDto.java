package com.gft.manager.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data@NoArgsConstructor
public class ReviewerResponseDto {
    private String userName;
    private String email;
    private String fullName;
    private boolean isAccountNoneLocked;
    private boolean active;
    private String  createdAt;
    private  int totalAssigned;
    private int totalReviewed;

}
