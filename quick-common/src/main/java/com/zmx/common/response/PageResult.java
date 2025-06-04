package com.zmx.common.response;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zmx.common.exception.BusinessException;
import lombok.Data;

import java.util.List;

@Data
public class PageResult<T> extends Result<List<T>>{
    //当前页码
    private long current;
    //每页显示记录数
    private long size;
    //总记录数
    private long total;
    //总页数
    private long pages;

    // 构造方法：从 IPage 对象转换
    public PageResult(IPage<T> page) {
        if (page == null){
            throw new BusinessException("PageResult 错误：page is null");
        }

        this.current = page.getCurrent();
        this.size = page.getSize();
        this.total = page.getTotal();
        this.pages = page.getPages();
        this.setData(page.getRecords());
    }

    // 构造方法：从 IPage 对象转换
    public PageResult(IPage<T> page, List<T> records) {
        if (page == null){
            throw new BusinessException("PageResult 错误：page is null");
        }

        this.current = page.getCurrent();
        this.size = page.getSize();
        this.total = page.getTotal();
        this.pages = page.getPages();
        this.setData(records);
    }

    public static PageResult success(IPage page){
        var result = new PageResult(page);
        result.setCode(200);
        result.setMsg("操作成功！");
        return result;
    }

    public static <T> PageResult<T> success(IPage page, List<T> records){
        var result = new PageResult<>(page, records);
        result.setCode(200);
        result.setMsg("操作成功！");
        return result;
    }
}
