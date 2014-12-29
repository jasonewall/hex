hex [![Build Status](https://travis-ci.org/thejayvm/hex.svg?branch=master)](https://travis-ci.org/thejayvm/hex)
===

Hex is not an MVC framework, it just looks like one.

Idealistic, hexagonal architecture inspired, pie in the sky Java development without all the weird J2EE servlet
stuff but still being 100% J2EE compatible.

Put **hex** in your existing Java app to start living the dream.

```xml
<!------------------------ web.xml ------------------------>
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
```

Yup! `/*`! That is the nice thing about using a filter. If **hex** doesn't know what to do with a route it just lets the rest
of your application deal with it.

## Development Mode

There is a second set of configuration classes you can use in development mode for creating **hex** applications. This mode
refreshes your applications classes on every request so there is no need to:

1. Compile your application yourself (during development)
2. Restart the Java server every time you make class changes.

```xml
<!------------------------ web.xml ------------------------>
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
```

Note the lack of the `ServletContextListener` configured in development mode. The `DevRoutingFilter` manages the lifecycle
of a **hex** application differently so it takes care of what the `hex.action.Application` context listener normally
initializes.

# Routes & Controllers

Define routes in your `ApplicationRoutes` class.

```java
// adapters/config/ApplicationRoutes.java
package config;

public class ApplicationRoutes extends RouteManager {
    public void defineRoutes() {
        matches("/", HomeController::new, "home");
    }
}
```

Create your controller!

```java
// adapters/controllers/HomeController.java
package controllers;

public class HomeController extends Controller {
    public void home() {
        // we don't do anything here if we're just forwarding to a view
    }
}
```

# Layouts And Views

Define an application layout:

```jsp
<!-- views/layouts/application.html.jsp -->
<%@ taglib uri="http://hex.org/tags" prefix="hex" %>

<html>
    <head>
        <title>My Hex Application</title>
        <hex:view-content name="styles"/> <!-- optional named content block -->
    </head>
    <body>
        <h1>My Hex Application</h1>

        <hex:view-content/> <!-- no name means write out the view contents -->
    </body>
</html>
```

Create an action view:

```jsp
<!-- views/home/home.html.jsp -->
<%@ taglib uri="http://hex.org/tags" prefix="hex" %>

<hex:content-for name="styles">
    <style type="text/css">
        h2 {
            color: green;
        }
    </style>
</hex:content>

<h2>Welcome Home!</h2>
```

# What Else You Got?

This is just the main project overview. **hex** is composed of several modules that are responsible for various sections
of an application. For more information on the controller and view layer, checkout the [hex_action README](./hex_action).

For information on **hex**'s repository pattern, checkout the [hex_repo](./hex_repo) module.

If you want to dig through the anatomy of standard **hex** application, checkout the [example application](./example_application).

# DISCLAIMER

**hex** is very much in alpha stages. Don't use it yet. Seriously. There isn't a whole let here. In fact, I could use [some help](https://github.com/thejayvm/hex/wiki/Contribution-Guide) getting it going!

# Contributing

If you're excited about creating **hex** applications and want to help jump start the development, checkout the [Contribution Guide](https://github.com/thejayvm/hex/wiki/Contribution-Guide).
