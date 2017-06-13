import com.jihuangdev.proxy.DynamicProxy;

/**
 * Created by wancan on 2016/7/18.
 */
public class DynamicProxyTest {
    //normal
    /*public static void main(String[] args) {
        Hello hello = new HelloImpl();
        DynamicProxy dynamicProxy = new DynamicProxy(hello);
        Hello helloProxy = (Hello) Proxy.newProxyInstance(hello.getClass().getClassLoader(), hello.getClass().getInterfaces(), dynamicProxy);
        helloProxy.say("hello");
    }*/

    public static void main(String[] args) {
        DynamicProxy dynamicProxy = new DynamicProxy(new HelloImpl());
        Hello helloProxy = dynamicProxy.getProxy();
        helloProxy.say("hello");
    }
}
