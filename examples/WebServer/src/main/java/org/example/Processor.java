package org.example;

import java.io.*;
import java.net.Socket;

/**
 * Processor of HTTP request.
 */
public class Processor {
    private final Socket socket;
    private final HttpRequest request;
    public String response = "";

    public Processor(Socket socket, HttpRequest request) {
        this.socket = socket;
        this.request = request;
    }


    public void process() throws IOException {
        System.out.println("Got request:");
        System.out.println(request.toString());
        System.out.flush();

        String s = request.toString();
        int pos;
        char c;
        String name = "";
        if(s.contains("create")) {

            for(pos = 12; s.charAt(pos) != ' '; ++pos) {
                c = s.charAt(pos);
                name = name + c;
            }

            File myObj = new File(name);
            if (myObj.createNewFile()) {
                String var10001 = this.response;
                this.response = var10001 + "File created: " + myObj.getName();
            } else {
                this.response = this.response + "File already exists.";
            }

        }
        else if(s.contains("delete")) {
            for(pos = 12; s.charAt(pos) != ' '; ++pos) {
                c = s.charAt(pos);
                name = name + c;
            }
            File obj = new File(name);
            if (obj.delete()) {
                String var10001 = this.response;
                this.response = var10001 + "File deleted: " + obj.getName();
            } else {
                this.response = this.response + "File successfully delete";
            }
        }
        else if(s.contains("write")){
            pos = 11;
            while(s.charAt(pos) != '/'){
                c = s.charAt(pos);
                name += c;
                pos++;
            }
//            FileWriter myWriter = new FileWriter(name);
//            myWriter.write("some text");
//            myWriter.close();
            PrintWriter writer = new PrintWriter("text", "UTF-8");
            writer.println("The United Nations Educational, Scientific and Cultural Organization[a] is a specialised agency of the United Nations (UN) aimed at promoting world peace and security through international cooperation in education, arts, sciences and culture.\n"+
                    "It has 193 member states and 12 associate members, as well as partners in the non-governmental, intergovernmental and private sector."+
                    "Headquartered at the World Heritage Centre in Paris, France, UNESCO has 53 regional field offices and 199 national commissions that facilitate its global mandate.");
            writer.close();
            this.response += "Successfully wrote to the file.";
        }
        else if(s.contains("execute")) {
            for (pos = 13; s.charAt(pos) != ' '; ++pos) {
                c = s.charAt(pos);
                name = name + c;
            }
            String ch="a";
            int count=0;
            BufferedReader myReader = new BufferedReader(new FileReader(name));
            String strCurrentLine;
            while ((strCurrentLine = myReader.readLine()) != null) {
                String[] words = strCurrentLine.split("");
                for (String word : words) {
                    if (word.equals(ch))
                    {
                        count++;
                    }
                }
                this.response += strCurrentLine;
            }
            if(count!=0)
            {
                this.response = "The written character is repeated for "+count+ " times in the given file";
            }
            else
            {
                this.response = "The written character is not present in the given file";
            }
            myReader.close();
        }



        PrintWriter output = new PrintWriter(socket.getOutputStream());

        // We are returning a simple web page now.
        output.println("HTTP/1.1 200 OK");
        output.println("Content-Type: text/html; charset=utf-8");
        output.println();
        output.println("<html>");
        output.println("<head><title>Hello</title></head>");
        output.println("<body><p>" + this.response + "</p></body>");
        output.println("</html>");
        output.flush();
        socket.close();
        this.response = "";
    }
}