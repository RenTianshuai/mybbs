package com.yaohan.bbs.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.yaohan.bbs.excel.common.ExcelException;
import com.yaohan.bbs.service.UserService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserModelListener extends AnalysisEventListener<UserModel> {

    private UserService userService;

    public UserModelListener(UserService userService){
        this.userService = userService;
    }

    @Override
    public void invoke(UserModel userModel, AnalysisContext analysisContext) {
        log.debug("读取到sheet:" + analysisContext.readSheetHolder().getSheetName() + "的第" + analysisContext.readRowHolder().getRowIndex() + "行");
        updateUser(userModel);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        log.info("读取完成");
    }

    private void updateUser(UserModel userModel){
        try {
            userService.updateOrganizationImport(userModel);
        }catch (Exception e){
            log.error(e.getMessage());
            throw new ExcelException(e.getMessage());
        }
    }
}
