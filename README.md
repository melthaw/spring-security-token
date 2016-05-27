# introduction

sometimes we only need a simple token based security framework to protect the rest api. obviously we like spring framework but spring security is too heavy to us.
it's possible to extends the spring security to match our requirement, but that means we have to pay the cost for the over-designed filter chain at the runtime.
daas-token is designed to reduce down the filters in the chain ,abandon the complicated structure , and try to make it simple , make it easy to understand.

we are still the heavy users of spring framework , so when we decide to develop the daas-token security framework , we only consider to support the spring framework without any hesitation.

thanks spring security. there are some useful apis available for us in spring security , but we don't like to depend on the fully jars , so we re-package them under the following package.
> in.clouthink.daas.security.token.repackage.org.springframework.security

# dependencies

* spring framework 3.2.x (core & web required)
* spring data redis (optional)
* spring data mongodb (optional)
* spymemcached (optional)

# usage

## maven

so far 1.5.0 is available 

    <dependency>
        <groupid>in.clouthink.daas</groupid>
        <artifactid>daas-token</artifactid>
        <version>${daas.token.version}</version>
    </dependency>

## spring configuration

use `@enabletoken` to get started 

    @configuration
    @enabletoken
    public class application {}

 
by default, the jvm memory-based token management is working for daas-token, `@scheduled(cron = "0 0/10 * * * ?")` is triggered every 10 minutes to clean up the expired token. 
so please enable the spring schedule feature if you does not change the default configuration.otherwise , **out of memory** should be a big problem.

    @configuration
    @enablescheduling
    @enabletoken
    public class application {}

## customize

implement the interface `tokenconfigurer` to customize the daas-token features

* login url 
* logout url
* authenticate url
* authorize url
* token alive timeout
* access control list

here is the sample 

    @bean
    public tokenconfigurer mytokenconfigurer() {
        //todo:create and return the tokenconfigurer instance here.
    }

or you can extend the stub class `tokenconfigureradapter` which has been supply the empty implementation for `tokenconfigurer`,
just override the methods you'd like to.

for example , we'd like to let the token only be alive for one hour.

    @bean
    public tokenconfigurer mytokenconfigurer() {
        return new tokenconfigureradapter() {
            @override
            public void configure(tokenlife tokenlife) {
                tokenlife.settokentimeout(60 * 60 * 1000);
            }
        }
    }

set the login process url:

    @bean
    public tokenconfigurer mytokenconfigurer() {
        return new tokenconfigureradapter() {
            @override
            public void configure(loginendpoint endpoint) {
                endpoint.setloginprocessesurl("/login");
            }
        }
    }
    

set the logout process url:

    @bean
    public tokenconfigurer mytokenconfigurer() {
        return new tokenconfigureradapter() {
            @override
            public void configure(logoutendpoint endpoint) {
                endpoint.setlogoutprocessesurl("/logout");
            }
        }
    }

set the protected rest url:

    @bean
    public tokenconfigurer mytokenconfigurer() {
        return new tokenconfigureradapter() {

            @override
            public void configure(authenticationfilter filter) {
                filter.setprocessesurl("/protected**");
            }

            @override
            public void configure(authorizationfilter filter) {
                filter.setprocessesurl("/protected**");
            }
            
        }
    }
 
the authenticationfilter is responsible to judge whether is user is authenticated or not.
and the authorizationfilter is responsible to decide whether the user is allowed to access the protected url.
normally, keep the process url of the two filters be the same value.

now here goes to the step to define the acl which is designed as rbac , but not only rbac. 
url acl is designed into three parts

* url  
 the http url (support regex and ant path)
* http method  
the http method (support get , put, post and delete)
* grant rule  
the user access is granted if the request is matching the rules

the grant rule is defined as expression, now we support the following two format :

* role:`xxx`  
 xxx should be the role name (`role#getname()`)
* username:`xxx`  
 xxx should be the user name (`user#getusername()`)

for example:
the user which's username is **testuser** or owns the role **test** can access the **/token/sample/helloworld** with the http **get** method

    @bean
    public tokenconfigurer mytokenconfigurer() {
        return new tokenconfigureradapter() {

            @override
            public void configure(urlaclproviderbuilder builder) {
                builder.add(urlaclbuilder.antpathbuilder()
                                         .url("/token/sample/helloworld")
                                         .httpmethods(httpmethod.get)
                                         .grantrules("role:test,username:testuser"));
            }
            
        }
    }
             
             

## redis 

as mentioned before, the jvm memory based token management is used by default, but we supplied the redis based token management, here is the way to enable the feature.

first, enabled the spring data redis feature as follow:

    @value("${redis.host}")
    private string redishost;
    
    @value("${redis.port}")
    private int redisport;
    
    @bean
    public redisconnectionfactory jedisconnectionfactory() {
        redisconnectionfactory result = new jedisconnectionfactory(new jedisshardinfo(redishost,
                                                                                      redisport));
        return result;
    }
    
    @bean
    public redistemplate redistemplate() {
        redistemplate result = new redistemplate();
        result.setconnectionfactory(jedisconnectionfactory());
        result.setkeyserializer(new stringredisserializer(charset.forname("utf-8")));
        return result;
    }
     
then create the bean `in.clouthink.daas.security.token.spi.impl.redis.tokenproviderredisimpl`.please remember to add `@primary` annotation with `@bean`,
it will take the place of the default implementation

    @primary
    @bean
    public tokenprovider redistokenprovider1() {
        return new tokenproviderredisimpl();
    }


## memcached

same as the way of redis token management, using memcached as the token store is easy to configure. we use the [https://github.com/couchbase/spymemcached](spymemcached) as the memcached java client.
    
    @value("${memcached.host}")
    private string memcachedhost;
    
    @value("${memcached.port}")
    private int memcachedport;
    
    @bean
    public memcachedclientfactorybean memcachedclientfactorybean() {
        memcachedclientfactorybean result = new memcachedclientfactorybean();
        result.setservers(memcachedhost + ":" + memcachedport);
        return result;
    }

then create the bean `in.clouthink.daas.security.token.spi.impl.memcached.tokenprovidermemcachedimpl`.please remember to add `@primary` annotation with `@bean`,
it will take the place of the default implementation

    @primary
    @bean
    public tokenprovider memcachedtokenprovider() {
        return new tokenprovidermemcachedimpl();
    }


## mongodb

mongodb is one of the most popular nosql data store , we support to save the token back to mongodb , here is the configuration

    @value("${mongodb.host}")
    private string mongodbhost;
    
    @value("${mongodb.port}")
    private int mongodbport;
    
    @value("${mongodb.database}")
    private string mongodbdatabase;
    
    @bean
    public mongodbfactory mongodbfactory() throws exception {
        return new simplemongodbfactory(new mongoclient(mongodbhost,
                                                        mongodbport),
                                        mongodbdatabase);
    }
    
    @bean
    public mongotemplate mongotemplate() throws exception {
        return new mongotemplate(mongodbfactory());
    }


then create the bean `in.clouthink.daas.security.token.spi.impl.mongodb.tokenprovidermongodbimpl`.please remember to add `@primary` annotation with `@bean`,
it will take the place of the default implementation
    
    @primary
    @bean
    public tokenprovider mongodbtokenprovider1() {
        return new tokenprovidermongodbimpl();
    }


## composite memcached and mongodb

redis is good choose to make the data cache-able and persist-able, but you can composite the memcached and mongodb together to achieve this target.
it's very easy to configure daas-token to support this feature.create the bean `in.clouthink.daas.security.token.spi.impl.compositetokenprovider` and
add `@primary` annotation to the compositetokenprovider.

    @bean
    public tokenprovider memcachedtokenprovider() {
        return new tokenprovidermemcachedimpl();
    }
    
    @bean
    public tokenprovider mongodbtokenprovider1() {
        return new tokenprovidermongodbimpl();
    }
    
    @primary
    @bean
    public tokenprovider compositetokenprovider() {
        return new compositetokenprovider(memcachedtokenprovider(), mongodbtokenprovider1());
    }
    



## advanced 

### performace

daas-token is one light-weighted security framework but it doesn't mean we will sacrifice the performance to exchange the simple usage.
the spi is available for the advanced user to adapt their own implementation, even we have supported the memcached, redis and mongodb out of the box.

just supply your implementation

    public interface tokenprovider<t extends token> {
        
        public void savetoken(t token);
        
        public t findbytoken(string token);
        
        public void revoketoken(t token);
        
    }

### customize the authorization

maybe you'd like to save the access control list back to the data store , and want to authorize the access request based on the dynamic data not hard-coded configuration.
the spi supplies the extension point if you want customize the authorization behaviors.

    public interface aclprovider<t extends acl> {
        
        public list<t> listall();
        
    }


    public interface accessrequestvoter<t extends accessrequest> {
        
        public accessresponse vote(t t, string grantrule);
        
    }
    
please refer to the default implementations by daas-token
    
* in.clouthink.daas.security.token.spi.impl.defaulturlaclprovider
* in.clouthink.daas.security.token.core.acl.accessrequestrolevoter
* in.clouthink.daas.security.token.core.acl.accessrequestuservoter


### java client

once the user passed the authentication , the token response is sent back to the user. 
and then you can access the protected url resource with the token in the http header.

    multivaluemap<string, string> bodymap = new linkedmultivaluemap<string, string>();
    bodymap.add("username", "your username");
    bodymap.add("password", "your password");
    
    httpheaders headers = new httpheaders();
    headers.setcontenttype(mediatype.application_form_urlencoded);
    
    httpentity<multivaluemap<string, string>> request = new httpentity<multivaluemap<string, string>>(bodymap,
                                                                                                      headers);
    
    map result = new resttemplate().postforobject("http://127.0.0.1/login",
                                                  request,
                                                  map.class);
                                                  
    string token = (string) ((map) result.get("data")).get("token");                            
                              
    httpheaders headers = new httpheaders();
    string bearer = new string(base64.encode(token.getbytes("utf-8")),
                               "utf-8");
    headers.set("authorization", "bearer " + bearer);
    
    httpentity request = new httpentity(headers);
    
    responseentity<string> result = new resttemplate().exchange("http://127.0.0.1/token/sample",
                                                                httpmethod.get,
                                                                request,
                                                                string.class);   


# appendix : error code explain 

error response format (json) for example:

    {"message":"the token is disabled","succeed":false,"errorcode":"error.tokenisdisabled"}
    
explain
    
error code | error message | http status code | description
-----------|-----------|-----------|-----------
error.invaliduserorpassword | invalid username or password. |  |
error.invalidtokenorexpired | the session is invalid or expired. | |
error.tokenisdisabled |the token is disabled. | |
error.userislocked | the user is locked. | | 
error.userisdisabled | the user is disabled. | |
error.userisexpired | the user is expired. | |
error.authenticationrequired | authentication required. | |
error.authenticationfailed | authentication failed. | |
error.authorizationfailed | authorization failed. | |
error.nopermission | no permission.access denied. | |
