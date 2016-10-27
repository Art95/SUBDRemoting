package server;

import subd.SUBDService;

import javax.xml.ws.Endpoint;

/**
 * Created by Artem on 21.10.2016.
 */
public class Publisher {
    public static void main(String... args) {
        Endpoint.publish("http://localhost:1987/wss/subd", new SUBDService());

        // http://localhost:1986/wss/subd?wsdl
    }
}
