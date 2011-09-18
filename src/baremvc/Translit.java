package baremvc;

import java.io.*;

import javax.servlet.ServletOutputStream;

public class Translit {
    public static void textToMp3(String text, OutputStream os) {
        File txt = null;
        File wav = null;
        File mp3 = null;
        try {
            txt = File.createTempFile("txt", ".txt");
            FileWriter fw = new FileWriter(txt);
            fw.write(text);
            fw.close();
            
            wav = File.createTempFile("wav", ".wav");
            if (!wav.delete()) {
                throw new RuntimeException("couldn't delete wav");
            }
            mp3 = File.createTempFile("mp3", ".mp3");
            if (!mp3.delete()) {
                throw new RuntimeException("couldn't delete mp3");
            }
            
            String cmd = String.format("/usr/bin/text2wave < %s -o %s", txt.getAbsolutePath(), wav.getAbsolutePath());
            
            Process p = Runtime.getRuntime().exec(new String[]{"/bin/bash", "-c", cmd});
            show(p.getErrorStream());
            System.out.println(p.waitFor()); // TODO check return code
            
            p = Runtime.getRuntime().exec(new String[]{"/usr/bin/lame", wav.getAbsolutePath(), mp3.getAbsolutePath()});
            System.out.println(p.waitFor()); // TODO check return code
            FileInputStream fis = new FileInputStream(mp3);
            int read = fis.read();
            while (read != -1) {
                os.write(read);
                read = fis.read();
            }
        } catch (Throwable t) {
            throw new RuntimeException(t);
        } finally {
            // TODO check return codes
            if (mp3 != null) {
                mp3.delete();
            }
            if (txt != null) {
                txt.delete();
            }
            if (wav != null) {
                wav.delete();
            }
        }
    }
    private static void show(InputStream errorStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(errorStream));
        String line = br.readLine();
        while (line != null) {
            System.err.println(line);
            line = br.readLine();
        }
    }
}
