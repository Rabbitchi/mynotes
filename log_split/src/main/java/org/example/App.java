package org.example;

import java.io.*;
/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        String inputFilePath = "src/main/resources/yg.log";
        long splitSize = 10; // 每个小文件的大小，这里为1MB
        try {
            splitFile(inputFilePath, splitSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void splitFile(String inputFilePath, long splitSize) throws IOException {
        File inputFile = new File(inputFilePath);

        RandomAccessFile raf = new RandomAccessFile(inputFile, "r");
        long fileLength = raf.length();
        int numSplits = Math.max((int) Math.ceil((double) fileLength / splitSize), 1);

        System.out.println("Total splits: " + numSplits);

        byte[] buffer = new byte[1024];
        int bytesRead;

        for (int i = 0; i < numSplits; i++) {
            long startByte = i * splitSize;
            long endByte = Math.min(startByte + splitSize, fileLength);

            raf.seek(startByte);

            FileOutputStream fos = new FileOutputStream(getOutputFilePath(inputFilePath, i + 1));

            while (startByte < endByte) {
                bytesRead = raf.read(buffer, 0, (int) Math.min(buffer.length, (endByte - startByte)));
                if (bytesRead == -1) break;
                fos.write(buffer, 0, bytesRead);
                startByte += bytesRead;
            }

            fos.close();
        }

        raf.close();
    }

    private static String getOutputFilePath(String inputFilePath, int partNumber) {
        String fileName = new File(inputFilePath).getName();
        String nameWithoutExtension = fileName.substring(0, fileName.lastIndexOf('.'));
        String extension = fileName.substring(fileName.lastIndexOf('.'));
        return nameWithoutExtension + "_part" + partNumber + extension;
    }
}
