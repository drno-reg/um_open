package UM.RU.Tests;


import io.prometheus.client.Gauge;

// Main application class
public class PrometheusExampleNodeExporterService {

    static final Gauge l = Gauge.build().name("labels").help("blah").labelNames("l").register();

    public static  void main(String[] args) throws Exception {


//        Server server = new Server(1234);
//        ServletContextHandler context = new ServletContextHandler();
//        context.setContextPath("/");
//        server.setHandler(context);
//        context.addServlet(new ServletHolder(new MetricsServlet()), "/metrics");
//        g.set(1);
//        c.inc(2);
//        s.observe(3);
//        h.observe(4);
//        l.labels("foo").inc(5);
//        server.start();
//        server.join();

    }
}

