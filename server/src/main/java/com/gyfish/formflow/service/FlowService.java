package com.gyfish.formflow.service;

import com.gyfish.formflow.domain.User;
import com.gyfish.formflow.domain.flow.Flow;
import com.gyfish.formflow.domain.flow.FlowNode;
import com.gyfish.formflow.domain.flow.Process;
import com.gyfish.formflow.vo.FlowQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

/**
 * @author geyu
 */
@Service
@Slf4j
public class FlowService {

    private final MongoTemplate mongoTemplate;

    private final UserService userService;

    @Autowired
    public FlowService(MongoTemplate mongoTemplate, UserService userService) {
        this.mongoTemplate = mongoTemplate;
        this.userService = userService;
    }

    @Transactional(rollbackFor = Exception.class)
    public void save(Flow meta) {

        // 先删除
        String id = meta.getId();
        if (!StringUtils.isEmpty(id)) {
            mongoTemplate.remove(meta);
        }

        // 设置时间
        meta.setUpdateTime(new Date());
        if (meta.getCreateTime() == null) {
            meta.setCreateTime(new Date());
        }

        // 设置链表
        List<FlowNode> nodes = meta.getNodes();
        for (int i = 0; i < nodes.size(); i++) {
            FlowNode node = nodes.get(i);
            node.setUuid(meta.getUuid());
            if (i < nodes.size() - 1) {
                FlowNode nextNode = nodes.get(i + 1);
                node.setNextNodeId(nextNode.getId());
            } else {
                node.setNextNodeId("end");
            }
        }

        mongoTemplate.save(meta);

        // 设置起始节点处理人权限
        FlowNode startNode = nodes.get(1);
        User user = userService.getById(startNode.getHandlerId());

        user.addFlow(meta.getId());
        userService.save(user);
    }

    public void delete(String id) {

        Flow meta = new Flow();
        meta.setId(id);

        mongoTemplate.remove(meta);
    }

    public List<Flow> getList(FlowQuery flowQuery) {

        Criteria criteria = Criteria.where("appId").is(flowQuery.getAppId());
        Query query = new Query(criteria);

        return mongoTemplate.find(query, Flow.class);
    }

    public List<Flow> getByUser(String userId) {

        User user = mongoTemplate.findById(userId, User.class);
        if (user == null) {
            return null;
        }

        List flowIds = user.getFlowList();
        if (CollectionUtils.isEmpty(flowIds)) {
            return null;
        }

        Criteria criteria = Criteria.where("id").in(flowIds);
        Query query = new Query(criteria);

        return mongoTemplate.find(query, Flow.class);
    }


    FlowNode next(Process p) {

        Flow flow = mongoTemplate.findById(p.getFlowId(), Flow.class);

        if (flow == null) {
            log.error("没有这个流程！");
            return null;
        }

        List<FlowNode> nodes = flow.getNodes();

        for (int i = 0; i < nodes.size(); i++) {
            if (nodes.get(i).getId().equals(p.getNodeId())) {
                if (i == nodes.size() - 1) {
                    return null;
                } else {
                    return nodes.get(i + 1);
                }
            }
        }

        return null;
    }

    Flow getById(String id) {

        return mongoTemplate.findById(id, Flow.class);
    }

}
