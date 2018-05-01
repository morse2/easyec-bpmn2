package com.googlecode.easyec.bpmn2.test;

import com.googlecode.easyec.bpmn2.domain.Comment;
import com.googlecode.easyec.bpmn2.domain.Process;
import com.googlecode.easyec.bpmn2.domain.impl.ProcessImpl;
import com.googlecode.easyec.bpmn2.engine.action.*;
import com.googlecode.easyec.bpmn2.engine.action.impl.*;
import com.googlecode.easyec.bpmn2.service.FlowStdService;
import com.googlecode.easyec.bpmn2.service.MyService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.repository.Deployment;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import javax.annotation.Resource;
import java.util.List;

@ContextConfiguration(locations = "classpath:spring/test/applicationContext-*.xml")
public class FlowOperateTestCase extends AbstractTransactionalJUnit4SpringContextTests {

    @Resource
    private MyService myService;
    @Resource
    private RepositoryService repositoryService;
    @Resource
    private RuntimeService runtimeService;
    @Resource
    private FlowStdService flowStdService;

    private Process _proc = new ProcessImpl();

    @Before
    public void beforeRun() {
        Authentication.setAuthenticatedUserId("admin01");
        _proc.setType("quote_flow");
    }

    @Test
    @Rollback(false)
    public void deployMyProcess() {
        String id = repositoryService.createDeployment()
            .addClasspathResource("template/quote_flow.bpmn")
            .deploy()
            .getId();

        Assert.assertNotNull(id);
    }

    @Test
    @Rollback(false)
    public void undeployProcess() {
        List<Deployment> ret = repositoryService.createDeploymentQuery()
            .deploymentId("1")
            .list();

        for (Deployment dep : ret) {
            repositoryService.deleteDeployment(dep.getId());
        }
    }

    @Test
    public void save() throws Exception {
        ProcessDraftAction _act
            = new ProcessDraftActionBuilder<>()
            .definition("quote_flow")
            .object(_proc)
            .build();

        myService.save(_act);
        Assert.assertNotNull(_act.getProcess().getUidPk());
    }

    @Test
    public void delete() throws Exception {
        save();

        ProcessDraftAction _act
            = new ProcessDraftActionBuilder()
            .object(_proc)
            .build();

        myService.delete(_act);
    }

    @Test
    @Rollback(false)
    public void startFlow() throws Exception {
        ProcessStartAction _act
            = new ProcessStartActionBuilder<>()
            .definition("quote_flow")
            .object(_proc)
            .arg("key1", "value01")
            .comment(
                new CommentApproveActionBuilder()
                    .content("开始新流程")
                    .role("applicant")
                    .action("new_apply")
                    .build()
            ).build();

        myService.start(_act);
    }

    @Test
    @Rollback(false)
    public void discardFlow() throws Exception {
        ProcessDiscardAction _act
            = new ProcessDiscardActionBuilder<>("817")
            .comment(
                new CommentApproveActionBuilder()
                    .content("废弃流程")
                    .role("applicant")
                    .action("discard")
                    .build()
            ).build();

        myService.discard(_act);
    }

    @Test
    @Rollback(false)
    public void completeTask() throws Exception {
        TaskApproveAction _act
            = new TaskApproveActionBuilder<>("257509")
            .approved(true)
            .comment(
                new CommentApproveActionBuilder()
                    .content("批准流程")
                    .role("MD")
                    .action("approval")
                    .build()
            ).build();
                                                                        
        myService.complete(_act);
    }

    @Test
    @Rollback(false)
    public void raiseToManager() throws Exception {
        TaskMessageEventAction _act
            = new TaskMessageEventActionBuilder<>("257509")
            .message("Raise To Manager")
            .build();

        flowStdService.sendMessage(_act);
    }

    @Test
    public void findTaskComments() throws Exception {
        List<Comment> result = flowStdService.findComments("970");
        Assert.assertNotNull(result);
    }
}
