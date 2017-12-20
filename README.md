# Smile
Smile框架

```
                                                                                                            
                                                                                                            
  .--.--.            ____             ,--,                           ,---,.                         ___     
 /  /    '.        ,'  , `.  ,--,   ,--.'|                         ,'  .'  \                      ,--.'|_   
|  :  /`. /     ,-+-,.' _ |,--.'|   |  | :                 ,---,.,---.' .' |   ,---.     ,---.    |  | :,'  
;  |  |--`   ,-+-. ;   , |||  |,    :  : '               ,'  .' ||   |  |: |  '   ,'\   '   ,'\   :  : ' :  
|  :  ;_    ,--.'|'   |  ||`--'_    |  ' |      ,---.  ,---.'   ,:   :  :  / /   /   | /   /   |.;__,'  /   
 \  \    `.|   |  ,', |  |,,' ,'|   '  | |     /     \ |   |    |:   |    ; .   ; ,. :.   ; ,. :|  |   |    
  `----.   \   | /  | |--' '  | |   |  | :    /    /  |:   :  .' |   :     \'   | |: :'   | |: ::__,'| :    
  __ \  \  |   : |  | ,    |  | :   '  : |__ .    ' / |:   |.'   |   |   . |'   | .; :'   | .; :  '  : |__  
 /  /`--'  /   : |  |/     '  : |__ |  | '.'|'   ;   /|`---'     '   :  '; ||   :    ||   :    |  |  | '.'| 
'--'.     /|   | |`-'      |  | '.'|;  :    ;'   |  / |          |   |  | ;  \   \  /  \   \  /   ;  :    ; 
  `--'---' |   ;/          ;  :    ;|  ,   / |   :    |          |   :   /    `----'    `----'    |  ,   /  
           '---'           |  ,   /  ---`-'   \   \  /           |   | ,'                          ---`-'   
                            ---`-'             `----'            `----'                                     
                                                                                                            
 SmileBoot :                                                            (1.0.0-SNAPSHOT)
 author    :                                                                   chinesszz

```

开心学习SpringBoot
使用netty,基于事件搭建web框架: Smile-boot

1. 了解Class加载机制
   - 1.1 扫描所有字节码 org.smileframework.tool.clazz.ClassTools
2. 掌握Spring IOC加载机制
   - 2.1 IOC加载容器 org.smileframework.ioc.bean.context.SmileApplicationContext
3. Java注解使用
4. 学会使用Proxy代理,实现自己的Aop
   - 4.1 org.smileframework.tool.proxy.CGLibProxy
5. 深读理解SpringBoot运行原理
   - 5.1 banner加载
   - 5.2 配置文件加载
6. 掌握Netty基于事件的web请求
   - 6.1 org.smileframework.web.server.dispatch.HttpDispatchServerHandler
7. 对多线程,线程池,线程工厂,拒绝策略有一个新的认识
   - 7.1 org.smileframework.web.threadpool
     - 7.1.1 拒绝策略类 SmileRejectedExecutionHandler
     - 7.1.2 线程工厂 SmileThreadFactory
     - 7.1.3 线程池 SmileThreadPoolExecutor