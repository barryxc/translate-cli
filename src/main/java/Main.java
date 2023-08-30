import com.baidu.translate.cli.EntryPoint;

import picocli.CommandLine;

public class Main {

    public static void main(String[] args) {
        new CommandLine(new EntryPoint()).execute(args);
    }
}
