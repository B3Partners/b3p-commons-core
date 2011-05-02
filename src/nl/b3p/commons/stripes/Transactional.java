package nl.b3p.commons.stripes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Transactional {
    public static final String DEFAULT_PERSISTENCE_UNIT = "[default]";

    String persistenceUnit() default DEFAULT_PERSISTENCE_UNIT;
}
