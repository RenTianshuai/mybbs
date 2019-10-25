package com.yaohan.bbs.excel.common;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * excel读取监听类
 */
@Slf4j
public class ExcelListener<T> extends AnalysisEventListener<T> {

    /**
     * 自定义用于暂时存储data。
     * 可以通过实例获取该值
     */
    private List<T> datas = new ArrayList<>();


    /**
     * 通过 AnalysisContext 对象还可以获取当前 sheet，当前行等数据
     */
    @Override
    public void invoke(T o, AnalysisContext analysisContext) {
        log.info("读取到sheet:" + analysisContext.readSheetHolder().getSheetName() + "的第" + analysisContext.readRowHolder().getRowIndex() + "行");
        datas.add(o);
    }

    private void doSomething(Object object){
        //做更多的事
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }

    public List<T> getDatas() {
        return datas;
    }

    public void setDatas(List<T> datas) {
        this.datas = datas;
    }
}
