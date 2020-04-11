import java.util.*;
import java.io.*;

public class HillCipher {

    private static final int LINE_LENGTH= 80;

    public static void main(String[] args) throws FileNotFoundException {
        Scanner sc= new Scanner(System.in);


        // Reading in the Key, plaintext, and output file names.

        String keyFilename;
        System.out.println("Please enter the name of the file storing the key.");
        keyFilename= sc.next();
        File keyFile= new File(keyFilename);

        String plaintextFilename;
        System.out.println("Please enter the name of the file to encrypt.");
        plaintextFilename= sc.next();
        File plaintextFile = new File(plaintextFilename);

        String outputFilename;
        System.out.println("Please enter the name of the file to store the ctArray.");
        outputFilename= sc.next();

        // The first integer tells us how large the multidimentional matrix will be. This code sets the dimensions of the key array.
        // The max array size is 10x10 as stated in the homework requirements.

        int keySize = 0;
        int[][] keyArray= new int[10][10];

        Scanner keyScanner = new Scanner(keyFile);
        keySize= keyScanner.nextInt();

        for (int i= 0; i < keySize; i++){
            for (int j= 0; j < keySize; j++)
                keyArray[i][j]= keyScanner.nextInt();
        }

        String ptInput= "";
        Scanner ptScanner = new Scanner(plaintextFile);

        // This code will keep reading and storing text into the ptInput string array until there is no more text left to read in the input file.
        while (ptScanner.hasNext() == true)
            ptInput += ptScanner.next();

        // While initializing two new character arrays, One for the raw, multicased and punctuated plaintext and the other for the converted
        // final plaintext. We will then loop through each string and check for misc characters. It will then add the final text to the final
        // plaintext array. It also keeps a running total of how many letters are in the plaintextArray (j).

        char[] unchangedPlaintext = ptInput.trim().toLowerCase().toCharArray();
        char[] finalPlaintext = new char[unchangedPlaintext.length];

        int runTotal= 0;
        for (int i= 0; i < unchangedPlaintext.length; i++){
            if (unchangedPlaintext[i] < 'a' || unchangedPlaintext[i] > 'z')
                continue;
            else
                finalPlaintext[runTotal++]= unchangedPlaintext[i];
        }

        // Creating the ciphertext array and doing the actual matrix multiplication. The if statement will check to make sure we still have plaintext
        // to encode, if not, we will substitute with X as instructed.
        char[] ctArray= new char[finalPlaintext.length+1];

        for (int letter = 0; letter < runTotal; letter += keySize){
            for (int row = 0; row < keySize; row++){
                for (int column = 0; column < keySize; column++){

                    if ((letter+column) < runTotal){
                        ctArray[letter+row] += (char) (keyArray[row][column] * (finalPlaintext[letter+column]-'a'));
                    }
                    else {
                        ctArray[letter+row] += (char) (keyArray[row][column] * ('x'-'a'));
                    }
                }
                ctArray[(letter+row)] = (char) (ctArray[(letter+row)] % 26 + 'a');
            }
        }

        // We now print out the final output and have to use a catch in case there is an IOException. We must only print 80 characters per line,
        // as instructed.
        try{
            FileWriter fw = new FileWriter(outputFilename);
            BufferedWriter bufferOut = new BufferedWriter(fw);

            for (int i=0; i < ctArray.length;){
                for(int j=0; j < LINE_LENGTH; j++){
                    if (i < ctArray.length)
                        bufferOut.write(ctArray[i++]);
                }
                bufferOut.write("\n");
            }

            bufferOut.close();
        }

        catch (IOException e){
            System.exit(1);
        }
    }
}