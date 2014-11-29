package hex.dev;

import hex.action.Application;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.webapp.WebAppContext;

import javax.servlet.DispatcherType;
import java.util.EnumSet;
import java.util.Scanner;

/**
 * Created by jason on 14-11-15.
 */
public class HexServer {
    public static void main(String[] args) {
        org.eclipse.jetty.server.Server server = new org.eclipse.jetty.server.Server(8080);

        WebAppContext context = new WebAppContext();
        FilterHolder holder = new FilterHolder(DevRoutingFilter.class);
        holder.setInitParameter(Application.ROOT, System.getProperty("user.dir"));
        context.addFilter(holder, "/*", EnumSet.of(DispatcherType.REQUEST));
        context.setResourceBase("./views");
        context.setInitParameter("org.eclipse.jetty.servlet.Default.dirAllowed", "false");
        server.setHandler(context);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                server.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));

        try {
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        new Thread(() -> {
            boolean running = true;
            Scanner scanner = new Scanner(System.in);
            while(running) {
                System.out.print("> ");
                running = !"exit".equals(scanner.next());
            }
            System.exit(0);
        }).start();

        try {
            server.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
