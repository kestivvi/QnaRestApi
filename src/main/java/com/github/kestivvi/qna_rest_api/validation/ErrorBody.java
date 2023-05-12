package com.github.kestivvi.qna_rest_api.validation;


import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class ErrorBody {

    private String errorCode;

    private Timestamp timestamp;
    private String description;

    public ErrorBody(String errorCode, String description) {
        this.errorCode = errorCode;
        this.description = description;
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }

}
