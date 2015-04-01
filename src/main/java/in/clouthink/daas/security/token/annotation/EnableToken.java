package in.clouthink.daas.security.token.annotation;

import java.lang.annotation.*;

import org.springframework.context.annotation.Import;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(TokenConfiguration.class)
@Documented
public @interface EnableToken {
}
