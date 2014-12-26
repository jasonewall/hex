package hex.action.views;

import hex.action.ControllerAction;
import hex.action.ViewContext;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.PageContext;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

/**
 * Created by jason on 14-12-26.
 */
public class ViewSupport {
    public static final String VIEW_CONTEXT_COPIED = "hex.action.views.ViewSupport.VIEW_CONTEXT_COPIED";

    public static void ensureViewContext(PageContext context) {
        ensureViewContext(context.getAttribute(VIEW_CONTEXT_COPIED), context::getAttribute, context::setAttribute);
    }

    public static void ensureViewContext(JspContext context) {
        ensureViewContext(context.getAttribute(VIEW_CONTEXT_COPIED),
                context::getAttribute,
                context::setAttribute);
    }

    private static void ensureViewContext(Object copied, BiFunction<String,Integer,Object> attributeGetter, BiConsumer<String,Object> attributeSetter) {
        if(copied == null) {
            ViewContext view = (ViewContext) attributeGetter.apply(ControllerAction.VIEW_CONTEXT, PageContext.REQUEST_SCOPE);
            view.forEach(attributeSetter);
            attributeSetter.accept(VIEW_CONTEXT_COPIED, VIEW_CONTEXT_COPIED);
        }
    }
}
