package com.fun.project.monitor.logininfor.controller;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.fun.common.utils.poi.ExcelUtil;
import com.fun.framework.aspectj.lang.annotation.Log;
import com.fun.framework.aspectj.lang.enums.BusinessType;
import com.fun.framework.shiro.service.PasswordService;
import com.fun.framework.web.controller.BaseController;
import com.fun.framework.web.domain.R;
import com.fun.framework.web.page.TableDataInfo;
import com.fun.project.monitor.logininfor.domain.Logininfor;
import com.fun.project.monitor.logininfor.service.ILogininforService;

/**
 * 系统访问记录
 *
 * @author fun
 */
@Controller
@RequestMapping("/monitor/logininfor")
public class LogininforController extends BaseController {
    private final String prefix = "monitor/logininfor";

    @Autowired
    private ILogininforService logininforService;

    @Autowired
    private PasswordService passwordService;

    @RequiresPermissions("monitor:logininfor:view")
    @GetMapping()
    public String logininfor() {
        return prefix + "/logininfor";
    }

    @RequiresPermissions("monitor:logininfor:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Logininfor logininfor) {
        startPage();
        List<Logininfor> list = logininforService.selectLogininforList(logininfor);
        return getDataTable(list);
    }

    @Log(title = "登录日志", businessType = BusinessType.EXPORT)
    @RequiresPermissions("monitor:logininfor:export")
    @PostMapping("/export")
    @ResponseBody
    public R export(Logininfor logininfor) {
        List<Logininfor> list = logininforService.selectLogininforList(logininfor);
        ExcelUtil<Logininfor> util = new ExcelUtil<Logininfor>(Logininfor.class);
        return util.exportExcel(list, "登录日志");
    }

    @RequiresPermissions("monitor:logininfor:remove")
    @Log(title = "登录日志", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public R remove(String ids) {
        return toAjax(logininforService.deleteLogininforByIds(ids));
    }

    @RequiresPermissions("monitor:logininfor:remove")
    @Log(title = "登录日志", businessType = BusinessType.CLEAN)
    @PostMapping("/clean")
    @ResponseBody
    public R clean() {
        logininforService.cleanLogininfor();
        return success();
    }

    @RequiresPermissions("monitor:logininfor:unlock")
    @Log(title = "账户解锁", businessType = BusinessType.OTHER)
    @PostMapping("/unlock")
    @ResponseBody
    public R unlock(String loginName) {
        passwordService.clearLoginRecordCache(loginName);
        return success();
    }
}
