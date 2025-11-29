package edu.ucu.ua.task2;


public class StampingVisitor<T> {
    private String key;
    private String value;

    public StampingVisitor(String key, String value) {
        this.key = key;
        this.value = value;
    }
    
    public void visitSignature(Signature<T> signature) {
        signature.setHeader(key, value);
    }
    
    public void visitGroup(Group<T> group) {
        group.setHeader(key, value);
        
        if (group.getTasks() != null) {
            for (Task<T> signature : group.getTasks()) {
                signature.accept(this);
            }
        }
    }
}
