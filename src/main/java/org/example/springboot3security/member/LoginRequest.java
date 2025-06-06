package org.example.springboot3security.member;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.example.springboot3security.crypto.DecryptDeserializer;

public record LoginRequest(String username, @JsonDeserialize(using = DecryptDeserializer.class) String password) {
}
