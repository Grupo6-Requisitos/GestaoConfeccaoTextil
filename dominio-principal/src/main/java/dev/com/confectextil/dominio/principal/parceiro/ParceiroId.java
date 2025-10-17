package dev.com.confectextil.dominio.principal.parceiro;

import java.util.Objects;

public class ParceiroId {
    private final String id;

    public ParceiroId(String id){
        if (id==null || id.trim().isEmpty()){
            throw new IllegalArgumentException("O id não pode ser nulo ou vazio");
        }
        this.id = id;
    }

    public String getId(){
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ParceiroId that)) return false;
        return Objects.equals(id, that.id);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    @Override
    public String toString() {
        return id;
    }
}
