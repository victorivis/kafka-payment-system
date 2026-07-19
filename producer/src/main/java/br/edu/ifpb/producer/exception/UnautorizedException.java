package br.edu.ifpb.producer.exception;

public class UnautorizedException extends RuntimeException {
    public UnautorizedException() {
        super("Você não tem permissão para acessar essa informação");
    }
}
