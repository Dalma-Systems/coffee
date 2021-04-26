package com.dalma.contract.dto.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseOrionAttributeOutputDto<T extends Serializable> implements Serializable {

    private static final long serialVersionUID = 6883974025540957590L;

    private String dateModified;
    private T value;
}
