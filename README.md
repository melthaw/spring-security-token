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
* Spymemcached (Optional)

# Usage

## Maven

So far 1.0.0 is available 

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

 
By default, the JVM memory-based token management is working for Daas-Token, `@Scheduled(cron = "0 0/10 * * * ?")` is triggered every 10 minutes to clean up the expired token. 
So please enable the spring schedule feature if you does not change the default configuration.Otherwise , **out of memory** should be a big problem.

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

* ROLE:`XXX`  
 XXX should be the role name (`Role#getName()`)
* USERNAME:`XXX`  
 XXX should be the user name (`User#getUsername()`)

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

As mentioned before, the JVM memory based token management is used by default, but we supplied the redis based token management, here is the way to enable the feature.

First, enabled the spring data redis feature as follow:

    @Value("${redis.host}")
    private String redisHost;
    
    @Value("${redis.port}")
    private int redisPort;
    
    @Bean
    public RedisConnectionFactory jedisConnectionFactory() {
        RedisConnectionFactory result = new JedisConnectionFactory(new JedisShardInfo(redisHost,
                                                                                      redisPort));
        return result;
    }
    
    @Bean
    public RedisTemplate redisTemplate() {
        RedisTemplate result = new RedisTemplate();
        result.setConnectionFactory(jedisConnectionFactory());
        result.setKeySerializer(new StringRedisSerializer(Charset.forName("UTF-8")));
        return result;
    }
     
Then create the bean `in.clouthink.daas.security.token.spi.impl.redis.TokenProviderRedisImpl`.Please remember to add `@Primary` annotation with `@Bean`,
it will take the place of the default implementation

    @Primary
    @Bean
    public TokenProvider redisTokenProvider1() {
        return new TokenProviderRedisImpl();
    }


## Memcached

Same as the way of redis token management, using memcached as the token store is easy to configure. We use the [https://github.com/couchbase/spymemcached](spymemcached) as the memcached java client.
    
    @Value("${memcached.host}")
    private String memcachedHost;
    
    @Value("${memcached.port}")
    private int memcachedPort;
    
    @Bean
    public MemcachedClientFactoryBean memcachedClientFactoryBean() {
        MemcachedClientFactoryBean result = new MemcachedClientFactoryBean();
        result.setServers(memcachedHost + ":" + memcachedPort);
        return result;
    }

Then create the bean `in.clouthink.daas.security.token.spi.impl.memcached.TokenProviderMemcachedImpl`.Please remember to add `@Primary` annotation with `@Bean`,
it will take the place of the default implementation

    @Primary
    @Bean
    public TokenProvider memcachedTokenProvider() {
        return new TokenProviderMemcachedImpl();
    }


## Mongodb

Mongodb is one of the most popular nosql data store , we support to save the token back to mongodb , here is the configuration

    @Value("${mongodb.host}")
    private String mongodbHost;
    
    @Value("${mongodb.port}")
    private int mongodbPort;
    
    @Value("${mongodb.database}")
    private String mongodbDatabase;
    
    @Bean
    public MongoDbFactory mongoDbFactory() throws Exception {
        return new SimpleMongoDbFactory(new MongoClient(mongodbHost,
                                                        mongodbPort),
                                        mongodbDatabase);
    }
    
    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        return new MongoTemplate(mongoDbFactory());
    }


Then create the bean `in.clouthink.daas.security.token.spi.impl.mongodb.TokenProviderMongodbImpl`.Please remember to add `@Primary` annotation with `@Bean`,
it will take the place of the default implementation
    
    @Primary
    @Bean
    public TokenProvider mongodbTokenProvider1() {
        return new TokenProviderMongodbImpl();
    }


## Composite memcached and mongodb

Redis is good choose to make the data cache-able and persist-able, but you can composite the memcached and mongodb together to achieve this target.
It's very easy to configure DaaS-Token to support this feature.Create the bean `in.clouthink.daas.security.token.spi.impl.CompositeTokenProvider` and
add `@Primary` annotation to the compositeTokenProvider.

    @Bean
    public TokenProvider memcachedTokenProvider() {
        return new TokenProviderMemcachedImpl();
    }
    
    @Bean
    public TokenProvider mongodbTokenProvider1() {
        return new TokenProviderMongodbImpl();
    }
    
    @Primary
    @Bean
    public TokenProvider compositeTokenProvider() {
        return new CompositeTokenProvider(memcachedTokenProvider(), mongodbTokenProvider1());
    }
    



## Advanced 

### Performace

Daas-Token is one light-weighted security framework but it doesn't mean we will sacrifice the performance to exchange the simple usage.
The SPI is available for the advanced user to adapt their own implementation, even we have supported the Memcached, Redis and Mongodb out of the box.

Just supply your implementation

    public interface TokenProvider<T extends Token> {
        
        public void saveToken(T token);
        
        public T findByToken(String token);
        
        public void revokeToken(T token);
        
    }

### Customize the authorization

Maybe you'd like to save the access control list back to the data store , and want to authorize the access request based on the dynamic data not hard-coded configuration.
The SPI supplies the extension point if you want customize the authorization behaviors.

    public interface AclProvider<T extends Acl> {
        
        public List<T> listAll();
        
    }


    public interface AccessRequestVoter<T extends AccessRequest> {
        
        public AccessResponse vote(T t, String grantRule);
        
    }
    
Please refer to the default implementations by DaaS-Token
    
* in.clouthink.daas.security.token.spi.impl.DefaultUrlAclProvider
* in.clouthink.daas.security.token.core.acl.AccessRequestRoleVoter
* in.clouthink.daas.security.token.core.acl.AccessRequestUserVoter


### Java Client

Once the user passed the authentication , the token response is sent back to the user. 
And then you can access the protected url resource with the token in the http header.

    MultiValueMap<String, String> bodyMap = new LinkedMultiValueMap<String, String>();
    bodyMap.add("username", "your username");
    bodyMap.add("password", "your password");
    
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    
    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(bodyMap,
                                                                                                      headers);
    
    Map result = new RestTemplate().postForObject("http://127.0.0.1/login",
                                                  request,
                                                  Map.class);
                                                  
    String token = (String) ((Map) result.get("data")).get("token");                            
                              
    HttpHeaders headers = new HttpHeaders();
    String bearer = new String(Base64.encode(token.getBytes("UTF-8")),
                               "UTF-8");
    headers.set("Authorization", "Bearer " + bearer);
    
    HttpEntity request = new HttpEntity(headers);
    
    ResponseEntity<String> result = new RestTemplate().exchange("http://127.0.0.1/token/sample",
                                                                HttpMethod.GET,
                                                                request,
                                                                String.class);   
                                                                  