import java.io.File;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

/**
 * This class handles the programs arguments.
 */
public class CommandLineValues {
    @Option(name = "-i", aliases = { "--input" }, required = true,
            usage = "input file, the file should be seperated by tab")
    public String input;
    @Option(name = "-col", aliases = { "--column" }, required = false,
            usage = "the column No. in the input file, one-based, numberic, default "+ 1)
    public int column=1;
    
    @Option(name = "-d", aliases = { "--database" }, required = true,
            usage = "see Jannovar: https://github.com/charite/jannovar")
    public String refseq;
    @Option(name = "-o", aliases = { "--output" }, required = true,
            usage = "specify output file ")
    public String output;
    
    
    public CommandLineValues(String... args) {
        CmdLineParser parser = new CmdLineParser(this);
        try {
            parser.parseArgument(args);
        } catch( CmdLineException e ) {
            int start = e.getMessage().indexOf('"')+1;
            int end   = e.getMessage().lastIndexOf('"');
            String wrongArgument = e.getMessage().substring(start, end);
            System.err.println("Unknown argument: " + wrongArgument);
            System.err.println("ant [options] [target [target2 [target3] ...]]");
            parser.printUsage(System.err);
            System.err.println();
           
        }
    }

   
    /**
     * Returns the source file.
     *
     * @return The source file.
     */
    public File getSource() {
        return new File(input);
    }
}