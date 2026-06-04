import rmi.FactoryRMI;
import rmi.SudokuServer;
import utils.Try;

import java.util.Optional;

public final class MainServer {

    public static void main(final String[] args) {
        Optional<SudokuServer> sudokuServer = Try.toOptional(FactoryRMI::createAndRegisterServer);
        while (sudokuServer.isEmpty()) sudokuServer = Try.toOptional(FactoryRMI::createAndRegisterServer);
    }
}
