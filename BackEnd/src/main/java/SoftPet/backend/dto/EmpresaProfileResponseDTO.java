package SoftPet.backend.dto;

public record EmpresaProfileResponseDTO(
        Long id,
        String nome,
        String razaoSocial,
        String cnpj,
        String logoPequena,
        String logoGrande,
        String endereco,
        String bairro,
        String cidade,
        String uf,
        String diretor,
        String site
) {}