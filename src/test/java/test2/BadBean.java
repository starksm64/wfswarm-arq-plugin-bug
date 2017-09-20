package test2;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BadBean {
    public void causeIllegalArgumentException() {
        throw new IllegalArgumentException();
    }

    public Integer ok() {
        return new Integer(1);
    }
}
