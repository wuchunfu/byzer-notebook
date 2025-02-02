package io.kyligence.notebook.console.controller;

import io.kyligence.notebook.console.bean.dto.IdNameDTO;
import io.kyligence.notebook.console.bean.dto.Response;
import io.kyligence.notebook.console.bean.dto.TaskInfoDTO;
import io.kyligence.notebook.console.bean.dto.req.CreateScheduleReq;
import io.kyligence.notebook.console.bean.dto.req.ModifyScheduleReq;
import io.kyligence.notebook.console.bean.dto.req.ScheduleCallbackReq;
import io.kyligence.notebook.console.service.SchedulerService;
import io.kyligence.notebook.console.support.Permission;
import io.kyligence.notebook.console.util.WebUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;


@Slf4j
@Validated
@RestController
@RequestMapping("api")
@Api("The documentation about operations on Schedule")
public class SchedulerController {

    @Autowired
    private SchedulerService schedulerService;

    @ApiOperation("Schedule Execute")
    @PostMapping("/schedule/execution")
    public Response<String> callback(@RequestBody @Validated ScheduleCallbackReq scheduleCallbackReq) {

        schedulerService.callback(
                scheduleCallbackReq.getToken(),
                scheduleCallbackReq.getUser(),
                scheduleCallbackReq.getEntityType(),
                scheduleCallbackReq.getEntityId()
        );
        return new Response<String>().msg("success");
    }

    @ApiOperation("List Schedulers")
    @GetMapping("/schedule/scheduler")
    @Permission
    public Response<List<IdNameDTO>> ListScheduler() {
        return new Response<List<IdNameDTO>>().data(schedulerService.getSchedulerList());
    }

    @ApiOperation("Get Schedule By ID")
    @GetMapping("/schedule/task/{id}")
    @Permission
    public Response<TaskInfoDTO> getScheduleById(@PathVariable("id") @NotNull Integer id,
                                                 @RequestParam(value = "scheduler", required = false) Integer schedulerId,
                                                 @RequestParam(value = "project_name", required = false) String projectName) {
        String user = WebUtils.getCurrentLoginUser();
        TaskInfoDTO taskInfoDTO = schedulerService.getScheduleById(user, schedulerId, projectName, id);
        return new Response<TaskInfoDTO>().data(taskInfoDTO);
    }

    @ApiOperation("Get Schedule List")
    @GetMapping("/schedule/task/list")
    @Permission
    public Response<List<TaskInfoDTO>> getScheduleList(@RequestParam(value = "scheduler", required = false) Integer schedulerId,
                                                       @RequestParam(value = "project_name", required = false) String projectName) {
        String user = WebUtils.getCurrentLoginUser();
        List<TaskInfoDTO> taskInfoDTO = schedulerService.getScheduleList(user, schedulerId, projectName);
        return new Response<List<TaskInfoDTO>>().data(taskInfoDTO);
    }

    @ApiOperation("Get Schedule By Entity")
    @GetMapping("/schedule/task")
    @Permission
    public Response<TaskInfoDTO> getScheduleByEntity(@RequestParam(value = "scheduler", required = false) Integer schedulerId,
                                                     @RequestParam(value = "project_name", required = false) String projectName,
                                                     @RequestParam(value = "entity_type", required = false) String entityType,
                                                     @RequestParam(value = "entity_id", required = false) Integer entityId) {
        String user = WebUtils.getCurrentLoginUser();
        TaskInfoDTO taskInfoDTO = schedulerService.getScheduleByEntity(user, schedulerId, projectName, entityType, entityId);
        return new Response<TaskInfoDTO>().data(taskInfoDTO);
    }

    @ApiOperation("Create Schedule")
    @PostMapping("/schedule/task")
    @Permission
    public Response<String> createSchedule(@RequestBody @Validated CreateScheduleReq createScheduleReq) {
        String user = WebUtils.getCurrentLoginUser();
        schedulerService.createSchedule(
                createScheduleReq.getSchedulerId(),
                createScheduleReq.getName(),
                createScheduleReq.getDescription(),
                user,
                createScheduleReq.getEntityType(),
                createScheduleReq.getEntityId(),
                createScheduleReq.getSchedule(),
                createScheduleReq.getExtra()
        );
        return new Response<String>().msg("success");
    }

    @ApiOperation("Modify Schedule")
    @PutMapping("/schedule/task/{id}")
    @Permission
    public Response<String> modifySchedule(@PathVariable("id") @NotNull Integer id, @RequestBody @Validated ModifyScheduleReq modifyScheduleReq) {
        String user = WebUtils.getCurrentLoginUser();
        schedulerService.updateSchedule(
                modifyScheduleReq.getSchedulerId(),
                id,
                modifyScheduleReq.getName(),
                modifyScheduleReq.getDescription(),
                user,
                modifyScheduleReq.getModification(),
                modifyScheduleReq.getSchedule(),
                modifyScheduleReq.getExtra()
        );
        return new Response<String>().msg("success");
    }

    @ApiOperation("Delete Schedule")
    @DeleteMapping("/schedule/task/{id}")
    @Permission
    public Response<String> deleteSchedule(@PathVariable("id") @NotNull Integer id,
                                           @RequestParam(value = "scheduler", required = false) Integer schedulerId,
                                           @RequestParam(value = "project_name", required = false) String projectName) {
        String user = WebUtils.getCurrentLoginUser();
        schedulerService.deleteSchedule(user, schedulerId, projectName, id);
        return new Response<String>().msg("success");
    }

}
