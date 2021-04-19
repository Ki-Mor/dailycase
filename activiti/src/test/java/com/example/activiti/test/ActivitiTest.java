package com.example.activiti.test;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.identity.Group;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.Deployment;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName ActivitiTest
 * @Description 测试类
 * @Author wangchen
 * @Date 2020/10/14 1:44 下午
 **/
@Service
public class ActivitiTest {

    /**
     * 并行网关在聚合处只有当分支任务都完成后才会进行下个任务
     * */
    private ProcessEngine processEngine= ProcessEngines.getDefaultProcessEngine();

    @Test
    public void queryTask(){
        //查出当前登录用户所在的用户组
        List<Group> groups = ProcessEngines.getDefaultProcessEngine().getIdentityService().createGroupQuery().groupMember("test03").list();
        List<String> name = groups.stream().map(group -> group.getName()).collect(Collectors.toList());
        System.out.println(name);

        //查询待审批请假流程列表
        List<Task> tasks = ProcessEngines.getDefaultProcessEngine().getTaskService().createTaskQuery().processDefinitionKey("action")
                .taskAssignee("test03")
                .list();
        System.out.println(tasks);

        //根据流程实例ID查询请假申请表单数据
        List<String> ids = tasks.stream().map(task -> task.getProcessInstanceId()).collect(Collectors.toList());
        System.out.println(ids);
    }
}
