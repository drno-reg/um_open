package UM.RU.Tests;


import io.prometheus.client.Gauge;
import io.prometheus.client.vertx.MetricsHandler;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;

import java.util.Timer;
import java.util.TimerTask;

// Main application class
public class PrometheusExampleNodeExporterService {

    static final Gauge l = Gauge.build().name("labels").help("blah").labelNames("l").register();

    public static  void main(String[] args) throws Exception {

        final Timer time = new Timer();
        final Vertx vertx = Vertx.vertx();
        final Router router = Router.router(vertx);

        router.route("/metrics").handler(new MetricsHandler());

        vertx.createHttpServer().requestHandler(router::accept).listen(8001);

        time.schedule(new TimerTask() {
            int i = 0;

            @Override
            public void run() { //ПЕРЕЗАГРУЖАЕМ МЕТОД RUN В КОТОРОМ ДЕЛАЕТЕ ТО ЧТО ВАМ НАДО

                l.labels("foo").inc(Math.random()*3 );
            System.out.println("4 second ago");
        }
    }, 0, 4000); //(4000 - ПОДОЖДАТЬ ПЕРЕД НАЧАЛОМ В МИЛИСЕК, ПОВТОРЯТСЯ 4 СЕКУНДЫ (1 СЕК = 1000 МИЛИСЕК))


    }
}

