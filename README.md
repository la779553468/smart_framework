    #smart_framwordk
    一个简单javaweb框架。
        该项目主要参考书籍《从零开始写javaweb框架》，同时结合自身所学,将其中
    bean容器及ioc部分应用到公司二次开发的jfinal中
    框架主要部分：
        1. 将所有Controller和Service注解下的类及其实例加入到BEAN_MAP容
           器中，此处采用class与object的映射
        2. Aop初始化，将类所有的代理形成代理列表，生成类与代理类列表的映射
           ProxyMap，然后生成代理对象并修改BEAN_MAP中class对应的实例
        3. 开始注入，遍历BEAN_MAP,根据对象中注入的属性是否配置多例，生成具
           体的实例或根据ProxyMap获取代理对象
        4. 初始化Action映射，这里简单的取controller类中method的Action，
           来生成请求路径与处理器（类和方法）的映射
        5. 处理请求，根据路径找到对应处理器处理，根据返回的对象是View还是
           Data判断是跳转页面和写json到响应流， 页面跳转又根据是否已“/”开头判断重定向和转发
