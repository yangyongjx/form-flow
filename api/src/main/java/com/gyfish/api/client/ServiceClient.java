package com.gyfish.api.client;

import com.alibaba.fastjson.JSON;
import com.gyfish.api.controller.vo.FlowVo;
import com.gyfish.api.controller.vo.FormVo;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.extern.slf4j.Slf4j;

/**
 * @author geyu
 */
@Component
@Slf4j
public class ServiceClient {

    private WebClient client = WebClient.builder()
            .baseUrl("http://localhost:7002/service")
            .build();

    public void saveFormMeta(FormVo formVo) {

        log.info(">> saveFormMeta");

        client.post()
                .uri("/form/saveFormMeta")
                .syncBody(formVo)
                .retrieve()
                .bodyToMono(String.class)
                .subscribe(log::info);
    }

    public void saveFlowMeta(FlowVo flowVo) {

        log.info(">> saveFlowMeta");

        client.post()
                .uri("/flow/saveFlowMeta")
                .syncBody(flowVo)
                .retrieve()
                .bodyToMono(String.class)
                .map(JSON::parseObject)
                .subscribe(res -> log.info("<< saveFlowMeta.res = {}", JSON.toJSONString(res, true)));
    }

}
