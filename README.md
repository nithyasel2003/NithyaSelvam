# Medicare Claims XML Generator using GenAI

This is a simple Java application that demonstrates how to use Generative AI to automatically generate XML input requests for Medicare claims based on requirement documents and membership information.

## Project Structure

```
├── src/
│   └── main/
│       ├── java/com/medicare/claims/
│       │   ├── GenAIXmlGenerator.java    # Main GenAI service class
│       │   └── MedicareClaimsApp.java    # Main application class
│       └── resources/
│           ├── sample_medicare_claim.xml  # Sample XML output
│           └── sample_requirement.txt     # Sample requirement document
├── README.md
└── (generated files will appear here when you run the application)
```

## Features

- **GenAI-Powered XML Generation**: Simulates AI processing of requirement documents to generate Medicare claims XML
- **Membership Input Processing**: Accepts member demographic and plan information
- **Template-Based Generation**: Uses configurable XML templates for consistent output
- **XML Validation**: Basic validation of generated XML structure
- **Interactive Mode**: Allows custom input through command-line interface
- **File Output**: Saves generated XML to files for further processing

## Sample XML Structure

The application generates Medicare claims XML with the following structure:

- **Header**: Transaction ID, submission date, provider NPI, batch number
- **Membership Info**: Member ID, Medicare beneficiary ID, demographics, address, plan type
- **Claim Details**: Claim number, service date, procedure codes, diagnosis codes, amounts
- **Provider Info**: Provider details, NPI, tax ID, contact information

## How to Run

### Prerequisites
- Java 8 or higher
- No additional dependencies required (uses only standard Java libraries)

### Compilation
```bash
# Navigate to the project root directory
cd /workspace

# Compile the Java files
javac -d . src/main/java/com/medicare/claims/*.java
```

### Execution
```bash
# Run the main application
java com.medicare.claims.MedicareClaimsApp
```

## Usage Examples

### 1. Demonstration Mode
The application first runs in demonstration mode with predefined sample data:
- Shows sample membership information
- Processes the requirement document
- Generates and validates XML
- Saves output to `generated_medicare_claim.xml`

### 2. Interactive Mode
After the demonstration, you can enter custom membership information:
- Member ID, Medicare Beneficiary ID
- Personal details (name, DOB, gender)
- Address information
- Plan type and service date
- Generates custom XML saved to `custom_medicare_claim.xml`

## Sample Input Data

**Membership Information:**
```
Member ID: MBR123456789
Medicare ID: 1AB2CD3EF45
Name: John Doe
DOB: 1945-06-15
Address: 123 Main Street, Anytown, CA 90210
Plan Type: Medicare Part B
```

**Generated XML Output:**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<MedicareClaimRequest xmlns="http://medicare.gov/claims/2023">
    <Header>
        <TransactionId>TXN-12345678</TransactionId>
        <SubmissionDate>2023-12-01T10:30:00Z</SubmissionDate>
        <ProviderNPI>1234567890</ProviderNPI>
        <BatchNumber>BATCH-001</BatchNumber>
    </Header>
    <MembershipInfo>
        <MemberID>MBR123456789</MemberID>
        <MedicareBeneficiaryId>1AB2CD3EF45</MedicareBeneficiaryId>
        <!-- ... more membership details ... -->
    </MembershipInfo>
    <!-- ... claim details and provider info ... -->
</MedicareClaimRequest>
```

## GenAI Simulation

The `GenAIXmlGenerator` class simulates AI processing by:

1. **Reading requirement documents** and extracting key information
2. **Processing membership input data** and mapping to XML fields
3. **Intelligent field extraction** based on document content analysis
4. **Template-based XML generation** with dynamic value substitution
5. **Validation and quality checks** on the generated XML

## Customization

You can customize the application by:

- **Modifying the XML template** in `GenAIXmlGenerator.java`
- **Adding new requirement documents** in the resources folder
- **Extending the AI simulation logic** for more complex processing
- **Adding new validation rules** for different claim types
- **Integrating with real AI services** (OpenAI, Azure AI, etc.)

## Notes

- This is a demonstration application that simulates GenAI functionality
- In a real implementation, you would integrate with actual AI services
- The XML structure follows common Medicare submission patterns
- All generated data is for demonstration purposes only