package servlet_mock.jsp;

import javax.servlet.jsp.JspWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by jason on 14-12-23.
 */
public class MockJspWriter extends JspWriter {
    // I have no idea
    private static final int WTF_BUFFER_SIZE = 1023;

    private BufferedWriter buffer;

    private StringWriter content;

    private PrintWriter printer;

    public MockJspWriter() {
        super(WTF_BUFFER_SIZE, true);
        content = new StringWriter();
        buffer = new BufferedWriter(content, WTF_BUFFER_SIZE);
        printer = new PrintWriter(buffer);
    }

    /**
     * Write a line separator.  The line separator string is defined by the
     * system property <tt>line.separator</tt>, and is not necessarily a single
     * newline ('\n') character.
     *
     * @throws java.io.IOException If an I/O error occurs
     */
    @Override
    public void newLine() throws IOException {
        buffer.newLine();
    }

    /**
     * Print a boolean value.  The string produced by <code>{@link
     * String#valueOf(boolean)}</code> is written to the
     * JspWriter's buffer or, if no buffer is used, directly to the
     * underlying writer.
     *
     * @param b The <code>boolean</code> to be printed
     * @throws java.io.IOException If an error occured while writing
     */
    @Override
    public void print(boolean b) throws IOException {
        printer.print(b);
    }

    /**
     * Print a character.  The character is written to the
     * JspWriter's buffer or, if no buffer is used, directly to the
     * underlying writer.
     *
     * @param c The <code>char</code> to be printed
     * @throws java.io.IOException If an error occured while writing
     */
    @Override
    public void print(char c) throws IOException {
        printer.print(c);
    }

    /**
     * Print an integer.  The string produced by <code>{@link
     * String#valueOf(int)}</code> is written to the
     * JspWriter's buffer or, if no buffer is used, directly to the
     * underlying writer.
     *
     * @param i The <code>int</code> to be printed
     * @throws java.io.IOException If an error occured while writing
     * @see Integer#toString(int)
     */
    @Override
    public void print(int i) throws IOException {
        printer.print(i);
    }

    /**
     * Print a long integer.  The string produced by <code>{@link
     * String#valueOf(long)}</code> is written to the
     * JspWriter's buffer or, if no buffer is used, directly to the
     * underlying writer.
     *
     * @param l The <code>long</code> to be printed
     * @throws java.io.IOException If an error occured while writing
     * @see Long#toString(long)
     */
    @Override
    public void print(long l) throws IOException {
        printer.print(l);
    }

    /**
     * Print a floating-point number.  The string produced by <code>{@link
     * String#valueOf(float)}</code> is written to the
     * JspWriter's buffer or, if no buffer is used, directly to the
     * underlying writer.
     *
     * @param f The <code>float</code> to be printed
     * @throws java.io.IOException If an error occured while writing
     * @see Float#toString(float)
     */
    @Override
    public void print(float f) throws IOException {
        printer.print(f);
    }

    /**
     * Print a double-precision floating-point number.  The string produced by
     * <code>{@link String#valueOf(double)}</code> is written to
     * the JspWriter's buffer or, if no buffer is used, directly to the
     * underlying writer.
     *
     * @param d The <code>double</code> to be printed
     * @throws java.io.IOException If an error occured while writing
     * @see Double#toString(double)
     */
    @Override
    public void print(double d) throws IOException {
        printer.print(d);
    }

    /**
     * Print an array of characters.  The characters are written to the
     * JspWriter's buffer or, if no buffer is used, directly to the
     * underlying writer.
     *
     * @param s The array of chars to be printed
     * @throws NullPointerException If <code>s</code> is <code>null</code>
     * @throws java.io.IOException If an error occured while writing
     */
    @Override
    public void print(char[] s) throws IOException {
        printer.print(s);
    }

    /**
     * Print a string.  If the argument is <code>null</code> then the string
     * <code>"null"</code> is printed.  Otherwise, the string's characters are
     * written to the JspWriter's buffer or, if no buffer is used, directly
     * to the underlying writer.
     *
     * @param s The <code>String</code> to be printed
     * @throws java.io.IOException If an error occured while writing
     */
    @Override
    public void print(String s) throws IOException {
        printer.print(s);
    }

    /**
     * Print an object.  The string produced by the <code>{@link
     * String#valueOf(Object)}</code> method is written to the
     * JspWriter's buffer or, if no buffer is used, directly to the
     * underlying writer.
     *
     * @param obj The <code>Object</code> to be printed
     * @throws java.io.IOException If an error occured while writing
     * @see Object#toString()
     */
    @Override
    public void print(Object obj) throws IOException {
        printer.print(obj);
    }

    /**
     * Terminate the current line by writing the line separator string.  The
     * line separator string is defined by the system property
     * <code>line.separator</code>, and is not necessarily a single newline
     * character (<code>'\n'</code>).
     *
     * @throws java.io.IOException If an error occured while writing
     */
    @Override
    public void println() throws IOException {
        printer.println();
    }

    /**
     * Print a boolean value and then terminate the line.  This method behaves
     * as though it invokes <code>{@link #print(boolean)}</code> and then
     * <code>{@link #println()}</code>.
     *
     * @param x the boolean to write
     * @throws java.io.IOException If an error occured while writing
     */
    @Override
    public void println(boolean x) throws IOException {
        printer.println(x);
    }

    /**
     * Print a character and then terminate the line.  This method behaves as
     * though it invokes <code>{@link #print(char)}</code> and then <code>{@link
     * #println()}</code>.
     *
     * @param x the char to write
     * @throws java.io.IOException If an error occured while writing
     */
    @Override
    public void println(char x) throws IOException {
        printer.println(x);
    }

    /**
     * Print an integer and then terminate the line.  This method behaves as
     * though it invokes <code>{@link #print(int)}</code> and then <code>{@link
     * #println()}</code>.
     *
     * @param x the int to write
     * @throws java.io.IOException If an error occured while writing
     */
    @Override
    public void println(int x) throws IOException {
        printer.println(x);
    }

    /**
     * Print a long integer and then terminate the line.  This method behaves
     * as though it invokes <code>{@link #print(long)}</code> and then
     * <code>{@link #println()}</code>.
     *
     * @param x the long to write
     * @throws java.io.IOException If an error occured while writing
     */
    @Override
    public void println(long x) throws IOException {
        printer.println(x);
    }

    /**
     * Print a floating-point number and then terminate the line.  This method
     * behaves as though it invokes <code>{@link #print(float)}</code> and then
     * <code>{@link #println()}</code>.
     *
     * @param x the float to write
     * @throws java.io.IOException If an error occured while writing
     */
    @Override
    public void println(float x) throws IOException {
        printer.println(x);
    }

    /**
     * Print a double-precision floating-point number and then terminate the
     * line.  This method behaves as though it invokes <code>{@link
     * #print(double)}</code> and then <code>{@link #println()}</code>.
     *
     * @param x the double to write
     * @throws java.io.IOException If an error occured while writing
     */
    @Override
    public void println(double x) throws IOException {
        printer.println(x);
    }

    /**
     * Print an array of characters and then terminate the line.  This method
     * behaves as though it invokes <code>print(char[])</code> and then
     * <code>println()</code>.
     *
     * @param x the char[] to write
     * @throws java.io.IOException If an error occured while writing
     */
    @Override
    public void println(char[] x) throws IOException {
        printer.println(x);
    }

    /**
     * Print a String and then terminate the line.  This method behaves as
     * though it invokes <code>{@link #print(String)}</code> and then
     * <code>{@link #println()}</code>.
     *
     * @param x the String to write
     * @throws java.io.IOException If an error occured while writing
     */
    @Override
    public void println(String x) throws IOException {
        printer.println(x);
    }

    /**
     * Print an Object and then terminate the line.  This method behaves as
     * though it invokes <code>{@link #print(Object)}</code> and then
     * <code>{@link #println()}</code>.
     *
     * @param x the Object to write
     * @throws java.io.IOException If an error occured while writing
     */
    @Override
    public void println(Object x) throws IOException {
        printer.println(x);
    }

    /**
     * Clear the contents of the buffer. If the buffer has been already
     * been flushed then the clear operation shall throw an IOException
     * to signal the fact that some data has already been irrevocably
     * written to the client response stream.
     *
     * @throws java.io.IOException If an I/O error occurs
     */
    @Override
    public void clear() throws IOException {
        throw new IOException(new UnsupportedOperationException("Mock writer does not support clear..."));
    }

    /**
     * Clears the current contents of the buffer. Unlike clear(), this
     * method will not throw an IOException if the buffer has already been
     * flushed. It merely clears the current content of the buffer and
     * returns.
     *
     * @throws java.io.IOException If an I/O error occurs
     */
    @Override
    public void clearBuffer() throws IOException {
        throw new IOException(new UnsupportedOperationException("Mock writer does not support clear..."));
    }

    /**
     * Flush the stream.  If the stream has saved any characters from the
     * various write() methods in a buffer, write them immediately to their
     * intended destination.  Then, if that destination is another character or
     * byte stream, flush it.  Thus one flush() invocation will flush all the
     * buffers in a chain of Writers and OutputStreams.
     * <p>
     * The method may be invoked indirectly if the buffer size is exceeded.
     * <p>
     * Once a stream has been closed,
     * further write() or flush() invocations will cause an IOException to be
     * thrown.
     *
     * @throws java.io.IOException If an I/O error occurs
     */
    @Override
    public void flush() throws IOException {
        printer.flush();
    }

    /**
     * Close the stream, flushing it first.
     * <p>
     * This method needs not be invoked explicitly for the initial JspWriter
     * as the code generated by the JSP container will automatically
     * include a call to close().
     * <p>
     * Closing a previously-closed stream, unlike flush(), has no effect.
     *
     * @throws java.io.IOException If an I/O error occurs
     */
    @Override
    public void close() throws IOException {
        printer.close();
    }

    /**
     * This method returns the number of unused bytes in the buffer.
     *
     * @return the number of bytes unused in the buffer
     */
    @Override
    public int getRemaining() {
        return 0;
    }

    /**
     * Writes a portion of an array of characters.
     *
     * @param cbuf Array of characters
     * @param off  Offset from which to start writing characters
     * @param len  Number of characters to write
     * @throws java.io.IOException If an I/O error occurs
     */
    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        printer.write(cbuf, off, len);
    }

    /**
     * Return the buffer's current value as a string.
     */
    @Override
    public String toString() {
        return content.toString();
    }
}
