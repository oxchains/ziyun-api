package com.oxchains.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static java.util.Optional.empty;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang.StringUtils.isBlank;

/**
 * @author aiet
 */
public class ResponseUtil {

    private static final Logger LOG = LoggerFactory.getLogger(ResponseUtil.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static Optional<String> extract(String json, String path) {
        try {
            LOG.info("json: " + json);
            JsonNode root = OBJECT_MAPPER.readTree(json);
            JsonNode data = root.at(path);
            return Optional.ofNullable(data.isObject() ? data.toString() : data.textValue());
        } catch (Exception e) {
            LOG.error("failed to extract value under path {} out of {}: {}", path, json, e.getMessage());
        }
        return empty();
    }

    public static <T> T resolve(String json, Class<T> tClass) {
        try {
            return OBJECT_MAPPER.readValue(json, tClass);
        } catch (IOException e) {
            LOG.error("failed to resolve data to {}: {}, cause: {}", tClass, json, e.getMessage());
        }
        return null;
    }

}
