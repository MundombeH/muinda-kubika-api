package com.api.muinda_kubika.security.jwt;

import java.util.Set;
import java.util.UUID;

public record JwtAuthPayload(
    UUID userId,
    Set<String> roles,
    Set<String> permissions
) {}
