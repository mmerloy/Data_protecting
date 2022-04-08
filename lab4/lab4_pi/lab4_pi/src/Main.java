import IO.ReaderWriter;


public class Main {

    public static void main(String[] arg) {

        Blowfish blowfish = new Blowfish();
        blowfish.generateKey("asdqwezxc".getBytes());

        byte[] textArray = blowfish.alignment(ReaderWriter.read("input.txt").getBytes(), ' ');
        System.out.println("Source message " + new String(textArray));


        blowfish.encrypt(textArray);
        ReaderWriter.write("encrypted.txt", new String(textArray));



        blowfish.decrypt(textArray);
        ReaderWriter.write("decrypted.txt", new String(textArray));
    }

}