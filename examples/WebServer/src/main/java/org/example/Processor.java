package org.example;

import java.io.IOException;
        import java.io.PrintWriter;
        import java.net.Socket;

/**
 * Processor of HTTP request.
 */
public class Processor {
    private final Socket socket;
    private final HttpRequest request;

    public Processor(Socket socket, HttpRequest request) {
        this.socket = socket;
        this.request = request;
    }

    public void process() throws IOException {
        // Print request that we received.
        System.out.println("Got request:");
        System.out.println(request);
        System.out.flush();

        // To send response back to the client.
        PrintWriter output = new PrintWriter(socket.getOutputStream());

        // We are returning a simple web page now.
        output.println("HTTP/1.1 200 OK");
        output.println("Content-Type: text/html; charset=utf-8");
        output.println();
        output.println("<html>");
        output.println("<head><title>Hello</title></head>");
        if (request.getRequestLine().equals("GET /create/itemid HTTP/1.1")) {
            output.println("<body><p>itemid has been created!</p></body>");
        } else if (request.getRequestLine().equals("GET /delete/itemid HTTP/1.1")) {
            output.println("<body><p>itemid has been deleted!</p></body>");
        } else if (request.getRequestLine().equals("GET /exec/params HTTP/1.1")) {
            output.println("<body><p>Parameters has been executed!</p></body>");
        } else {
            String req = request.getRequestLine();
            req = req.replace("GET /", "").replace(" HTTP/1.1", "");
            String[] reqs = req.split("/");
            System.out.println(reqs);
            if (reqs[0].equals("calculate") && reqs[1].equals("factorial")) {
                double f = 1;
                for (int i = 1; i <= Integer.parseInt(reqs[2]); i++) {
                    f *= i;
                }
                output.println("<body><p>Factorial of " + reqs[2] + " is " + f + "</p></body>");
            }
        }
        output.println("<body><p>Hello, world!</p></body>");
        output.println("</html>");
        output.flush();

        socket.close();
    }
}