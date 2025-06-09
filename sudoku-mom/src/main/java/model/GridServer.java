package model;

public interface GridServer {
    static GridServer create() {
        return new GridServerImpl();
    }
    
    
    class GridServerImpl implements GridServer {
        
    }
}
