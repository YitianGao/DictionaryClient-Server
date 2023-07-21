/*
Name Yitian Gao
Student ID 1318692
 */

package server;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;



public class MyRunnable implements Runnable{

    public static HashMap<String, String> wordToMeaning = new HashMap<String, String>();

    public static String path ="src/Dic.txt";
    Socket socket;
    public MyRunnable(Socket socket, String path){
        this.socket = socket;
        this.path = path;

    }

    @Override
    public void run() {
        synchronized (MyRunnable.class){
            try {

                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream());




                String[] messageFromClient = input.readUTF().split("-");

                for (int i = 0; i < messageFromClient.length; i++) {
                    System.out.println(messageFromClient[i]);
                }

                String operation = messageFromClient[1];

                switch (operation) {
                    case "add":
                        if (wordExistsInFile(path,messageFromClient[0])) {
                            output.writeUTF("the word already exist");
                            System.out.println("-----------------");
                            break;
                            //                    System.out.println();
                        } else if (wordExistsInFile(path,messageFromClient[0])!= true) {
                            addWord(messageFromClient[0], messageFromClient[2]);
                            System.out.println("--------------------------");
                            output.writeUTF("the word and its meaning has successfully added");
                            break;
                            //                    System.out.println();
                        } else {
                            output.writeUTF("you don't follow the instructions,pls try again");
                            //                    System.out.println();
                            break;

                        }

                    case "remove":
                        if (wordExistsInFile(path,messageFromClient[0])){
                            removeWordFromFile(path,messageFromClient[0]);
                            output.writeUTF("the word has been successfully removed");
                            break;
                        } else if (wordExistsInFile(path,messageFromClient[0]) == false) {
                            output.writeUTF("this word doesn't exist and you can't remove");
                            break;

                        }else {
                            output.writeUTF("you didn't follow the instructions");
                            break;

                        }

                    case "query":
                        if(wordExistsInFile(path,messageFromClient[0])){
                            String wordMeaning = getWordMeaningFromFile(path, messageFromClient[0]);
                            //                    System.out.println(wordMeaning);


//                            if (wordMeaning != null) {
//                                System.out.println("---"+ wordMeaning);
                                output.writeUTF("The meaning of the word: " + wordMeaning);
//                            }

                            break;




                        }else{
                            output.writeUTF("the word doesn't exist, type the meaning below and add it first");
                            break;
                        }
                    case "update":
                        try {
                            if(messageFromClient[2].equals("default")){
                                System.out.println("You didn't input meaning");
                                output.writeUTF("You didn't input meaning");
                            } else if (wordExistsInFile(path,messageFromClient[0])){
                                updateMeaningInFile(path,messageFromClient[0],messageFromClient[2]);
                                output.writeUTF("Words meaning successfully updated");
                                break;

                            }else {
                                output.writeUTF("the doesn't exist, you can't update");
                                break;
                            }
                        } catch (IOException e) {
                            System.out.println("You didn't input meaning");
                            output.writeUTF("You didn't input meaning");
                            //throw new RuntimeException(e);
                        }

                    default:
                           output.writeUTF("you don't enter the correct operation");
                           break;





                }




            } catch (IOException e) {
                throw new RuntimeException(e);
            }finally {
                if (socket != null){
                    try {
                        socket.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
        }
    }


    public static void removeWordFromFile(String filePath, String word) {
        File file = new File(filePath);
        try {
            // Create temporary file
            File tempFile = new File("temp.txt");
            BufferedReader reader = new BufferedReader(new FileReader(file));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
            String currentLine;
            boolean wordFound = false;

            // Read each line of the file
            while ((currentLine = reader.readLine()) != null) {
                // Check if the line contains the word
                if (currentLine.contains(word)) {
                    wordFound = true;
                    continue; // Skip line if it contains the word
                }
                // Write line to temporary file
                writer.write(currentLine + System.getProperty("line.separator"));
            }
            writer.close();
            reader.close();

            // If the word was found, delete the original file and rename the temporary file to the original file name
            if (wordFound) {
                if (file.delete()) {
                    tempFile.renameTo(file);
                    System.out.println("Word removed successfully from file.");
                } else {
                    System.out.println("Error occurred while removing word from file.");
                }
            } else {
                // If the word was not found, print a message
                System.out.println("Word does not exist in the file.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred while removing word from file.");
            e.printStackTrace();
        }
    }

    public static void addWord(String word, String meaning){
        wordToMeaning.put(word, meaning);
        saveToFile(path);

    }


public String getWordMeaningFromFile(String fileName, String word) {
    BufferedReader reader = null;
    try {
        reader = new BufferedReader(new FileReader(fileName));
        String line;
        while ((line = reader.readLine()) != null) {
            // Do something with the line
            if (line.contains(word)) {
                return line;
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    } finally {
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    return null;
}


    public static boolean wordExistsInFile(String filePath, String word) {
        try {
            File file = new File(filePath);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                if (parts[0].equals(word)) {
                    scanner.close();
                    return true;
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred while reading the file.");
            e.printStackTrace();
        }
        return false;
    }

    public static void saveToFile(String fileName) {
        try {
            FileWriter writer = new FileWriter(fileName,true);
            for (Map.Entry<String, String> entry : wordToMeaning.entrySet()) {
                String word = entry.getKey();
                String meaning = entry.getValue();
                writer.write(word + "," + meaning + "\n");
                System.out.println("write one line");
            }
            wordToMeaning.clear();
            writer.close();
            System.out.println("Dictionary saved to file: " + fileName);
        } catch (IOException e) {
            System.out.println("An error occurred while saving the dictionary to file: " + e.getMessage());
        }
    }

    public static void updateMeaningInFile(String filepath, String word, String newMeaning) {
        try {
            File file = new File(filepath);
            Scanner scanner = new Scanner(file);

            String fileContent = "";
            boolean wordFound = false;

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");

                if (parts[0].trim().equalsIgnoreCase(word)) {
                    parts[1] = newMeaning;
                    line = parts[0].trim() + "," + parts[1];
                    wordFound = true;
                }

                fileContent += line + "\n";
            }

            scanner.close();

            if (!wordFound) {
                System.out.println("Word not found in file: " + word);
                return;
            }

            FileWriter writer = new FileWriter(file);
            writer.write(fileContent);
            writer.close();

            System.out.println("Meaning updated for word: " + word);
        } catch (IOException e) {
            System.out.println("An error occurred while updating meaning in file: " + e.getMessage());
        }
    }



}
