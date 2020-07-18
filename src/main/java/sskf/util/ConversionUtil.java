package sskf.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import sskf.model.request.WrapperRequest;

import java.time.ZoneId;
import java.util.TimeZone;

public class ConversionUtil {

    public static WrapperRequest convertJsonToWrapperRequest(String json) throws JsonProcessingException {
        WrapperRequest wrapperRequest = new ObjectMapper().setTimeZone(TimeZone.getTimeZone(ZoneId.systemDefault()))
                .readValue(json, WrapperRequest.class);
        return wrapperRequest;
    }

}
