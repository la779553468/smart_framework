import com.jihuangdev.proxy.CglibProxy;
import net.sf.cglib.proxy.Enhancer;

/**
 * Created by wancan on 2016/7/18.
 */
public class CglibProxyTest {
    public static void main(String[] args) {
        CglibProxy cglibProxy = new CglibProxy();
//        HelloImpl helloProxy = cglibProxy.getProxy(HelloImpl.class);
//        HelloImpl helloProxy = cglibProxy.getProxy(HelloImpl.class);
        HelloImpl helloProxy = (HelloImpl) Enhancer.create(HelloImpl.class, cglibProxy);
        helloProxy.say("hello");
    }
}
