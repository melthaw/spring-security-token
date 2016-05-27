package in.clouthink.daas.security.token.event.authentication;

import org.springframework.context.ApplicationEvent;

/**
 * @since 1.5.0
 */
public abstract class AbstractAuthenticationEvent extends ApplicationEvent {

	public AbstractAuthenticationEvent(Object source) {
		super(source);
	}

}
