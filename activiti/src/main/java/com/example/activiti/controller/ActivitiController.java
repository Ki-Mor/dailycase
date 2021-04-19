package com.example.activiti.controller;

import org.activiti.engine.*;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName ActivitiController
 * @Description 工作流测试接口类
 * @Author wangchen
 * @Date 2020/10/13 2:18 下午
 **/
@RestController
@RequestMapping("activiti")
public class ActivitiController {

    @Resource
    private RuntimeService runtimeService;

    @Resource
    private TaskService taskService;

    @Resource
    private IdentityService identityService;

    @Resource
    private RepositoryService repositoryService;

    @Resource
    private ProcessEngine processEngine;

    @Resource
    private HistoryService historyService;


    /**
     * 每个项目都有自己的用户、角色表，Activiti也有自己的用户、用户组表。因此项目中的用户、角色与Activiti中的用户、用户组要做整合。
     */
    @PostMapping("userTest")
    public void userTest(){
        //项目中每创建一个新用户，对应的要创建一个Activiti用户
        //两者的userId和userName一致
        User admin=identityService.newUser("user02");
        admin.setPassword("user02");
        admin.setLastName("user02");
        admin.setEmail("user02@163.com");
        identityService.saveUser(admin);

        //项目中每创建一个角色，对应的要创建一个Activiti用户组
        Group adminGroup=identityService.newGroup("group01");
        adminGroup.setName("group01");
        identityService.saveGroup(adminGroup);

        //用户与用户组关系绑定
        identityService.createMembership("user02","group01");
    }

    /**
     * 启动实例
     * @param userId
     * @param days
     */
    @PostMapping("/runtime")
    public void runtime(String userId,String days){
        try {
            //启动流程实例，字符串"action"是BPMN模型文件里process元素的id
            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("action");
            //流程实例启动后，流程会跳转到请假申请节点
            Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
            //设置请假申请任务的执行人
            taskService.setAssignee(task.getId(),userId);

            //设置流程参数：请假天数和表单ID
            //流程引擎会根据请假天数days>3判断流程走向
            //formId是用来将流程数据和表单数据关联起来
            Map<String, Object> map = new HashMap<>(5);
            map.put("startDate","2020-10-13");
            map.put("endDate","2020-10-15");
            map.put("days",days);
            map.put("reason","测试");
            map.put("formId", "1");
            //完成请假申请任务
            taskService.complete(task.getId(), map);
            System.out.println("=======完成请假申请任务=========");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 查询
     */
    @GetMapping("queryTask")
    public void queryTask(String userId){
        //查出当前登录用户所在的用户组
        List<Group> groups = identityService.createGroupQuery().groupMember(userId).list();
        List<String> name = groups.stream().map(group -> group.getName()).collect(Collectors.toList());
        System.out.println(name);

        //查询待审批请假流程列表
        List<Task> tasks = taskService.createTaskQuery().processDefinitionKey("action")
                .taskAssignee(userId)
                .list();
        System.out.println(tasks);

        //根据流程实例ID查询请假申请表单数据
        List<String> ids = tasks.stream().map(task -> task.getProcessInstanceId()).collect(Collectors.toList());
        System.out.println(ids);
    }

    /**
     * 审批节点
     */
    @PostMapping("check")
    public void check(String taskId,String result,String userId){
        //查询当前审批节点
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        System.out.println(task);

        if ("ok".equals(result)){
            //设置流程参数：审批ID
            Map<String, Object> args = new HashMap<>(1);
            args.put("check","同意");

            //设置审批任务的执行人
            taskService.claim(task.getId(),userId);
            //完成审批任务
            taskService.complete(task.getId(), args);
        }else {
            //审批不通过，结束流程
            runtimeService.deleteProcessInstance(task.getProcessInstanceId(), "拒绝");
        }
    }
}
