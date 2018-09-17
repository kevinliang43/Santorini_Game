import org.junit.Assert;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

/**
 * Created by KevinLiang on 9/13/18.
**/

public class MainTest {

  public static void main(String args[]) {
    org.junit.runner.JUnitCore.main("MainTest");

  }

  @Test
  public void testEmptyStringJson() {
    // Set up stdin and expected stdout
    String inputString = "\"\"";
    String expectedOut = "[0, \"\"]\n";

    // Set up Streams
    System.setIn(new ByteArrayInputStream(inputString.getBytes()));
    ByteArrayOutputStream actualOut = new ByteArrayOutputStream();
    PrintStream actualOutPrint = new PrintStream(actualOut);
    System.setOut(actualOutPrint);

    // Call Function
    try {
      Main.main(new String[0]);
    } catch (IOException e){
      // Do nothing
    }

    // Check validity
    Assert.assertEquals(expectedOut, actualOut.toString());
  }

  @Test
  public void testSingleStringJson() {
    // Set up stdin and expected stdout
    String inputString = "\"Fish\"";
    String expectedOut = "[0, \"Fish\"]\n";

    // Set up Streams
    System.setIn(new ByteArrayInputStream(inputString.getBytes()));
    ByteArrayOutputStream actualOut = new ByteArrayOutputStream();
    PrintStream actualOutPrint = new PrintStream(actualOut);
    System.setOut(actualOutPrint);

    // Call Function
    try {
      Main.main(new String[0]);
    } catch (IOException e){
      // Do nothing
    }

    // Check validity
    Assert.assertEquals(expectedOut, actualOut.toString());
  }

  @Test
  public void testMultiStringJson() {
    // Set up stdin and expected stdout
    String inputString = "\"Fish\" \"Fish\"";
    String expectedOut = "[1, \"Fish\"]\n[0, \"Fish\"]\n";

    // Set up Streams
    System.setIn(new ByteArrayInputStream(inputString.getBytes()));
    ByteArrayOutputStream actualOut = new ByteArrayOutputStream();
    PrintStream actualOutPrint = new PrintStream(actualOut);
    System.setOut(actualOutPrint);

    // Call Function
    try {
      Main.main(new String[0]);
    } catch (IOException e){
      // Do nothing
    }

    // Check validity
    Assert.assertEquals(expectedOut, actualOut.toString());
  }

  @Test
  public void testSingleNumberJson() {
    // Set up stdin and expected stdout
    String inputString = "3";
    String expectedOut = "[0, 3]\n";

    // Set up Streams
    System.setIn(new ByteArrayInputStream(inputString.getBytes()));
    ByteArrayOutputStream actualOut = new ByteArrayOutputStream();
    PrintStream actualOutPrint = new PrintStream(actualOut);
    System.setOut(actualOutPrint);

    // Call Function
    try {
      Main.main(new String[0]);
    } catch (IOException e){
      // Do nothing
    }

    // Check validity
    Assert.assertEquals(expectedOut, actualOut.toString());
  }

  @Test
  public void testMultiNumberJson() {
    // Set up stdin and expected stdout
    String inputString = "3 4";
    String expectedOut = "[1, 3]\n[0, 4]\n";

    // Set up Streams
    System.setIn(new ByteArrayInputStream(inputString.getBytes()));
    ByteArrayOutputStream actualOut = new ByteArrayOutputStream();
    PrintStream actualOutPrint = new PrintStream(actualOut);
    System.setOut(actualOutPrint);

    // Call Function
    try {
      Main.main(new String[0]);
    } catch (IOException e){
      // Do nothing
    }

    // Check validity
    Assert.assertEquals(expectedOut, actualOut.toString());
  }

  @Test
  public void testMultiLineNumberJson() {
    // Set up stdin and expected stdout
    String inputString = "3\n4";
    String expectedOut = "[1, 3]\n[0, 4]\n";

    // Set up Streams
    System.setIn(new ByteArrayInputStream(inputString.getBytes()));
    ByteArrayOutputStream actualOut = new ByteArrayOutputStream();
    PrintStream actualOutPrint = new PrintStream(actualOut);
    System.setOut(actualOutPrint);

    // Call Function
    try {
      Main.main(new String[0]);
    } catch (IOException e){
      // Do nothing
    }

    // Check validity
    Assert.assertEquals(expectedOut, actualOut.toString());
  }

  @Test
  public void testEmptyArrayJson() {
    // Set up stdin and expected stdout
    String inputString = "[]";
    String expectedOut = "[0, []]\n";

    // Set up Streams
    System.setIn(new ByteArrayInputStream(inputString.getBytes()));
    ByteArrayOutputStream actualOut = new ByteArrayOutputStream();
    PrintStream actualOutPrint = new PrintStream(actualOut);
    System.setOut(actualOutPrint);

    // Call Function
    try {
      Main.main(new String[0]);
    } catch (IOException e){
      // Do nothing
    }

    // Check validity
    Assert.assertEquals(expectedOut, actualOut.toString());
  }

  @Test
  public void testSingleArrayJson() {
    // Set up stdin and expected stdout
    String inputString = "[3]";
    String expectedOut = "[0, [3]]\n";

    // Set up Streams
    System.setIn(new ByteArrayInputStream(inputString.getBytes()));
    ByteArrayOutputStream actualOut = new ByteArrayOutputStream();
    PrintStream actualOutPrint = new PrintStream(actualOut);
    System.setOut(actualOutPrint);

    // Call Function
    try {
      Main.main(new String[0]);
    } catch (IOException e){
      // Do nothing
    }

    // Check validity
    Assert.assertEquals(expectedOut, actualOut.toString());
  }

  @Test
  public void testMultiArrayJson() {
    // Set up stdin and expected stdout
    String inputString = "[2,3]";
    String expectedOut = "[0, [2,3]]\n";

    // Set up Streams
    System.setIn(new ByteArrayInputStream(inputString.getBytes()));
    ByteArrayOutputStream actualOut = new ByteArrayOutputStream();
    PrintStream actualOutPrint = new PrintStream(actualOut);
    System.setOut(actualOutPrint);

    // Call Function
    try {
      Main.main(new String[0]);
    } catch (IOException e){
      // Do nothing
    }

    // Check validity
    Assert.assertEquals(expectedOut, actualOut.toString());
  }

  @Test
  public void testMultiLineArrayJson() {
    // Set up stdin and expected stdout
    String inputString = "[2,\n3]";
    String expectedOut = "[0, [2,3]]\n";

    // Set up Streams
    System.setIn(new ByteArrayInputStream(inputString.getBytes()));
    ByteArrayOutputStream actualOut = new ByteArrayOutputStream();
    PrintStream actualOutPrint = new PrintStream(actualOut);
    System.setOut(actualOutPrint);

    // Call Function
    try {
      Main.main(new String[0]);
    } catch (IOException e){
      // Do nothing
    }

    // Check validity
    Assert.assertEquals(expectedOut, actualOut.toString());
  }

  @Test
  public void testSingleJsonObj() {
    // Set up stdin and expected stdout
    String inputString = "{\"string\" : \"string\"}";
    String expectedOut = "[0, {\"string\":\"string\"}]\n";

    // Set up Streams
    System.setIn(new ByteArrayInputStream(inputString.getBytes()));
    ByteArrayOutputStream actualOut = new ByteArrayOutputStream();
    PrintStream actualOutPrint = new PrintStream(actualOut);
    System.setOut(actualOutPrint);

    // Call Function
    try {
      Main.main(new String[0]);
    } catch (IOException e){
      // Do nothing
    }

    // Check validity
    Assert.assertEquals(expectedOut, actualOut.toString());
  }

  @Test
  public void testMultiJsonObj() {
    // Set up stdin and expected stdout
    String inputString = "{\"string\":\"string\"} {\"string\":\"string\"}";
    String expectedOut = "[1, {\"string\":\"string\"}]\n[0, {\"string\":\"string\"}]\n";

    // Set up Streams
    System.setIn(new ByteArrayInputStream(inputString.getBytes()));
    ByteArrayOutputStream actualOut = new ByteArrayOutputStream();
    PrintStream actualOutPrint = new PrintStream(actualOut);
    System.setOut(actualOutPrint);

    // Call Function
    try {
      Main.main(new String[0]);
    } catch (IOException e){
      // Do nothing
    }

    // Check validity
    Assert.assertEquals(expectedOut, actualOut.toString());
  }

  @Test
  public void testMultiLineJsonObj() {
    // Set up stdin and expected stdout
    String inputString = "{\"string\":\n\"string\"}";
    String expectedOut = "[0, {\"string\":\"string\"}]\n";

    // Set up Streams
    System.setIn(new ByteArrayInputStream(inputString.getBytes()));
    ByteArrayOutputStream actualOut = new ByteArrayOutputStream();
    PrintStream actualOutPrint = new PrintStream(actualOut);
    System.setOut(actualOutPrint);

    // Call Function
    try {
      Main.main(new String[0]);
    } catch (IOException e){
      // Do nothing
    }

    // Check validity
    Assert.assertEquals(expectedOut, actualOut.toString());
  }

}


