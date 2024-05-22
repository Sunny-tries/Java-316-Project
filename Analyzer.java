// Project by Sunny Hardyal & Alexis Gonzalez
import java.io.*;
import java.util.*;

public class Analyzer {

    enum TokenType {
        RESERVED_WORD, OPERATOR, IDENTIFIER, CONSTANT, COMMENT, UNKNOWN
    }

    // Reserved words set
    static Set<String> RESERVED_WORDS = new HashSet<>(Arrays.asList("if", "else", "int", "double", "String", "Boolean", "True", "False"));

    // Operators set
    static Set<String> OPERATORS = new HashSet<>(Arrays.asList("=", "+", "-", "*", "/", "==", "!=", "<", "<=", ">", ">=", ";"));

    // Digits set
    static Set<Character> DIGITS = new HashSet<>(Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9'));

    // Letters set
    static Set<Character> LETTERS = new HashSet<>(Arrays.asList(
        'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
    ));

    public static void main(String[] args) {
        String filename = "input.txt";
        try {
            // Read the file line by line
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = reader.readLine()) != null) {
                checkLine(line); // Check each line
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    // Method to check each line for tokens
    public static void checkLine(String line) {
        List<String> tokens = tokenize(line);
        boolean hasError = false;
        for (String token : tokens) {
            TokenType type = getTokenType(token);
            if (type == TokenType.UNKNOWN) {
                System.out.println("ERROR: " + token + "\n"); // Print error if any token is unknown
                hasError = true;
                break;
            } else {
                System.out.printf("Token: %-15s Lexeme: %-15s\n", type, token); // Print the token type and lexeme
            }
        }
        if (!hasError) {
            System.out.println("Line accepted\n"); // Print line accepted if all tokens are valid
        } else {
            System.out.println("Line rejected\n");
        }
    }

    // Method to tokenize the input line
    public static List<String> tokenize(String line) {
        List<String> tokens = new ArrayList<>();
        StringBuilder token = new StringBuilder();
        boolean inComment = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            // Handle comments
            if (c == '/' && i + 1 < line.length() && line.charAt(i + 1) == '/') {
                inComment = true;
                tokens.add(line.substring(i));
                break;
            }

            if (Character.isWhitespace(c)) {
                if (token.length() > 0) {
                    tokens.add(token.toString());
                    token.setLength(0);
                }
            } else if (LETTERS.contains(c) || DIGITS.contains(c) || c == '.') {
                token.append(c);
            } else {
                if (token.length() > 0) {
                    tokens.add(token.toString());
                    token.setLength(0);
                }
                tokens.add(String.valueOf(c));
            }
        }

        if (token.length() > 0) {
            tokens.add(token.toString());
        }
        return tokens;
    }

    // Method to determine the token type
    private static TokenType getTokenType(String token) {
        if (token.startsWith("//")) {
            return TokenType.COMMENT;
        } else if (RESERVED_WORDS.contains(token)) {
            return TokenType.RESERVED_WORD;
        } else if (OPERATORS.contains(token)) {
            return TokenType.OPERATOR;
        } else if (isIdentifier(token)) {
            return TokenType.IDENTIFIER;
        } else if (isConstant(token)) {
            return TokenType.CONSTANT;
        } else {
            return TokenType.UNKNOWN;
        }
    }

    // Method to check if a token is a valid identifier
    private static boolean isIdentifier(String token) {
        if (token.isEmpty() || !LETTERS.contains(token.charAt(0))) {
            return false;
        }
        for (int i = 1; i < token.length(); i++) {
            char c = token.charAt(i);
            if (!LETTERS.contains(c) && !DIGITS.contains(c)) {
                return false;
            }
        }
        return true;
    }

    // Method to check if a token is a valid constant
    private static boolean isConstant(String token) {
        if (token.isEmpty()) {
            return false;
        }
        boolean dotSeen = false;
        for (int i = 0; i < token.length(); i++) {
            char c = token.charAt(i);
            if (c == '.') {
                if (dotSeen) {
                    return false;
                }
                dotSeen = true;
            } else if (!DIGITS.contains(c)) {
                return false;
            }
        }
        return true;
    }
}