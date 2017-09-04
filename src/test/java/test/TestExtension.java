package test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.DeploymentException;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.ProcessInjectionPoint;

public class TestExtension implements Extension {

    void processClaimValueInjections(@Observes ProcessInjectionPoint pip) {
        System.out.printf("pipRaw: %s\n", pip.getInjectionPoint());
        InjectionPoint ip = pip.getInjectionPoint();
        if (ip.getAnnotated().isAnnotationPresent(TestQualifier.class) && ip.getType() instanceof Class) {
            Class rawClass = (Class) ip.getType();
            if(Modifier.isFinal(rawClass.getModifiers())) {
                TestQualifier claim = ip.getAnnotated().getAnnotation(TestQualifier.class);
                Class declaringClass = ip.getMember().getDeclaringClass();
                Annotation[] appScoped = declaringClass.getAnnotationsByType(ApplicationScoped.class);
                Annotation[] sessionScoped = declaringClass.getAnnotationsByType(SessionScoped.class);
                if((appScoped != null && appScoped.length > 0) || (sessionScoped != null && sessionScoped.length > 0)) {
                    String err = String.format("A raw type cannot be injected into application/session scope: IP=%s", ip);
                    pip.addDefinitionError(new DeploymentException(err));
                }
            }
        }
    }
}
