package org.prgms.kdt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;


class CalculatorImpl implements Calculator{

    @Override
    public int add(int a, int b) {
        return a+b;
    }
}

interface Calculator{
    int add(int a, int b);
}

class LoggingInvocationHandler implements InvocationHandler{

    private static final Logger log = LoggerFactory.getLogger(LoggingInvocationHandler.class);
    private final Object target;

    public LoggingInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        log.info("{} executed in {}", method.getName(), target.getClass().getCanonicalName());
        return method.invoke(target,args);// target(여기서는 Calculator)의 method(여기서는 add)를 실행
    }
}

public class JdkProxyTest {
    private static final Logger log = LoggerFactory.getLogger(JdkProxyTest.class);

    public static void main(String[] args) {
        var calculator = new CalculatorImpl();
        // Proxy를 사용하려면 InvocationHandler가 필요해서 위에 해당 class를 만듬
        Calculator proxyInstance= (Calculator) Proxy.newProxyInstance(
                LoggingInvocationHandler.class.getClassLoader(),
                new Class[]{Calculator.class},
                new LoggingInvocationHandler(calculator));
        var res=proxyInstance.add(1,2);
        log.info("Add -> {}", res);
    }
}
