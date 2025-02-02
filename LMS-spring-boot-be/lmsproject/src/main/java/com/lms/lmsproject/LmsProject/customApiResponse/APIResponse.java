package com.lms.lmsproject.LmsProject.customApiResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class APIResponse<T> {
    private int status; // changed to int
    private String message; // changed to String
    private T data; // generic type
}
