package com.api.muinda_kubika.Defaults;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
public class DefaultDto {
    private UUID id;
    private LocalDateTime createdAt,updatedAt;
    private  Boolean isActive;
}
