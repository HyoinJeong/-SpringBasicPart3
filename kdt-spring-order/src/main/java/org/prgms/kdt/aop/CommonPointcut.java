package org.prgms.kdt.aop;

import org.aspectj.lang.annotation.Pointcut;

public class CommonPointcut {
    @Pointcut("execution(public * org.prgms.kdt..*Service.*(..))")
    public void servicePublicMethodPointcut(){};

    @Pointcut("execution(* org.prgms.kdt..*Repository.*(..))")
    public void repositoryMethodPointcut(){};

    @Pointcut("execution(* org.prgms.kdt..*Repository.insert(..))")
    public void repositoryInsertMethodPointcut(){};
}
