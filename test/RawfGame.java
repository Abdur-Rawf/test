import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class RawfGame {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) { // Game loop to repeat until the user exits
            // Welcome message and user input
            System.out.print("Enter your name: ");
            String userName = scanner.nextLine();
            System.out.println("Welcome, " + userName + "!");

            // Select difficulty level
            System.out.println("Select difficulty level:");
            System.out.println("1. Easy (5 questions)");
            System.out.println("2. Medium (10 questions)");
            System.out.println("3. Hard (20 questions)");
            int numberOfQuestions;

            switch (scanner.nextInt()) {
                case 1 -> numberOfQuestions = 5;
                case 2 -> numberOfQuestions = 10;
                case 3 -> numberOfQuestions = 20;
                default -> {
                    System.out.println("Invalid choice! Defaulting to Easy level.");
                    numberOfQuestions = 5;
                }
            }

            // Read and parse questions from Input.txt
            List<String> questions = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader("Input.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    // Split questions by comma
                    String[] parts = line.split(",");
                    for (String question : parts) {
                        questions.add(question.trim());
                    }
                }
            } catch (IOException e) {
                System.out.println("Error reading Input.txt: " + e.getMessage());
                return;
            }

            // Check if enough questions are available
            if (questions.size() < numberOfQuestions) {
                System.out.println("Not enough questions in Input.txt for the selected difficulty level.");
                return;
            }

            // Shuffle and select questions
            Collections.shuffle(questions);
            questions = questions.subList(0, numberOfQuestions);

            // Quiz user and calculate score
            int score = 0;
            scanner.nextLine(); // Consume leftover newline character

            for (String question : questions) {
                System.out.println("Solve: " + question);
                try {
                    // Parse and calculate the correct answer
                    String[] parts = question.split("\\+");
                    if (parts.length != 2) throw new IllegalArgumentException("Invalid question format");

                    int num1 = Integer.parseInt(parts[0].trim());
                    int num2 = Integer.parseInt(parts[1].trim());
                    int correctAnswer = num1 + num2;

                    // Get user's answer
                    System.out.print("Your answer: ");
                    int userAnswer = scanner.nextInt();

                    if (userAnswer == correctAnswer) {
                        score++;
                    }
                } catch (Exception e) {
                    System.out.println("Error in question format or input. Skipping this question.");
                }
            }

            // Display the user's score
            System.out.println("Quiz completed!");
            System.out.println(userName + ", your score is: " + score + "/" + numberOfQuestions);

            // Get current date and time
            String dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

            // Save the result to Output.txt in the specified format
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("Output.txt", true))) {
                writer.write(String.format("%-10s %-6s %-20s", userName, score, dateTime));
                writer.newLine();
            } catch (IOException e) {
                System.out.println("Error writing to Output.txt: " + e.getMessage());
            }

            // Ask if the user wants to play again
            System.out.println("Do you want to play again?\n1. Yes\n2. No");
            int playAgain = scanner.nextInt();
            scanner.nextLine(); // Consume leftover newline character

            if (playAgain != 1) {
                System.out.println("Thank you for playing! Goodbye!");
                break; // Exit the loop if the user chooses not to play again
            }
        }

        scanner.close();
    }
}
