package org.example.springboot3security.crypto;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class DecryptDeserializer extends JsonDeserializer<String> {
    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) {
        try {
            return CryptoManager.decrypt(p.getText());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
