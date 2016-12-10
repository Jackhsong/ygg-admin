package com.ygg.admin.time.base;

import org.apache.log4j.Logger;

/**
 * 定时任务 服务类抽象类
 */
public abstract class AbstractJobService {
	private Logger log = Logger.getLogger(AbstractJobService.class);
	
	/**
	 * 定时任务简短描述
	 * @return
	 */
    abstract public String getDescription();
    
    
    /**
     * 用于定时任务日志记录的代理方法
     */
    public void execute(){
    	 //记录开始时间
    	long startAt = System.currentTimeMillis();
    	log.info(getDescription() + " - 开始");
    	try{
    		//执行具体业务逻辑
    		doExecute();
    		//记录结束时间
    		long endAt = System.currentTimeMillis();
    		log.info(getDescription() + " - 结束。耗时" + (endAt - startAt) + "毫秒");
    	}catch(Exception e){
    		//记录异常结束时间
    		long endAt = System.currentTimeMillis();
    		log.error(getDescription() + " - 异常终止。耗时：" + (endAt - startAt) + "毫秒", e);
    	}
    }
    
    /**
     * 子类负责实例化具体的业务逻辑处理，并抛出异常
     * @throws Exception
     */
    abstract public void doExecute() throws Exception;
}
