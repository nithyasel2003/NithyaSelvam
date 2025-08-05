package com.medicare.claims;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Main application class for Medicare Claims XML Generation using GenAI
 * Demonstrates the complete workflow from requirement document to XML output
 */
public class MedicareClaimsApp {
    
    private static final GenAIXmlGenerator xmlGenerator = new GenAIXmlGenerator();
    
    public static void main(String[] args) {
        System.out.println("==============================================");
        System.out.println("Medicare Claims XML Generator using GenAI");
        System.out.println("==============================================\n");
        
        try {
            // Demo with predefined membership data
            demonstrateWithSampleData();
            
            // Interactive mode
            System.out.println("\n" + "=".repeat(50));
            System.out.println("INTERACTIVE MODE");
            System.out.println("=".repeat(50));
            runInteractiveMode();
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Demonstrates XML generation with sample membership data
     */
    private static void demonstrateWithSampleData() throws IOException {
        System.out.println("1. DEMONSTRATION WITH SAMPLE DATA");
        System.out.println("-".repeat(40));
        
        // Create sample membership input
        Map<String, String> membershipInput = createSampleMembershipData();
        
        System.out.println("Sample Membership Input:");
        printMembershipData(membershipInput);
        
        // Generate XML from requirement document
        String requirementPath = "src/main/resources/sample_requirement.txt";
        System.out.println("\nProcessing requirement document: " + requirementPath);
        
        String generatedXml = xmlGenerator.generateXmlFromRequirement(requirementPath, membershipInput);
        
        // Validate the generated XML
        boolean isValid = xmlGenerator.validateXml(generatedXml);
        System.out.println("\nXML Validation Result: " + (isValid ? "VALID" : "INVALID"));
        
        // Save the generated XML
        String outputPath = "generated_medicare_claim.xml";
        xmlGenerator.saveXmlToFile(generatedXml, outputPath);
        
        System.out.println("\nGenerated XML Preview:");
        System.out.println("-".repeat(40));
        printXmlPreview(generatedXml);
    }
    
    /**
     * Interactive mode for custom membership input
     */
    private static void runInteractiveMode() throws IOException {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Enter membership information (press Enter for defaults):\n");
        
        Map<String, String> membershipInput = new HashMap<>();
        
        // Collect member information
        membershipInput.put("memberId", promptWithDefault(scanner, "Member ID", "MBR987654321"));
        membershipInput.put("medicareId", promptWithDefault(scanner, "Medicare Beneficiary ID", "9ZY8XW7VU65"));
        membershipInput.put("firstName", promptWithDefault(scanner, "First Name", "Jane"));
        membershipInput.put("lastName", promptWithDefault(scanner, "Last Name", "Smith"));
        membershipInput.put("dateOfBirth", promptWithDefault(scanner, "Date of Birth (YYYY-MM-DD)", "1950-03-20"));
        membershipInput.put("gender", promptWithDefault(scanner, "Gender (M/F)", "F"));
        membershipInput.put("street", promptWithDefault(scanner, "Street Address", "789 Oak Avenue"));
        membershipInput.put("city", promptWithDefault(scanner, "City", "Springfield"));
        membershipInput.put("state", promptWithDefault(scanner, "State", "IL"));
        membershipInput.put("zipCode", promptWithDefault(scanner, "ZIP Code", "62701"));
        membershipInput.put("planType", promptWithDefault(scanner, "Plan Type", "Medicare Part A"));
        membershipInput.put("serviceDate", promptWithDefault(scanner, "Service Date (YYYY-MM-DD)", "2023-12-01"));
        
        System.out.println("\nProcessing your input with GenAI...");
        
        // Generate XML
        String requirementPath = "src/main/resources/sample_requirement.txt";
        String generatedXml = xmlGenerator.generateXmlFromRequirement(requirementPath, membershipInput);
        
        // Validate and save
        boolean isValid = xmlGenerator.validateXml(generatedXml);
        System.out.println("XML Validation Result: " + (isValid ? "VALID" : "INVALID"));
        
        String outputPath = "custom_medicare_claim.xml";
        xmlGenerator.saveXmlToFile(generatedXml, outputPath);
        
        System.out.println("\nGenerated XML Preview:");
        System.out.println("-".repeat(40));
        printXmlPreview(generatedXml);
        
        scanner.close();
    }
    
    /**
     * Creates sample membership data for demonstration
     */
    private static Map<String, String> createSampleMembershipData() {
        Map<String, String> membershipInput = new HashMap<>();
        membershipInput.put("memberId", "MBR123456789");
        membershipInput.put("medicareId", "1AB2CD3EF45");
        membershipInput.put("firstName", "John");
        membershipInput.put("lastName", "Doe");
        membershipInput.put("dateOfBirth", "1945-06-15");
        membershipInput.put("gender", "M");
        membershipInput.put("street", "123 Main Street");
        membershipInput.put("city", "Anytown");
        membershipInput.put("state", "CA");
        membershipInput.put("zipCode", "90210");
        membershipInput.put("effectiveDate", "2023-01-01");
        membershipInput.put("planType", "Medicare Part B");
        membershipInput.put("serviceDate", "2023-11-15");
        return membershipInput;
    }
    
    /**
     * Prints membership data in a formatted way
     */
    private static void printMembershipData(Map<String, String> membershipData) {
        System.out.println("  Member ID: " + membershipData.get("memberId"));
        System.out.println("  Medicare ID: " + membershipData.get("medicareId"));
        System.out.println("  Name: " + membershipData.get("firstName") + " " + membershipData.get("lastName"));
        System.out.println("  DOB: " + membershipData.get("dateOfBirth"));
        System.out.println("  Address: " + membershipData.get("street") + ", " + 
                          membershipData.get("city") + ", " + membershipData.get("state") + " " + 
                          membershipData.get("zipCode"));
        System.out.println("  Plan Type: " + membershipData.get("planType"));
    }
    
    /**
     * Prints a preview of the generated XML (first 20 lines)
     */
    private static void printXmlPreview(String xml) {
        String[] lines = xml.split("\n");
        int maxLines = Math.min(20, lines.length);
        
        for (int i = 0; i < maxLines; i++) {
            System.out.println(lines[i]);
        }
        
        if (lines.length > maxLines) {
            System.out.println("... (truncated - see full XML in output file)");
        }
    }
    
    /**
     * Prompts user for input with a default value
     */
    private static String promptWithDefault(Scanner scanner, String prompt, String defaultValue) {
        System.out.print(prompt + " [" + defaultValue + "]: ");
        String input = scanner.nextLine().trim();
        return input.isEmpty() ? defaultValue : input;
    }
}