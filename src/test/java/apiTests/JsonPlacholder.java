package apiTests;

import org.testng.annotations.Test;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.StringJoiner;

public class JsonPlacholder {

    // StringJoiner, String.joiner, StringBuilder, StringBuffer
    // String.format,

    @Test
    public void testJsonPlaceholder() {

      String d =  """
              <html>
                  <head>
                      <title>%s</title>
                  </head>
                  <body>
                      <h1>%d</h1>
                  </body>
              """.formatted("demo", 20);
        d.lines().forEach(System.out::println);


    }
}

