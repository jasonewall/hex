hex [![Build Status](https://travis-ci.org/thejayvm/hex.svg?branch=master)](https://travis-ci.org/thejayvm/hex)
===

Hex is not an MVC framework, it just looks like one.

Idealistic, hexagonal architecture inspired, pie in the sky Java development without all the weird J2EE servlet
stuff but still being 100% J2EE compatible.

Put **hex** in your existing Java app to start living the dream.

    web.xml
    =========================
    <listener>
        <listener-class>hex.action.Application</listener-class>
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

## Development Mode

There is a second set of configuration classes you can use in development mode for creating **hex** applications. This mode
refreshes your applications classes on every request so there is no need to:
1. Compile your application yourself.
2. Restart the Java server every time you make class changes.

    <filter>
        <filter-name>HexFilter</filter-name>
        <filter-class>hex.dev.DevRoutingFilter</filter-class>
        <init-param>
            <param-name>hex.action.Application.ROOT</param-name>
            <param-value>/path/to/hex/application</param-value>
        </init-param>
    </filter>

    <filter-mapping>
        <filter-name>HexFilter</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>

Note the lack of the `ServletContextListener` configured in development mode. The `DevRoutingFilter` manages the lifecycle
of a **hex** application differently so it takes care of what the `hex.action.Application` context listener normally
initializes.

# Routes & Controllers

Define routes in your `ApplicationRoutes` class.

    package config;

    public class ApplicationRoutes extends RouteManager {
        public void defineRoutes() {
            matches("/", HomeController::new, "home");
        }
    }

Create your controller!

    package controllers;

    public class HomeController extends Controller {
        public void home() {
            // we don't do anything here if we're just forwarding to a view
        }
    }

# DISCLAIMER (and Contributing)

**hex** is very much in alpha stages. Don't use it yet. Seriously. There isn't a whole let here. In fact, I could use [some help](https://github.com/thejayvm/hex/wiki/Contribution-Guide) getting it going!
