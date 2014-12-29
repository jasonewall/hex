hex_action
====================================

Layout and request handling framework for developing Java Web Applications.

# Controllers

Controllers are the dispatchers of your application. They take in form input and send that input some where that knows
what to do with it. (i.e.: Your business logic.)

**hex** controllers are designed to get you in and out as fast as possible. We'll get into how they do that in some
examples below. Controller action methods also do not require a return type and do not require any specific parameter
types.

By default a controller action renders a **hex** view based on the name of the action. (e.g.: `public void index()` in
`PostsController` will render the view `views/posts/index.html.jsp`. This is very much a Ruby on Rails inspired mechanism so if
this sounds familiar that's why. You can override this by calling one of the several render methods provided by
extending `hex.action.Controller`.

## Example Controller

```java
package controllers;

import repositories.PostsRepository;

import hex.action.Controller;
import hex.utils.Memo;

public class PostsController extends Controller {
    public void index() {
        // Standard pattern for searching a series of optional params
        PostsRepository repo = new PostsRepository();
        Stream<Post> posts = Memo.of(repo.stream())
            .andThen(q -> params.ifPresent("author", author -> q.authorLike(author))
            .andThen(q -> params.ifPresent("tags", tags -> q.taggedByAnyOf(tags))
            .finish();
        view.put("posts", posts.toArray());
    }

    public void show(@RouteParam("id") int id) {
        PostsRepository repo = new PostsRepository();
        Post post = repo.find(id).orElseThrow(() -> new RecordNotFoundException("posts", id));
        view.put("post", post);
    }

    public void create(@Param("post") Post post) {
        PostsRepository repo = new PostsRepository();
        repo.create(post);
        redirectTo(post);
    }
}
```

### ViewContext

In two of the actions above you'll note a call to `view#put(String,Object)`. `view` is a member inherited from
`Controller` that is of type `hex.action.views.ViewContext`. This is basically a grab bag of everything you want
available to your view when it is rendered. Everything put in the view context is made available to the page (in the
case of JSP pages, the `javax.servlet.jsp.PageContext`) as soon as possible.

### Action Method Parameters

Two of the action methods also have parameters. They are annotated with two different types of annotations. First, the
`show(int)` method is annotated with `@RouteParam("id")`. This tells the `ControllerAction` to look in the `RouteParams`
collection for a param `id`. Since the parameter is of type `int`, it tries to coerce the param value into an `int` and
then pass the result as the first parameter of the method `show()`.

The `create(Post)` action also has a parameter, but it's parameter is a complex type, `Post`, and is annotated with
`@Param("post")`. The `@Param` annotation tells the `ControllerAction` to look in all parameter types: query string, form data, and route params for
a parameter named `"post"`. Then it tries to coerce that value into the proper class type.

    NOTE: Of course you can't send complex data types from HTML forms. **hex**'s complex type coercion looks for a
    series of request parameters that are formatted a specific way and uses those parameters to infer which properties
    on the target object should be populated. As long as you're using the jsp helpers tags packaged with **hex** you
    don't need to worry about this.

### Implicit Members

There are a few member variables inherited that you will want to know about.

Name | Description
---- | ------------
`view` | The `ViewContext` described above.
`params` | The parameter collection. Has several helper methods to get parameter values out in the type that you want. See docs for details (and sorry if they are not written yet).
`request` | The raw `javax.servlet.HttpServletRequest`. Needing to reference this will probably be a rare occurrence but it's here just in case.
`response` | The raw `javax.servlet.HttpServletResponse`. Same story as `request`.

## Controllers Teaser Trailer

There is probably a lot more to **hex** controllers than is covered here. There will be more comprehensive docs in the
Wiki eventually.
