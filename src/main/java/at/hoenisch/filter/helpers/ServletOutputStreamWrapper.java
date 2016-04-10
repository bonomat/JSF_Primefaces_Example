package at.hoenisch.filter.helpers;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Philipp Hoenisch on 08/04/16.
 * A custom servlet outputsream wrapper to intercept the output stream
 */
public class ServletOutputStreamWrapper extends ServletOutputStream {

    private OutputStream m_out;
    private boolean m_closed = false;

    public ServletOutputStreamWrapper(OutputStream realStream) {
        this.m_out = realStream;
    }

    @Override
    public void close() throws IOException {
        if (m_closed) {
            throw new IOException("This output stream has already been closed");
        }
        m_out.flush();
        m_out.close();

        m_closed = true;
    }

    @Override
    public void flush() throws IOException {
        if (m_closed) {
            throw new IOException("Cannot flush a closed output stream");
        }
        m_out.flush();
    }

    @Override
    public void write(int b) throws IOException {
        if (m_closed) {
            throw new IOException("Cannot write to a closed output stream");
        }
        m_out.write((byte) b);
    }

    @Override
    public void write(byte b[]) throws IOException {
        write(b, 0, b.length);
    }

    @Override
    public void write(byte b[], int off, int len) throws IOException {
        // System.out.println("writing...");
        if (m_closed) {
            throw new IOException("Cannot write to a closed output stream");
        }
        m_out.write(b, off, len);
    }

}