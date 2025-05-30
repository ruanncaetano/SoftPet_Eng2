package SoftPet.backend.dto;

// Para validações, se necessário no futuro (ex: Spring Boot 3+)
// import jakarta.validation.constraints.NotNull;

public class EstoqueUpdateRequestDTO {

    // @NotNull(message = "A quantidade para alteração do estoque é obrigatória.")
    private Integer quantidade; // Usamos Integer para permitir que o campo seja nulo se não enviado,
    // mas as validações no serviço/controller devem tratar isso.

    public EstoqueUpdateRequestDTO() {
    }

    public EstoqueUpdateRequestDTO(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }
}