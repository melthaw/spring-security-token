# Introduction

Sometimes we only need a simple token based security framework to protect the rest API. Obviously we like Spring Framework but Spring Security is too heavy to us.
It's possible to extends the Spring Security to match our requirement, but that means we have to pay the cost for the over-designed filter chain at the runtime.
Daas-Token is designed to reduce down the filters in the chain ,abandon the complicated structure , and try to make it simple , make it easy to understand.

We are still the heavy users of Spring Framework , so when we decide to develop the Daas-Token security framework , we only consider to support the Spring Framework without any hesitation.

Thanks Spring Security. There are some useful APIs available for us in Spring Security , but we don't like to depend on the fully JARS , so we re-package them under the following package.
> in.clouthink.daas.security.token.repackage.org.springframework.security

# Dependencies

* Spring Framework 3.2.x (Core & Web)
* Spring Data Redis (Optional)
* Spring Data Mongodb (Optional)

# Source guide

| Package | Description |
| in.clouthink.daas.security.token.annotation | The annotation for Spring Configuration |
| in.clouthink.daas.security.token.configure | Where to configure the Daas-Token |
| in.clouthink.daas.security.token.core | The Daas-Token core engine |
| in.clouthink.daas.security.token.exception | The Daas-Token exception definitions |
| in.clouthink.daas.security.token.repackage | Re-packaged 3rd party library |
| in.clouthink.daas.security.token.spi | The SPI definition where the user can provide their own implementation |
| in.clouthink.daas.security.token.support | Spring Web Mvc Support |



# Usage

## Maven

    <dependency>
        <groupId>in.clouthink.daas</groupId>
        <artifactId>daas-token</artifactId>
        <version>${daas.token.version}</version>
    </dependency>

## Spring Configuration

Use `@EnableToken` to get started 

    @Configuration
    @EnableToken
    public class Application {}

 
By default, the JVM memmory-based token management is working for Daas-Token, `@Scheduled(cron = "0 0/10 * * * ?")` is triggered every 10 minutes to clean up the expired token. 
So please enable the spring schedule feature if you does not change the default configuration.Otherwise , **out of memmory** should be a big problem.

    @Configuration
    @EnableScheduling
    @EnableToken
    public class Application {}

## Customize

Implement the interface `TokenConfigurer` to customize the Daas-Token features

* login url 
* logout url
* authenticate url
* authorize url
* token alive timeout
* access control list

Here is the sample 

    @Bean
    public TokenConfigurer myTokenConfigurer() {
        //TODO:create and return the TokenConfigurer instance here.
    }

Or you can extend the stub class `TokenConfigurerAdapter` which has been supply the empty implementation for `TokenConfigurer`,
just override the methods you'd like to.

For example , we'd like to let the token only be alive for one hour.

    @Bean
    public TokenConfigurer myTokenConfigurer() {
        return new TokenConfigurerAdapter() {
            @Override
            public void configure(TokenLife tokenLife) {
                tokenLife.setTokenTimeout(60 * 60 * 1000);
            }
        }
    }

Set the login process url:

    @Bean
    public TokenConfigurer myTokenConfigurer() {
        return new TokenConfigurerAdapter() {
            @Override
            public void configure(LoginEndpoint endpoint) {
                endpoint.setLoginProcessesUrl("/login");
            }
        }
    }
    

Set the logout process url:

    @Bean
    public TokenConfigurer myTokenConfigurer() {
        return new TokenConfigurerAdapter() {
            @Override
            public void configure(LogoutEndpoint endpoint) {
                endpoint.setLogoutProcessesUrl("/logout");
            }
        }
    }

Set the protected rest url:

    @Bean
    public TokenConfigurer myTokenConfigurer() {
        return new TokenConfigurerAdapter() {

            @Override
            public void configure(AuthenticationFilter filter) {
                filter.setProcessesUrl("/protected**");
            }

            @Override
            public void configure(AuthorizationFilter filter) {
                filter.setProcessesUrl("/protected**");
            }
            
        }
    }
 
The AuthenticationFilter is responsible to judge whether is user is authenticated or not.
And the AuthorizationFilter is responsible to decide whether the user is allowed to access the protected url.
Normally, keep the process url of the two filters be the same value.

Now here goes to the step to define the ACL which is designed as RBAC , but not only RBAC. 
Url Acl is designed into three parts

* URL  
 The http url (support regex and ant path)
* Http Method  
the http method (support GET , PUT, POST and DELETE)
* Grant Rule  
the user access is granted if the request is matching the rules

The Grant Rule is defined as expression, now we support the following two format :

* ROLE:XXX   
XXX should be the role name (Role#getName())
* USERNAME:XXX    
XXX should be the user name (User#getUsername())

For example:
The user which's username is **TESTUSER** or owns the Role **TEST** can access the **/token/sample/helloworld** with the http **GET** method

    @Bean
    public TokenConfigurer myTokenConfigurer() {
        return new TokenConfigurerAdapter() {

            @Override
            public void configure(UrlAclProviderBuilder builder) {
                builder.add(UrlAclBuilder.antPathBuilder()
                                         .url("/token/sample/helloworld")
                                         .httpMethods(HttpMethod.GET)
                                         .grantRules("ROLE:TEST,USERNAME:TESTUSER"));
            }
            
        }
    }
             
             

## Redis 



## Mongodb

We supply the 


## Advanced 

### Performace

Daas-Token is one light-weighted security framework but it doesn't mean we will sacrifice the performance to exchange the simple usage.
The SPI is available for the advanced user to adapt their own implementation, even we have supported the Memcached, Redis and Mongodb out of the box.

