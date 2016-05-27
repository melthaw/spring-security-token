package in.clouthink.daas.security.token.event.authentication;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.util.ClassUtils;

/**
 * The sample to process the authentication event
 *
 * @since 1.5.0
 */
public class LoggerListener implements ApplicationListener<AbstractAuthenticationEvent> {

	// ~ Static fields/initializers
	// =====================================================================================

	private static final Log logger = LogFactory.getLog(LoggerListener.class);

	// ~ Methods
	// =====================================================================================

	public void onApplicationEvent(AbstractAuthenticationEvent event) {
		if (event instanceof HttpLoginEvent) {
			if (logger.isInfoEnabled()) {
				HttpLoginEvent httpLoginEvent = (HttpLoginEvent) event;
				StringBuilder sb = new StringBuilder();
				sb.append("Authentication event ")
				  .append(ClassUtils.getShortName(event.getClass()))
				  .append(": ")
				  .append(httpLoginEvent.isAuthenticated() ? "Authenticated" : "Un-Authenticated")
				  .append(": ");

				if (httpLoginEvent.isAuthenticated()) {
					sb.append(httpLoginEvent.getAuthentication().currentToken());
				}
				else {
					sb.append("Exception->").append(httpLoginEvent.getException());
				}
				logger.info(sb.toString());
			}
			return;
		}

		if (event instanceof FederationLoginEvent) {
			if (logger.isInfoEnabled()) {
				FederationLoginEvent federationLoginEvent = (FederationLoginEvent) event;
				StringBuilder sb = new StringBuilder();
				sb.append("Authentication event ")
				  .append(ClassUtils.getShortName(event.getClass()))
				  .append(": ")
				  .append(federationLoginEvent.isAuthenticated() ? "Authenticated" : "Un-Authenticated")
				  .append(": ");

				if (federationLoginEvent.isAuthenticated()) {
					sb.append(federationLoginEvent.getAuthentication().currentToken());
				}
				else {
					sb.append("Exception->").append(federationLoginEvent.getException());
				}
				logger.info(sb.toString());
			}
			return;
		}

		if (event instanceof HttpLogoutEvent) {
			if (logger.isInfoEnabled()) {
				HttpLogoutEvent httpLogoutEvent = (HttpLogoutEvent) event;
				StringBuilder sb = new StringBuilder();
				sb.append("Authentication event ");
				sb.append(ClassUtils.getShortName(event.getClass()));
				sb.append(": ");
				sb.append(httpLogoutEvent.getAuthentication().currentToken());

				logger.info(sb.toString());
			}
			return;
		}

		if (event instanceof FederationLogoutEvent) {
			if (logger.isInfoEnabled()) {
				FederationLogoutEvent federationLogoutEvent = (FederationLogoutEvent) event;
				StringBuilder sb = new StringBuilder();
				sb.append("Authentication event ");
				sb.append(ClassUtils.getShortName(event.getClass()));
				sb.append(": ");
				sb.append(federationLogoutEvent.getAuthentication().currentToken());

				logger.info(sb.toString());
			}
			return;
		}

	}

}