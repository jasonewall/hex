hex [![Build Status](https://travis-ci.org/thejayvm/hex.svg?branch=master)](https://travis-ci.org/thejayvm/hex)
===

Hex is not an MVC framework, it just looks like one.

Idealistic, hexagonal architecture inspired, pie in the sky Java development without all the weird J2EE servlet 
stuff but still being 100% J2EE compatible.

Put **hex** in your existing Java app to start living the dream.

    web.xml
    =========================
    <listener>
        <listener-class>
            hex.action.InitializerRunner
        </listener-class>
    </listener>
    
    <filter>
        <filter-name>HexFilter</filter-name>
        <filter-class>hex.routing.RoutingFilter</filter-class>
    </filter>
    
    <filter-mapping>
        <filter-name>HexFilter</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>
    
Yup! `/*`! That is the nice thing about using a filter. If **hex** doesn't know what to do with a route it just lets the rest
of your application deal with it.

# DISCLAIMER

**hex** is very much in alpha stages. Don't use it yet. Seriously. There isn't a whole let here. In fact, I could use some help getting it going!
